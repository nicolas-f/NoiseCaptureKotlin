/*
 * This file is part of the NoiseCapture application and OnoMap system.
 *
 * The 'OnoMaP' system is led by Lab-STICC and Ifsttar and generates noise maps via
 * citizen-contributed noise data.
 *
 * This application is co-funded by the ENERGIC-OD Project (European Network for
 * Redistributing Geospatial Information to user Communities - Open Data). ENERGIC-OD
 * (http://www.energic-od.eu/) is partially funded under the ICT Policy Support Programme (ICT
 * PSP) as part of the Competitiveness and Innovation Framework Programme by the European
 * Community. The application work is also supported by the French geographic portal GEOPAL of the
 * Pays de la Loire region (http://www.geopal.org).
 *
 * Copyright (C) IFSTTAR - LAE and Lab-STICC – CNRS UMR 6285 Equipe DECIDE Vannes
 *
 * NoiseCapture is a free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of
 * the License, or(at your option) any later version. NoiseCapture is distributed in the hope that
 * it will be useful,but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.You should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation,Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301  USA or see For more information,  write to Ifsttar,
 * 14-20 Boulevard Newton Cite Descartes, Champs sur Marne F-77447 Marne la Vallee Cedex 2 FRANCE
 *  or write to scientific.computing@ifsttar.fr
 */
package org.noise_planet.noisecapture.shared.signal

import kotlin.math.log10
import kotlin.test.expect

/**
 * A digital biquad filter is a second order recursive linear filter,
 * containing two poles and two zeros. "Biquad" is an abbreviation of "bi-quadratic",
 * which refers to the fact that in the Z domain, its transfer function is
 * the ratio of two quadratic functions
 * It is a conversion of https://github.com/SonoMKR/sonomkr-core/blob/master/src/biquadfilter.cpp
 * @author Nicolas Fortin, Université Gustave Eiffel
 * @author Valentin Le Bescond, Université Gustave Eiffel
 */
class BiquadFilter(
    var b0: DoubleArray,
    var b1: DoubleArray,
    var b2: DoubleArray,
    var a1: DoubleArray,
    var a2: DoubleArray
) {
    var delay1: DoubleArray
    var delay2: DoubleArray

    init {
        delay1 = DoubleArray(b0.size)
        delay2 = DoubleArray(b0.size)
        if(!(b0.size == b1.size && b1.size == b2.size && b2.size == a1.size
                    && a1.size == a2.size)) {
            throw IllegalArgumentException("Issues with filter size b0.size == ${b0.size}" +
                    " b1.size == ${b1.size} b2.size == ${b2.size} a1.size == ${a1.size}" +
                    " a2.size = ${a2.size}")
        }
    }

    fun reset() {
        delay1 = DoubleArray(b0.size)
        delay2 = DoubleArray(b0.size)
    }

    fun filterThenLeq(samples: FloatArray): Double {
        var square_sum = 0.0
        var output_acc: Double
        for (i in samples.indices) {
            var input_acc = samples[i].toDouble()
            for (j in b0.indices) {
                input_acc -= delay1[j] * a1[j]
                input_acc -= delay2[j] * a2[j]
                output_acc = input_acc * b0[j]
                output_acc += delay1[j] * b1[j]
                output_acc += delay2[j] * b2[j]
                delay2[j] = delay1[j]
                delay1[j] = input_acc
                input_acc = output_acc
            }
            square_sum += input_acc * input_acc
        }
        return 10 * log10(square_sum / samples.size)
    }

    fun filterSlice(samplesIn: FloatArray, samplesOut: FloatArray, subsampling_factor: Int) {
        var samples_out_index = 0
        var output_acc: Double
        for (i in samplesIn.indices) {
            var input_acc = samplesIn[i].toDouble()
            for (j in b0.indices) {
                input_acc -= delay1[j] * a1[j]
                input_acc -= delay2[j] * a2[j]
                output_acc = input_acc * b0[j]
                output_acc += delay1[j] * b1[j]
                output_acc += delay2[j] * b2[j]
                delay2[j] = delay1[j]
                delay1[j] = input_acc
                input_acc = output_acc
            }
            if (i % subsampling_factor == 0) {
                samplesOut[samples_out_index] = input_acc.toFloat()
                samples_out_index++
            }
        }
    }
}
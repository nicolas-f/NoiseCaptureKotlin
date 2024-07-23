package org.noiseplanet.noisecapture.shared.signal

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow

/**
 * Digital filtering of audio samples
 * @author Nicolas Fortin, Université Gustave Eiffel
 * @author Valentin Le Bescond, Université Gustave Eiffel
 * @link https://github.com/SonoMKR/sonomkr-core/blob/master/src/spectrumchannel.cpp
 */
class SpectrumChannel {

    private var subsamplingRatio = 0
    var minimumSamplesLength = 0
    private val subSamplers: MutableList<BiquadFilter> = mutableListOf()

    // Cascaded filters are placed here, each element will take less and less samples as input
    private var iirFilters: MutableList<HashMap<Int, BiquadFilter>> = mutableListOf()
    private var aWeightingFilter: DigitalFilter? = null
    private var cWeightingFilter: DigitalFilter? = null
    private var bandFilterSize = 0
    private val nominalFrequency: MutableList<Double> = mutableListOf()

    /**
     * Load configuration generated by filterdesign.py
     * @param configuration Filters configuration
     * @param useCascade Reduce computation time by subsampling the audio according to filter frequency range
     */
    fun loadConfiguration(configuration: SpectrumChannelConfiguration, useCascade: Boolean = true) {
        clearState()
        if (configuration.bandpass.isNotEmpty()) {
            var maxSubsampling = 0
            if (useCascade) {
                maxSubsampling = configuration.bandpass.maxOfOrNull { it.subsamplingDepth } ?: 0
            }
            iirFilters = mutableListOf()
            subsamplingRatio = configuration.antiAliasing.sampleRatio
            minimumSamplesLength = subsamplingRatio.toDouble().pow(maxSubsampling).toInt()
            val filterConf = configuration.antiAliasing
            for (i in 0 until maxSubsampling) {
                val filter = BiquadFilter(
                    filterConf.b0,
                    filterConf.b1,
                    filterConf.b2,
                    filterConf.a1,
                    filterConf.a2
                )
                subSamplers.add(filter)
            }
            // init cascaded filter storage
            for (i in 0..maxSubsampling) {
                iirFilters.add(HashMap())
            }
            // fill cascaded filter instances
            for (i in 0 until configuration.bandpass.size) {
                bandFilterSize += 1
                val biquad = configuration.bandpass[i]
                nominalFrequency.add(biquad.nominalFrequency)
                val refFilter: Sos = when {
                    useCascade -> biquad.subsamplingFilter.sos
                    else -> biquad.sos
                }
                val filter = BiquadFilter(
                    refFilter.b0,
                    refFilter.b1,
                    refFilter.b2,
                    refFilter.a1,
                    refFilter.a2
                )
                if (useCascade) {
                    iirFilters[biquad.subsamplingDepth][i] = filter
                } else {
                    iirFilters[0][i] = filter
                }
            }
            if (configuration.aWeighting != null) {
                aWeightingFilter = DigitalFilter(
                    configuration.aWeighting.filterNumerator,
                    configuration.aWeighting.filterDenominator
                )
            }
            if (configuration.cWeighting != null) {
                cWeightingFilter = DigitalFilter(
                    configuration.cWeighting.filterNumerator,
                    configuration.cWeighting.filterDenominator
                )
            }
        }
    }

    /**
     * @return Nominal frequency for printing results of columns of [.processSamples]
     */
    fun getNominalFrequency(): List<Double> {
        return nominalFrequency
    }

    fun processSamplesWeightA(samples: FloatArray): Double {
        return checkNotNull(aWeightingFilter?.filterLeq(samples)) {
            "A weighting filter not configured"
        }
    }

    fun processSamplesWeightC(samples: FloatArray): Double {
        return checkNotNull(cWeightingFilter?.filterLeq(samples)) {
            "C weighting filter not configured"
        }
    }

    suspend fun processSamples(samples: FloatArray, parallel: Boolean = true): DoubleArray {
        if (samples.size % minimumSamplesLength != 0) {
            throw IllegalArgumentException(
                "Provided samples len should be a" +
                    " factor of $minimumSamplesLength samples"
            )
        }
        if (iirFilters.isEmpty()) {
            throw IllegalStateException(
                "Loaded configuration does not contain bandpass" +
                    " filters"
            )
        }
        var lastFilterSamples = samples
        val leqs = DoubleArray(bandFilterSize)
        for (cascadeIndex in iirFilters.indices) {
            val cascadeFilters: HashMap<Int, BiquadFilter> = iirFilters[cascadeIndex]
            if (parallel) {
                coroutineScope {
                    for ((key, value) in cascadeFilters.entries) {
                        launch {
                            leqs[key] = value.filterThenLeq(lastFilterSamples)
                        }
                    }
                }
            } else {
                for ((key, value) in cascadeFilters.entries) {
                    leqs[key] = value.filterThenLeq(lastFilterSamples)
                }
            }
            // subsampling for next iteration
            if (cascadeIndex < subSamplers.size) {
                val nextFilterSamples = FloatArray(lastFilterSamples.size / subsamplingRatio)
                subSamplers[cascadeIndex].filterSlice(
                    lastFilterSamples, nextFilterSamples,
                    subsamplingRatio
                )
                lastFilterSamples = nextFilterSamples
            }
        }
        return leqs
    }

    private fun clearState() {
        subSamplers.clear()
        bandFilterSize = 0
        aWeightingFilter = null
        cWeightingFilter = null
        iirFilters.clear()
        subsamplingRatio = 0
        nominalFrequency.clear()
    }
}
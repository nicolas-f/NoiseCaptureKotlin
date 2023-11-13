package org.noise_planet.noisecapture

interface AudioCallback {
    fun onAudio(samples: FloatArray)
}

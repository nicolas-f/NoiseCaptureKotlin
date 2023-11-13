package org.noise_planet.noisecapture

import org.noise_planet.noisecapture.AndroidAudioSource
import org.noise_planet.noisecapture.AudioSource

actual fun CreateAudioSource(): AudioSource {
    return AndroidAudioSource()
}

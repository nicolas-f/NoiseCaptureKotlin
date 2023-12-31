package org.noise_planet.noisecapture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Common interface to access Audio samples from device microphone
 * As each device
 */
interface AudioSource {
    val samples: MutableSharedFlow<AudioSamples>

    enum class MicrophoneLocation { LOCATION_UNKNOWN, LOCATION_MAIN_BODY,
        LOCATION_MAIN_BODY_MOVABLE, LOCATION_PERIPHERAL }

    enum class InitializeErrorCode { INITIALIZE_OK, INITIALIZE_WRONG_BUFFER_SIZE,
        INITIALIZE_SAMPLE_RATE_NOT_SUPPORTED, INITIALIZE_ALREADY_INITIALIZED}
    /**
     * @param sampleRate Sample rate in Hz
     * @param bufferSize Buffer size in bytes
     * @return InitializeErrorCode instance
     */
    fun setup(sampleRate: Int, bufferSize: Int) : InitializeErrorCode

    fun getSampleRate() : Int

    /**
     * Release device and will require to setup again before getting new samples
     * Will abort samples flow
     */
    fun release()

    fun getMicrophoneLocation() : MicrophoneLocation

}

data class AudioSamples(val epoch : Long, val samples : FloatArray, val errorCode: ErrorCode) {
    enum class ErrorCode { OK, ABORTED, DEVICE_ERROR}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AudioSamples

        if (epoch != other.epoch) return false
        if (!samples.contentEquals(other.samples)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = epoch.hashCode()
        result = 31 * result + samples.contentHashCode()
        return result
    }
}

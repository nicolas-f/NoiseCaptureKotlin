package org.noise_planet.noisecapture

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder

class AndroidAudioSource : AudioSource {
    private lateinit var audioRecord: AudioRecord

    @SuppressLint("MissingPermission")
    override fun setup(
        sampleRate: Int,
        bufferSize: Int,
        callback: AudioCallback
    ): AudioSource.InitializeErrorCode {
        val channel = AudioFormat.CHANNEL_IN_MONO
        val encoding = AudioFormat.ENCODING_PCM_FLOAT
        val minimalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, encoding)
        when {
            minimalBufferSize == AudioRecord.ERROR_BAD_VALUE || minimalBufferSize == AudioRecord.ERROR  ->
                return AudioSource.InitializeErrorCode.INITIALIZE_SAMPLE_RATE_NOT_SUPPORTED
            minimalBufferSize > bufferSize ->
                return AudioSource.InitializeErrorCode.INITIALIZE_WRONG_BUFFER_SIZE
        }
        audioRecord = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, sampleRate, channel, encoding, bufferSize)
        return AudioSource.InitializeErrorCode.INITIALIZE_OK
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun getMicrophoneLocation(): AudioSource.MicrophoneLocation {
        TODO("Not yet implemented")
    }
}

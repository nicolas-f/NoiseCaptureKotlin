package org.noiseplanet.noisecapture.ui.features.measurement.spectrogram

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.noiseplanet.noisecapture.audio.ANDROID_GAIN
import org.noiseplanet.noisecapture.measurements.MeasurementsService
import org.noiseplanet.noisecapture.ui.components.measurement.SpectrogramBitmap
import org.noiseplanet.noisecapture.ui.features.measurement.SPECTROGRAM_STRIP_WIDTH

class SpectrogramPlotViewModel(
    private val measurementsService: MeasurementsService,
) : ViewModel() {

    private val rangedB = 40.0
    private val mindB = 0.0
    private val dbGain = ANDROID_GAIN

    private var canvasSize: IntSize = IntSize.Zero
    private val spectrogramBitmaps = mutableStateListOf<SpectrogramBitmap>()

    val scaleMode = SpectrogramBitmap.ScaleMode.SCALE_LOG

    val sampleRateFlow: Flow<Double> = measurementsService
        .getSpectrumDataFlow()
        .map { it.sampleRate.toDouble() }

    val currentStripData: SpectrogramBitmap?
        get() = spectrogramBitmaps.lastOrNull()
    val spectrogramBitmapFlow: StateFlow<List<SpectrogramBitmap>>
        get() = MutableStateFlow(spectrogramBitmaps)

    init {
        viewModelScope.launch {
            // Listen to spectrum data updates and build spectrogram along the way
            // TODO: We may want to pause this when the app goes into background
            measurementsService.getSpectrumDataFlow()
                .collect { spectrumData ->
                    currentStripData?.let { currentStripData ->
                        // Update current strip data
                        val newStripData = currentStripData.copy()
                        newStripData.pushSpectrumData(
                            spectrumData, mindB, rangedB, dbGain
                        )
                        spectrogramBitmaps[spectrogramBitmaps.size - 1] = newStripData

                        if (currentStripData.offset == SPECTROGRAM_STRIP_WIDTH) {
                            // Spectrogram band complete, push new band to list
                            if ((spectrogramBitmaps.size - 1) * SPECTROGRAM_STRIP_WIDTH > canvasSize.width) {
                                // remove offscreen bitmaps
                                spectrogramBitmaps.removeAt(0)
                            }
                            withContext(Dispatchers.Main) {
                                spectrogramBitmaps.add(
                                    SpectrogramBitmap(
                                        size = canvasSize,
                                        scaleMode = scaleMode,
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }

    /**
     * Updates the canvas size used to generate spectrogram bitmaps.
     * Should be called when screen size changes.
     *
     * @param newSize New canvas size.
     */
    fun updateCanvasSize(newSize: IntSize) {
        if (newSize == canvasSize) {
            return
        }
        canvasSize = newSize
        spectrogramBitmaps.clear()
        spectrogramBitmaps.add(
            SpectrogramBitmap(
                size = canvasSize,
                scaleMode = scaleMode,
            )
        )
    }
}

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
internal fun GeoScreen(
    backAction: () -> Unit
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )

    GeoScreen(
        backAction = backAction,
        viewModel = getViewModel(
            key = "geo-screen",
            factory = viewModelFactory {
                GeoViewModel(
                    locationTracker = locationTrackerFactory.createLocationTracker()
                )
            }
        )
    )
}

@Composable
private fun GeoScreen(
    backAction: () -> Unit,
    viewModel: GeoViewModel
) = NavigationScreen(title = "moko-geo", backAction = backAction) { paddingValues ->
    BindLocationTrackerEffect(viewModel.locationTracker)

    val text: String by viewModel.result.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)

        Button(onClick = viewModel::onStartClick) {
            Text(text = "Start")
        }

        Button(onClick = viewModel::onStopClick) {
            Text(text = "Stop")
        }
    }
}

internal class GeoViewModel(
    val locationTracker: LocationTracker
) : ViewModel() {
    private val _result: MutableStateFlow<String> = MutableStateFlow("press button")
    val result: StateFlow<String> get() = _result

    init {
        locationTracker.getLocationsFlow()
            .onEach { _result.value = it.toString() }
            .launchIn(viewModelScope)
    }

    fun onStartClick() {
        viewModelScope.launch {
            try {
                locationTracker.startTracking()
            } catch (exc: Exception) {
                _result.value = exc.toString()
            }
        }
    }

    fun onStopClick() {
        locationTracker.stopTracking()
    }
}

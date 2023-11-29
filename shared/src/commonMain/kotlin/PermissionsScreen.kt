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
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.math.round
import kotlin.math.sqrt

@Composable
internal fun PermissionsScreen(
    backAction: () -> Unit
) {
    val permissionsControllerFactory: PermissionsControllerFactory =
        rememberPermissionsControllerFactory()

    PermissionsScreen(
        backAction = backAction,
        viewModel = getViewModel(
            key = "permissions-screen",
            factory = viewModelFactory {
                PermissionsViewModel(permissionsControllerFactory.createPermissionsController())
            }
        )
    )
}

@Composable
private fun PermissionsScreen(
    backAction: () -> Unit,
    viewModel: PermissionsViewModel
) = NavigationScreen(title = "moko-permissions", backAction = backAction) { paddingValues ->
    BindEffect(viewModel.permissionsController)

    val state: String by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state)

        Button(onClick = viewModel::onButtonLocationPermClick) {
            Text(text = "Request Location")
        }
        Button(onClick = viewModel::onButtonRecordPermClick) {
            Text(text = "Request Audio Record")
        }
        Button(onClick = viewModel::onButtonRecordStartRecordClick) {
            Text(text = "Start Audio Record")
        }
        Button(onClick = viewModel::onButtonRecordStopRecordClick) {
            Text(text = "Stop Audio Record")
        }
    }
}

internal class PermissionsViewModel(
    val permissionsController: PermissionsController
) : ViewModel() {
    private val _state: MutableStateFlow<String> = MutableStateFlow("press button")
    val state: StateFlow<String> get() = _state
    val audioSource = createAudioSource()

    fun onButtonLocationPermClick() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)

                _state.value = "location permission granted"
            } catch (exc: RequestCanceledException) {
                _state.value = "location permission cancelled $exc"
            } catch (exc: DeniedException) {
                _state.value = "location permission denied $exc"
            }
        }
    }
    fun onButtonRecordPermClick() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.RECORD_AUDIO)
                _state.value = "audio permission granted"
            } catch (exc: RequestCanceledException) {
                _state.value = "audio permission cancelled $exc"
            } catch (exc: DeniedException) {
                _state.value = "audio permission denied $exc"
            }
        }
    }
    fun onButtonRecordStartRecordClick() {
        audioSource.setup(48000, 8192, callback = {
                samples: FloatArray ->
            val spl = 10*log10(samples.map { it * it }.average())
            println(spl)
            viewModelScope.launch {
                _state.value = "${round(spl*100)/100} dB"
            }
        })
    }


    fun onButtonRecordStopRecordClick() {
        audioSource.release()
    }
}


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.bumble.appyx.navigation.integration.ScreenSize
import com.bumble.appyx.navigation.integration.WebNodeHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import org.noise_planet.noisecapture.JsAudioSource
import org.noise_planet.noisecapture.shared.MeasurementService
import org.noise_planet.noisecapture.shared.initKoin
import org.noise_planet.noisecapture.shared.root.RootNode
import org.noise_planet.noisecapture.shared.ui.theme.AppyxStarterKitTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val events: Channel<Unit> = Channel()
    onWasmReady {
        CanvasBasedWindow() {
            AppyxStarterKitTheme {
                val requester = remember { FocusRequester() }
                var hasFocus by remember { mutableStateOf(false) }

                var screenSize by remember { mutableStateOf(ScreenSize(0.dp, 0.dp)) }
                val eventScope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
                val logger = PrintLogger()
                WebNodeHost(
                    screenSize = screenSize,
                    onBackPressedEvents = events.receiveAsFlow(),
                    modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged { screenSize = ScreenSize(it.width.dp, it.height.dp) }
                        .onKeyEvent { event ->
                            onKeyEvent(event, events, eventScope)
                        }
                        .focusRequester(requester)
                        .focusable()
                        .onFocusChanged { hasFocus = it.hasFocus },
                ) {
                        buildContext ->
                    val koinApplication = initKoin(additionalModules = listOf(module {
                        factory<MeasurementService> { MeasurementService(JsAudioSource(), logger) }
                    } )).logger(logger)
                    RootNode(
                        buildContext = buildContext,
                        koin = koinApplication.koin
                    )
                }
                if (!hasFocus) {
                    LaunchedEffect(Unit) {
                        requester.requestFocus()
                    }
                }
            }
        }
    }
}

private fun onKeyEvent(
    keyEvent: KeyEvent,
    events: Channel<Unit>,
    coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
): Boolean =
    when {
        keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace -> {
            coroutineScope.launch { events.send(Unit) }
            true
        }

        else -> false
    }

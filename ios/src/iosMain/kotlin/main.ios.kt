import androidx.compose.foundation.background
import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeUIViewController
import com.bumble.appyx.navigation.integration.IosNodeHost
import com.bumble.appyx.navigation.integration.MainIntegrationPoint
import org.noise_planet.noisecapture.shared.root.RootNode
import org.noise_planet.noisecapture.shared.ui.theme.AppyxStarterKitTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.dsl.module
import org.noise_planet.noisecapture.AudioSource
import org.noise_planet.noisecapture.shared.initKoin

val backEvents: Channel<Unit> = Channel()

private val integrationPoint = MainIntegrationPoint()

@Suppress("FunctionNaming", "Unused")
fun MainViewController() = ComposeUIViewController {

    val koinApplication = initKoin( )

    AppyxStarterKitTheme {
        Scaffold(
            modifier = Modifier
                .background(Color.Black)
        ) {
            IosNodeHost(
                modifier = Modifier,
                onBackPressedEvents = backEvents.receiveAsFlow(),
                integrationPoint = remember { integrationPoint }
            ) { buildContext ->
                RootNode(
                    buildContext = buildContext,
                    koin = koinApplication.koin
                )
            }
        }
    }
}.also { uiViewController ->
    integrationPoint.setViewController(uiViewController)
}

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.MediaPickerControllerFactory
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.media.compose.toImageBitmap
import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.media.picker.MediaSource
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
internal fun MediaScreen(
    backAction: () -> Unit
) {
    val mediaPickerControllerFactory: MediaPickerControllerFactory =
        rememberMediaPickerControllerFactory()

    MediaScreen(
        backAction = backAction,
        viewModel = getViewModel(
            key = "media-screen",
            factory = viewModelFactory {
                MediaViewModel(
                    mediaPickerController = mediaPickerControllerFactory.createMediaPickerController()
                )
            }
        )
    )
}

@Composable
private fun MediaScreen(
    backAction: () -> Unit,
    viewModel: MediaViewModel
) = NavigationScreen(title = "moko-media", backAction = backAction) { paddingValues ->
    BindMediaPickerEffect(viewModel.mediaPickerController)

    val image: Bitmap? by viewModel.image.collectAsState()
    val imageBitmap: ImageBitmap? = remember(image) { image?.toImageBitmap() }

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageBitmap != null) {
            Image(bitmap = imageBitmap, contentDescription = null)
        }

        Button(onClick = viewModel::onButtonClick) {
            Text(text = "Click on me")
        }
    }
}

internal class MediaViewModel(
    val mediaPickerController: MediaPickerController
) : ViewModel() {
    private val _image: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val image: StateFlow<Bitmap?> get() = _image

    fun onButtonClick() {
        viewModelScope.launch {
            try {
                _image.value = mediaPickerController.pickImage(MediaSource.GALLERY)
            } catch (exc: Exception) {
                println("error $exc")
            }
        }
    }
}

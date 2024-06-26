package org.noise_planet.noisecapture.shared.child

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adrianwitaszak.kmmpermissions.permissions.model.Permission
import com.adrianwitaszak.kmmpermissions.permissions.model.PermissionState
import com.adrianwitaszak.kmmpermissions.permissions.service.PermissionsService
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.noise_planet.noisecapture.shared.ui.theme.Check
import org.noise_planet.noisecapture.shared.ui.theme.Close
import org.noise_planet.noisecapture.shared.ui.theme.QuestionMark

class PermissionScreen(buildContext: BuildContext,
                       private val permissionsService: PermissionsService,
    private val onNextClick: () -> Unit) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val scope = rememberCoroutineScope()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 64.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column {
                            Text(
                                text = "Permissions",
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.onSurface,
                            )
                            Divider()
                            Text(
                                text = "In order to measure the sound level and place it on a map the application need to have access to sensitive sensors. Please tap on Request buttons.",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSurface,
                            )
                        }
                    }
                    val requiredPermissions = arrayOf(Permission.RECORD_AUDIO, Permission.LOCATION_SERVICE_ON, Permission.LOCATION_FOREGROUND, Permission.LOCATION_BACKGROUND)
                    item {
                        nextPanelAfterGranted(requiredPermissions, onNextClick = onNextClick, permissionsService)
                    }
                    items(requiredPermissions) { permission ->
                        val permissionState by permissionsService.checkPermissionFlow(permission)
                            .collectAsState(PermissionState.NOT_DETERMINED)
                        if(permissionState != PermissionState.NO_PERMISSION_DELEGATE) {
                            permissionItem(
                                permissionName = permission.name,
                                permissionState = permissionState,
                                onRequestClick = {
                                    scope.launch {
                                        permissionsService.providePermission(permission)
                                    }
                                },
                                onOpenSettingsClick = {
                                    permissionsService.openSettingPage(permission)
                                },
                            )
                        }
                    }
                }
            }
        }
}

@Composable
internal fun nextPanelAfterGranted(permissions: Array<Permission>, onNextClick: () -> Unit,
                                   permissionsService: PermissionsService) {
    val permissionsState by combine(permissions.map { p-> permissionsService.checkPermissionFlow(p)},
        transform = {it.all { p -> p != PermissionState.DENIED }})
        .collectAsState(false)
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.fillMaxSize())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(onClick = onNextClick, enabled = permissionsState) {
                Text("Next")
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
internal fun permissionItem(
    permissionName: String,
    permissionState: PermissionState,
    onRequestClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
) {
    val colorState by animateColorAsState(
        when (permissionState) {
            PermissionState.GRANTED -> Color.Green
            PermissionState.NOT_DETERMINED -> Color.Gray
            else -> Color.Red
        }
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = permissionName,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = when (permissionState) {
                    PermissionState.GRANTED -> Check
                    PermissionState.NOT_DETERMINED -> QuestionMark
                    else -> Close
                },
                tint = colorState,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Button(
                onClick = onOpenSettingsClick,
            ) {
                Text(
                    text = "Settings",
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
        AnimatedVisibility(permissionState.notGranted()) {
            Button(
                onClick = onRequestClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Request",
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}

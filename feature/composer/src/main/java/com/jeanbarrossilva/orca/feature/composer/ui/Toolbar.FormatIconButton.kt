package com.jeanbarrossilva.orca.feature.composer.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

@Composable
internal fun FormatIconButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val role = Role.Switch
    val contentColor by animateColorAsState(
        if (isEnabled) OrcaTheme.colors.brand.container else LocalContentColor.current,
        label = "ContentColor"
    )

    Box(
        modifier
            .clip(OrcaTheme.shapes.small)
            .clickable(role = role, onClick = onClick)
            .padding(4.dp)
            .size(24.dp)
            .semantics {
                this.role = role
                toggleableState = if (isEnabled) ToggleableState.On else ToggleableState.Off
            },
        Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DisabledFormatIconButtonPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            FormatIconButton(isEnabled = false)
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EnabledFormatIconButtonPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            FormatIconButton(isEnabled = true)
        }
    }
}

@Composable
private fun FormatIconButton(isEnabled: Boolean, modifier: Modifier = Modifier) {
    FormatIconButton(isEnabled, onClick = { }, modifier) {
        Icon(OrcaTheme.Icons.AddAPhoto, contentDescription = "Add photo")
    }
}
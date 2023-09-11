package com.jeanbarrossilva.orca.feature.tootdetails.ui.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

@Composable
internal fun Stat(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier,
        Arrangement.spacedBy(OrcaTheme.spacings.small),
        Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides OrcaTheme.colors.secondary,
            LocalTextStyle provides OrcaTheme.typography.bodySmall
        ) {
            content()
        }
    }
}

@Composable
@MultiThemePreview
private fun StatPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            Stat {
                Icon(OrcaTheme.iconography.comment.outlined, contentDescription = "Comments")
                Text("8")
            }
        }
    }
}

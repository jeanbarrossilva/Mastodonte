package com.jeanbarrossilva.orca.core.http.auth.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/**
 * Visually notifies that authorization is running in the background.
 *
 * @param modifier [Modifier] to be applied to the underlying [Surface].
 */
@Composable
internal fun HttpAuthorization(modifier: Modifier = Modifier) {
    Surface(modifier, color = OrcaTheme.colors.background.container) {
        Column(
            Modifier.fillMaxSize(),
            Arrangement.spacedBy(OrcaTheme.spacings.medium, Alignment.CenterVertically),
            Alignment.CenterHorizontally
        ) {
            Icon(OrcaTheme.iconography.login, contentDescription = "Link", Modifier.size(64.dp))

            Text(
                "Authorizing...",
                textAlign = TextAlign.Center,
                style = OrcaTheme.typography.headlineLarge
            )
        }
    }
}

/** Preview of an [HttpAuthorization]. **/
@Composable
@MultiThemePreview
private fun HttpAuthorizationPreview() {
    OrcaTheme {
        HttpAuthorization()
    }
}
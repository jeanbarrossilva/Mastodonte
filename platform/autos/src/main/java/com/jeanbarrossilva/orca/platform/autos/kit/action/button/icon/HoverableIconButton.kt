/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [IconButton] that gets visually highlighted when it's hovered.
 *
 * @param onClick Callback run whenever this [HoverableIconButton] is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Hoverable].
 * @param content [Icon] to be shown.
 */
@Composable
fun HoverableIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val interactionSource = remember {
    IgnoringMutableInteractionSource(PressInteraction.Press::class, HoverInteraction::class)
  }

  Hoverable(modifier) {
    IconButton(onClick, interactionSource = interactionSource, content = content)
  }
}

@Composable
@MultiThemePreview
private fun HoverableIconButtonPreview() {
  AutosTheme {
    HoverableIconButton(onClick = {}) {
      Icon(AutosTheme.iconography.home.outlined.asImageVector, contentDescription = "Home")
    }
  }
}

/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.feature.gallery.test.ui

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import br.com.orcinus.orca.feature.gallery.ui.Actions
import br.com.orcinus.orca.feature.gallery.ui.Gallery
import br.com.orcinus.orca.feature.gallery.ui.GalleryActionsCloseButtonTag
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton

/** [SemanticsNodeInteraction] of a [Gallery]'s [Actions]' close [HoverableIconButton]. */
fun SemanticsNodeInteractionsProvider.onCloseActionButton(): SemanticsNodeInteraction {
  return onNodeWithTag(GalleryActionsCloseButtonTag)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [HorizontalPager].
 *
 * @see isPager
 */
fun SemanticsNodeInteractionsProvider.onPager(): SemanticsNodeInteraction {
  return onNode(isPager())
}

/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.markdown.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import br.com.orcinus.orca.platform.markdown.MarkdownTextField

/**
 * [SemanticsNodeInteraction] of a [MarkdownTextField].
 *
 * @see isMarkdownTextField
 */
fun SemanticsNodeInteractionsProvider.onMarkdownTextField(): SemanticsNodeInteraction {
  return onNode(isMarkdownTextField())
}

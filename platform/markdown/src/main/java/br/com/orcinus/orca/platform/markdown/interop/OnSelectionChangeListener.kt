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

package br.com.orcinus.orca.platform.markdown.interop

/**
 * Listener to be notified of changes in text selection.
 *
 * @see onSelectionChange
 */
internal fun interface OnSelectionChangeListener {
  /**
   * Callback called whenever text selection is changed.
   *
   * @param selection [IntRange] containing the indices encompassed by the selected portion of text.
   */
  fun onSelectionChange(selection: IntRange)
}

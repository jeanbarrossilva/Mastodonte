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

package com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.test

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.GALLERY_PREVIEW_TAG
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.GalleryPreview

/** [SemanticsMatcher] that matches a [GalleryPreview]. */
internal fun isGalleryPreview(): SemanticsMatcher {
  return hasTestTag(GALLERY_PREVIEW_TAG)
}

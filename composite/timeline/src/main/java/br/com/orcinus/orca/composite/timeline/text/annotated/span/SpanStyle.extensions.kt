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

@file:JvmName("SpanStyles")

package br.com.orcinus.orca.composite.timeline.text.annotated.span

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.isUnspecified
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.text.annotated.span.category.Categorizer
import br.com.orcinus.orca.composite.timeline.text.annotated.toAnnotatedString
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

/**
 * [SpanStyle] into which an emboldened portion within [Markdown] will be turned when converting it
 * into an [AnnotatedString].
 *
 * @see Style.Bold
 * @see toAnnotatedString
 */
@JvmField internal val BoldSpanStyle = SpanStyle(fontWeight = FontWeight.Bold)

/**
 * [SpanStyle] into which an italicized portion within [Markdown] will be turned when converting it
 * into an [AnnotatedString].
 *
 * @see Style.Italic
 * @see toAnnotatedString
 */
@JvmField internal val ItalicSpanStyle = SpanStyle(fontStyle = FontStyle.Italic)

/**
 * Creates a [SpanStyle] into which a [Style.Link] within [Markdown] will be turned when converting
 * it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @param category Describes the text being styled; should be created by the [Categorizer].
 * @see toAnnotatedString
 * @see category
 */
internal fun createLinkSpanStyle(colors: Colors, category: String): SpanStyle {
  return SpanStyle(colors.link.asColor, fontFeatureSettings = category)
}

/**
 * Returns whether the given [SpanStyle] contains all of the attributes that this one has set as
 * being explicitly defined (that is, neither an "unspecified" instance of the object, such as
 * [Color.Unspecified], nor `null`).
 *
 * @param other [SpanStyle] to compare the receiver one with.
 */
internal operator fun SpanStyle.contains(other: SpanStyle): Boolean {
  return (other.background.isUnspecified || background == other.background) &&
    (other.baselineShift == null || baselineShift == other.baselineShift) &&
    (other.brush == null || brush == other.brush) &&
    (other.color.isUnspecified || color == other.color) &&
    (other.drawStyle == null || drawStyle == other.drawStyle) &&
    (other.fontFamily == null || fontFamily == other.fontFamily) &&
    (other.fontFeatureSettings == null || fontFeatureSettings == other.fontFeatureSettings) &&
    (other.fontSize.isUnspecified || fontSize == other.fontSize) &&
    (other.fontStyle == null || fontStyle == other.fontStyle) &&
    (other.fontSynthesis == null || fontSynthesis == other.fontSynthesis) &&
    (other.fontWeight == null || fontWeight == other.fontWeight) &&
    (other.letterSpacing.isUnspecified || letterSpacing == other.letterSpacing) &&
    (other.localeList?.let { oll -> localeList?.let { ll -> oll.containsAll(ll) } } ?: true) &&
    (other.platformStyle == null || platformStyle == other.platformStyle) &&
    (other.shadow == null || shadow == other.shadow) &&
    (other.textDecoration == null || textDecoration == other.textDecoration)
}

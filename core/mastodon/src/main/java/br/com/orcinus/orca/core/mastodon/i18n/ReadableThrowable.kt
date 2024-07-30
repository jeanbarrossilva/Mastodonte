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

package br.com.orcinus.orca.core.mastodon.i18n

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import java.util.Locale

/**
 * [Throwable] that defines a [Locale]-specific message which explains an issue in non-technical
 * vocabulary for non-engineers to be able to understand plainly what has happened and (possibly)
 * take action regarding the problem.
 *
 * All [Exception]s directly thrown by core structures in the Mastodon variant should have a
 * [ReadableThrowable] as a [cause] as a mean to provide a description of the occurrence in all
 * supported languages. Such [Exception]s are intended to be caught and have their [cause]s
 * presented to the user in the UI, explained by the localized version of the message.
 *
 * @property context [Context] from which default and localized messages are to be obtained.
 * @property messageResourceID Resource identifier of the message that explains in practical,
 *   intelligible terms what has gone wrong and, when applicable, the steps that can be taken by the
 *   user for the issue to be resolved. These instructions shall not rely on client-specific
 *   environment and/or configuration, as they may change over time and might make very accurate
 *   descriptions outdated rapidly; instead, focus on the *operations* that can be performed to
 *   mitigate the drawback, since these are supposed to be intuitively doable from the UI (e. g.,
 *   prefer "Feed couldn't be loaded at this moment. Try again by reloading." instead of "An error
 *   prevented the feed from being loaded. Swipe from the top to the bottom of the screen to
 *   reload.")
 *
 *   If this identifier isn't that of a [String] resource, a [Resources.NotFoundException] will be
 *   thrown when either the default or the localized message is retrieved.
 *
 * @see message
 * @see getLocalizedMessage
 */
internal class ReadableThrowable(
  private val context: Context,
  @StringRes private val messageResourceID: Int
) : Throwable() {
  override val message
    @Throws(IllegalStateException::class, Resources.NotFoundException::class)
    get() = context.getDefaultString(messageResourceID)

  @Throws(Resources.NotFoundException::class)
  override fun getLocalizedMessage(): String {
    return context.getString(messageResourceID)
  }
}

/**
 * Obtains a [String] in the default [Locale].
 *
 * @param resourceID Resource ID of the [String] to be obtained.
 * @throws IllegalStateException If either creating the [Context] with the default [Locale] returns
 *   `null` or such [Context]'s [Resources] are `null`.
 * @throws Resources.NotFoundException If a [String] identified with the [resourceID] isn't found.
 * @see Locale.getDefault
 * @see Context.getResources
 */
@Throws(IllegalStateException::class, Resources.NotFoundException::class)
private fun Context.getDefaultString(@StringRes resourceID: Int): String {
  val locale = Locale.getDefault()
  return at(locale).getString(resourceID)
}

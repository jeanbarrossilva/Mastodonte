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

package br.com.orcinus.orca.core.mastodon.test.auth.authorization

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithText
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.platform.autos.kit.action.button.SecondaryButton
import br.com.orcinus.orca.platform.testing.asString

/**
 * [SemanticsNodeInteraction] of the [SecondaryButton] that requests an [Account] to be registered.
 */
fun SemanticsNodeInteractionsProvider.onRegisterButton(): SemanticsNodeInteraction {
  return onNodeWithText(
    br.com.orcinus.orca.core.mastodon.R.string.core_mastodon_authorization_register.asString()
  )
}

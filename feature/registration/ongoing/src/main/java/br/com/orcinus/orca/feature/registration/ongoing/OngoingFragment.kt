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

package br.com.orcinus.orca.feature.registration.ongoing

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.argument
import br.com.orcinus.orca.platform.navigation.transition.opening
import br.com.orcinus.orca.std.injector.Injector

class OngoingFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<OngoingModule>() }
  private val email by argument<String>(EMAIL_KEY)
  private val password by argument<String>(PASSWORD_KEY)
  private val viewModel by
    viewModels<OngoingViewModel> {
      OngoingViewModel.createFactory(module.registrar(), email, password)
    }

  constructor(email: String, password: String) : this() {
    arguments = bundleOf(EMAIL_KEY to email, PASSWORD_KEY to password)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.register()
  }

  @Composable
  override fun Content() {
    Ongoing(viewModel)
  }

  companion object {
    private const val EMAIL_KEY = "email"
    private const val PASSWORD_KEY = "password"

    const val ROUTE = "ongoing"

    fun navigate(navigator: Navigator, email: String, password: String) {
      navigator.navigate(opening()) { to(ROUTE) { OngoingFragment(email, password) } }
    }
  }
}

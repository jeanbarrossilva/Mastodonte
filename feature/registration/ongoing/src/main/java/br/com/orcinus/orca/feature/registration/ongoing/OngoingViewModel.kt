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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.core.instance.registration.Registrar
import br.com.orcinus.orca.core.instance.registration.Registration
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.flow.loadable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class OngoingViewModel(
  private val registrar: Registrar,
  private val email: String,
  private val password: String
) : ViewModel() {
  private val registrationLoadableMutableFlow = MutableSharedFlow<Loadable<Registration>>()

  val registrationLoadableFlow =
    registrationLoadableMutableFlow.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(),
      Loadable.Loading()
    )

  fun register() {
    viewModelScope.launch {
      registrar.register(email, password).loadable(this).collect(registrationLoadableMutableFlow)
    }
  }

  companion object {
    fun createFactory(
      registrar: Registrar,
      email: String,
      password: String
    ): ViewModelProvider.Factory {
      return viewModelFactory { initializer { OngoingViewModel(registrar, email, password) } }
    }
  }
}

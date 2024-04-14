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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.orcinus.orca.composite.status.Status
import br.com.orcinus.orca.composite.status.StatusCard
import br.com.orcinus.orca.composite.status.state.rememberStatusCardState
import br.com.orcinus.orca.core.instance.registration.Registration
import br.com.orcinus.orca.core.sample.instance.registration.sample
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.template.onboarding.Onboarding
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.stack.Stack
import br.com.orcinus.orca.platform.stack.StackScope
import com.jeanbarrossilva.loadable.Loadable

@Composable
internal fun Ongoing(viewModel: OngoingViewModel, modifier: Modifier = Modifier) {
  when (val registrationLoadable = viewModel.registrationLoadableFlow.collectAsState().value) {
    is Loadable.Loading -> Ongoing(modifier)
    is Loadable.Loaded -> Ongoing(registrationLoadable.content, modifier)
    is Loadable.Failed -> Unit
  }
}

@Composable
private fun Ongoing(modifier: Modifier = Modifier) {
  Ongoing(modifier) { CircularProgressIndicator() }
}

@Composable
private fun Ongoing(registration: Registration, modifier: Modifier = Modifier) {
  var statusCardStackScope by remember { mutableStateOf<StackScope?>(null) }
  val statusCardAnimationSpec = remember { tween<Float>() }
  val statusCardEnterTransition =
    remember(statusCardAnimationSpec) {
      fadeIn(statusCardAnimationSpec) + scaleIn(statusCardAnimationSpec, initialScale = .8f)
    }

  LaunchedEffect(registration, statusCardStackScope, statusCardEnterTransition) {
    if (statusCardStackScope != null) {
      statusCardStackScope?.item {
        AnimatedVisibility(
          remember { MutableTransitionState(false).apply { targetState = true } },
          enter = statusCardEnterTransition
        ) {
          StatusCard(
            rememberStatusCardState(
              targetStatus = if (registration.hasSucceeded) Status.Succeeded else Status.Failed
            )
          ) {
            Text("${registration.domain}")
          }
        }
      }
    }
  }

  Ongoing(modifier) { Stack { statusCardStackScope = this } }
}

@Composable
private fun Ongoing(modifier: Modifier = Modifier, illustration: @Composable () -> Unit) {
  Scaffold(modifier) {
    expanded {
      Onboarding(
        illustration,
        title = { Text(stringResource(R.string.feature_registration_ongoing)) },
        description = { Text(stringResource(R.string.feature_registration_ongoing_description)) },
        contentPadding = it
      )
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingOngoingPreview() {
  AutosTheme { Ongoing() }
}

@Composable
@MultiThemePreview
private fun LoadedOngoingPreview() {
  AutosTheme { Ongoing(Registration.sample) }
}

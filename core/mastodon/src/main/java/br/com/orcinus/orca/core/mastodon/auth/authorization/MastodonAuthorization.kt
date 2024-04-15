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

package br.com.orcinus.orca.core.mastodon.auth.authorization

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import br.com.orcinus.orca.platform.autos.kit.action.button.PrimaryButton
import br.com.orcinus.orca.platform.autos.kit.action.button.SecondaryButton
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.error.containsErrorsAsState
import br.com.orcinus.orca.platform.autos.kit.input.text.error.rememberErrorDispatcher
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.kit.scaffold.plus
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester

/**
 * Screen that asks for the [Domain] to which the user belongs (and to which an empty one is given).
 *
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
@VisibleForTesting
fun MastodonAuthorization(modifier: Modifier = Modifier) {
  MastodonAuthorization(
    domain = "",
    onDomainChange = {},
    onNavigationToRegistration = {},
    onSignIn = {},
    modifier
  )
}

/**
 * Screen that asks for the [Domain] to which the user belongs.
 *
 * @param viewModel [MastodonAuthorizationViewModel] to which updates to the inserted [Domain] will
 *   be sent.
 * @param onNavigationToRegistration Action to be performed when navigation to registration is
 *   requested.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun MastodonAuthorization(
  viewModel: MastodonAuthorizationViewModel,
  onNavigationToRegistration: () -> Unit,
  modifier: Modifier = Modifier
) {
  val domain by viewModel.domainFlow.collectAsState()

  MastodonAuthorization(
    domain,
    onDomainChange = viewModel::setDomain,
    onNavigationToRegistration,
    onSignIn = viewModel::authorize,
    modifier
  )
}

/**
 * Screen that asks for the [Domain] to which the user belongs.
 *
 * @param domain [String] version of the [Domain].
 * @param onDomainChange Callback run whenever the user inputs to the domain [FormTextField].
 * @param onSignIn Callback run whenever the sign-in [PrimaryButton] is clicked.
 * @param onNavigationToRegistration Action to be performed when navigation to registration is
 *   requested.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun MastodonAuthorization(
  domain: String,
  onDomainChange: (domain: String) -> Unit,
  onNavigationToRegistration: () -> Unit,
  onSignIn: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val spacing = AutosTheme.spacings.extraLarge.dp

  @OptIn(ExperimentalMaterial3Api::class)
  val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  val lazyListState = rememberLazyListState()
  val isHeaderHidden by
    remember(lazyListState) { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }
  val headerTitle = stringResource(R.string.core_http_authorization_account_origin)
  val focusRequester = rememberImmediateFocusRequester()
  val errorDispatcher = rememberErrorDispatcher {
    error(context, R.string.core_http_authorization_empty_domain, String::isBlank)
    error(context, R.string.core_http_authorization_invalid_domain) {
      it.isNotBlank() && !Domain.isValid(it)
    }
  }
  val containsErrors by errorDispatcher.containsErrorsAsState
  val onDone by rememberUpdatedState {
    errorDispatcher.dispatch()
    if (!containsErrors) {
      onSignIn()
    }
  }

  Box(modifier) {
    Scaffold(
      bottom = {
        Column(verticalArrangement = Arrangement.spacedBy(ButtonBarDefaults.spacing)) {
          FormTextField(
            domain,
            onDomainChange,
            Modifier.focusRequester(focusRequester)
              .padding(start = ButtonBarDefaults.spacing, end = ButtonBarDefaults.spacing)
              .fillMaxWidth(),
            errorDispatcher,
            KeyboardOptions(imeAction = ImeAction.Go),
            KeyboardActions(onGo = { onDone() }),
            isSinglyLined = true
          ) {
            Text(stringResource(R.string.core_http_authorization_domain))
          }

          ButtonBar(lazyListState) {
            PrimaryButton(onClick = onDone, isEnabled = !containsErrors) {
              Text(stringResource(R.string.core_http_authorization_sign_in))
            }

            SecondaryButton(onClick = onNavigationToRegistration) {
              Text(stringResource(R.string.core_mastodon_authorization_register))
            }
          }
        }
      }
    ) {
      expanded {
        LazyColumn(
          state = lazyListState,
          contentPadding = it + PaddingValues(spacing),
          verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.small.dp)
        ) {
          item {
            Text(
              stringResource(R.string.core_http_authorization_welcome),
              textAlign = TextAlign.Center,
              style = AutosTheme.typography.headlineLarge
            )
          }

          item {
            Text(
              headerTitle,
              textAlign = TextAlign.Center,
              style = AutosTheme.typography.titleSmall
            )
          }
        }
      }
    }

    AnimatedVisibility(
      visible = isHeaderHidden,
      enter = slideInVertically { -it },
      exit = slideOutVertically { -it }
    ) {
      Column {
        @OptIn(ExperimentalMaterial3Api::class)
        TopAppBar(title = { AutoSizeText(headerTitle) }, scrollBehavior = topAppBarScrollBehavior)

        HorizontalDivider()
      }
    }
  }
}

/** Preview of [MastodonAuthorization]. */
@Composable
@MultiThemePreview
private fun MastodonAuthorizationPreview() {
  AutosTheme { MastodonAuthorization() }
}

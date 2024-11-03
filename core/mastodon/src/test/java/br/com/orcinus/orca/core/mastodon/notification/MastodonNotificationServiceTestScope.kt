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

package br.com.orcinus.orca.core.mastodon.notification

import android.content.Context
import android.content.Intent
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.AuthenticatedRequester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.mastodon.notification.security.Locksmith
import br.com.orcinus.orca.platform.testing.context
import io.ktor.client.HttpClient
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import org.robolectric.android.controller.ServiceController

/**
 * Default implementation of a [MastodonNotificationServiceTestScope] which will be used in the
 * tests.
 */
private class MastodonNotificationServiceEnvironment(
  override val requester: AuthenticatedRequester,
  override val clientResponseProvider: MastodonNotificationsClientResponseProvider,
  override val context: Context,
  delegate: TestScope
) : MastodonNotificationServiceTestScope(), CoroutineScope by delegate

/** Scope in which a [MastodonNotificationService] test is run. */
internal sealed class MastodonNotificationServiceTestScope : CoroutineScope {
  /** [ServiceController] by which the lifecycle of the [MastodonNotificationService] is managed. */
  private val controller by lazy {
    MastodonNotificationService(requester, requester.lock, Locksmith())
      .apply { setCoroutineContext(coroutineContext) }
      .let { ServiceController.of(it, Intent(context, it::class.java)) }
  }

  /** [Context] in which the [service] is instantiated. */
  protected abstract val context: Context

  /** [AuthenticatedRequester] by which subscriptions are pushed. */
  abstract val requester: AuthenticatedRequester

  /** [MastodonNotificationsClientResponseProvider] by which responses to requests are provided. */
  abstract val clientResponseProvider: MastodonNotificationsClientResponseProvider

  /** [MastodonNotificationService] being tested. */
  val service: MastodonNotificationService
    get() = controller.get()

  /**
   * Creates the [service].
   *
   * @see MastodonNotificationService.onCreate
   */
  fun create() {
    controller.create()
  }

  /**
   * Destroys the [service].
   *
   * @see MastodonNotificationService.onDestroy
   */
  fun destroy() {
    controller.destroy()
  }
}

/**
 * Runs a [MastodonNotificationService]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param coroutineContext [CoroutineContext] in which [Job]s are launched by default.
 * @param body Testing to be performed on a [MastodonNotificationService].
 */
@OptIn(ExperimentalContracts::class)
internal fun runMastodonNotificationServiceTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  onAuthentication: () -> Unit = {},
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  body: suspend MastodonNotificationServiceTestScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  lateinit var baseURI: URI

  /*
   * Responses are provided by a MastodonNotificationsClientResponseProvider, which responds to
   * requests that aim to obtain the notifications received from the Mastodon server. The specified
   * provider is used only if they do not.
   */
  val defaultClientResponseProvider =
    MastodonNotificationsClientResponseProvider({ baseURI }, next = clientResponseProvider)

  runAuthenticatedRequesterTest(defaultClientResponseProvider, onAuthentication, coroutineContext) {
    baseURI = requester.baseURI
    MastodonNotificationServiceEnvironment(
        requester,
        defaultClientResponseProvider,
        context,
        delegate
      )
      .run {
        try {
          body()
        } finally {
          destroy()
        }
      }
  }
}
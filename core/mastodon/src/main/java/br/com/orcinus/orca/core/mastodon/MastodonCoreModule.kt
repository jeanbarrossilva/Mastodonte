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

package br.com.orcinus.orca.core.mastodon

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.InstanceProvider
import br.com.orcinus.orca.core.instance.registration.Registrar
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.std.injector.module.Inject
import br.com.orcinus.orca.std.injector.module.injection.Injection

/**
 * [CoreModule] into which Mastodon-specific core structures are injected.
 *
 * @param termMuter [TermMuter] by which terms will be muted.
 * @param authenticationLock [AuthenticationLock] that will lock authentication-dependent
 *   functionality behind a "wall".
 * @param registrar [Registrar] for registering an [Account].
 * @param instanceProvider [InstanceProvider] that will provide the [Instance] in which the
 *   currently [authenticated][Actor.Authenticated] [Actor] is.
 */
open class MastodonCoreModule(
  @Inject internal val instanceProvider: Injection<InstanceProvider>,
  @Inject internal val authenticationLock: Injection<SomeAuthenticationLock>,
  @Inject internal val registrar: Injection<Registrar>,
  @Inject internal val termMuter: Injection<TermMuter>
) : CoreModule(instanceProvider, authenticationLock, registrar, termMuter)

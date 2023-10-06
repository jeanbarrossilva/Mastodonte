package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.instance.HttpInstanceProvider
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.toot.muting.SharedPreferencesTermMuter
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainCoreModule : CoreModule() {
    override val dependencies: Scope.() -> Unit = {
        inject { HttpAuthorizer(context = get()) }
        inject<ActorProvider> { SharedPreferencesActorProvider(context = get()) }
        inject { HttpAuthenticator(context = get(), get<HttpAuthorizer>(), actorProvider = get()) }
        inject { SharedPreferencesTermMuter(context = get()) }
        super.dependencies(this)
    }

    override fun Injector.authenticationLock(): SomeAuthenticationLock {
        return AuthenticationLock(get<HttpAuthenticator>(), actorProvider = get())
    }

    override fun Injector.termMuter(): TermMuter {
        return SharedPreferencesTermMuter(context = get())
    }

    override fun Injector.instanceProvider(): InstanceProvider {
        return HttpInstanceProvider(context = get())
    }
}

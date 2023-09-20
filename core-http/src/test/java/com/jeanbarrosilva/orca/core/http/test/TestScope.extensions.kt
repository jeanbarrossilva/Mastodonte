package com.jeanbarrosilva.orca.core.http.test

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.CoreHttpClient
import com.jeanbarrossilva.orca.core.http.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.authenticateAndPost
import com.jeanbarrossilva.orca.core.http.authenticateAndSubmitForm
import com.jeanbarrossilva.orca.core.http.authenticateAndSubmitFormWithBinaryData
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import com.jeanbarrossilva.orca.core.test.TestAuthorizer
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequest
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] created from the [TestScope] in which the test in running, that houses
 * [CoreHttpClient]-test-related structures.
 *
 * @param T Specified [Actor] for performing the testing.
 * @param delegate [TestScope] that's been launched and will provide [CoroutineScope]-like
 * functionality to this [CoreHttpClientTestScope].
 * @param authenticationLock [AuthenticationLock] with which `authenticateAnd*` [HttpClient]
 * extension methods can be called.
 * @param client [CoreHttpClient] for executing the intended [HttpRequest]s.
 * @param actor [Actor] used when running the test.
 * @see HttpClient.authenticateAndGet
 * @see HttpClient.authenticateAndPost
 * @see HttpClient.authenticateAndSubmitForm
 * @see HttpClient.authenticateAndSubmitFormWithBinaryData
 **/
internal class CoreHttpClientTestScope<T : Actor>(
    delegate: TestScope,
    val authenticationLock: AuthenticationLock,
    val client: HttpClient,
    val actor: T
) : CoroutineScope by delegate

/**
 * Configures an environment for a [CoreHttpClient] test with an
 * [authenticated][Actor.Authenticated] [Actor], providing the proper [CoreHttpClientTestScope].
 *
 * @param body Callback run when the environment has been set up and is, therefore, ready to be
 * used.
 **/
internal fun runAuthenticatedTest(
    body: suspend CoreHttpClientTestScope<Actor.Authenticated>.() -> Unit
) {
    val actor = Actor.Authenticated(TestAuthorizer.AUTHORIZATION_CODE)
    runCoreHttpClientTest(actor, onAuthentication = { }, body)
}

/**
 * Configures an environment for a [CoreHttpClient] test with an
 * [unauthenticated][Actor.Unauthenticated] [Actor], providing the proper [CoreHttpClientTestScope].
 *
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param body Callback run when the environment has been set up and is, therefore, ready to be
 * used.
 **/
internal fun runUnauthenticatedTest(
    onAuthentication: () -> Unit = { },
    body: suspend CoreHttpClientTestScope<Actor.Unauthenticated>.() -> Unit
) {
    runCoreHttpClientTest(Actor.Unauthenticated, onAuthentication, body)
}

/**
 * Configures an environment for a [CoreHttpClient] test, providing the proper
 * [CoreHttpClientTestScope].
 *
 * @param actor [Actor] to be fixedly returned by the underlying [ActorProvider]. Determines the
 * behavior of `authenticateAnd*` calls done on the [CoreHttpClient] within the [body], given that
 * an [unauthenticated][Actor.Unauthenticated] [Actor] will be requested to be authenticated when
 * such invocations take place, while having an [authenticated][Actor.Authenticated] [Actor] would
 * simply mean that the operation derived from the [HttpMethod] will be performed.
 * @param onAuthentication Action run whenever the [Actor] is authenticated. At this point, the most
 * up-to-date [Actor] is probably different from the one in the [CoreHttpClientTestScope] given to
 * the [body].
 * @param body Callback run when the environment has been set up and is, therefore, ready to be
 * used.
 * @see HttpClient.authenticateAndGet
 * @see HttpClient.authenticateAndPost
 * @see HttpClient.authenticateAndSubmitForm
 * @see HttpClient.authenticateAndSubmitFormWithBinaryData
 **/
private fun <T : Actor> runCoreHttpClientTest(
    actor: T,
    onAuthentication: () -> Unit,
    body: suspend CoreHttpClientTestScope<T>.() -> Unit
) {
    val actorProvider = FixedActorProvider(actor)
    val authenticator = TestAuthenticator { onAuthentication() }
    val engine = MockEngine { respondOk() }
    val engineFactory = object : HttpClientEngineFactory<MockEngineConfig> {
        override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
            return engine
        }
    }
    val authenticationLock = AuthenticationLock(authenticator, actorProvider)
    val client = CoreHttpClient(engineFactory, TestLogger)
    runTest {
        CoreHttpClientTestScope(delegate = this, authenticationLock, client, actor).body()
    }
}

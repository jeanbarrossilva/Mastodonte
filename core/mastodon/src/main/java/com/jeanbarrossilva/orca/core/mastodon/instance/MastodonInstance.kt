package com.jeanbarrossilva.orca.core.mastodon.instance

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.mastodon.client.CoreHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.set
import io.ktor.http.takeFrom

/** An [MastodonInstance] with a generic [Authorizer] and an [Authenticator]. */
internal typealias SomeHttpInstance = MastodonInstance<*, *>

/**
 * [Instance] that performs all of its underlying operations by sending [HttpRequest]s to the API.
 *
 * @param F [Authorizer] with which authorization will be performed.
 * @param S [Authenticator] to authenticate the user with.
 * @param authorizer [Authorizer] by which the user will be authorized.
 */
abstract class MastodonInstance<F : Authorizer, S : Authenticator>(
  final override val domain: Domain,
  internal val authorizer: F
) : Instance<S>() {
  /** [Url] to which routes will be appended when [HttpRequest]s are sent. */
  internal val url = URLBuilder().apply { set(scheme = "https", host = "$domain") }.build()

  /** [HttpClient] by which [HttpRequest]s will be sent. */
  open val client = CoreHttpClient { defaultRequest { url.takeFrom(this@MastodonInstance.url) } }
}

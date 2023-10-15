package com.jeanbarrossilva.orca.core.http.client

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.http.client.test.assertThatRequestAuthorizationHeaderOf
import com.jeanbarrossilva.orca.core.http.client.test.runAuthenticatedTest
import com.jeanbarrossilva.orca.core.http.client.test.runUnauthenticatedTest
import com.jeanbarrossilva.orca.core.sample.rule.SampleCoreTestRule
import io.ktor.http.parametersOf
import org.junit.Rule
import kotlin.test.Test

internal class CoreHttpClientTests {
  @get:Rule
  val sampleCoreRule = SampleCoreTestRule()

  @Test
  fun requestsAuthenticationOnAuthenticateAndGetWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndGet(route = "")
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndGetWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(client.authenticateAndGet(route = ""))
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }

  @Test
  fun requestsAuthenticationOnAuthenticateAndPostWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndPost(route = "")
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndPostWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(client.authenticateAndPost(route = ""))
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }

  @Test
  fun requestsAuthenticationOnAuthenticateAndSubmitFormWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndSubmitForm(route = "", parametersOf())
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndSubmitFormWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(
          client.authenticateAndSubmitForm(route = "", parametersOf())
        )
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }

  @Test
  fun requestsAuthenticationOnAuthenticateAndSubmitFormWithBinaryDataWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndSubmitFormWithBinaryData(route = "", formData = emptyList())
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndSubmitFormWithBinaryDataWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(
          client.authenticateAndSubmitFormWithBinaryData(route = "", formData = emptyList())
        )
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }
}

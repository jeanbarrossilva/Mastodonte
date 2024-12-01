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

package br.com.orcinus.orca.core.mastodon.notification.security

import assertk.assertThat
import assertk.assertions.first
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.core.mastodon.notification.WebPush
import java.util.Base64
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class WebPushTests {
  private val webPush = WebPush()
  private val base64Decoder: Base64.Decoder = Base64.getUrlDecoder()

  @Test
  fun clientPublicKeyIs65BytesLong() {
    assertThat(webPush)
      .prop(WebPush::base64EncodedClientPublicKey)
      .transform("decoded", base64Decoder::decode)
      .hasSize(65)
  }

  @Test
  fun clientPublicKeyIsUncompressed() {
    assertThat(webPush)
      .prop(WebPush::base64EncodedClientPublicKey)
      .transform("decoded", base64Decoder::decode)
      .prop(ByteArray::toSet)
      .first()
      .isEqualTo(WebPush.UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER)
  }

  @Test
  fun clientPublicKeyIsRandom() {
    assertThat(webPush)
      .prop(WebPush::base64EncodedClientPublicKey)
      .isNotEqualTo(WebPush().base64EncodedClientPublicKey)
  }

  @Test
  fun authenticationKeyIs16BytesLong() {
    assertThat(webPush)
      .prop(WebPush::base64EncodedAuthenticationKey)
      .transform("decoded", base64Decoder::decode)
      .hasSize(16)
  }

  @Test
  fun authenticationKeyIsRandom() {
    assertThat(webPush)
      .prop(WebPush::base64EncodedAuthenticationKey)
      .isNotEqualTo(WebPush().base64EncodedAuthenticationKey)
  }
}
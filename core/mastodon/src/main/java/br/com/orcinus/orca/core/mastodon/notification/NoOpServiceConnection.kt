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

import android.app.Service
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

/** [ServiceConnection] which does nothing upon connection or disconnection to a [Service]. */
internal object NoOpServiceConnection : ServiceConnection {
  override fun onServiceConnected(name: ComponentName?, service: IBinder?) {}

  override fun onServiceDisconnected(name: ComponentName?) {}
}
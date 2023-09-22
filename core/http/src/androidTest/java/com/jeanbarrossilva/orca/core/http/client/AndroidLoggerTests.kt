package com.jeanbarrossilva.orca.core.http.client

import android.util.Log
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test

internal class AndroidLoggerTests {
    @Test
    fun infoCallsAndroidLogI() {
        mockkStatic(Log::class) {
            Logger.android.info("😮")
            verify { Log.i(Logger.ANDROID_LOGGER_TAG, "😮") }
        }
    }

    @Test
    fun errorCallsAndroidLogE() {
        mockkStatic(Log::class) {
            Logger.android.error("😵")
            verify { Log.e(Logger.ANDROID_LOGGER_TAG, "😵") }
        }
    }
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style

/** [Style] that applies a heavier weight to the target's font. **/
data class Bold(override val indices: IntRange) : Style() {
    /** [Style.Delimiter] for emboldened portions of a [String]. **/
    sealed class Delimiter : Style.Delimiter() {
        /**
         * [Delimiter] that's the [parent] of all [Delimiter.Child] instances and considers [Bold]
         * parts of a [String] surrounded by a [Bold.SYMBOL].
         **/
        class Parent private constructor() : Delimiter() {
            override val parent = null
            override val regex = Regex("\\$SYMBOL.+\\$SYMBOL")

            override fun onGetTarget(match: String): String {
                return match.substringAfter(SYMBOL).substringBefore(SYMBOL)
            }

            override fun onTarget(target: String): String {
                return SYMBOL + target + SYMBOL
            }

            companion object {
                /** Single instance of a [Parent]. **/
                internal val instance = Parent()
            }
        }

        /** [Delimiter] that's a child of [Parent]. **/
        abstract class Child : Delimiter() {
            final override val parent = Parent.instance
        }
    }

    companion object {
        /** [Char] that indicates the start and the end of a bold target. **/
        internal const val SYMBOL = '*'
    }
}

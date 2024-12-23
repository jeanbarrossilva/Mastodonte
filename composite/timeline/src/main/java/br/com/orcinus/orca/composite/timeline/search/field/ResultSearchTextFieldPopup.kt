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

package br.com.orcinus.orca.composite.timeline.search.field

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Popup
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester
import com.jeanbarrossilva.loadable.list.ListLoadable
import java.lang.ref.WeakReference

/**
 * Default lambda which is a no-op for when the query changes.
 *
 * @see ResultSearchTextFieldPopup.onQueryChange
 */
private val noOpOnQueryChange = { _: String -> }

/**
 * Default [ListLoadable] of query results; an empty one.
 *
 * @see ResultSearchTextFieldPopup.resultsLoadable
 * @see ListLoadable.Empty
 */
private val emptyResultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Empty()

/** Default implementation of [ResultSearchTextFieldPopup]. */
private class DefaultResultSearchTextFieldPopup
private constructor(
  override val contextRef: WeakReference<Context>,
  override val hostViewTreeOwner: ViewTreeOwner?
) : ResultSearchTextFieldPopup<Context>() {
  constructor(
    context: Context,
    hostViewTreeOwner: ViewTreeOwner?
  ) : this(WeakReference(context), hostViewTreeOwner)

  override fun onWillShow() = Unit
}

/**
 * Overlay of a [ResultSearchTextField] which can be shown and dismissed. Acts as an alternative to
 * a [Popup], since it was the solution that had been previously adopted by Orca (prior to 0.3.2)
 * and had issues such as inability to have both focusability and dismissibility via an outside
 * click, and negative text-editing-decorators-Y-offsetting (see
 * [#378](https://github.com/orcinusbr/orca-android/pull/378)).
 *
 * This class **should not** be referenced by external APIs, given that its machinery is a mere
 * implementation detail; it shall only be so by tests. To compose a popup, call the composable
 * function whose name is the same as this class'.
 *
 * In order to produce an instance, construct the test-focused owned popup or invoke
 * [rememberResultSearchTextFieldPopup] for a Compose-based one created from the [Context] and the
 * [View] local to the calling composable.
 *
 * @see show
 * @see dismiss
 * @see OwnedResultSearchTextFieldPopup
 */
@VisibleForTesting
internal sealed class ResultSearchTextFieldPopup<C : Context> {
  /** [Modifier] applied to the [ResultSearchTextField]. */
  private var modifier by mutableStateOf<Modifier>(Modifier)

  /** Content being looked up in the [ResultSearchTextField]. */
  private var query by mutableStateOf("")

  /** Lambda invoked whenever the query changes in the [ResultSearchTextField]. */
  private var onQueryChange by mutableStateOf(noOpOnQueryChange)

  /** [Profile] results found by the [query] in the [ResultSearchTextField]. */
  private var resultsLoadable by mutableStateOf(emptyResultsLoadable)

  /**
   * Listener that gets notified after the [delegate] is dismissed.
   *
   * @see Dialog.dismiss
   * @see dismiss
   * @see setOnDidDismissListener
   */
  private var onDidDismissListener: (() -> Unit)? = null

  /** [WeakReference] to the [Context] in which the [delegate] is to be displayed. */
  protected abstract val contextRef: WeakReference<C>

  /**
   * Owner by which the tree of the host [View] is owned.
   *
   * @see createHostView
   */
  protected abstract val hostViewTreeOwner: ViewTreeOwner?

  /**
   * [Dialog] by which a [ResultSearchTextField] is displayed; `null` if the [context] has been
   * garbage-collected by the time it is instantiated.
   */
  protected val delegate by lazy {
    context?.let {
      Dialog(it, R.style.Theme_Autos).apply {
        /*
         * setCanceledOnTouchOutside(true) requires this delegate's window to wrap its content,
         * since it initially matches its parent's — decor view's — size by default and, thus, no
         * interactions are considered external to it.
         */
        window?.setLayout(
          /* width = */ WindowManager.LayoutParams.MATCH_PARENT,
          /* height = */ WindowManager.LayoutParams.WRAP_CONTENT
        )

        window?.attributes?.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        window?.setBackgroundDrawable(transparentDrawable)
        setCanceledOnTouchOutside(true)
        setOnDismissListener { onDidDismissListener?.invoke() }
      }
    }
  }

  /** [Context] referenced by the [contextRef]. */
  protected inline val context
    get() = contextRef.get()

  /**
   * Shows this popup when it enters composition and dismisses it when it is decomposed.
   *
   * This overload is stateless by default and for testing purposes only.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @throws IllegalStateException If it is already composed. Simultaneous compositions cannot occur
   *   because the given parameters are observed, and changes to them trigger an update to their
   *   single, equivalent internal values. Parallel [Content]s could introduce inconsistent
   *   renderings and callback calls on both composables, given that their state would be shared.
   */
  @Composable
  @NonRestartableComposable
  @Throws(IllegalStateException::class)
  fun Content(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (query: String) -> Unit = noOpOnQueryChange,
    resultsLoadable: ListLoadable<ProfileSearchResult> = emptyResultsLoadable
  ) = Content(query, onQueryChange, resultsLoadable, modifier)

  /**
   * Shows this popup when it enters composition and dismisses it when it is decomposed.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @throws IllegalStateException If it is already composed. Simultaneous compositions cannot occur
   *   because the given parameters are observed, and changes to them trigger an update to their
   *   single, equivalent internal values. Parallel [Content]s could introduce inconsistent
   *   renderings and callback calls on both composables, given that their state would be shared.
   */
  @Composable
  @NonRestartableComposable
  @Throws(IllegalStateException::class)
  fun Content(
    query: String,
    onQueryChange: (query: String) -> Unit,
    resultsLoadable: ListLoadable<ProfileSearchResult>,
    modifier: Modifier = Modifier
  ) {
    SimultaneousCompositionProhibitionEffect()
    HostViewRecompositionEffect(modifier, query, onQueryChange, resultsLoadable)
    AppearanceEffect()
  }

  /** Shows this popup, by which the [ResultSearchTextField] is displayed. */
  fun show() {
    val delegate = delegate
    val hostView = createHostView()
    val hostViewTreeOwner = hostViewTreeOwner
    if (delegate != null && hostView != null && hostViewTreeOwner != null) {
      onWillShow()
      hostViewTreeOwner.own(hostView)
      delegate.setContentView(hostView)
      delegate.show()
    }
  }

  /**
   * Schedules the execution of an action for after the popup has been dismissed. Ultimately, allows
   * the standalone composable to replace the current listener by another one upon a recomposition
   * due to the previously set `onDismissal` lambda having changed, and remove it when it leaves
   * composition.
   *
   * @param onDidDismissListener Listener to be notified of dismissals, or `null` for removing the
   *   previously defined one.
   */
  fun setOnDidDismissListener(onDidDismissListener: (() -> Unit)?) {
    delegate?.setOnDismissListener(onDidDismissListener?.let { { it() } })
  }

  /** Dismisses this popup, by which the [ResultSearchTextField] is displayed. */
  fun dismiss() {
    delegate?.dismiss()
  }

  /**
   * Callback called whenever this popup is requested to be shown, before it is displayed.
   *
   * @see show
   */
  protected abstract fun onWillShow()

  /**
   * Creates a [ComposeView] by which a [ResultSearchTextField] is hosted, whose tree ownership is
   * configured upon a request to show this popup and deconfigured whenever it is dismissed — both
   * respectively triggered by calls to [show] and [dismiss].
   *
   * @return The host [View], or `null` in case this method gets called after the [context] has been
   *   garbage-collected.
   * @see hostViewTreeOwner
   */
  private fun createHostView() =
    context?.let {
      ComposeView(it).apply {
        setContent {
          AutosTheme {
            Box(
              Modifier.padding(
                start = SearchTextFieldDefaults.spacing,
                top = SearchTextFieldDefaults.spacing,
                end = SearchTextFieldDefaults.spacing,

                /*
                 * Dimensions of the shadow cast by the text field are disregarded by the view when
                 * it is measured; without the padding below, it gets clipped. Calling
                 * setClipChildren(false) is futile in this case, given that the text field itself
                 * is a composable rather than a child view.
                 */
                bottom = SearchTextFieldDefaults.Elevation
              )
            ) {
              ResultSearchTextField(
                query,
                onQueryChange,
                onDismissal = ::dismiss,
                resultsLoadable,
                modifier.focusRequester(rememberImmediateFocusRequester()).fillMaxWidth()
              )
            }
          }
        }
      }
    }

  /**
   * Effect that runs once upon a composition and ensures that this popup is not already displayed;
   * it being so implies a request for a parallel rendering of it, which outreaches its intended
   * capacity of one appearance at a time (given that it is represented by a single [Dialog]).
   *
   * @throws IllegalStateException If this popup is already composed.
   * @see delegate
   */
  @Composable
  @NonRestartableComposable
  @Throws(IllegalStateException::class)
  private fun SimultaneousCompositionProhibitionEffect() =
    DisposableEffect(Unit) {
      delegate?.let {
        check(!it.isShowing) {
          "Cannot perform simultaneous compositions of a result search text field popup!"
        }
      }
      onDispose {}
    }

  /**
   * Effect that triggers recompositions on a host [View] by assigning the given parameters to this
   * popup's [State]-based properties. Each of them is then reset upon a decomposition, with a
   * default, empty value.
   *
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @see createHostView
   * @see ResultSearchTextFieldPopup.modifier
   * @see ResultSearchTextFieldPopup.query
   * @see ResultSearchTextFieldPopup.onQueryChange
   * @see ResultSearchTextFieldPopup.resultsLoadable
   */
  @Composable
  @NonRestartableComposable
  private fun HostViewRecompositionEffect(
    modifier: Modifier,
    query: String,
    onQueryChange: (query: String) -> Unit,
    resultsLoadable: ListLoadable<ProfileSearchResult>
  ) {
    DisposableEffect(modifier) {
      this@ResultSearchTextFieldPopup.modifier = modifier
      onDispose { this@ResultSearchTextFieldPopup.modifier = Modifier }
    }

    DisposableEffect(query) {
      this@ResultSearchTextFieldPopup.query = query
      onDispose { this@ResultSearchTextFieldPopup.query = "" }
    }

    DisposableEffect(onQueryChange) {
      this@ResultSearchTextFieldPopup.onQueryChange = onQueryChange
      onDispose { this@ResultSearchTextFieldPopup.onQueryChange = noOpOnQueryChange }
    }

    DisposableEffect(resultsLoadable) {
      this@ResultSearchTextFieldPopup.resultsLoadable = resultsLoadable
      onDispose { this@ResultSearchTextFieldPopup.resultsLoadable = emptyResultsLoadable }
    }
  }

  /**
   * Effect that shows and dismisses this popup.
   *
   * @see show
   * @see dismiss
   */
  @Composable
  @NonRestartableComposable
  private fun AppearanceEffect() =
    DisposableEffect(Unit) {
      show()
      onDispose(::dismiss)
    }

  private companion object {
    /**
     * [ColorDrawable] defined as the [delegate]'s [Window] background for making it transparent.
     */
    @JvmStatic private val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
  }
}

/**
 * [ResultSearchTextFieldPopup] owned by a [ComponentActivity] which serves as the [Context] and the
 * owner of both the host [View] tree and the underlying [delegate]; whose [View] tree ownership is
 * configured before this popup is shown.
 *
 * @see createHostView
 * @see show
 */
@VisibleForTesting
internal class OwnedResultSearchTextFieldPopup
private constructor(override val contextRef: WeakReference<ComponentActivity>) :
  ResultSearchTextFieldPopup<ComponentActivity>() {
  override val hostViewTreeOwner by lazy { context?.let(ViewTreeOwner::from) }

  constructor(context: ComponentActivity) : this(WeakReference(context))

  override fun onWillShow() {
    val activity = context
    val delegate = delegate
    if (activity != null && delegate != null) {
      activity.initializeViewTreeOwners()
      delegate.setOwnerActivity(activity)
    }
  }
}

/**
 * Popup by which a [ResultSearchTextField] is displayed.
 *
 * This overload is stateless by default and for testing purposes only.
 *
 * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found by the [query].
 * @param onDismissal Operation performed whenever this popup is dismissed.
 */
@Composable
@NonRestartableComposable
@VisibleForTesting
fun ResultSearchTextFieldPopup(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = noOpOnQueryChange,
  resultsLoadable: ListLoadable<ProfileSearchResult> = emptyResultsLoadable,
  onDismissal: () -> Unit = {}
) = ResultSearchTextFieldPopup(query, onQueryChange, resultsLoadable, onDismissal, modifier)

/**
 * Popup by which a [ResultSearchTextField] is displayed.
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found by the [query].
 * @param onDismissal Operation performed whenever this popup is dismissed.
 * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
 */
@Composable
@NonRestartableComposable
internal fun ResultSearchTextFieldPopup(
  query: String,
  onQueryChange: (query: String) -> Unit,
  resultsLoadable: ListLoadable<ProfileSearchResult>,
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier
) {
  val popup = rememberResultSearchTextFieldPopup()

  DisposableEffect(onDismissal) {
    popup.setOnDidDismissListener(onDismissal)
    onDispose { popup.setOnDidDismissListener(null) }
  }

  popup.Content(query, onQueryChange, resultsLoadable, modifier)
}

/**
 * Produces a remembered [ResultSearchTextFieldPopup] by which the results of a query can be shown
 * on top of preexisting content. In order for it to actually be displayed, its content should be
 * invoked.
 *
 * @see ResultSearchTextFieldPopup.Content
 * @see LocalView
 */
@Composable
private fun rememberResultSearchTextFieldPopup(): ResultSearchTextFieldPopup<Context> {
  val context = LocalContext.current
  val viewTreeOwner = rememberViewTreeOwner()
  return remember(context, viewTreeOwner) {
    DefaultResultSearchTextFieldPopup(context, hostViewTreeOwner = viewTreeOwner)
  }
}

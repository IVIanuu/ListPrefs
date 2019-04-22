package com.ivianuu.listprefs

import android.content.Context
import android.content.SharedPreferences
import com.ivianuu.list.ItemController
import com.ivianuu.list.ItemFactory
import com.ivianuu.list.addTo

/**
 * Preference epoxy controller
 */
// todo remove once we came up with a better solution
abstract class PreferenceItemController(
    val context: Context,
    val sharedPreferencesName: String? = ListPrefsPlugins.getDefaultSharedPreferencesName(context)
) : ItemController() {

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val prefsChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            requestItemBuild()
        }

    fun attachListener() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefsChangeListener)
    }

    fun detachListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefsChangeListener)
    }

    inline operator fun <T : AbstractPreferenceItem<*>> ItemFactory<T>.invoke(
        body: T.() -> Unit
    ): T = create().injectContext().apply(body).addTo(this@PreferenceItemController)

    inline operator fun <T : AbstractPreferenceItem<*>> T.invoke(body: T.() -> Unit): T =
        injectContext().apply(body).addTo(this@PreferenceItemController)

    inline fun <T : AbstractPreferenceItem<*>> T.add(body: T.() -> Unit): T =
        injectContext().apply(body).addTo(this@PreferenceItemController)

    @PublishedApi
    internal fun <T : AbstractPreferenceItem<*>> T.injectContext(): T = apply {
        context = this@PreferenceItemController.context
    }
}

open class SimplePreferenceItemController(
    context: Context,
    private val block: PreferenceItemController.() -> Unit
) : PreferenceItemController(context) {
    override fun buildItems() {
        block.invoke(this)
    }
}

fun Context.preferenceItemController(block: PreferenceItemController.() -> Unit): PreferenceItemController =
    SimplePreferenceItemController(this, block)

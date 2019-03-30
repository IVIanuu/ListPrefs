package com.ivianuu.listprefs

import android.content.Context
import android.content.SharedPreferences
import com.ivianuu.list.ListModelFactory
import com.ivianuu.list.ModelController
import com.ivianuu.list.addTo

/**
 * Preference epoxy controller
 */
// todo remove once we came up with a better solution
abstract class PreferenceModelController(
    val context: Context,
    val sharedPreferencesName: String? = ListPrefsPlugins.getDefaultSharedPreferencesName(context)
) : ModelController() {

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val prefsChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            requestModelBuild()

        }

    fun attachListener() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefsChangeListener)
    }

    fun detachListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefsChangeListener)
    }

    inline operator fun <T : PreferenceModel> ListModelFactory<T>.invoke(
        body: T.() -> Unit
    ): T = create().injectContext().apply(body).addTo(this@PreferenceModelController)

    inline operator fun <T : PreferenceModel> T.invoke(body: T.() -> Unit): T =
        injectContext().apply(body).addTo(this@PreferenceModelController)

    inline fun <T : PreferenceModel> T.add(body: T.() -> Unit): T =
        injectContext().apply(body).addTo(this@PreferenceModelController)

    @PublishedApi
    internal fun <T : PreferenceModel> T.injectContext(): T = apply {
        context = this@PreferenceModelController.context
    }
}

open class SimplePreferenceModelController(
    context: Context,
    private val block: PreferenceModelController.() -> Unit
) : PreferenceModelController(context) {
    override fun buildModels() {
        block.invoke(this)
    }
}

fun Context.preferenceModelController(block: PreferenceModelController.() -> Unit): PreferenceModelController =
        SimplePreferenceModelController(this, block)

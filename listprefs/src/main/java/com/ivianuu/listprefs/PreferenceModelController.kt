package com.ivianuu.listprefs

import android.content.Context
import android.content.SharedPreferences
import com.ivianuu.list.ModelController
import com.ivianuu.list.addModelListener

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

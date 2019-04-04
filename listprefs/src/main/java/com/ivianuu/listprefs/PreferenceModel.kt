/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.listprefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.ivianuu.list.*
import com.ivianuu.list.common.LayoutContainerHolder
import com.ivianuu.listprefs.internal.tryToResolveDefaultValue
import kotlinx.android.synthetic.main.item_preference.*

/**
 * Base preference
 */
open class PreferenceModel : ListModel<PreferenceModel.Holder>() {

    var context by requiredProperty<Context>("context", doHash = false)

    var key: String
        get() = getProperty("key")!!
        set(value) {
            setProperty("key", value)
            id(value)
        }

    var title by optionalProperty<String>("title")
    var summary  by optionalProperty<String>("summary")
    var icon by optionalProperty<Drawable>("icon")
    var preserveIconSpace by property("preserveIconSpace") { false }
    var defaultValue by optionalProperty<Any?>("defaultValue")
    var enabled by property("enabled") { true }
    var clickable by property("clickable") { true }

    var dependencies by property("dependencies") { mutableListOf<Dependency>() }
    val allowedByDependencies by property("allowedByDependencies") {
        dependencies.all { it.isOk(sharedPreferences) }
    }

    var onClick by optionalProperty<(preference: PreferenceModel) -> Boolean>("onClick", doHash = false)
    var onChange by optionalProperty<(preference: PreferenceModel, newValue: Any?) -> Boolean>("onChange", doHash = false)
    var sharedPreferences: SharedPreferences by property("sharedPreferences", doHash = false) {
        if (sharedPreferencesName != null) {
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        } else {
            ListPrefsPlugins.getDefaultSharedPreferences(context)
        }
    }
    var sharedPreferencesName by optionalProperty<String>("sharedPreferencesName")
    var useCommit by property("useCommit") { false }
    var persistent by property("persistent") { true }
    var widgetLayoutRes by property("widgetLayoutRes") { 0 }
    val value by property("value") {
        if (persistent) {
            sharedPreferences.all[key] ?: defaultValue
        } else if (!persistent) {
            defaultValue
        } else {
            null
        }
    }

    final override var layoutRes = R.layout.item_preference

    final override val viewType: Int
        get() = layoutRes + widgetLayoutRes

    override fun bind(holder: Holder) {
        super.bind(holder)

        val enabled = enabled && allowedByDependencies

        holder.title?.let {
            it.text = title
            it.visibility = if (title != null) View.VISIBLE else View.GONE
            it.isEnabled = enabled
        }

        holder.summary?.let {
            it.text = summary
            it.visibility = if (summary != null) View.VISIBLE else View.GONE
            it.isEnabled = enabled
        }

        holder.icon?.let {
            it.setImageDrawable(icon)
            it.isEnabled = enabled
        }

        holder.icon_frame?.let {
            it.visibility = if (icon != null || preserveIconSpace) View.VISIBLE else View.GONE
        }

        holder.widget_frame?.let {
            (0 until it.childCount)
                .map(it::getChildAt)
                .forEach { it.isEnabled = enabled }
        }

        holder.containerView.apply {
            isEnabled = enabled
            isClickable = clickable

            if (clickable) {
                setOnClickListener {
                    val handled =
                        onClick?.invoke(this@PreferenceModel) ?: false
                    if (!handled) {
                        onClick()
                    }
                }
            } else {
                setOnClickListener(null)
            }
        }
    }

    override fun createHolder(): Holder = Holder()

    override fun buildView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutRes, parent, false)

        val widgetFrame = view.findViewById<ViewGroup>(android.R.id.widget_frame)

        if (widgetFrame != null) {
            if (widgetLayoutRes != 0) {
                inflater.inflate(widgetLayoutRes, widgetFrame)
            } else {
                widgetFrame.visibility = View.GONE
            }
        }

        return view
    }

    protected open fun onClick() {
    }

    protected fun callChangeListener(newValue: Any?): Boolean =
        onChange?.invoke(this, newValue) ?: true

    protected fun shouldPersist(): Boolean = persistent

    @SuppressLint("ApplySharedPref")
    protected fun editSharedPreferences(edit: SharedPreferences.Editor.() -> Unit) {
        if (shouldPersist()) {
            sharedPreferences.edit()
                .also(edit)
                .run {
                    if (useCommit) {
                        commit()
                    } else {
                        apply()
                    }
                }
        }
    }

    protected fun persistBoolean(key: String?, value: Boolean) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putBoolean(key, value) }
        }
    }

    protected fun persistFloat(key: String?, value: Float) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putFloat(key, value) }
        }
    }

    protected fun persistInt(key: String?, value: Int) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putInt(key, value) }
        }
    }

    protected fun persistLong(key: String?, value: Long) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putLong(key, value) }
        }
    }

    protected fun persistString(key: String?, value: String) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putString(key, value) }
        }
    }

    protected fun persistStringSet(key: String?, value: MutableSet<String>) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putStringSet(key, value) }
        }
    }

    open class Holder : LayoutContainerHolder()

    data class Dependency(
        val key: String,
        val value: Any?,
        val defaultValue: Any?
    ) {
        fun isOk(sharedPreferences: SharedPreferences): Boolean {
            return (sharedPreferences.all[key]
                ?: defaultValue
                ?: value.tryToResolveDefaultValue()) == value
        }
    }

    companion object : ListModelFactory<PreferenceModel>(::PreferenceModel)

}

fun PreferenceModel.key(keyRes: Int) {
    key = context.getString(keyRes)
}

fun PreferenceModel.title(titleRes: Int) {
    title = context.getString(titleRes)
}

fun PreferenceModel.summary(summaryRes: Int) {
    summary = context.getString(summaryRes)
}

fun PreferenceModel.icon(iconRes: Int) {
    icon = ContextCompat.getDrawable(context, iconRes)
}

fun PreferenceModel.dependency(
    key: String,
    value: Any?,
    defaultValue: Any? = null
) {
    dependency(PreferenceModel.Dependency(key, value, defaultValue))
}

fun PreferenceModel.dependency(dependency: PreferenceModel.Dependency) {
    dependencies.add(dependency)
}

@JvmName("changeListenerTyped")
inline fun <reified T> PreferenceModel.onChange(crossinline changeListener: (preference: PreferenceModel, newValue: T) -> Boolean) {
    onChange = { preference: PreferenceModel, newValue: Any? ->
        changeListener(
            preference,
            newValue as T
        )
    }
}

fun PreferenceModel.onClickIntent(intent: (PreferenceModel) -> Intent) {
    onClick = {
        it.context.startActivity(intent(it))
        true
    }
}

fun PreferenceModel.onClickUrl(url: (PreferenceModel) -> String) {
    onClickIntent {
        Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url(it)) }
    }
}
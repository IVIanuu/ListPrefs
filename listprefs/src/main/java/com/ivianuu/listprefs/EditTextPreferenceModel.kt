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

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.ivianuu.list.ModelController
import com.ivianuu.list.annotations.Model

/**
 * A edit text preference
 */
@Model open class EditTextPreferenceModel : DialogPreferenceModel() {

    var dialogHint by optionalProperty<String>("dialogHint")

    override fun showDialog() {
        val prefill = value as? String ?: ""

        MaterialDialog(context)
            .applyDialogSettings()
            .input(
                hint = dialogHint ?: "",
                prefill = prefill
            ) { _, input ->
                if (callChangeListener(input.toString())) {
                    persistString(key, input.toString())
                }
            }
            .show()
    }

}

inline fun PreferenceModelController.editTextPreference(
    block: EditTextPreferenceModel.() -> Unit
): EditTextPreferenceModel {
    return (this as ModelController).editTextPreference {
        context(this@editTextPreference.context)
        block.invoke(this)
    }
}
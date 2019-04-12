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

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.ivianuu.list.ItemFactory


/**
 * A edit text preference
 */
open class EditTextPreferenceItem : DialogPreferenceItem() {

    var dialogHint by optionalProperty<String>()

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

    companion object : ItemFactory<EditTextPreferenceItem>(::EditTextPreferenceItem)
}
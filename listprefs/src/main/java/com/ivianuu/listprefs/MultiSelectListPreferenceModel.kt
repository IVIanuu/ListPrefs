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
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.ivianuu.list.ListModelFactory


/**
 * A multi select list preference
 */
open class MultiSelectListPreferenceModel : ListPreferenceModel() {

    override fun showDialog() {
        val entries = entries ?: emptyList()
        val entryValues = entryValues ?: emptyList()

        val currentValues = value as? Set<String> ?: emptySet()
        val selectedIndices = currentValues
            .map(entryValues::indexOf)
            .filter { it != -1 }
            .toIntArray()

        MaterialDialog(context)
            .applyDialogSettings()
            .listItemsMultiChoice(
                items = entries,
                initialSelection = selectedIndices,
                allowEmptySelection = true
            ) { _, positions, _ ->
                val newValue = entryValues
                    .filterIndexed { index, _ -> positions.contains(index) }
                    .map(String::toString)
                    .toMutableSet()

                if (callChangeListener(newValue)) {
                    persistStringSet(key, newValue)
                }
            }
            .show()
    }

    companion object :
        ListModelFactory<MultiSelectListPreferenceModel>(::MultiSelectListPreferenceModel)
}

fun MultiSelectListPreferenceModel.defaultValue(vararg defaultValues: String) {
    defaultValue = mutableSetOf(defaultValues)
}
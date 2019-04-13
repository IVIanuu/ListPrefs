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
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.ivianuu.list.ItemFactory


/**
 * A single item preference
 */
open class SingleItemListPreferenceItem : ListPreferenceItem() {

    override fun showDialog() {
        val entries = entries ?: emptyList()
        val entryValues = entryValues ?: emptyList()

        val currentValue = value as? String ?: ""
        val selectedIndex = entryValues.indexOf(currentValue)

        MaterialDialog(context)
            .applyDialogSettings(applyPositiveButtonText = false)
            .listItemsSingleChoice(
                initialSelection = selectedIndex,
                items = entries,
                waitForPositiveButton = false
            ) { dialog, position, _ ->
                val newValue = entryValues[position]
                if (callChangeListener(newValue)) {
                    persistString(key, newValue)
                }

                dialog.dismiss()
            }
            .show()
    }

    companion object :
        ItemFactory<SingleItemListPreferenceItem>(::SingleItemListPreferenceItem)

}
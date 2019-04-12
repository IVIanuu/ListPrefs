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

/**
 * A list preference item
 */
abstract class ListPreferenceItem : DialogPreferenceItem() {
    var entries by optionalProperty<List<String>>()
    var entryValues by optionalProperty<List<String>>()
}

fun ListPreferenceItem.entries(entriesRes: Int) {
    entries = context.resources.getStringArray(entriesRes).toList()
}

fun ListPreferenceItem.entries(vararg entries: String) {
    this.entries = entries.toList()
}

fun ListPreferenceItem.entryValues(entryValuesRes: Int) {
    entryValues = context.resources.getStringArray(entryValuesRes).toList()
}

fun ListPreferenceItem.entryValues(vararg entryValues: String) {
    this.entryValues = entryValues.toList()
}
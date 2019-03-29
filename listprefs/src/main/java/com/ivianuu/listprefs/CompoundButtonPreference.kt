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

import android.widget.CompoundButton
import com.ivianuu.list.annotations.Model

/**
 * A preference for compound buttons
 */
@Model abstract class CompoundButtonPreferenceModel : PreferenceModel() {

    protected abstract val PreferenceModel.Holder.compoundButton: CompoundButton?

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.compoundButton?.isChecked = value as? Boolean ?: false
    }

    override fun onClick() {
        super.onClick()
        val newValue = (value as? Boolean ?: false).not()
        if (callChangeListener(newValue)) {
            persistBoolean(key, newValue)
        }
    }

}
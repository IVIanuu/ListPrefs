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
import android.widget.CompoundButton
import com.ivianuu.list.ModelController
import com.ivianuu.list.annotations.Model
import kotlinx.android.synthetic.main.widget_preference_switch.switchWidget

/**
 * A switch preference
 */
@Model open class SwitchPreferenceModel : CompoundButtonPreferenceModel() {

    override val Holder.compoundButton: CompoundButton?
        get() = switchWidget

    init {
        widgetLayoutRes = R.layout.widget_preference_switch
    }

}

inline fun PreferenceModelController.switchPreference(
    block: SwitchPreferenceModel.() -> Unit
): SwitchPreferenceModel {
    return (this as ModelController).switchPreference {
        context(this@switchPreference.context)
        block.invoke(this)
    }
}
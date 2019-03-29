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

import android.widget.SeekBar
import com.ivianuu.list.ModelController
import com.ivianuu.list.annotations.Model
import kotlinx.android.synthetic.main.item_preference_seekbar.seekbar
import kotlinx.android.synthetic.main.item_preference_seekbar.seekbar_value
import kotlin.math.round

/**
 * Abstract seek bar preference model
 */
@Model open class SeekBarPreferenceModel : PreferenceModel() {

    var min by property("min") { 0 }
    var max by property("max") { 0 }
    var incValue by property("incValue") { 1 }

    var valueTextProvider by optionalProperty<(Int) -> String>("valueTextProvider", doHash = false)

    private var internalValue = 0

    init {
        layoutRes = R.layout.item_preference_seekbar
    }

    override fun bind(holder: Holder) {
        super.bind(holder)
        internalValue = value as? Int ?: 0

        holder.seekbar.max = max - min
        holder.seekbar.progress = internalValue - min

        holder.seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) syncView(holder)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (callChangeListener(internalValue)) {
                    persistInt(key, internalValue)
                }
            }
        })

        syncView(holder)
    }

    private fun syncView(holder: Holder) {
        var progress = min + holder.seekbar.progress

        if (progress < min) {
            progress = min
        }

        if (progress > max) {
            progress = max
        }

        internalValue = (round((progress / incValue).toDouble()) * incValue).toInt()

        val provider = valueTextProvider

        val text = provider?.invoke(internalValue) ?: internalValue.toString() // fallback

        holder.seekbar.progress = internalValue - min
        holder.seekbar_value.text = text
    }

}

inline fun PreferenceModelController.seekBarPreference(
    block: SeekBarPreferenceModel.() -> Unit
): SeekBarPreferenceModel {
    return (this as ModelController).seekBarPreference {
        context(this@seekBarPreference.context)
        block.invoke(this)
    }
}
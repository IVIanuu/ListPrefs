package com.ivianuu.listprefs

import android.content.Context
import android.widget.CompoundButton
import com.ivianuu.list.ModelController
import com.ivianuu.list.annotations.Model
import kotlinx.android.synthetic.main.widget_preference_radio.radio

/**
 * A radio button preference
 */
@Model open class RadioButtonPreferenceModel : CompoundButtonPreferenceModel() {

    override val Holder.compoundButton: CompoundButton?
        get() = radio

    init {
        widgetLayoutRes = R.layout.widget_preference_radio
    }

}

inline fun PreferenceModelController.radioButtonPreference(
    block: RadioButtonPreferenceModel.() -> Unit
): RadioButtonPreferenceModel {
    return (this as ModelController).radioButtonPreference {
        context(this@radioButtonPreference.context)
        block.invoke(this)
    }
}
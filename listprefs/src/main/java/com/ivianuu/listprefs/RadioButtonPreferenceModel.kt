package com.ivianuu.listprefs

import android.widget.CompoundButton
import com.ivianuu.list.ListModelFactory
import kotlinx.android.synthetic.main.widget_preference_radio.radio

/**
 * A radio button preference
 */
open class RadioButtonPreferenceModel : CompoundButtonPreferenceModel() {

    override val Holder.compoundButton: CompoundButton?
        get() = radio

    init {
        widgetLayoutRes = R.layout.widget_preference_radio
    }

    companion object : ListModelFactory<EditTextPreferenceModel>(::EditTextPreferenceModel)

}
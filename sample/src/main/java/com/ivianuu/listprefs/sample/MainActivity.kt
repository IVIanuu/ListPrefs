package com.ivianuu.listprefs.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivianuu.listprefs.*
import kotlinx.android.synthetic.main.activity_main.list

class MainActivity : AppCompatActivity() {

    private val controller by lazy {
        preferenceModelController {

            for (i in 1..1) {
                CategoryPreferenceModel {
                    key = "category_$i"
                    title = "Category $i"
                }

                SwitchPreferenceModel {
                    key = "my_switch_$i"
                    title = "Switch"
                    summary = "Nice a switch"
                }

                if (sharedPreferences.getBoolean("my_switch_$i", false)) {
                    EditTextPreferenceModel {
                        key = "my_edit_text_$i"
                        title = "Edit text"
                        summary = "Edit text"
                        dialogHint = "Hello lets type something"
                    }
                }

                PreferenceModel {
                    key = "my_key_$i"
                    title = "Title"
                    summary = "This is a summary."
                    dependency("my_switch_$i", true)
                    onClickUrl { "https://www.google.de/" }
                }

                CheckboxPreferenceModel {
                    key = "my_checkbox_$i"
                    title = "CheckBox"
                    summary = "Oh a checkbox"
                    dependency("my_switch_$i", true)
                }

                RadioButtonPreferenceModel {
                    key = "my_radio_$i"
                    title = "Radio"
                    summary = "A radio button"
                    dependency("my_switch_$i", true)
                }

                SeekBarPreferenceModel {
                    key = "my_seekbar_$i"
                    title = "SeekBar"
                    max = 100
                    summary = "Hey there im a seekbar"
                    dependency("my_switch_$i", true)
                }

                SingleItemListPreferenceModel {
                    key = "single_item_list_$i"
                    title = "Single item list"
                    defaultValue = "1"
                    entries("1", "2", "3")
                    entryValues("1", "2", "3")
                    dependency("my_switch_$i", true)
                }

                MultiSelectListPreferenceModel {
                    key = "multi_select_list_$i"
                    title = "Multi select list"
                    defaultValue("A", "B")
                    entries("A", "B", "C")
                    entryValues("A", "B", "C")
                    dependency("my_switch_$i", true)
                }

                PreferenceModel {
                    key = "my_key1_$i"
                    title = "Another Title"
                    summary = "This is another summary."
                    dependency("my_switch_$i", true)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addItemDecoration(PreferenceDividerDecoration(this))

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = controller.adapter

        controller.requestModelBuild()
    }

    override fun onStart() {
        super.onStart()
        controller.attachListener()
    }

    override fun onStop() {
        super.onStop()
        controller.detachListener()
    }

}
package com.ivianuu.listprefs.sample

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.ivianuu.list.ListModel
import com.ivianuu.list.ModelProperty
import com.ivianuu.list.addModelListener
import com.ivianuu.list.common.modelController
import com.ivianuu.listprefs.PreferenceDividerDecoration
import com.ivianuu.listprefs.categoryPreference
import com.ivianuu.listprefs.checkboxPreference
import com.ivianuu.listprefs.defaultValue
import com.ivianuu.listprefs.dependency
import com.ivianuu.listprefs.dialogHint
import com.ivianuu.listprefs.editTextPreference
import com.ivianuu.listprefs.entries
import com.ivianuu.listprefs.entryValues
import com.ivianuu.listprefs.key
import com.ivianuu.listprefs.max
import com.ivianuu.listprefs.multiSelectListPreference
import com.ivianuu.listprefs.onChange
import com.ivianuu.listprefs.onClickUrl
import com.ivianuu.listprefs.persistent
import com.ivianuu.listprefs.preference
import com.ivianuu.listprefs.preferenceModelController
import com.ivianuu.listprefs.radioButtonPreference
import com.ivianuu.listprefs.seekBarPreference
import com.ivianuu.listprefs.singleItemListPreference
import com.ivianuu.listprefs.summary
import com.ivianuu.listprefs.switchPreference
import com.ivianuu.listprefs.title
import kotlinx.android.synthetic.main.activity_main.list

class MainActivity : AppCompatActivity() {

    private val controller by lazy {
        preferenceModelController {

            for (i in 1..1) {
                categoryPreference {
                    key("category_$i")
                    title("Category $i")
                }

                switchPreference {
                    key("my_switch_$i")
                    title("Switch")
                    summary("Nice a switch")
                }

                if (sharedPreferences.getBoolean("my_switch_$i", false)) {
                    editTextPreference {
                        key("my_edit_text_$i")
                        title("Edit text")
                        summary("Edit text")
                        dialogHint("Hello lets type something")
                    }
                }

                preference {
                    key("my_key_$i")
                    title("Title")
                    summary("This is a summary.")
                    dependency("my_switch_$i", true)
                    onClickUrl { "https://www.google.de/" }
                }

                checkboxPreference {
                    key("my_checkbox_$i")
                    title("CheckBox")
                    dependency("my_switch_$i", true)
                    summary("Oh a checkbox")
                }

                radioButtonPreference {
                    key("my_radio_$i")
                    title("Radio")
                    dependency("my_switch_$i", true)
                    summary("A radio button")
                }

                seekBarPreference {
                    key("my_seekbar_$i")
                    title("SeekBar")
                    max(100)
                    summary("Hey there im a seekbar")
                    dependency("my_switch_$i", true)
                }

                singleItemListPreference {
                    key("single_item_list_$i")
                    title("Single item list")
                    entries("1", "2", "3")
                    entryValues("1", "2", "3")
                    dependency("my_switch_$i", true)
                }

                multiSelectListPreference {
                    key("multi_select_list_$i")
                    title("Multi select list")
                    entries("A", "B", "C")
                    entryValues("A", "B", "C")
                    dependency("my_switch_$i", true)
                }

                preference {
                    key("my_key1_$i")
                    title("Another Title")
                    summary("This is another summary.")
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
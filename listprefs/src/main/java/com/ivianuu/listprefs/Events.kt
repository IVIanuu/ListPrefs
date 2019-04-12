package com.ivianuu.listprefs

import com.ivianuu.list.ItemEvents

class PrefClicks : ItemEvents<() -> Boolean> {
    internal var callback: (() -> Boolean)? = null
    override fun setCallback(callback: () -> Boolean) {
        this.callback = callback
    }
}

class PrefChanges : ItemEvents<(Any?) -> Boolean> {
    internal var callback: ((Any?) -> Boolean)? = null
    override fun setCallback(callback: (Any?) -> Boolean) {
        this.callback = callback
    }
}
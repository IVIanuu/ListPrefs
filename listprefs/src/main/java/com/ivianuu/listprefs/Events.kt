package com.ivianuu.listprefs

import com.ivianuu.list.ItemEvents

class PrefClicks : ItemEvents<() -> Boolean> {
    internal var callback: (() -> Boolean)? = null
    override fun setCallback(callback: () -> Boolean) {
        this.callback = callback
    }
}

class PrefChanges<T> : ItemEvents<(T) -> Boolean> {
    internal var callback: ((T) -> Boolean)? = null
    override fun setCallback(callback: (T) -> Boolean) {
        this.callback = callback
    }
}
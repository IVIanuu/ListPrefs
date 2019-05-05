package com.ivianuu.listprefs

internal fun Any?.tryToResolveDefaultValue(): Any? = when (this) {
    is Boolean -> false
    is Float -> 0f
    is Int -> 0
    is Long -> 0L
    is String -> ""
    is Set<*> -> emptySet<String>()
    else -> null
}
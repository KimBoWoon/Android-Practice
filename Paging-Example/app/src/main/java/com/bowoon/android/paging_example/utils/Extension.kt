package com.bowoon.android.paging_example.utils

infix fun <T: Any?> T?.ifNotNull(action: (T) -> Unit): T {
    if (this != null) {
        action(this)
        return this
    }

    return this!!
}

infix fun <T: Any?> T?.ifNull(action: (T?) -> Unit): T? {
    if (this == null) {
        action(this)
    }

    return null
}
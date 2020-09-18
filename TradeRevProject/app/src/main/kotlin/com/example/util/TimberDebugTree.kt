package com.example.util

import timber.log.Timber

class TimberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
            "`=[%s.%s]",
            element.methodName,
            super.createStackElementTag(element)
        )
    }
}

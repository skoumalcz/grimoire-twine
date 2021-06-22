package com.skoumal.grimoire.twine.tool

import org.slf4j.Logger

inline fun Logger.debug(body: () -> String) {
    if (isDebugEnabled) {
        debug(body())
    }
}

inline fun Logger.error(body: () -> String) {
    if (isErrorEnabled) {
        error(body())
    }
}
package com.ternaryop.mos.jexl

import org.apache.commons.jexl3.JexlContext

fun JexlContext.update(str: String, index: Int) {
    val x = getNumericValue(str)

    set("i", index);
    set("s", str);
    set("x", x);
}

fun getNumericValue(str: String): Int {
    try {
        return Integer.parseInt(str.trim())
    } catch (_: Exception) {}

    return 0;
}


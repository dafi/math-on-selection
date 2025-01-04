package com.ternaryop.mos.jexl

import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.JexlContext
import org.apache.commons.jexl3.JexlExpression
import org.apache.commons.jexl3.JexlFeatures
import org.apache.commons.jexl3.introspection.JexlPermissions

fun JexlExpression.resolve(context: JexlContext): String {
    try {
        return evaluate(context).toString()
    } catch (_: Exception) {}

    return "";
}

fun createExpression(
    expression: String
): JexlExpression {
    val features = JexlFeatures
        .createNone()
        .methodCall(true)

    val jexl = JexlBuilder()
        .features(features)
        .permissions(JexlPermissions.parse("com.ternaryop.mos.ExportedFunctions"))
        .create()

    return jexl.createExpression(expression)
}

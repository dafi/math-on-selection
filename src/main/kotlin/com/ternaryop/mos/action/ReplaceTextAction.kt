package com.ternaryop.mos.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.ternaryop.mos.format
import com.ternaryop.mos.jexl.createExpression
import com.ternaryop.mos.jexl.resolve
import com.ternaryop.mos.jexl.update
import org.apache.commons.jexl3.JexlContext
import org.apache.commons.jexl3.MapContext

class ReplaceTextAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(EDITOR)

        if (project == null || editor == null) {
            return
        }

        val expressionString: String? = Messages.showInputDialog(
            project,
            "Insert the expression",
            "Expression",
            Messages.getQuestionIcon()
        )

        if (expressionString.isNullOrEmpty()) {
            return
        }

        val expression = createExpression(expressionString)

        WriteCommandAction.runWriteCommandAction(project) {
            val context = defaultContext()
            val document = editor.document

            for ((i, caret) in editor.caretModel.allCarets.withIndex()) {
                context.update(getSelection(document, caret), i);
                replaceSelection(document, caret, expression.resolve(context))
            }
        }
    }

    private fun replaceSelection(
        document: Document,
        caret: Caret,
        selection: String
    ) {
        val start = caret.selectionStart
        val end = caret.selectionEnd

        document.replaceString(start, end, selection)
        caret.setSelection(start, start + selection.length)
        caret.moveToOffset(start + selection.length)
    }

    private fun getSelection(
        document: Document,
        caret: Caret
    ): String {
        if (caret.hasSelection()) {
            val start = caret.selectionStart
            val end = caret.selectionEnd

            return document.getText(TextRange(start, end))
        }
        return ""
    }

    private fun defaultContext(): JexlContext = MapContext(
        mapOf(
            "format" to ::format,
            "Math" to Math::class.java
        )
    )
}

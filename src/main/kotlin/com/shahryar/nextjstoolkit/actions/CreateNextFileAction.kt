package com.shahryar.nextjstoolkit.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.shahryar.nextjstoolkit.generators.SourceLanguage
import com.shahryar.nextjstoolkit.generators.TemplateDefinition

class CreateNextFileAction(
    private val template: TemplateDefinition,
    private val language: SourceLanguage
) : AnAction(template.title) {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val directory = resolveTargetDirectory(event)
        event.presentation.isEnabledAndVisible =
            event.project != null && directory != null && directory.isWritable
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val directory = resolveTargetDirectory(event) ?: return
        val fileName = template.fileName(language)

        if (directory.findChild(fileName) != null) {
            Messages.showErrorDialog(
                project,
                "The file '$fileName' already exists in this directory.",
                "Next.js Toolkit"
            )
            return
        }

        try {
            WriteCommandAction.runWriteCommandAction(
                project,
                "Create $fileName",
                null,
                Runnable {
                    val newFile = directory.createChildData(this, fileName)
                    VfsUtil.saveText(newFile, template.content(language))
                }
            )

        } catch (exception: Exception) {
            Messages.showErrorDialog(
                project,
                exception.message ?: "Could not create '$fileName'.",
                "Next.js Toolkit"
            )
        }
    }

    private fun resolveTargetDirectory(event: AnActionEvent): VirtualFile? {
        val selectedFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
            ?: event.getData(CommonDataKeys.PSI_FILE)?.virtualFile
            ?: return null

        return if (selectedFile.isDirectory) selectedFile else selectedFile.parent
    }
}

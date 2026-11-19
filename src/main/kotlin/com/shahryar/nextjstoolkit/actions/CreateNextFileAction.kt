
package com.shahryar.nextjstoolkit.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.shahryar.nextjstoolkit.generators.SourceLanguage
import com.shahryar.nextjstoolkit.generators.TemplateDefinition

class CreateNextFileAction(
    private val template: TemplateDefinition,
    private val language: SourceLanguage,
) : AnAction(template.title, null, template.icon), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(event: AnActionEvent) {
        val directory = resolveTargetDirectory(event)

        event.presentation.isEnabledAndVisible =
            event.project != null &&
                    directory != null &&
                    directory.isWritable
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val directory = resolveTargetDirectory(event) ?: return

        // Get custom name only when required.
        val customName = if (template.requiresName) {
            Messages.showInputDialog(
                project,
                "Enter component name:",
                "Create ${template.title}",
                Messages.getQuestionIcon(),
                "",
                null
            )?.trim() ?: return
        } else {
            null
        }

        // Validate custom name.
        if (customName != null && !isValidFileName(customName)) {
            Messages.showErrorDialog(
                project,
                "Invalid component name.",
                "Next.js Toolkit"
            )
            return
        }

        // Convert file name to React component identifier.
        val componentName = customName?.let(::toPascalCase)

        val fileName = try {
            template.fileName(language, customName)
        } catch (exception: IllegalArgumentException) {
            Messages.showErrorDialog(
                project,
                exception.message ?: "Invalid template configuration.",
                "Next.js Toolkit"
            )
            return
        }

        if (directory.findChild(fileName) != null) {
            Messages.showErrorDialog(
                project,
                "The file '$fileName' already exists in this directory.",
                "Next.js Toolkit"
            )
            return
        }

        val content = template.content(language, componentName)

        try {
            WriteCommandAction.runWriteCommandAction(
                project,
                "Create $fileName",
                null,
                Runnable {
                    // Check again to avoid creating a duplicate.
                    if (directory.findChild(fileName) != null) {
                        throw IllegalStateException(
                            "The file '$fileName' already exists in this directory."
                        )
                    }

                    val newFile = directory.createChildData(this, fileName)
                    VfsUtil.saveText(newFile, content)
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

    private fun resolveTargetDirectory(
        event: AnActionEvent
    ): VirtualFile? {
        val selectedFile =
            event.getData(CommonDataKeys.VIRTUAL_FILE)
                ?: event.getData(CommonDataKeys.PSI_FILE)?.virtualFile
                ?: event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
                    ?.firstOrNull()
                ?: return null

        return if (selectedFile.isDirectory) {
            selectedFile
        } else {
            selectedFile.parent
        }
    }

    private fun isValidFileName(name: String): Boolean {
        if (name.isBlank()) return false

        if (name.startsWith(".") || name.endsWith(".")) {
            return false
        }

        // Allow letters, numbers, hyphens and underscores.
        return name.matches(Regex("[a-zA-Z][a-zA-Z0-9_-]*"))
    }

    private fun toPascalCase(name: String): String {
        return name
            .split(Regex("[-_]+"))
            .filter { it.isNotBlank() }
            .joinToString("") { part ->
                part.replaceFirstChar { char ->
                    char.uppercase()
                }
            }
    }
}

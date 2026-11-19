package com.shahryar.nextjstoolkit.actions

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.shahryar.nextjstoolkit.generators.NextTemplateGenerator
import com.shahryar.nextjstoolkit.generators.SourceLanguage

/**
 * Root submenu registered in both Project View and Editor popup menus.
 *
 * The language groups are populated once when the action group is created.
 */
class NextJsTemplatesGroup : DefaultActionGroup("Next.js", true) {

    init {
        val typescriptGroup = DefaultActionGroup("TypeScript", true)
        NextTemplateGenerator.templates.forEach { template ->
            typescriptGroup.add(
                CreateNextFileAction(template, SourceLanguage.TYPESCRIPT)
            )
        }

        val javascriptGroup = DefaultActionGroup("JavaScript", true)
        NextTemplateGenerator.templates.forEach { template ->
            javascriptGroup.add(
                CreateNextFileAction(template, SourceLanguage.JAVASCRIPT)
            )
        }

        add(typescriptGroup)
        add(javascriptGroup)
    }
}

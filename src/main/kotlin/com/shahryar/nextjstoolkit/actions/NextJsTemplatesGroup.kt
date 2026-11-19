package com.shahryar.nextjstoolkit.actions

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.shahryar.nextjstoolkit.generators.NextTemplateGenerator
import com.shahryar.nextjstoolkit.generators.SourceLanguage
import com.shahryar.nextjstoolkit.icons.NextIcons
import javax.swing.Icon

class NextJsTemplatesGroup : DefaultActionGroup(
    "Next.js",
    true
) {

    init {
        templatePresentation.icon = NextIcons.NEXT

        add(
            createLanguageGroup(
                title = "TypeScript",
                icon = NextIcons.TS,
                language = SourceLanguage.TYPESCRIPT
            )
        )

        add(
            createLanguageGroup(
                title = "JavaScript",
                icon = NextIcons.JS,
                language = SourceLanguage.JAVASCRIPT
            )
        )
    }

    private fun createLanguageGroup(
        title: String,
        icon: Icon?,
        language: SourceLanguage
    ): DefaultActionGroup {

        return DefaultActionGroup(title, true).apply {
            templatePresentation.icon = icon

            NextTemplateGenerator.templates.forEach { template ->
                add(
                    CreateNextFileAction(
                        template = template,
                        language = language
                    )
                )
            }
        }
    }
}

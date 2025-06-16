package org.jetbrains.plugins.template.startup

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.jetbrains.plugins.template.disableItemsOnStartup

class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        disableItemsOnStartup(project, thisLogger())
    }
}
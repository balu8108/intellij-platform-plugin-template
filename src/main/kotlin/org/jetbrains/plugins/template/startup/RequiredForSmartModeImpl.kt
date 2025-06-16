package org.jetbrains.plugins.template.startup

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.plugins.template.disableItemsOnStartup

class RequiredForSmartModeImpl : StartupActivity.RequiredForSmartMode {
    override fun runActivity(project: Project) {
        disableItemsOnStartup(project, thisLogger())
    }
}
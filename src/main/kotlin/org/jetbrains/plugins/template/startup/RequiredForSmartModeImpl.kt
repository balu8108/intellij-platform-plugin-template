package org.jetbrains.plugins.template.startup

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.wm.ToolWindowManager

class RequiredForSmartModeImpl : StartupActivity.RequiredForSmartMode {
    override fun runActivity(project: Project) {
        val allowedToolWindows = setOf("Project", "Version Control", "Pull Requests", "Commit")
        ApplicationManager.getApplication().invokeLater {
            val toolWindowManager = ToolWindowManager.getInstance(project)

            thisLogger().info("balatag: appFrameCreated: ${toolWindowManager.toolWindowIdSet} , ${toolWindowManager.toolWindowIds.toList()}")

            toolWindowManager.toolWindowIdSet
                .filterNot { it in allowedToolWindows }
                .forEach { id ->
                    try {
                        toolWindowManager.unregisterToolWindow(id)
                    } catch (th: Throwable) {
                        thisLogger().warn("Failed to unregister tool window: $id", th)
                    }
                }
        }

        val actionManager = ActionManager.getInstance()
        val allActionIds = actionManager.getActionIdList("")
        thisLogger().info("Registered actions: ${allActionIds.joinToString(", ")}")
    }
}
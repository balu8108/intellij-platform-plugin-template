package org.jetbrains.plugins.template


import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

fun disableItemsOnStartup(project: Project, logger: Logger) {
    ApplicationManager.getApplication().invokeLater {
        val toolWindowManager = ToolWindowManager.getInstance(project)

        logger.info("balatag: MyProjectActivity: ${toolWindowManager.toolWindowIdSet} , ${toolWindowManager.toolWindowIds.toList()}")

        toolWindowManager.toolWindowIdSet
            .filterNot { it in allowedToolWindows }
            .forEach { id ->
                try {
                    toolWindowManager.unregisterToolWindow(id)
                } catch (th: Throwable) {
                    logger.warn("Failed to unregister tool window: $id", th)
                }
            }

        PluginManagerCore.plugins.filterNot {
            val id = it.pluginId.idString
            id in allowedPluginIds || (!id.contains("jetbrains") && !id.contains("intellij"))
        }.forEach {
            val id = it.pluginId.idString
            logger.info("Disabling plugin: ${it.name} (ID: $id)")
            try {
                PluginManagerCore.disablePlugin(it.pluginId)
            } catch (e: Exception) {
                logger.warn("Failed to disable plugin: ${it.name} (ID: $id)", e)
            }
        }

        restrictProjectViewToProjectPane(project, logger)
    }
}

fun restrictProjectViewToProjectPane(project: Project, logger: Logger) {
    val projectView = ProjectView.getInstance(project)
    val allowedPaneId = "Scope" // typically "Project"

    projectView.changeView(allowedPaneId)
    projectView.paneIds.forEach {
        logger.info("balatag: ProjectView pane: $it")
        if (it != allowedPaneId) {
            try {
                projectView.removeProjectPane(projectView.getProjectViewPaneById(it))
            } catch (e: Exception) {
                logger.warn("Failed to remove project pane: $it", e)
            }
        }
    }
}
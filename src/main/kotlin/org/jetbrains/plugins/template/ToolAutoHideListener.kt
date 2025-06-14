package org.jetbrains.plugins.template

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener

@Service(Service.Level.PROJECT)
class ToolAutoHideListener(private val project: Project) : ToolWindowManagerListener {
    val allowedToolWindows = listOf("Project", "Version Control", "Pull Requests", "Commit")

    override fun toolWindowsRegistered(ids: List<String?>, toolWindowManager: ToolWindowManager) {
        ids.forEach {
            val id = it ?: return@forEach
            thisLogger().info("balatag: toolWindowRegistered: $id")
            if (id !in allowedToolWindows) {
//                try {
//                    toolWindowManager.getToolWindow(id)?.hide(null)
//                    toolWindowManager.unregisterToolWindow(id)
//                } catch (e: Throwable) {
//                    thisLogger().warn("Failed to unregister tool window: $id", e)
//                }
                thisLogger().info("balatag: toolWindowRegistered not allowed: $id")
            } else {
                thisLogger().info("balatag: toolWindowRegistered allowed: $id")
            }
        }
        super.toolWindowsRegistered(ids, toolWindowManager)
    }

    override fun toolWindowShown(toolWindow: ToolWindow) {
        val activeId = toolWindow.id
        val manager = ToolWindowManager.getInstance(project)

        thisLogger().info("balatag: toolWindowShown: $activeId")
//        if (activeId !in allowedToolWindows) {
//            manager.unregisterToolWindow(activeId)
//            toolWindow.hide(null)
//            return
//        }

        manager.toolWindowIds
            .filter { it != activeId }
            .forEach { id ->
                manager.getToolWindow(id)?.hide(null)
            }
        toolWindow.activate {
            toolWindow.isAutoHide = false
            toolWindow.isShowStripeButton = true
            manager.setMaximized(toolWindow, true)
//            toolWindow.setMaximized(true)
        }
    }
}
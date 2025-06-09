package org.jetbrains.plugins.template

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener

@Service(Service.Level.PROJECT)
class ToolAutoHideListener(private val project: Project) : ToolWindowManagerListener {
    override fun toolWindowShown(toolWindow: ToolWindow) {
        val manager = ToolWindowManager.getInstance(project)
        val activeId = toolWindow.id
        manager.toolWindowIds
            .filter { it != activeId }
            .forEach { id ->
                manager.getToolWindow(id)?.hide(null)
            }
        toolWindow.activate {
            toolWindow.setAutoHide(false)
            toolWindow.setShowStripeButton(true)
//            toolWindow.setMaximized(true)
        }
    }
}
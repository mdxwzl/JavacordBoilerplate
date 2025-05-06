package tech.mdxwzl.annotations

import org.javacord.api.entity.permission.PermissionType
import tech.mdxwzl.enums.ContextMenuType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadContextMenu(
    val name: String,
    val type: ContextMenuType,
    val permission: PermissionType = PermissionType.USE_APPLICATION_COMMANDS,
    val enabledInDms: Boolean = false
)
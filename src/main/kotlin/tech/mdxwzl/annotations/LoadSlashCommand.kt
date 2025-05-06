package tech.mdxwzl.annotations

import org.javacord.api.entity.permission.PermissionType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadSlashCommand(
    val name: String,
    val description: String,
    val permission: PermissionType = PermissionType.USE_APPLICATION_COMMANDS,
    val options: Boolean = false,
    val enabledInDms: Boolean = false
)

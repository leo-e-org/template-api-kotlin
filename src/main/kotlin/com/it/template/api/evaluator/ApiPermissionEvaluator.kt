package com.it.template.api.evaluator

import org.apache.commons.lang3.StringUtils
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class ApiPermissionEvaluator : PermissionEvaluator {

    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        if (authentication == null || permission !is String) return false
        return hasPrivilege(authentication, targetDomainObject.toString().uppercase(), permission.uppercase())
    }

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        if (authentication == null || permission !is String) return false
        return validateToken(authentication)
    }

    private fun hasPrivilege(authentication: Authentication, targetType: String, permission: String): Boolean {
        return authentication.authorities.any {
            it.authority.startsWith(targetType) && it.authority.contains(permission)
        }
    }

    private fun validateToken(authentication: Authentication): Boolean {
        val token = authentication.credentials?.toString()
        return StringUtils.isNotBlank(token) && StringUtils.startsWith(token, "Bearer ")
    }
}

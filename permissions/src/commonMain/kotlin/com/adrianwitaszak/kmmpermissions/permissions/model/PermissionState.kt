package com.adrianwitaszak.kmmpermissions.permissions.model


/**
 * Represents the state of a permission
 */
enum class PermissionState {
    /**
     * Indicates that the permission has not been requested yet
     */
    NOT_DETERMINED,

    /**
     * Indicates that the permission has been requested and accepted.
     */
    GRANTED,

    /**
     * No permission delegate is available for this permission
     * It has not been implemented or it is no required on this platform
     */
    NO_PERMISSION_DELEGATE,

    /**
     * Indicates that the permission has been requested but the user denied the permission
     */
    DENIED;

    /**
     * Extension function to check if the permission is not granted
     */
    fun notGranted(): Boolean {
        return this != GRANTED
    }
}


package com.example.photovideoclockframe.utility.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager(private val activity: Activity) {

    fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestReadExternalStoragePermission(requestCode: Int) {
        requestPermissions(READ_EXTERNAL_STORAGE_PERMISSION, requestCode)
    }

    fun requestPermissions(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun readExternalStorageGranted(): Boolean {
        return isGranted(READ_EXTERNAL_STORAGE_PERMISSION)
    }

    companion object {
        const val READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
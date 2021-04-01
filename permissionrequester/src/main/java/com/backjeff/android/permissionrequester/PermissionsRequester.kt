package com.backjeff.android.permissionrequester

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionsRequester<F>(
    private val fragmentListener: F,
    private val permissions: Array<String>
) where F : Fragment, F : PermissionRequesterListener {

    fun checkPermissions() {
        if (allPermissionsGranted()) {
            fragmentListener.onRequestPermissionGranted()
            return
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        if (!allPermissionsGranted()) {
            fragmentListener.requestPermissions(
                permissions,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            fragmentListener.requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var permissionsAllowed = true

        if (requestCode != REQUEST_CODE_PERMISSIONS) return

        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                permissionsAllowed = false

                if (shouldShowRequestPermissionRationale(
                        fragmentListener.requireActivity(),
                        permission
                    )
                ) {
                    fragmentListener.onRequestPermissionDenied(false)
                } else {
                    // user also CHECKED "never ask again"
                    fragmentListener.onRequestPermissionDenied(true)
                }

                return@forEachIndexed
            }
        }

        if (permissionsAllowed && allPermissionsGranted()) {
            fragmentListener.onRequestPermissionGranted()
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 0x3E3
        fun hasAllPermissions(context: Context, permissions: Array<String>) = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}

interface PermissionRequesterListener {
    fun onRequestPermissionGranted() { /* empty */ }
    fun onRequestPermissionDenied(isDefinitive: Boolean) { /* empty */ }
}

package com.backjeff.android.permissionrequester.app

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.backjeff.android.permissionrequester.PermissionRequesterListener
import com.backjeff.android.permissionrequester.PermissionsRequester

class PermissionFragment : Fragment(R.layout.fragment_permission), PermissionRequesterListener {

    private val permissionsRequester by lazy {
        PermissionsRequester(
            this,
            arrayOf(Manifest.permission.CAMERA)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                permissionsRequester.checkPermissions()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (permissionsRequester.allPermissionsGranted()) {
            toast("All permissions granted")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsRequester.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onRequestPermissionGranted() {
        toast("Permission granted")
    }

    override fun onRequestPermissionDenied(isDefinitive: Boolean) {
        if (isDefinitive) {
            toast("Permission definitely denied")
        } else {
            toast("Permission denied")
        }
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

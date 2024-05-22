package com.mynewsapp.fcm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mynewsapp.MainActivity

object Permissions {
    // Function to request permission

    fun requestNotificationPermission(activity: MainActivity): Boolean{
        var hasNotificationPermission = false
        val launchPermissionRequest = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionGranted ->
            hasNotificationPermission = permissionGranted
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launchPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        return hasNotificationPermission
    }
}
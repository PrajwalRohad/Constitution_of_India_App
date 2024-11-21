package com.example.constitutionofindia

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.IndiaCanon.constitutionofindia.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed


class AppUpdate(activity: Activity) : InstallStateUpdatedListener {

    private var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 500
    private var parentActivity: Activity = activity
    private var installStateUpdatedListener: InstallStateUpdatedListener

    private var currentType = AppUpdateType.FLEXIBLE

    init {
        appUpdateManager = AppUpdateManagerFactory.create(CoIApplication.appContext)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            // Check if update is available
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) { // UPDATE IS AVAILABLE

                if (info.isFlexibleUpdateAllowed) {
                    startUpdate(info, currentType)
                }

            }
        }

        installStateUpdatedListener = InstallStateUpdatedListener { state ->
            onStateUpdate(state)
        }


        appUpdateManager.registerListener(installStateUpdatedListener)
    }


    private fun startUpdate(info: AppUpdateInfo, type: Int) {
        appUpdateManager.startUpdateFlowForResult(
            info,
            parentActivity,
            AppUpdateOptions.defaultOptions(currentType),
            MY_REQUEST_CODE
        )
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (currentType == AppUpdateType.FLEXIBLE) {
                // If the update is downloaded but not installed, notify the user to complete the update.
                if (info.installStatus() == InstallStatus.DOWNLOADED)
                    flexibleUpdateDownloadCompleted()
            } else if (currentType == AppUpdateType.IMMEDIATE) {
                // for AppUpdateType.IMMEDIATE only, already executing updater
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdate(info, AppUpdateType.IMMEDIATE)
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                // If the update is cancelled or fails, you can request to start the update again.
//                Log.e("UPDATE1234", "Update flow failed! Result code: $resultCode")
            }
        }
    }

    private fun flexibleUpdateDownloadCompleted() {
        Snackbar.make(
            parentActivity.findViewById(R.id.activity_main_drawer),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAnchorView(R.id.activity_main_cvAdCard)
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(Color.WHITE)
            show()
        }
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            flexibleUpdateDownloadCompleted()
        }
    }

}


















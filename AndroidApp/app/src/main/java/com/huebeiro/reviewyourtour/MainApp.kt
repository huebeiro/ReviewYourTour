package com.huebeiro.reviewyourtour

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.huebeiro.reviewyourtour.database.AppDatabase

/**
 * Author: Adilson Ribeiro
 * Date: 16/01/18
 */
class MainApp : Application() {


    override fun onCreate() {
        super.onCreate()

        mInstance = this
        mDatabase = Room.databaseBuilder(this, AppDatabase::class.java, BuildConfig.APPLICATION_ID).build()
        mRequestQueue = Volley.newRequestQueue(this)

        val message = String.format(
                "Application Started!\n" +
                        "    App Version: %s(%d)\n" +
                        "    System Build: %s(%d)",
                versionName,
                versionCode,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT
        )
        Log.i(TAG, message)

    }

    companion object {

        val TAG = "MainApp"
        private var mInstance: MainApp? = null
        private var mDatabase: AppDatabase? = null
        private var mRequestQueue: RequestQueue? = null

        /**
         * @return the Context of this application
         */
        val appContext: Context
            get() = mInstance!!.applicationContext

        /**
         * Get the versionName from the manifest.
         *
         * @return The versionName as a String.
         */
        val versionName: String?
            get() {
                val context = appContext
                val packageName = context.packageName
                try {
                    val pm = context.packageManager
                    return pm.getPackageInfo(packageName, 0).versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(TAG, "Unable to find the name $packageName in the package")
                    return null
                }
            }
        /**
         * Get the versionName from the manifest.
         *
         * @return The versionName as a String.
         */
        val versionCode: Int?
            get() {
                val context = appContext
                val packageName = context.packageName
                try {
                    val pm = context.packageManager
                    return pm.getPackageInfo(packageName, 0).versionCode
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(TAG,"Unable to find the name $packageName in the package")
                    return null
                }
            }

        /**
         * The current package name.
         *
         * @return The package name of the current context.
         */
        val packageName: String
            get(){
                return appContext.packageName
            }

        val mainDatabase: AppDatabase
            get(){
                return mDatabase!!
            }

        val mainRequestQueue: RequestQueue
            get(){
                return mRequestQueue!!
            }
    }

}
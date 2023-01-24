package com.chatlog.aep

import android.app.Application
import android.content.ContentValues.TAG
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import io.github.rybalkinsd.kohttp.ext.httpGet
import okhttp3.Response
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class MyApp: Application() {
    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGet(token: String) {
        val url = URL("https://chatlog.ru/api/aep/new-token/$token")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT > 9) {
            val gfgPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(gfgPolicy)
        }
        FirebaseApp.initializeApp(this@MyApp)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {   task ->
            if(!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            Log.e("TAG", "Token -> $token")
            sendGet(token)
        }
    }
}
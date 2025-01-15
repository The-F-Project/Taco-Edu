package org.thefproject.taco

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.multidex.BuildConfig
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime

class updater : AppCompatActivity() {
    private lateinit var branchSwitch: Switch
    private var downloadId: Long = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updater)

        val osVersion = android.os.Build.VERSION.SDK_INT
        println("Версия Android: $osVersion")
        val osVersionName = android.os.Build.VERSION.RELEASE
        println("Сборка: $osVersionName")

        val upds = findViewById<View>(R.id.upd)

        upds.setOnClickListener {
            checkForUpdates()

        }

        val currentDateTime = LocalDateTime.now()
        println("Текущая дата и время: $currentDateTime")

        val textView: TextView = findViewById(R.id.textView30)

        val sdkVersion = android.os.Build.VERSION.SDK_INT
        val Pacname = "PAC-NAME $packageName"
        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE
        val sdkVersionText = "Версия SDK: $sdkVersion"

        textView.text =
            "Версия Android: $osVersion ANDROID BUILD-ID: $osVersionName Текущее вермя: $currentDateTime :Версия EduCA $versionName Номер сборки $versionCode"


        branchSwitch = findViewById(R.id.branchSwitch)


    }


    fun checkForUpdates() {

        val client = OkHttpClient()

        val url = if (branchSwitch.isChecked) {

            "https://raw.githubusercontent.com/The-F-Project/EduCA/refs/heads/dev-branch/betaversionapp.json"

        } else {

            "https://raw.githubusercontent.com/The-F-Project/EduCA/refs/heads/dev-branch/versionapp.json"

        }


        val request = Request.Builder()

            .url(url)

            .build()


        client.newCall(request).enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {

                runOnUiThread {

                    Toast.makeText(
                        this@updater,
                        "Невозможно подключиться к серверу",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }


            override fun onResponse(call: okhttp3.Call, response: Response) {

                if (response.isSuccessful) {

                    response.body?.string()?.let { jsonString ->

                        val jsonObject = JSONObject(jsonString)

                        val latestVersion = jsonObject.getString("version")

                        val downloadUrl = jsonObject.getString("url")


                        val currentVersion = BuildConfig.VERSION_NAME


                        if (latestVersion > currentVersion) {

                            runOnUiThread {

                                Toast.makeText(
                                    this@updater,
                                    "Доступна новая версия: $latestVersion",
                                    Toast.LENGTH_SHORT
                                ).show()

                                downloadAPK(downloadUrl)

                            }

                        } else {

                            runOnUiThread {

                                Toast.makeText(
                                    this@updater,
                                    "Ваша версия актуальна",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }

                    }

                } else {

                    runOnUiThread {

                        Toast.makeText(
                            this@updater,
                            "Ошибка: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            }

        })

    }

    fun downloadAPK(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Загрузка обновления для EduCA")
        request.setDescription("Происходит загрузка обновления")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(this, null, "EduCA.apk")

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)




    }

    val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                Toast.makeText(this@updater, "Загрузка завершена", Toast.LENGTH_SHORT).show()
                installAPK()
            }
        }
    }

    fun installAPK() {
        val apkFile = Uri.fromFile(getExternalFilesDir(null)?.resolve("EduCA.apk"))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(apkFile, "application/vnd.android.package-archive")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(downloadReceiver)
    }




}
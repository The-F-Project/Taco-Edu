package org.thefproject.taco

import android.content.Intent
import android.os.Bundle
import android.support.multidex.BuildConfig
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class list10th : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list10th)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val geom = findViewById<View>(R.id.button9)
        val russian = findViewById<View>(R.id.button21)
        val profile = findViewById<View>(R.id.profile)
        val algebra = findViewById<View>(R.id.button2)
        val english = findViewById<View>(R.id.button9)
        val belmova = findViewById<View>(R.id.button)
        val himia = findViewById<View>(R.id.button12)
        val history = findViewById<View>(R.id.buttonh)
        val geo = findViewById<View>(R.id.button23)


        geo.setOnClickListener {


            val intent = Intent(this,geography10::class.java)
            startActivity(intent)

        }


        geom.setOnClickListener {

            val intent = Intent(this, geometrya10::class.java)
            startActivity(intent)
            checkForUpdates()
        }

        history.setOnClickListener {


            val intent = Intent(this,history10::class.java)
            startActivity(intent)

        }

        himia.setOnClickListener {


            val intent = Intent(this,himia10::class.java)
            startActivity(intent)
            checkForUpdates()


        }
        belmova.setOnClickListener {

            val intent = Intent(this,belmova10::class.java)
            startActivity(intent)
            checkForUpdates()
        }

        english.setOnClickListener {
            val intent = Intent(this,english10::class.java)
            startActivity(intent)
            checkForUpdates()

        }
        russian.setOnClickListener {

            val intent = Intent(this, Russian10::class.java)
            startActivity(intent)
            checkForUpdates()
        }

        algebra.setOnClickListener {

            checkForUpdates()
            val intent = Intent(this, Algebra10::class.java)
            startActivity(intent)
        }

        val predmet = findViewById<View>(R.id.prdmet)
        predmet.setOnClickListener {

            openOtherApp("org.thefproject.fedu")

        }
        profile.setOnClickListener {



            val intent = Intent(this, updater::class.java)
            startActivity(intent)
            checkForUpdates()
        }
    }


    private fun openOtherApp(packageName: String) {
        listInstalledApps()
        Log.d("OpenOtherApp", "Trying to open app with package name: $packageName")

        val intent = packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {
            listInstalledApps()
            Log.d("OpenOtherApp", "Launching app: $packageName")

            startActivity(intent)

        } else {

            Log.d("OpenOtherApp", "App not found, showing error message")

            Toast.makeText(this, "Приложение не установлено", Toast.LENGTH_SHORT).show()

        }

    }
    private fun listInstalledApps() {

        val packages = packageManager.getInstalledPackages(0)

        for (packageInfo in packages) {

            Log.d("InstalledApp", "App: ${packageInfo.packageName}")

        }







    }

    fun checkForUpdates() {

        val client = OkHttpClient()

        val url = "https://raw.githubusercontent.com/The-F-Project/EduCA/refs/heads/dev-branch/frameinstall.json"


        val request = Request.Builder()

            .url(url)

            .build()


        client.newCall(request).enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {

                runOnUiThread {



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



                            }


                        }

                    }

                } else {

                    runOnUiThread {
                        val s = Intent(applicationContext,updater::class.java)

                        startActivity(s)

                    }

                }

            }

        })

    }
}
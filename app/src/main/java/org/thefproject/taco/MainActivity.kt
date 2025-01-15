package org.thefproject.taco

import android.app.VoiceInteractor
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.multidex.BuildConfig
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val openAppButton = findViewById<Button>(R.id.button3)
        openAppButton.setOnClickListener {


            val i = Intent(this, list10th::class.java)
            startActivity(i)

        }
        val class9 = findViewById<View>(R.id.button2)
        val class8 = findViewById<View>(R.id.button)
        class8.setOnClickListener {


            Toast.makeText(this, "Временно не доступно", Toast.LENGTH_SHORT).show()
        }
        class9.setOnClickListener {
            val i = Intent(this, list9lass::class.java)
            startActivity(i)
            Toast.makeText(this@MainActivity, "ВНИМАНИЕ! ЭТОТ РАЗДЕЛ НЕ ГОТОВ", Toast.LENGTH_SHORT).show()
        }

        val predmets = findViewById<View>(R.id.prdmet)
        predmets.setOnClickListener {

        }
        val reshebnik10 = findViewById<View>(R.id.reshebnik)

        reshebnik10.setOnClickListener {

        }
        val profile = findViewById<View>(R.id.profile)
        profile.setOnClickListener {
            val i = Intent(this, settings::class.java)
            startActivity(i)
        }



    }





    private fun openOtherApp(packageName: String) {
        Log.d("OpenOtherApp", "Trying to open app with package name: $packageName")

        val intent = packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {
            startActivity(intent)
            Log.d("OpenOtherApp", "Launching app: $packageName")
        } else {
            Log.d("OpenOtherApp", "App not found, showing error message")
            Toast.makeText(this, "Приложение не установлено", Toast.LENGTH_SHORT).show()

            val i = Intent(this, NoInstallApp::class.java)
            startActivity(i)
        }

    }

    private fun listInstalledApps() {

        val packages = packageManager.getInstalledPackages(0)

        for (packageInfo in packages) {

            Log.d("InstalledApp", "App: ${packageInfo.packageName}")

        }

    }
}
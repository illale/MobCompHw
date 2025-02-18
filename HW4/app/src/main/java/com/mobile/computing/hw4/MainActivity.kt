package com.mobile.computing.hw4

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.computing.hw4.ui.theme.HW4Theme
import kotlinx.serialization.Serializable

@Serializable
object Cons;


class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var temperatureViewModel: TemperatureViewModel
    private var mTemperature: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        temperatureViewModel = TemperatureViewModel()
        setContent {
            HW4Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Cons) {
                    composable<Cons> {
                        Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                            DataLayout(
                                enableNotifications = {
                                    ActivityCompat.requestPermissions(this@MainActivity,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                                },
                                temperatureViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        val temp = p0?.values?.get(0)
        if (temp != null) {
            Log.d("TEMP", "TEMPERATURE: $temp")
            temperatureViewModel.updateTemperature(temp)
            if (temp >= 1) {
                notification(this)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        mTemperature?.also { temperature ->
            sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Channel"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Something", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
        notificationManager.createNotificationChannel(channel)


    }
}

fun notification(context: Context) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, "Something")
        .setSmallIcon(R.drawable.ic_android_black_24dp)
        .setContentTitle("Temperature has changed")
        .setContentText("It's getting hot in here")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            return@with
        }

        notify(1, builder.build())
    }

}


package com.aybarsacar.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val buttonOneTimeRequest = findViewById<Button>(R.id.button)
    val buttonPeriodicRequest = findViewById<Button>(R.id.button_periodic_request)
    val tvOneTimeRequest = findViewById<TextView>(R.id.tv_main_display)

    buttonOneTimeRequest.setOnClickListener {

      // create constraints to make sure the work manager only runs
      // when the following constraints are met
      val oneTimeRequestConstraints = Constraints.Builder()
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

      val data = Data.Builder()
      data.putString("inputKey", "input value")

      val sampleWork = OneTimeWorkRequest
        .Builder(OneTimeRequestWorker::class.java)
        .setInputData(data.build())
        .setConstraints(oneTimeRequestConstraints)
        .build()

      // enqueue our Work request to the WorkManager
      WorkManager.getInstance(this@MainActivity).enqueue(sampleWork)

      WorkManager.getInstance(this@MainActivity)
        .getWorkInfoByIdLiveData(sampleWork.id)
        .observe(this@MainActivity, { workInfo ->
          OneTimeRequestWorker.Companion.logger(workInfo.state.name)

          if (workInfo != null) {
            when (workInfo.state) {
              WorkInfo.State.ENQUEUED -> {
                tvOneTimeRequest.text = "Task Enqueued"
              }

              WorkInfo.State.BLOCKED -> {
                tvOneTimeRequest.text = "Task Blocked"
              }

              WorkInfo.State.RUNNING -> {
                tvOneTimeRequest.text = "Task Running"
              }

              else -> {
                tvOneTimeRequest.text = "Task State Else"
              }
            }
          }

          if (workInfo != null && workInfo.state.isFinished) {
            // our task has run already
            when (workInfo.state) {
              WorkInfo.State.SUCCEEDED -> {
                tvOneTimeRequest.text = "Task Successful "

                // get the output data
                val successfulOutputData = workInfo.outputData
                val outputText = successfulOutputData.getString("outputKey")
                Log.i("Worker Output", "$outputText")
              }

              WorkInfo.State.FAILED -> {
                tvOneTimeRequest.text = "Task Failed"
              }

              WorkInfo.State.CANCELLED -> {
                tvOneTimeRequest.text = "Task Cancelled"
              }

              else -> {
                tvOneTimeRequest.text = "Task State Finshed Else"
              }
            }
          }
        })
    }

    buttonPeriodicRequest.setOnClickListener {

      // create constraints to make sure the work manager only runs
      // when the following constraints are met
      val periodicRequestConstraints = Constraints.Builder()
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

      // create a periodic request
      val periodicRequest = PeriodicWorkRequest.Builder(PeriodicRequestWorker::class.java, 4, TimeUnit.SECONDS)
        .setConstraints(periodicRequestConstraints)
        .build()

      WorkManager.getInstance(this@MainActivity)
        .enqueueUniquePeriodicWork("Periodic work request", ExistingPeriodicWorkPolicy.KEEP, periodicRequest)

    }
  }
}
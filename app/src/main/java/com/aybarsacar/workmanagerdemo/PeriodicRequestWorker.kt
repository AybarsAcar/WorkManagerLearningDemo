package com.aybarsacar.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*


class PeriodicRequestWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

  override fun doWork(): Result {

    val date = getDate(System.currentTimeMillis())

    Log.i("Periodic WorkRequest", "doWork Execution DateTime: $date")

    return Result.success()
  }

  private fun getDate(milliSeconds: Long): String {

    val formatter = SimpleDateFormat("dd/mm/yyyy hh:mm:ss.SSS", Locale.getDefault())

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds

    return formatter.format(calendar.time)
  }


}
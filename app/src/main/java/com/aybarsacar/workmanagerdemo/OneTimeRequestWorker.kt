package com.aybarsacar.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class OneTimeRequestWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

  override fun doWork(): Result {

    val inputValue = inputData.getString("inputKey")

    println("Worker input: $inputValue")

    // Background running Task logic
    Thread.sleep(2000)

    return Result.success(createOutputData())
  }

  private fun createOutputData(): Data {
    return Data.Builder().putString("outputKey", "output value").build()
  }

  object Companion {
    fun logger(message: String) = Log.i("WorkRequest Status", message)
  }
}
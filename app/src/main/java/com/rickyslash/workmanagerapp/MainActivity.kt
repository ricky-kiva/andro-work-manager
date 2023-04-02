package com.rickyslash.workmanagerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.rickyslash.workmanagerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var workManager: WorkManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workManager = WorkManager.getInstance(this)
        binding.btnOneTimeTask.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnOneTimeTask -> startOneTimeTask()
        }
    }

    private fun startOneTimeTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(oneTimeWorkRequest)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this@MainActivity) { workInfo ->
                val status = workInfo.state.name
                binding.textStatus.append("\n" + status)
            }
    }
}

// WorkManager allow background process to always be executed (deferrable) even though the app is closed / device restarted
// WorkManager benefit:
// - support one-time task / periodic task
// - support to make chain between task (continuous process)
// - support to API level 14
// - available even without Google Play Service
// - works well with doze mode
// - supports LiveData to see the status (Work status)

// When to use WorkManager: (suitable for action that is deferrable (useful though it's not directly being done))
// - send log to server
// - send media to server
// - sync database periodically from server
// - apply filter to image

// Flow of what component to use for task:
// - Local Deferrable Work & sync -> WorkManager
// -- Is Online Triggered -> Firebase Cloud Messaging & WorkManager
// --- User expects to run immediately without interruption -> Foreground Service
// ---- Need to execute at exact time -> Alarm Manager

// WorkManager component:
// - Worker: put the task that will be executed in background thread (doWork())
// - WorkRequest: OneTimeWorkRequest for 1 time execution, PeriodicWorkRequest for periodic execution
// - Constraint: manage when the task is being executed (when there's internet, when device is charged, etc)
// - Data: to send data to Worker with key-value format
// - WorkManager: to run WorkRequest & manage task-chaining
// - Result: to see Worker status (waiting, failed, or success)
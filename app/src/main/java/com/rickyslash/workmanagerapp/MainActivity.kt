package com.rickyslash.workmanagerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
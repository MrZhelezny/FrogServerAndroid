package com.zhelezny.frog.server.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zhelezny.frog.server.domain.ServerService
import com.zhelezny.frog.server.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            startService()
            binding.buttonStart.isEnabled = false
        }

        binding.buttonStop.setOnClickListener {
            stopService()
            binding.buttonStart.isEnabled = true
        }
    }

    private fun startService() {
        val serviceIntent = Intent(this, ServerService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopService() {
        val serviceIntent = Intent(this, ServerService::class.java)
        stopService(serviceIntent)
    }
}
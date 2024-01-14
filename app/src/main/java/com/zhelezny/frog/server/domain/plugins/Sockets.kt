package com.zhelezny.frog.server.domain.plugins

import android.os.Build
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pingPeriod = Duration.ofSeconds(15)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeout = Duration.ofSeconds(15)
        }
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}

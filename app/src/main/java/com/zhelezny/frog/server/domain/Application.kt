package com.zhelezny.frog.server.domain

import com.typesafe.config.ConfigFactory
import com.zhelezny.frog.server.domain.plugins.configureRouting
import com.zhelezny.frog.server.domain.plugins.configureSockets
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.config.*

fun startServer() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        module {
            configureRouting()
            configureSockets()
        }

        connector {
            port = 8080
//            host = config.property("ktor.deployment.host").getString()
        }
    }).start(wait = true)
}

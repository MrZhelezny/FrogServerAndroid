package com.zhelezny.frog.server.domain.plugins

import android.util.Log
import com.zhelezny.frog.server.data.PlayerName
import com.zhelezny.frog.server.data.getRandomColor
import com.zhelezny.frog.server.domain.Connection
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Collections
import java.util.UUID
import kotlin.collections.set

fun Application.configureRouting() {

    routing {

        val TAG = "Routing"
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

        val userForGameSession = mutableListOf<PlayerName>()

        webSocket("/joinGame") {

            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                Log.d(TAG, "Новое соединение! Всего: ${connections.count()}")

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue

                    val nickname = frame.readText()
                    userForGameSession.add(PlayerName(nickname))

                    val jsonPlayerNames = Json.encodeToString(userForGameSession)

                    connections.forEach {
                        Log.d(TAG, "Отправка сообщения: $jsonPlayerNames")
                        it.session.send(jsonPlayerNames)
                    }

                    Log.d(TAG, "Старт таймер...")
                    repeat(20) {
                        delay(1000L)
                    }

                    //по истечению таймера проверяем сколько игроков было в очереди
                    Log.d(TAG, "Истёк таймер")
                    userForGameSession.clear()
                    when (connections.size) {
                        in 2..5 -> {
                            val listColor = getRandomColor(connections.size)
                            var i = 0
                            connections.forEach {
                                val colorForConnection = listColor[i++]
                                Log.d(TAG, "Присваивание цвета: $colorForConnection")
                                it.session.send("Color $colorForConnection")
                            }
                            close(CloseReason(CloseReason.Codes.NORMAL, "End search session. Start GAME!"))
                        }

                        else -> {
                            close(CloseReason(CloseReason.Codes.NORMAL, "Need one more player!!!"))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
            } finally {
                Log.d(TAG, "Removing $thisConnection!")
                connections -= thisConnection
            }
        }

        webSocket("/goPlay2") {
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
            } finally {
                Log.d(TAG, "Removing $thisConnection!")
                connections -= thisConnection
            }
        }


        val allUsers = mutableMapOf<String, String>()

        webSocket("/getUid") {
            try {
                val msg = incoming.receive() as Frame.Text
//                val user = msg.readText().split(":")
//                val nickName = user[0]
//                if (user.size == 2) {
//                    if (allUsers.containsValue(user[1])) {
//                        send("!GOOD")
//                    } else {
//                        val id = UUID.randomUUID().toString()
//                        allUsers[nickName] = id
//                        send(id)
//                    }
//                } else {
//                    val id = UUID.randomUUID().toString()
//                    allUsers[nickName] = id
//                    send(id)
//                }
                val nickName = msg.readText()
                if (!allUsers.containsKey(nickName)) {
                    val id = UUID.randomUUID().toString()
                    Log.d(TAG, "Отправляем UID: $id")
                    allUsers[nickName] = id
                    send(id)
                } else {
                    send("Это имя занято")
                }
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
            } finally {
                Log.d(TAG, "Close connect!")
            }
        }

        webSocket("/chat") {
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
            } finally {
                Log.d(TAG, "Removing $thisConnection!")
                connections -= thisConnection
            }
        }


//        val nickName = call.request.queryParameters["user"].toString()
//
//        if (!usersForGameSession.containsKey(nickName)) {
//            usersForGameSession[nickName] = usersForGameSession.size.toString()
//        }
//
//        if (usersForGameSession.size == 2) {
//            call.respondText("Нанчинаем игру!", status = HttpStatusCode.Accepted)
//            usersForGameSession.clear()
//        } else {
//            call.respondText("Ожидаем")
//        }


    }
}

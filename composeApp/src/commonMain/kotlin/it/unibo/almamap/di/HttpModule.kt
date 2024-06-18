package it.unibo.almamap.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import it.unibo.almamap.data.createAlmaClass
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val httpModule = module {
    single {
        Ktorfit.Builder()
            .baseUrl("http://almaclass.campusfc.unibo.it:3001/")
            .httpClient(HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(Logging) {
                    logger = object: Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    level = LogLevel.ALL
                }
            })
            .build()
            .createAlmaClass()
    }
}
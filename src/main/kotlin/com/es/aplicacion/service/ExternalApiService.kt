package com.es.aplicacion.service

import com.es.aplicacion.domain.DatosProvincias
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class ExternalApiService(private val webClient: WebClient.Builder) {

    @Value("\${API_KEY}")
    private lateinit var apiKey: String

    fun obtenerDatosDesdeApi(): DatosProvincias? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/provincias?type=JSON&key=$apiKey")
            .retrieve()
            .bodyToMono(DatosProvincias::class.java)
            .block() // ⚠️ Esto bloquea el hilo, usar `subscribe()` en código reactivo
    }

    fun obtenerCiudadDesdeApi(): DatosProvincias? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/municipios?type=JSON&key=$apiKey")
            .retrieve()
            .bodyToMono(DatosProvincias::class.java)
            .block() // ⚠️ Esto bloquea el hilo, usar `subscribe()` en código reactivo

    }
}
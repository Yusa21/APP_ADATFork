package com.es.aplicacion.dto

import com.es.aplicacion.model.Direccion
import com.es.aplicacion.model.Usuario
import com.es.aplicacion.security.SecurityConfig
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String?,
    val calle: String,
    val numero: String,
    val cp: String,
    val provincia: String,
    val ciudad: String,
)

fun UsuarioRegisterDTO.toModel(): Usuario {
    var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    val encodedPassword = passwordEncoder.encode(password)
    return Usuario(
        null,
        this.username,
        encodedPassword,
        this.email,
        "USER",
        Direccion(this.calle, this.numero, this.cp,this.ciudad,this.provincia),
    )
}
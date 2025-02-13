package com.es.aplicacion.service

import com.es.aplicacion.dto.UsuarioDTO
import com.es.aplicacion.dto.UsuarioRegisterDTO
import com.es.aplicacion.dto.toModel
import com.es.aplicacion.error.exception.NotFoundException
import com.es.aplicacion.error.exception.UnauthorizedException
import com.es.aplicacion.model.Usuario
import com.es.aplicacion.repository.UsuarioRepository
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var apiService: ExternalApiService


    override fun loadUserByUsername(username: String?): UserDetails {
        var usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO? {
        if(usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat){
            throw BadRequestException("Las contraseñas debe ser iguales")
        }
        val usuarioModel = usuarioInsertadoDTO.toModel()
        val datosProvincias = apiService.obtenerDatosDesdeApi()

        // Si los datos vienen rellenos entonces busco la provincia dentro del resultado de la llamada
        if (datosProvincias != null) {
            if(datosProvincias.data != null) {
                datosProvincias.data.stream().filter {
                    it.PRO == usuarioModel.direccion.ciudad.uppercase()
                }.findFirst().orElseThrow {
                    NotFoundException("Provincia ${usuarioModel.direccion.ciudad.uppercase()} no válida")
                }
            }
        }
        usuarioRepository.insert(usuarioModel)
        return null

    }

}
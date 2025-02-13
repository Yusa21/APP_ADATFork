package com.es.aplicacion.error.exception

class NotFoundException (message: String)
    : kotlin.Exception("Not found exception (404). $message") {

}
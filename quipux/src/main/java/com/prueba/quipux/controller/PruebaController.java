package com.prueba.quipux.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hola mundo. Usuario autenticado: " + authentication.getName();
    }
}

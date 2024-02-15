package com.example.dataManagerWebApp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Adress not found")
public class AdressNotFoundException extends RuntimeException{
}

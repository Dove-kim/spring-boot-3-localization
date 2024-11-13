package com.dove.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.TimeZone;

@RestController
public class MainController {
    @GetMapping("/")
    public ResponseEntity<String> mainGet() {
        TimeZone timeZone = LocaleContextHolder.getTimeZone();
        return ResponseEntity.ok(timeZone.getID());
    }
}

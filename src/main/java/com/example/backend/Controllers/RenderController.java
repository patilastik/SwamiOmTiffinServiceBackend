// startServer() is used to activate the render server manually

package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/render")
public class RenderController {

    @GetMapping("/startServer")
    public ResponseEntity<?> startServer() {
        return ResponseEntity.ok("Swami Om Tiffin Service Backend is running!");
    }
}
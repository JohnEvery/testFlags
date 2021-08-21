package com.eve.flags.controller;

import com.eve.flags.service.interfaces.FlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/flag")
@RequiredArgsConstructor
public class FlagController {

    private final FlagService flagService;

    @GetMapping("/download")
    public ResponseEntity<Void> downloadFlags() throws IOException {
        flagService.downloadAllFlags();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

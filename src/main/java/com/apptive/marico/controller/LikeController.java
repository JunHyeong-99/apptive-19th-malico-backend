package com.apptive.marico.controller;

import com.apptive.marico.dto.LikeRequestDto;
import com.apptive.marico.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/like/member")
@RequiredArgsConstructor
public class LikeController {
    final private LikeService likeService;

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addLike(Principal principal, @RequestBody LikeRequestDto likeRequestDto) {
        likeService.addlike(principal.getName(), likeRequestDto);
    }
}

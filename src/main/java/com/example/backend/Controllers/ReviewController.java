package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.demo.exceptions.InvalidValueException;
import com.example.demo.exceptions.ActionNotAllowed;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/storeReview")
    public ResponseEntity<?> storeReview(@RequestBody ReviewDTO reviewDTO) {
        try
         {
            reviewService.storeReview(reviewDTO);
            return ResponseEntity.ok("Review stored successfully");
         }
        catch(InvalidValueException e)
         {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
         }
        catch(ActionNotAllowed e)
         {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         }
        catch(RuntimeException e)
         {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }
    }
}
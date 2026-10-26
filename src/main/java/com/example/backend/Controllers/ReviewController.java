package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ReviewDTO;

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
    }
}
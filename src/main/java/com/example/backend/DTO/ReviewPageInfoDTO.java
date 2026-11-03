// Object of this class contains list of reviews, average rating and total reviews

package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.example.backend.dto.ReviewsDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPageInfoDTO {
   private List<ReviewsDTO> reviewsDTO;
   private double averageRating;
   private Long totalReviews;

}
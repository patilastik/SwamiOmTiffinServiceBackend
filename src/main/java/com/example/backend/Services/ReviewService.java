package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Reviewer;
import org.springframework.stereotype.Service;
import com.example.demo.exceptions.InvalidValueException;

@Service
public class ReviewService {

    private final ReviewerRepository reviewerRepository;

    // Spring Boot automatically injects the objects of these repositories classes
    public ReviewService(ReviewerRepository reviewerRepository) {
        this.reviewerRepository = reviewerRepository;
    }

    public void storeReview(ReviewDTO reviewDTO) 
     {
        // Check whether data for all fields is send from frontend
        if(reviewDTO.getEmail().isEmpty()||reviewDTO.getEmail()==null)
        {
            throw new InvalidValueException("Email is required");
        }
        if(reviewDTO.getFirstName().isEmpty()||reviewDTO.getFirstName()==null)
        {
            throw new InvalidValueException("First Name is required");
        }
        if(reviewDTO.getLastName().isEmpty()||reviewDTO.getLastName()==null)
        {
            throw new InvalidValueException("Last Name is required");
        }
        if(reviewDTO.getMessage().isEmpty()||reviewDTO.getMessage()==null)
        {
            throw new InvalidValueException("Message is required");
        }
        if(reviewDTO.getStars()<0||reviewDTO.getStars()>5)
        {
            throw new InvalidValueException("Stars should be between 0 and 5");
        }
        String email = reviewDTO.getEmail();
        String firstName = reviewDTO.getFirstName();
        String lastName = reviewDTO.getLastName();
        String message = reviewDTO.getMessage();
        short stars = reviewDTO.getStars();
        Reviewer reviewer = new Reviewer(); // Entity object creation 
        reviewer.setEmail(email);
        reviewer.setMessage(message);
        reviwer.setFirstName(firstName);
        reviwer.setLastName(lastName);
        reviwer.setStars(stars);
        // Call the save() method of the repository class to save the object to the database
        Reviewer savedReviewer = reviewerRepository.save(reviewer);

     }
}

package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Reviewer;
import org.springframework.stereotype.Service;
import com.example.demo.exceptions.InvalidValueException;
import com.example.demo.exceptions.ActionNotAllowed;

import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import com.openai.models.moderations.ModerationCreateParams;
import com.theokoken.ai.openai.OpenAIClient;
import com.theokoken.ai.openai.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ReviewService {

    private final ReviewerRepository reviewerRepository;
    private final OpenAIClient client;

    // Spring Boot automatically injects the objects of these repositories classes
    public ReviewService(ReviewerRepository reviewerRepository,@Value("${openai.api.key}") String apiKey) {
        this.reviewerRepository = reviewerRepository;
        this.client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
    }

    // Things needs to be check:
    // 1) Checking whether frontend is sending data for all member variables or not, if any data is missing throw exception - Done
    // 2) Checking whether email is valid or not using regex - Done
    // 3) Check if the current email has already a review associated with,if yes then throw exception - Done
    // 4) Check if the first name and last name contains any bad word, if yes then throw exception.
    // 5) Check if the stars are between 0 and 5, if not then throw exception - Done
    // 6) Check if the message contains any bad word, if yes then throw exception.
    public void storeReview(ReviewDTO reviewDTO) 
     {
        // Check whether data for all fields is send from frontend
        if(reviewDTO.getEmail()==null || reviewDTO.getEmail().isBlank())
        {
            throw new InvalidValueException("Email is required");
        }
        if(reviewDTO.getFirstName()==null || reviewDTO.getFirstName().isBlank())
        {
            throw new InvalidValueException("First Name is required");
        }
        if(reviewDTO.getLastName()==null || reviewDTO.getLastName().isBlank())
        {
            throw new InvalidValueException("Last Name is required");
        }
        if(reviewDTO.getMessage()==null || reviewDTO.getMessage().isBlank())
        {
            throw new InvalidValueException("Message is required");
        }
        if(reviewDTO.getStars()==null || reviewDTO.getStars()<0||reviewDTO.getStars()>5)
        {
            throw new InvalidValueException("Stars should be between 0 and 5");
        }
        String email = reviewDTO.getEmail();
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if(!email.matches(emailRegex)) // If the email is invalid
         {
            throw new InvalidValueException("Invalid Email");
         }
        Optional<Reviewer> existingReview = reviewerRepository.findByEmail(email);
        if(existingReview.isPresent()) // If review already present with same email
         {
            throw new ActionNotAllowed("You have already submitted a review.");
         }
        String firstName = reviewDTO.getFirstName();
        if(containsHarmfulContent(firstName))
        {
            throw new InvalidValueException("First Name contains bad word");
        }
        String lastName = reviewDTO.getLastName();
        if(containsHarmfulContent(lastName))
        {
            throw new InvalidValueException("Last Name contains bad word");
        }
        String message = reviewDTO.getMessage();
        if(containsHarmfulContent(message))
        {
            throw new InvalidValueException("Message contains bad word");
        }
        short stars = reviewDTO.getStars();
        Reviewer reviewer = new Reviewer(); // Entity object creation 
        reviewer.setEmail(email);
        reviewer.setMessage(message);
        reviewer.setFirstName(firstName);
        reviewer.setLastName(lastName);
        reviewer.setStars(stars);
        // Call the save() method of the repository class to save the object to the database
        Reviewer savedReviewer = reviewerRepository.save(reviewer);
     }
    
    // Method to check whether a string contains bad word or content
    public boolean containsHarmfulContent(String message) 
     {
       try 
       {
         ModerationCreateParams params = ModerationCreateParams.builder()
                 .model("omni-moderation-latest")
                 .input(message)
                 .build();
     
         var response = client.moderations().create(params);
     
         return response.results().get(0).flagged();
       }
       catch(Exception e)
       {
         throw new RuntimeException("Something went wrong, please try again later!");
       }
    }
}

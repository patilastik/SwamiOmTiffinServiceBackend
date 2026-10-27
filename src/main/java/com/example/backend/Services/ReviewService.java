// package com.example.demo.service;
package com.example.backend.services;

import com.example.backend.dto.ReviewDTO;
import com.example.backend.entities.Reviewer;
import org.springframework.stereotype.Service;
import com.example.backend.exceptions.InvalidValueException;
import com.example.backend.exceptions.ActionNotAllowed;
import com.example.backend.exceptions.NoReviewsFoundException;

import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.openai.models.moderations.ModerationCreateParams;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;


import com.example.backend.repositories.ReviewerRepository;

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
        if(reviewDTO.getStars()<0||reviewDTO.getStars()>5)
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
        // Set which contains bad words, which I am going to check for first name, last name and message.
        Set<String> badWords = new HashSet<>(Set.of("fuck", "bitch", "bastard", "asshole", "cunt", "hell", "damn", "shit", 
        "motherfucker", "sonofabitch", "motherfucking", "sonofabitching", "sex", "porn", "pornography", "pornstar", "dick", 
        "pussy", "vagina", "penis", "tit","boob", "breast", "nipple", "anus", "arse", "arsey", "arsehole"));
        String firstName = reviewDTO.getFirstName();
        String firstNameLower = firstName.toLowerCase();
        for(String badWord : badWords) // first layer for checking bad word
         {
            if(firstNameLower.startsWith(badWord)) // If first name starts with any bad word 
             {
                throw new InvalidValueException("First Name contains bad word");
             }
         }
        if(containsHarmfulContent(firstName)) // second layer for checking bad word
        {
            throw new InvalidValueException("First Name contains bad word");
        }
        String lastName = reviewDTO.getLastName();
        String lastNameLower = lastName.toLowerCase();
        for(String badWord : badWords) // first layer for checking bad word
         {
            if(lastNameLower.startsWith(badWord)) // If last name starts with any bad word 
             {
                throw new InvalidValueException("Last Name contains bad word");
             }
         }
        if(containsHarmfulContent(lastName)) // second layer for checking bad word
        {
            throw new InvalidValueException("Last Name contains bad word");
        }
        String message = reviewDTO.getMessage();
        String[] messageWords = message.toLowerCase().split("\\W+"); //Splitting the message into 
        for(String word:messageWords) // iterating the review message and checking whether it contains any bad word or not
        {
            if(badWords.contains(word)) 
            {
                throw new InvalidValueException("Message contains bad word");
            }
        }
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
        reviewerRepository.save(reviewer);
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
         System.out.println("API Response: " + response.toString());
         return response.results().get(0).flagged();
       }
       catch(Exception e)
       {
         throw new RuntimeException("Something went wrong, please try again later!");
       }
    }

    // Returns the first 5 reviews
    public  List<ReturnReviewsDTO> returnReviews()
     {
        List<Reviewer> reviewers = reviewerRepository.findTop5ByOrderByCreatedAtDesc();
        if(reviewers.isEmpty())
        {
            throw new NoReviewsFoundException("No reviews found");
        }
        List<ReturnReviewsDTO> returnReviewsDTOs = new ArrayList<>();
        for(Reviewer reviewer : reviewers)
         {
            ReturnReviewsDTO returnReviewsDTO = new ReturnReviewsDTO();
            returnReviewsDTO.setEmail(reviewer.getEmail());
            returnReviewsDTO.setName(reviewer.getFirstName()+" "+reviewer.getLastName());
            returnReviewsDTO.setMessage(reviewer.getMessage());
            returnReviewsDTO.setStars(reviewer.getStars());
            returnReviewsDTO.setCreatedAt(reviewer.getCreatedAt());
            returnReviewsDTOs.add(returnReviewsDTO);
         }
        return returnReviewsDTOs;
     }

}

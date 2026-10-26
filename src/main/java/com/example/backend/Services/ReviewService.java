package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Reviewer;
import org.springframework.stereotype.Service;
import com.example.demo.exceptions.InvalidValueException;
import com.example.demo.exceptions.ActionNotAllowed;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ReviewService {

    private final ReviewerRepository reviewerRepository;

    // Spring Boot automatically injects the objects of these repositories classes
    public ReviewService(ReviewerRepository reviewerRepository) {
        this.reviewerRepository = reviewerRepository;
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
        // setting up some bad words which should not be in first name, last name and in the review message
        Set<String> badWords = new HashSet<>(Set.of("fuck", "bitch", "bastard", "asshole", "cunt", "hell", "damn", "shit", 
        "motherfucker", "sonofabitch", "motherfucking", "sonofabitching", "sex", "porn", "pornography", "pornstar", "dick", 
        "pussy", "vagina", "penis", "tit","boob", "breast", "nipple", "anus", "arse", "arsey", "arsehole"));
        String firstName = reviewDTO.getFirstName();
        for(String badWord : badWords)
         {
            if(firstName.toLowerCase().startsWith(badWord)) // If first name starts with any bad word 
             {
                throw new InvalidValueException("First Name contains bad word");
             }
         }
        String lastName = reviewDTO.getLastName();
        for(String badWord : badWords)
         {
            if(lastName.toLowerCase().startsWith(badWord)) // If last name starts with any bad word 
             {
                throw new InvalidValueException("Last Name contains bad word");
             }
         }
        String message = reviewDTO.getMessage();
        String[] messageWords = message.toLowerCase().split("\\W+"); //Splitting the message into words
        for(String word:messageWords) // iterating the review message and checking whether it contains any bad word or not
        {
            if(badWords.contains(word))
            {
                throw new InvalidValueException("Message contains bad word");
            }
        }
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


package com.example.backend.repositories;

import com.example.backend.entities.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
    Optional<Reviewer> findByEmail(String email); // returns the record with provided email if presnet otherwise returns null
    List<Reviewer> findTop5ByOrderByCreatedAtDesc(); // returns the first 5 reviews
}
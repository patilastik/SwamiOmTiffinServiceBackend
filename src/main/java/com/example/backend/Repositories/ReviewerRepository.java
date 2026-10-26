
package com.example.demo.repository;

import com.example.demo.entity.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
    Optional<Reviewer> findByEmail(String email); // returns the record with provided email if presnet otherwise returns null
}
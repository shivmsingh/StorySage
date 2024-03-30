package com.major.recommendation.Repository;

import com.major.recommendation.Modal.Recommendation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUsernameAndBookId(String username, Long book_id, Pageable page);
    void deleteByUsernameAndBookId(String username, Long book_id);
}

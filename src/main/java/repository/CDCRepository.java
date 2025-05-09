package repository;

import Models.CDC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CDCRepository extends JpaRepository<CDC, UUID> {
    List<CDC> findByTitleContainingIgnoreCase(String title);
//    List<CDC> findByType(String type);
//    boolean existsByTitle(String title);
}

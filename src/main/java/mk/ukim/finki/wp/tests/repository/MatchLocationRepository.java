package mk.ukim.finki.wp.tests.repository;

import mk.ukim.finki.wp.tests.model.MatchLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchLocationRepository extends JpaRepository<MatchLocation, Long> {
}

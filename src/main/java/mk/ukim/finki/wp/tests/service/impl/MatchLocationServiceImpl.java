package mk.ukim.finki.wp.tests.service.impl;

import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.tests.repository.MatchLocationRepository;
import mk.ukim.finki.wp.tests.service.MatchLocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchLocationServiceImpl implements MatchLocationService {
    private final MatchLocationRepository matchLocationRepository;

    public MatchLocationServiceImpl(MatchLocationRepository matchLocationRepository) {
        this.matchLocationRepository = matchLocationRepository;
    }

    @Override
    public MatchLocation findById(Long id) {
        return matchLocationRepository.findById(id).orElseThrow(() -> new InvalidMatchLocationIdException("Invalid locationId"));
    }

    @Override
    public List<MatchLocation> listAll() {
        return matchLocationRepository.findAll();
    }

    @Override
    public MatchLocation create(String name) {
        MatchLocation location = new MatchLocation(name);
        return matchLocationRepository.save(location);
    }
}

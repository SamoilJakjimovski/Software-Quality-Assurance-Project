package mk.ukim.finki.wp.sep2022.service.impl;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.sep2022.repository.MatchLocationRepository;
import mk.ukim.finki.wp.sep2022.repository.MatchRepository;
import mk.ukim.finki.wp.sep2022.service.MatchService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchLocationRepository matchLocationRepository;
    private final PasswordEncoder passwordEncoder;

    public MatchServiceImpl(MatchRepository matchRepository, MatchLocationRepository matchLocationRepository, PasswordEncoder passwordEncoder) {
        this.matchRepository = matchRepository;
        this.matchLocationRepository = matchLocationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Match> listAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
    }

    @Override
    public Match create(String name, String description, Double price, MatchType type, Long location) {
        if (name == null || description == null || price == null || type == null || location == null || price < 0 ||
                name.isEmpty() || description.isEmpty() || price.isInfinite()) {
            throw new IllegalArgumentException("Invalid argument");
        }
        MatchLocation loc = matchLocationRepository.findById(location).orElseThrow(() -> new InvalidMatchLocationIdException("Invalid locationId"));
        Match match = new Match(name, description, price, type, loc);
        return matchRepository.save(match);
    }

    @Override
    public Match update(Long id, String name, String description, Double price, MatchType type, Long location) {
        Match match = matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        MatchLocation loc = matchLocationRepository.findById(location).orElseThrow(() -> new InvalidMatchLocationIdException("Invalid locationId"));

        match.setName(name);
        match.setDescription(description);
        match.setPrice(price);
        match.setType(type);
        match.setLocation(loc);

        return matchRepository.save(match);
    }

    @Override
    public Match delete(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        matchRepository.delete(match);
        return match;
    }

    @Override
    public Match follow(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        if (canFollow(match)) {
            match.setFollows(match.getFollows() + 1);
        }
        return matchRepository.save(match);
    }

    @Override
    public boolean canFollow(Match match) {
        return (match.getFollows() >= 0 && match.getFollows() <= 25) || !match.getType().equals(MatchType.UNASSIGNED);
    }

    @Override
    public List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type) {
        System.out.println(type);
        if (price != null && type != null) {
            return matchRepository.findAllByPriceLessThanEqualAndTypeEquals(price, type);
        } else if (price != null) {
            return matchRepository.findAllByPriceLessThanEqual(price);
        } else if (type != null) {
            List<Match> a = matchRepository.findAllByTypeEquals(type);
            return matchRepository.findAllByTypeEquals(type);
        }
        return matchRepository.findAll();
    }


}

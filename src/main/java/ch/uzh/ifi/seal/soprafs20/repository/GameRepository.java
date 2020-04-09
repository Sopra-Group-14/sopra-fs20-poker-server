package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameHostName(String name);

    Game findByGameHostUsername(String username);

    Game findByGameHostToken(String token);

    Game findById(long id);

    List<Game> findAll();
}
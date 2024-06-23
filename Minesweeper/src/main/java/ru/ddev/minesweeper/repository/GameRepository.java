package ru.ddev.minesweeper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ddev.minesweeper.domain.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
}

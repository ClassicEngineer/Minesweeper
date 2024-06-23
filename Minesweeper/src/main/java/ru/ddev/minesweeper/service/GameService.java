package ru.ddev.minesweeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ddev.minesweeper.domain.Field;
import ru.ddev.minesweeper.domain.Game;
import ru.ddev.minesweeper.exception.GameAlreadyCompletedException;
import ru.ddev.minesweeper.exception.GameNotFoundException;
import ru.ddev.minesweeper.exception.InvalidTurnException;
import ru.ddev.minesweeper.repository.GameRepository;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;
  private final FieldService fieldService;

  public Game newGame(int width, int height, int minesCount) {
    Field fullField = fieldService.createFullField(width, height, minesCount);
    Game game = Game.builder()
        .width(width)
        .height(height)
        .minesCount(minesCount)
        .field(fullField.toViewList())
        .mineField(fullField.toValueList())
        .completed(false)
        .build();
    return gameRepository.save(game);
  }

  public Game makeTurn(String gameId, int col, int row) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " was not found"));
    validate(game, col, row);
    boolean isCompleted = fieldService.makeTurn(game, col, row);
    game.setCompleted(isCompleted);
    gameRepository.save(game);
    return game;
  }

  private void validate(Game game, int col, int row) {
    if (game.isCompleted()) {
      throw new GameAlreadyCompletedException("Game with id " + game.getGameId() + " already completed");
    }
    if (col < 0 || row < 0 || col >= game.getHeight() || row >= game.getWidth()) {
      throw new InvalidTurnException("Turn with col = " + col + " and row = " + row + " is forbidden");
    }
  }
}

package ru.ddev.minesweeper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.ddev.minesweeper.controller.dto.ErrorResponse;
import ru.ddev.minesweeper.controller.dto.NewGameRequest;
import ru.ddev.minesweeper.controller.dto.TurnRequest;
import ru.ddev.minesweeper.domain.Game;
import ru.ddev.minesweeper.exception.GameException;

import ru.ddev.minesweeper.service.GameService;

@Validated
@RestController
@RequiredArgsConstructor
public class GameController {

  private final GameService gameService;

  @PostMapping("/new")
  public ResponseEntity<Game> newGame(@Valid @RequestBody NewGameRequest createDto) {
    Game game = gameService.newGame(createDto.width(), createDto.height(), createDto.minesCount());
    return ResponseEntity.ok(game);
  }

  @PostMapping("/turn")
  public ResponseEntity<Game> makeTurn(@Valid @RequestBody TurnRequest turnDto) {
    Game game = gameService.makeTurn(turnDto.gameId(), turnDto.col(), turnDto.row());
    return ResponseEntity.ok(game);
  }

  @ExceptionHandler(GameException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleGameException(GameException e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
  }
}

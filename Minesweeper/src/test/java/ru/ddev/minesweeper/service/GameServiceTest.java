package ru.ddev.minesweeper.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ddev.minesweeper.domain.Game;
import ru.ddev.minesweeper.repository.GameRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

  @Mock
  private GameRepository gameRepository;

  @Mock
  private FieldService fieldService;

  @InjectMocks
  private GameService gameService;

  @Test
  public void shouldCreateNewGame_WhenParamsAreCorrect() {
    int width = 10;
    int height = 10;
    int minesCount = 3;
    Mockito.when(fieldService.createFullField(width, height, minesCount)).thenCallRealMethod();
    Mockito.when(gameRepository.save(Mockito.any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

    Game game = gameService.newGame(width, height, minesCount);

    Assertions.assertEquals(width, game.getWidth());
    Assertions.assertEquals(height, game.getHeight());
    Assertions.assertEquals(minesCount, game.getMinesCount());
    Assertions.assertFalse(game.isCompleted());
    Assertions.assertNotNull(game.getField());
    Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any(Game.class));

  }

  @Test
  public void shouldMakeLastWinTurn_WhenParamsAreCorrectAndGameEasyUnfinished() {
    String gameId = "1";
    Game game = createUnfinishedGame();
    Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
    Mockito.when(fieldService.makeTurn(game, 0, 0)).thenCallRealMethod();

    Game actual = gameService.makeTurn(gameId, 0, 0);

    Assertions.assertTrue(actual.isCompleted());
    Assertions.assertEquals(gameId, actual.getGameId());
    Assertions.assertEquals("M", actual.getField().get(1).get(1));
    Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any(Game.class));
  }

  @Test
  public void shouldMakeLastFailTurn_WhenParamsAreCorrectAndGameEasyUnfinished() {
    String gameId = "1";
    Game game = createUnfinishedGame();
    Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
    Mockito.when(fieldService.makeTurn(game, 1, 1)).thenCallRealMethod();

    Game actual = gameService.makeTurn(gameId, 1, 1);

    Assertions.assertTrue(actual.isCompleted());
    Assertions.assertEquals(gameId, actual.getGameId());
    Assertions.assertEquals("X", actual.getField().get(1).get(1));
    Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any(Game.class));
  }

  private Game createUnfinishedGame() {
    return Game.builder()
        .gameId("1")
        .completed(false)
        .width(2)
        .height(2)
        .minesCount(1)
        .field(createEasyField())
        .mineField(createEasyMineField())
        .build();
  }

  private List<List<Integer>> createEasyMineField() {
    List<Integer> firstRow = List.of(1, 1);
    List<Integer> secondRow = List.of(1, -1);
    return List.of(firstRow, secondRow);
  }

  private List<List<String>> createEasyField() {
    List<String> firstRow = List.of(" ", " ");
    List<String> secondRow = List.of(" ", " ");
    return List.of(firstRow, secondRow);
  }

}
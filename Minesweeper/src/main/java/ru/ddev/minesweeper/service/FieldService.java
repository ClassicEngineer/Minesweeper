package ru.ddev.minesweeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ddev.minesweeper.domain.Field;
import ru.ddev.minesweeper.domain.Game;

@Service
@RequiredArgsConstructor
public class FieldService {

  public Field createFullField(int width, int height, int minesCount) {
    Field field = new Field(width, height, minesCount);
    field.fill();
    return field;
  }

  /**
   * @return is game completed
   */
  public boolean makeTurn(Game game, int col, int row) {
    Field field = new Field(game.getField(), game.getMineField(), game.getMinesCount());
    if (field.isMineTurn(row, col)) {
      field.showAll(false);
      game.setField(field.toViewList());
      return true;
    } else if (field.isZeroTurn(row, col)) {
      field.open(row, col);
    } else {
      field.openNumberTurn(row, col);
    }
    if (!field.allMinesDefused()) {
      field.showAll(true);
      game.setField(field.toViewList());
      return true;
    }
    game.setField(field.toViewList());
    return false;
  }
}

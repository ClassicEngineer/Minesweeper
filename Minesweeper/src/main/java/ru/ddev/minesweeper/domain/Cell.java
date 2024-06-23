package ru.ddev.minesweeper.domain;

import lombok.Data;
import ru.ddev.minesweeper.constant.Constants;

@Data
public class Cell {
  // mineCount, -1 for mine
  private Integer value;
  private String view;

  public boolean isMine() {
    return value.equals(Constants.MINE_VALUE);
  }

  public void showNumber() {
    setView(String.valueOf(value));
  }

  public boolean isEmpty() {
    return view.equals(Constants.EMPTY);
  }

  public boolean isShowed() {
    return !isEmpty();
  }

  public boolean zeroMines() {
    return getValue() == 0;
  }
}

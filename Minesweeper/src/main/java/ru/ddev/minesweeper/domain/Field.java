package ru.ddev.minesweeper.domain;

import ru.ddev.minesweeper.constant.Constants;
import ru.ddev.minesweeper.exception.InvalidTurnException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Field {

  private final Random random = new Random();

  private final Cell[][] field;

  private final int width;

  private final int height;

  private final int minesCount;

  public Field(int width, int height, int minesCount) {
    this.width = width;
    this.height = height;
    this.minesCount = minesCount;
    this.field = new Cell[height][width];
  }

  public Field(List<List<String>> viewList, List<List<Integer>> valueList, int minesCount) {
    this.height = viewList.size();
    this.width = viewList.getFirst().size();
    this.minesCount = minesCount;
    this.field = new Cell[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        field[i][j] = new Cell();
        field[i][j].setView(viewList.get(i).get(j));
        field[i][j].setValue(valueList.get(i).get(j));
      }
    }
  }

  public List<List<String>> toViewList() {
    List<List<String>> result = new ArrayList<>();
    for (int i = 0; i < this.height; i++) {
      List<String> row = new ArrayList<>();
      for (int j = 0; j < this.width; j++) {
        row.add(field[i][j].getView());
      }
      result.add(row);
    }
    return result;
  }

  public List<List<Integer>> toValueList() {
    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < this.height; i++) {
      List<Integer> row = new ArrayList<>();
      for (int j = 0; j < this.width; j++) {
        row.add(field[i][j].getValue());
      }
      result.add(row);
    }
    return result;
  }

  public void fill() {
    fillEmpty();
    fillMines(minesCount);
    fillNumbers();
  }

  public boolean isMineTurn(int i, int j) {
    validateTurn(i, j);
    return field[i][j].isMine();
  }

  public boolean isZeroTurn(int i, int j) {
    validateTurn(i, j);
    return field[i][j].zeroMines();
  }

  public boolean allMinesDefused() {
    int emptyFields = 0;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (field[i][j].isEmpty()) {
          emptyFields++;
        }
      }
    }
    return emptyFields == minesCount;
  }

  public void showAll(boolean isWin) {
    traverseFull(cell -> {
      if (cell.isMine()) {
        cell.setView(isWin ? Constants.DEFUSED : Constants.MINE);
      } else {
        cell.showNumber();
      }
    });
  }

  public void openNumberTurn(int i, int j) {
    validateTurn(i, j);
    field[i][j].showNumber();
  }

  public void open(int i, int j) {
    Optional<Cell> cellOptional = getSafe(i, j);
    if (cellOptional.isEmpty() || cellOptional.get().isMine()) {
      return;
    }
    cellOptional.ifPresent(Cell::showNumber);
    traverseNear(i, j, (cell, coordinates) -> {
      if (cell.isMine()) {
        return;
      }
      if (cell.isEmpty() && !cell.isShowed()) {
        cell.showNumber();
        open(coordinates.i(), coordinates.j());
      } else if (!cell.isShowed()) {
        cell.showNumber();
      }
    });
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        sb.append(field[i][j].getView()).append("(")
            .append(field[i][j].getValue()).append(")");
        sb.append(" ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  private void validateTurn(int i, int j) {
    if (getSafe(i, j).map(Cell::isShowed).orElse(false)) {
      throw new InvalidTurnException("Cell already showed");
    }
  }

  private void fillNumbers() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (!field[i][j].isMine()) {
          field[i][j].setValue(countMines(i, j));
        }
      }
    }
  }

  private int countMines(int i, int j) {
    final AtomicInteger mines = new AtomicInteger(0); // atomic for final usage in consumer
    traverseNear(i, j, (cell, coords) ->  {
      if (cell.isMine()) {
        mines.incrementAndGet();
      }
    });
    return mines.get();
  }

  private Optional<Cell> getSafe(int i, int j) {
    if (i < 0 || i >= height || j < 0 || j >= width) {
      return Optional.empty();
    }
    return Optional.of(field[i][j]);
  }

  private void fillMines(int minesCount) {
    while (minesCount > 0) {
      int randomWidth = random.nextInt(width);
      int randomHeight = random.nextInt(height);
      if (!field[randomHeight][randomWidth].isMine()) {
        field[randomHeight][randomWidth].setValue(Constants.MINE_VALUE);
        minesCount--;
      }
    }
  }

  private void fillEmpty() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        field[i][j] = new Cell();
        field[i][j].setView(Constants.EMPTY);
        field[i][j].setValue(0);
      }
    }
  }

  /**
   * (i - 1, j - 1) (i - 1, j) (i -1, j + 1)
   * (i, j - 1)   (i, j)     (i, j + 1)
   * (i + 1, j - 1) (i + 1, j) (i + 1, j+ 1)
   */
  private void traverseNear(int i, int j, BiConsumer<Cell, Coordinates> cellConsumer) {
    getSafe(i - 1, j - 1).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates( i - 1, j - 1)));
    getSafe(i - 1, j).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i - 1, j)));
    getSafe(i - 1, j + 1).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i - 1, j + 1)));
    getSafe(i, j - 1).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i, j - 1)));
    getSafe(i, j + 1).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i, j + 1)));
    getSafe(i + 1, j - 1).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i + 1, j - 1)));
    getSafe(i + 1, j).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i + 1, j)));
    getSafe(i + 1, j + 1).ifPresent(cell -> cellConsumer.accept(cell, new Coordinates(i + 1, j + 1)));
  }

  private void traverseFull(Consumer<Cell> cellConsumer) {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        cellConsumer.accept(field[i][j]);
      }
    }
  }
}

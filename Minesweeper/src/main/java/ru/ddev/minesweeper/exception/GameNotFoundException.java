package ru.ddev.minesweeper.exception;

public class GameNotFoundException extends GameException {
  public GameNotFoundException(String message) {
    super(message);
  }
}

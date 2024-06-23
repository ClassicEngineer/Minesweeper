package ru.ddev.minesweeper.exception;

public class GameAlreadyCompletedException extends GameException {
  public GameAlreadyCompletedException(String message) {
    super(message);
  }
}

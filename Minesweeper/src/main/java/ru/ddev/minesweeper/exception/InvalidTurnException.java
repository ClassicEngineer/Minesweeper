package ru.ddev.minesweeper.exception;

public class InvalidTurnException extends GameException {
  public InvalidTurnException(String message) {
    super(message);
  }
}

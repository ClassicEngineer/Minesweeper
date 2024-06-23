package ru.ddev.minesweeper.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ddev.minesweeper.controller.dto.NewGameRequest;

public class NewGameValidator implements ConstraintValidator<CorrectGame, NewGameRequest> {
  @Override
  public boolean isValid(NewGameRequest request, ConstraintValidatorContext context) {
    return request.height() * request.width() - 1 >= request.minesCount();
  }
}

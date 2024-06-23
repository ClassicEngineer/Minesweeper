package ru.ddev.minesweeper.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import ru.ddev.minesweeper.controller.validation.CorrectGame;

@CorrectGame
public record NewGameRequest(@JsonProperty(required = true) @Min(1) @Max(30) int width,
                             @JsonProperty(required = true) @Min(1) @Max(30) int height,
                             @JsonProperty(value = "mines_count", required = true) @Min(1) int minesCount) {

}

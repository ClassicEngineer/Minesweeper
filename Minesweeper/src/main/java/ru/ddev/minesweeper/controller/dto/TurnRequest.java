package ru.ddev.minesweeper.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;



public record TurnRequest(@JsonProperty(value = "game_id", required = true) String gameId,
                          @JsonProperty(required = true) @Min(0) int col,
                          @JsonProperty(required = true) @Min(0) int row) {

}

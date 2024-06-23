package ru.ddev.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.util.List;

@Data
@Builder
@Document(collection = "games")
public class Game {

  @MongoId
  @JsonProperty("game_id")
  private String gameId;

  private int width;

  private int height;

  @JsonProperty("mines_count")
  private int minesCount;

  private boolean completed;

  private List<List<String>> field;

  @JsonIgnore
  private List<List<Integer>> mineField;

}

package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Запрос на выдачу книги читателю")

public class IssueRequest {
  @Schema(name = "Идентификатор читателя")
  private Long readerId;

  @Schema(name = "Идентификатор книги")
  private Long bookId;
}

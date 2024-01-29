package ru.gb.springdemo.api;

import lombok.Data;

@Data
public class IssueRequest {
  private Long readerId;
  private Long bookId;
}

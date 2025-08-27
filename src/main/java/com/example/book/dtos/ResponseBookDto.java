package com.example.book.dtos;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseBookDto(Long id, String title, String author, String publisher, Optional<ResponseUserDto> user) {
    
}

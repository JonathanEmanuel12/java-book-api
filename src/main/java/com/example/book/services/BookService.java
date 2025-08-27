package com.example.book.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.book.dtos.CreateBookDto;
import com.example.book.dtos.ResponseBookDto;
import com.example.book.dtos.ResponseUserDto;
import com.example.book.models.Book;
import com.example.book.models.User;
import com.example.book.repositories.BookRepository;
import com.example.book.repositories.UserRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> findAllBySearch(String search, Pageable pageable) {
        return bookRepository.findBySearch(search, pageable);
    }

    public Optional<ResponseBookDto> findById(Long id) {
        return bookRepository.findById(id).map(book -> toDto(book, true));
    }

    public Book save(CreateBookDto bookDto) {
        User user = userRepository.findById(bookDto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                
        Book book = new Book();
        book.setTitle(bookDto.title());
        book.setAuthor(bookDto.author());
        book.setPublisher(bookDto.publisher());
        book.setUser(user);
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private ResponseBookDto toDto(Book book, boolean includeUser) {
        var user = book.getUser();
        Optional<ResponseUserDto> userDto = includeUser 
            ? Optional.of(new ResponseUserDto(user.getId(), user.getEmail(), user.getName()))
            : Optional.empty();

        return new ResponseBookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPublisher(), userDto);
    }
}

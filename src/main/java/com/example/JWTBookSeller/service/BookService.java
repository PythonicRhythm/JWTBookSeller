package com.example.JWTBookSeller.service;

import com.example.JWTBookSeller.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookByID(long isbn);
    Book addNewBook(Book newBook);
    Book updateBook(Book book);
    String deleteBookByID(long isbn);
}

package com.example.JWTBookSeller.service;

import com.example.JWTBookSeller.entity.Book;
import com.example.JWTBookSeller.repository.BookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    public BookDAO bookDAO;

    @Override
    public List<Book> getAllBooks() {
        return this.bookDAO.findAll();
    }

    @Override
    public Book getBookByID(long isbn) {
        Optional<Book> t = this.bookDAO.findById(isbn);
        Book task = null;
        if(t.isPresent())
            task=t.get();
        else
            throw new RuntimeException("Book Not Found: "+isbn);

        return task;
    }

    @Override
    public Book addNewBook(Book newBook) {
        return this.bookDAO.save(newBook);
    }

    @Override
    public Book updateBook(Book book) {
        return this.bookDAO.save(book);
    }

    @Override
    public String deleteBookByID(long isbn) {
        this.bookDAO.deleteById(isbn);
        return "Book deleted successfully!";
    }
}

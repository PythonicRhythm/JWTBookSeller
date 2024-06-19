package com.example.JWTBookSeller.repository;

import com.example.JWTBookSeller.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDAO extends JpaRepository<Book, Long> {
}

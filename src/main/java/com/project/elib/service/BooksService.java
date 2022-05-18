package com.project.elib.service;

import com.project.elib.models.Books;
import com.project.elib.repo.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BooksService {

    private BooksRepository booksRepo;

    @Autowired
    public BooksService(BooksRepository booksRepo){
        this.booksRepo = booksRepo;
    }

    public Iterable<Books> getAllBooks(){
        return booksRepo.findAll();
    }

    public List<Books> getAllBooksByGenre(int genre) {
        return booksRepo.findAllByGenre(genre);
    }

    public Books getBooksById(int id){
        return booksRepo.findById(id);
    }

    public void saveBooks(Books books){
        booksRepo.save(books);
    }

    public void deleteBooksById(int id){
        booksRepo.deleteById(id);
    }
}

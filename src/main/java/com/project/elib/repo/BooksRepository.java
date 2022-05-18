package com.project.elib.repo;

import com.project.elib.models.Books;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BooksRepository extends CrudRepository<Books, Long> {
    List<Books> findAllByGenre(int genre);

    Books findById(int id);

    List<Books> findByBookName(String name);

    Long deleteById(int id);
}

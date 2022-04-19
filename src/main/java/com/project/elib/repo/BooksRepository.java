package com.project.elib.repo;

import com.project.elib.models.Books;
import org.springframework.data.repository.CrudRepository;

public interface BooksRepository extends CrudRepository<Books, Long> {
}

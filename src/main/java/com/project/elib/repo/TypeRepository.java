package com.project.elib.repo;

import com.project.elib.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TypeRepository extends JpaRepository<Type, Integer> {

    Type findById(int id);
    Long deleteById(int id);
}

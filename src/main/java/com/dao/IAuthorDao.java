package com.dao;


import com.model.Author;

import java.util.List;

public interface IAuthorDao {
    List<Author> findAllEmail();
}

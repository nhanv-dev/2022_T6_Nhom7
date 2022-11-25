package com.dao.implement;


import com.dao.IAuthorDao;
import com.mapper.AuthorMapper;
import com.model.Author;
import com.model.Configuration;

import java.util.List;

public class AuthorDao extends AbstractDao<Author> implements IAuthorDao {
    @Override
    public List<Author> findAllEmail() {
        return query(Configuration.getProperty("database.find_author"), Configuration.getProperty("database.controller"), new AuthorMapper());
    }
}

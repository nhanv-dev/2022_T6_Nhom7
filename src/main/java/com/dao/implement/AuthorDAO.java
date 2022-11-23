package com.dao.implement;


import com.dao.IAuthorDAO;
import com.mapper.AuthorMapper;
import com.model.AuthorModel;

import java.util.List;

public class AuthorDAO extends AbstractDao<AuthorModel> implements IAuthorDAO {


    @Override
    public List<AuthorModel> listAuthor() {
        String sql = "SELECT email from author";
        return query(sql, "author",new AuthorMapper());
    }
}

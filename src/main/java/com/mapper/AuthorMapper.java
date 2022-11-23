package com.mapper;

import com.model.AuthorModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements IRowMapper<AuthorModel> {
    @Override
    public AuthorModel mapRow(ResultSet rs){
        try {
            AuthorModel author = new AuthorModel();
            author.setName(rs.getString("name"));
            author.setPhone(rs.getString("phone"));
            author.setEmail(rs.getString("email"));
            return author;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

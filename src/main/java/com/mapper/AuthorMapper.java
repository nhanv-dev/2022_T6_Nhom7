package com.mapper;

import com.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements IRowMapper<Author> {
    @Override
    public Author mapRow(ResultSet rs) {
        try {
            Author author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setName(rs.getString("author_name"));
            author.setPhone(rs.getString("phone"));
            author.setEmail(rs.getString("email"));
            return author;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

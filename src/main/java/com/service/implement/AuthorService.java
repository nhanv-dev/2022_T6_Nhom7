package com.service.implement;

import com.dao.IAuthorDao;
import com.dao.implement.AuthorDao;
import com.model.Author;
import com.service.IAuthorService;

import java.util.ArrayList;
import java.util.List;

public class AuthorService implements IAuthorService {
    private final IAuthorDao authorDao = new AuthorDao();

    public List<String> listEmailAuthor() {
        List<Author> authors = authorDao.findAllEmail();
        List<String> emails = new ArrayList<>();
        for (Author author : authors) {
            if (author != null) {
                emails.add(author.getEmail());
                System.out.println("email" + author.getEmail());
            }

        }
        return emails;
    }
}

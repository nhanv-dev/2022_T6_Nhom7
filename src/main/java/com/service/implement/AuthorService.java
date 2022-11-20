package com.service.implement;

import com.dao.IAuthorDAO;
import com.dao.implement.AuthorDAO;
import com.model.AuthorModel;
import com.service.IAuthorService;

import java.util.ArrayList;
import java.util.List;

public class AuthorService implements IAuthorService {
    public List<String> listEmailAuthor() {
        IAuthorDAO authorDAO = new AuthorDAO() ;
        List<AuthorModel> listAuthor = authorDAO.listAuthor();
        List<String> l = new ArrayList<>();
        for (AuthorModel a: listAuthor
        ) {
            l.add(a.getEmail());
        }
        return l;
    }
}

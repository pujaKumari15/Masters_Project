package com.master.project.service;

import com.master.project.dao.UserDao;
import com.master.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User loginUser(User user) {

        Optional<User> authenticatedUser = userDao.findByUserIDAndPassword(user.getUserID(), user.getPassword());

        return authenticatedUser.orElse(null);
    }
}


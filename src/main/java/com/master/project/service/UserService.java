package com.master.project.service;

import com.master.project.dao.UserDao;
import com.master.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User loginUser(User user) {

        Optional<User> authenticatedUser = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());

        return authenticatedUser.orElse(null);
    }

    public User addUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return userDao.save(user);
    }

    public User getUserById(String id) {
        Optional<User> userOpt = userDao.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(null);
            user.setCreatedDate(null);
            user.setUpdatedDate(null);
            return user;
        }
        return null;
    }

    public User updateUser(String id, User userDetails) {
        Optional<User> existingUserOpt = userDao.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if(userDetails.getFirstName() != null)
                existingUser.setFirstName(userDetails.getFirstName());
            if(userDetails.getLastName() != null)
                existingUser.setLastName(userDetails.getLastName());
            if(userDetails.getEmail() != null)
                existingUser.setEmail(userDetails.getEmail());
            if(userDetails.getPassword() != null)
                existingUser.setPassword(userDetails.getPassword());
            if(userDetails.getUserType() != null)
                existingUser.setUserType(userDetails.getUserType());
            existingUser.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

            return userDao.save(existingUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(String id) {
        Optional<User> userOpt = userDao.findById(id);

        if (userOpt.isPresent()) {
            userDao.delete(userOpt.get());
            return true;
        } else {
            return false;
        }
    }
}


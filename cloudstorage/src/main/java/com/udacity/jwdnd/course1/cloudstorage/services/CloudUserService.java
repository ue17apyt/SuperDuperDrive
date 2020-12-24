package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CloudUserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Service
public class CloudUserService {

    @Autowired
    private CloudUserMapper userMapper;

    @Autowired
    private HashService hashService;

    public boolean isUsernameAvailable(String username) {
        return this.userMapper.findUserByUsername(username) == null;
    }

    public CloudUser findUserByUsername(String username) {
        return this.userMapper.findUserByUsername(username);
    }

    public int createUser(CloudUser user) {
        Random random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = this.hashService.getHashedValue(user.getPassword(), encodedSalt);
        return this.userMapper.insertUser(
                new CloudUser(
                        null,
                        user.getUsername(),
                        encodedSalt,
                        hashedPassword,
                        user.getFirstName(),
                        user.getLastName()
                )
        );
    }

}
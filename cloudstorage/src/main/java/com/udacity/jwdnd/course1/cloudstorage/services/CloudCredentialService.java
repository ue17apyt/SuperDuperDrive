package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CloudCredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudCredential;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class CloudCredentialService {

    @Autowired
    private CloudCredentialMapper credentialMapper;

    @Autowired
    private CloudUserService userService;

    @Autowired
    private EncryptionService encryptionService;

    public List<CloudCredential> findAllCredentialsByUserId(Integer userId) {
        return this.credentialMapper.findAllCredentialsByUserId(userId);
    }

    public String getDecryptedPassword(Integer credentialId, Authentication authentication) {
        CloudCredential credential = this.credentialMapper.findCredentialById(credentialId);
        return this.encryptionService.decryptValue(credential.getPassword(), credential.getUserKey());
    }

    public int upgradeCredential(CloudCredential credential, Authentication authentication) {

        CloudUser user = this.userService.findUserByUsername(authentication.getName());
        Random random = new SecureRandom();
        byte[] randomKey = new byte[16];
        random.nextBytes(randomKey);
        String encodedKey = Base64.getEncoder().encodeToString(randomKey);
        String encryptedPassword = this.encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setUserKey(encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setUserId(user.getUserId());

        if (credential.getCredentialId() == null) {
            return this.credentialMapper.insertCredential(credential);
        } else {
            return this.credentialMapper.updateCredential(credential);
        }

    }

    public int deleteCredentialById(Integer credentialId) {
        return this.credentialMapper.deleteCredentialById(credentialId);
    }

}
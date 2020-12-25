package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CloudFileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CloudFileService {

    @Autowired
    private CloudFileMapper fileMapper;

    public boolean isFilenameAvailable(Integer userId, String filename) {
        return this.fileMapper.findDuplicateFile(userId, filename) == null;
    }

    public CloudFile findFileById(Integer fileId) {
        return this.fileMapper.findFileById(fileId);
    }

    public List<CloudFile> findAllFilesByUserId(Integer userId) {
        return this.fileMapper.findAllFilesByUserId(userId);
    }

    public int insertFile(MultipartFile file, Integer userId) throws IOException {
        CloudFile cloudFile = new CloudFile();
        cloudFile.setFilename(file.getOriginalFilename());
        cloudFile.setContentType(file.getContentType());
        long filesize = file.getSize();
        if (filesize >= 1024 * 1024 * 1024) {
            cloudFile.setFilesize(String.format("%.3f", filesize / Math.pow(1024, 3)) + " GB");
        } else if (filesize >= 1024 * 1024) {
            cloudFile.setFilesize(String.format("%.3f", filesize / Math.pow(1024, 2)) + " MB");
        } else if (filesize >= 1024) {
            cloudFile.setFilesize(String.format("%.3f", filesize / 1024.0) + " KB");
        } else {
            cloudFile.setFilesize(filesize + " Bytes");
        }

        cloudFile.setUserId(userId);
        cloudFile.setFileData(file.getInputStream());
        return this.fileMapper.insertFile(cloudFile);
    }

    public int deleteFileById(Integer fileId) {
        return this.fileMapper.deleteFileById(fileId);
    }

}
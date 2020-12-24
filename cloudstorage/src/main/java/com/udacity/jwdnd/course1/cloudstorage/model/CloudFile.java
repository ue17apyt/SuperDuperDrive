package com.udacity.jwdnd.course1.cloudstorage.model;

import java.io.InputStream;

public class CloudFile {

    private Integer fileId;
    private String filename;
    private String contentType;
    private String filesize;
    private Integer userId;
    private InputStream fileData;

    public Integer getFileId() {
        return this.fileId;
    }

    public void setFileId(java.lang.Integer fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilesize() {
        return this.filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public InputStream getFileData() {
        return this.fileData;
    }

    public void setFileData(InputStream fileData) {
        this.fileData = fileData;
    }

}
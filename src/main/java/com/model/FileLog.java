package com.model;


import java.util.Date;

public class FileLog {

    private long id, configId, authorId;
    private String path, status;
    private Date createdDate;

    public FileLog() {
    }

    public FileLog(long configId, long authorId, String path, Date createdDate) {
        this.configId = configId;
        this.authorId = authorId;
        this.path = path;
        this.createdDate = createdDate;
    }

    public FileLog(long configId, long authorId, String path, Date createdDate, String status) {
        this.configId = configId;
        this.authorId = authorId;
        this.path = path;
        this.createdDate = createdDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "FileLog{" +
                "id=" + id +
                ", configId=" + configId +
                ", authorId=" + authorId +
                ", path='" + path + '\'' +
                ", status='" + status + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

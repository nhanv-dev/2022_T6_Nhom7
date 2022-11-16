package com.model;


import java.util.Date;

public class FileLog {
    public static final String EXTRACT_STATUS = "ER";
    public static final String TRANSFORM_STATUS = "TR";
    public static final String LOAD_STATUS = "LOAD";
    public static final String DONE_STATUS = "DONE";
    public static final String ERROR_STATUS = "ERR";
    private long id, configId, authorId;
    private String path, status;
    private Date createdDate;

    public FileLog(long configId, long authorId, String path, Date createdDate) {
        this.configId = configId;
        this.authorId = authorId;
        this.path = path;
        this.createdDate = createdDate;
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

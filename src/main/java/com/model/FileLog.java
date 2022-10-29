package com.model;

public class FileLog {
    public static final String EXTRACT_STATUS = "ER";
    public static final String TRANSFORM_STATUS = "TR";
    public static final String LOAD_STATUS = "LOAD";
    public static final String ERROR_STATUS = "ERR";
    private int id, configId, authorId;
    private String path, status;

    public FileLog(int id, int configId, int authorId, String path, String status) {
        this.id = id;
        this.configId = configId;
        this.authorId = authorId;
        this.path = path;
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
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
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
}

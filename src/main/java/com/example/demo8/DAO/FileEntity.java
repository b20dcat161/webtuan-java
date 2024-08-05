package com.example.demo8.DAO;
public class FileEntity {
    private int id;
    private int userId;
    private String fileName;
    private String filePath;
    private String type;

    // Constructors
    public FileEntity() {}

    public FileEntity(int id, int userId, String fileName, String filePath, String type) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.type = type;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

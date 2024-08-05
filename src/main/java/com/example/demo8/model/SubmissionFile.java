package com.example.demo8.model;

public class SubmissionFile {
    private int id;
    private int userId;
    private int homeworkId;
    private String fileName;
    private String filePath;

    // Constructor, Getters, and Setters

    public SubmissionFile(int id, int userId, int homeworkId, String fileName, String filePath) {
        this.id = id;
        this.userId = userId;
        this.homeworkId = homeworkId;
        this.fileName = fileName;
        this.filePath = filePath;
    }

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

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
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
// Getters and Setters
}

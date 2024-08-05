package com.example.demo8.DAO;


import java.util.List;

public interface FileDAO {
    void saveFile(FileEntity file);
    List<FileEntity> getFilesByUserId(int userId);
    FileEntity getFileById(int fileId);
    void deleteFile(int fileId);
}
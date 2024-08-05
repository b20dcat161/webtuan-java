package com.example.demo8.model;

public class Challenge {

    private int id;
    private String fileName;
    private String hint;

    public Challenge() {
    }

    public Challenge(int id, String fileName, String hint) {
        this.id = id;
        this.fileName = fileName;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", hint='" + hint + '\'' +

                '}';
    }
}
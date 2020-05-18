package com.example.dataoperator.dto;

public class StudentVo {

    private String type;
    private String name;
    private String score;

    public StudentVo() {
    }

    public StudentVo(String type, String name, String score) {
        this.type = type;
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

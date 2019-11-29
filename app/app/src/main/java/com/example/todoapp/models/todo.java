package com.example.todoapp.models;

import java.util.Date;

public class todo {
    private String id, name;
    private Integer status;
    private Date date;

    public todo(String id, String name, Integer status, Date date){
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

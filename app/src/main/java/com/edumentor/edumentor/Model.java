package com.edumentor.edumentor;

public class Model {
    private String task, description, id, date, in_date, in_time ;

    private Model(){

    }

    public Model(String task, String description, String id, String date, String in_date, String in_time) {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;
        this.in_date = in_date;
        this.in_time = in_time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }
}



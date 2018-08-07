package edu.coursemgr.model;

import java.io.Serializable;

public class QuestionType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer sortNo;

    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}
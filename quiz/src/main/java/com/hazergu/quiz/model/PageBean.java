package com.hazergu.quiz.model;

import java.util.List;

public class PageBean {
    private Integer total;
    private List<User> row;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<User> getRow() {
        return row;
    }

    public void setRow(List<User> row) {
        this.row = row;
    }

    public PageBean(Integer total, List<User> row) {
        this.total = total;
        this.row = row;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "total=" + total +
                ", row=" + row +
                '}';
    }
}

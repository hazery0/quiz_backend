package com.hazergu.quiz.model;

import java.util.List;

public class QSBeanPage {
    private int total;
    private List<QSBeanOutManage> qsBeanList;

    //添加无参构造函数，否则 service 中 new QSBeanPage() 会报错
    public QSBeanPage() {
    }

    public QSBeanPage(int total, List<QSBeanOutManage> qsBeanList) {
        this.total = total;
        this.qsBeanList = qsBeanList;
    }

    @Override
    public String toString() {
        return "QSBeanPage{" +
                "total=" + total +
                ", qsBeanList=" + qsBeanList +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<QSBeanOutManage> getQsBeanList() {
        return qsBeanList;
    }

    public void setQsBeanList(List<QSBeanOutManage> qsBeanList) {
        this.qsBeanList = qsBeanList;
    }
}
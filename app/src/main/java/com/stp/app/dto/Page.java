package com.stp.app.dto;

import java.io.Serializable;
import java.security.PublicKey;

public class Page {
    private int pageSize;
    private int pageIndex;

    public Page(){
        pageSize = 20;
        pageIndex = 1;
    }

    public Page(int pageSize, int pageIndex) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                '}';
    }
}

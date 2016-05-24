package com.licheng.app.rxandroidsample.data;

/**
 * Created by licheng on 24/5/16.
 */
public class Page {
    private int pageIndex;
    private int pageSize;

    public Page(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

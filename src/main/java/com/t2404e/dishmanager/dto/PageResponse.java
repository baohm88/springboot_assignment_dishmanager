package com.t2404e.dishmanager.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int limit;
    private int totalPages;
    private int totalElements;

    public PageResponse() {
    }
    public PageResponse(List<T> content, int page, int limit, int totalPages, int totalElements) {
        this.content = content;
        this.page = page;
        this.limit = limit;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getTotalElements() { return totalElements; }
    public void setTotalElements(int totalElements) { this.totalElements = totalElements; }
}

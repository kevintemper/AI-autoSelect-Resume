package com.example.zwtcampuscareerview.DTO;

import java.util.List;

public class ResumeBatchRequest {
    private List<String> resumes;
    private int topk;

    public List<String> getResumes() {
        return resumes;
    }

    public void setResumes(List<String> resumes) {
        this.resumes = resumes;
    }

    public int getTopk() {
        return topk;
    }

    public void setTopk(int topk) {
        this.topk = topk;
    }
}

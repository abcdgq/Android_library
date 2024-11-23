package com.xkx.book.openai;

import java.util.List;

public class CompletionResponse {
    private List<Answer> output; // 返回的对象数组

    // Getters 和 Setters
    public List<Answer> getOutput() {
        return output;
    }

    public void setOutput(List<Answer> output) {
        this.output = output;
    }
}
package com.xkx.book.openai;

public class CompletionRequest {
    private String inputs;
    private int max_tokens;

    public CompletionRequest(String prompt, int max_tokens) {
        this.inputs = prompt;
        this.max_tokens = max_tokens;
    }
}

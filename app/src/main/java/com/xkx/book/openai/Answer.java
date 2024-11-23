package com.xkx.book.openai;

import java.util.List;

public class Answer {
    private String answer; // 答案
    private double score; // 概率
    private int start; // 起始位置
    private int end; // 结束位置

    // Getters 和 Setters
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}


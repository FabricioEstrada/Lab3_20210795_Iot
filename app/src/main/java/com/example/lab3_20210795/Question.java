package com.example.lab3_20210795;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// Clase interna para representar una pregunta
public class Question implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("category")
    private String category;

    @SerializedName("question")
    private String question;

    @SerializedName("correct_answer")
    private boolean correctAnswer;  // Cambiado a boolean

    @SerializedName("incorrect_answers")
    private List<String> incorrectAnswers;

    // Getters y Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}

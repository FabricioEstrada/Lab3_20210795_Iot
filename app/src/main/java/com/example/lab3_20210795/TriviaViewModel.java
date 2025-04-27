package com.example.lab3_20210795;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class TriviaViewModel extends ViewModel {
    public List<Question> preguntas;
    public int currentQuestionIndex = 0;
    public int correctAnswersCount = 0;
    public int incorrectAnswersCount = 0;
    public int unansweredCount = 0;
    public long timeRemaining = 0;

    public boolean isGameFinished = false;
}

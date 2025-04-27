package com.example.lab3_20210795;

import androidx.lifecycle.ViewModel;
import java.util.List;

public class TriviaViewModel extends ViewModel {
    public List<Question> preguntas;
    public int currentQuestionIndex = 0;
    public int correctAnswersCount = 0;
    public int incorrectAnswersCount = 0;
    public int unansweredCount = 0;
    public long timeRemaining;

    // Método para configurar las preguntas y tiempo inicial
    public void initialize(List<Question> preguntas, int dificultad) {
        this.preguntas = preguntas;
        int totalPreguntas = preguntas.size();
        int tiempoPorPregunta = dificultad == 1 ? 5000 : (dificultad == 2 ? 7000 : 10000);
        timeRemaining = totalPreguntas * tiempoPorPregunta;
    }
}

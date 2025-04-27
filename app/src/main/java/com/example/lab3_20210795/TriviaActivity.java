package com.example.lab3_20210795;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

public class TriviaActivity extends AppCompatActivity {

    private List<Question> preguntas;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private int incorrectAnswersCount = 0;
    private int unansweredCount = 0;  // Contador de preguntas sin responder
    private long timeRemaining;
    private CountDownTimer countDownTimer;

    private TextView tvPregunta, tvContador, tvTiempoRestante;
    private RadioGroup radioGroupOpciones;
    private Button btnSiguiente, btnVolverAJugar;
    private RadioButton optionTrue, optionFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        // Inicializar vistas
        tvPregunta = findViewById(R.id.tvPregunta);
        tvContador = findViewById(R.id.tvContador);
        tvTiempoRestante = findViewById(R.id.tvTiempoRestante);
        radioGroupOpciones = findViewById(R.id.radioGroupOpciones);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnVolverAJugar = findViewById(R.id.btnVolverAJugar);
        optionTrue = findViewById(R.id.optionTrue);
        optionFalse = findViewById(R.id.optionFalse);

        // Obtener la lista de preguntas del Intent
        Intent intent = getIntent();
        preguntas = (List<Question>) intent.getSerializableExtra("preguntas");

        if (preguntas != null && !preguntas.isEmpty()) {
            // Calcular el tiempo dependiendo de la dificultad
            int totalPreguntas = preguntas.size();
            int dificultad = intent.getIntExtra("dificultad", 1); // dificultad 1: fácil, 2: medio, 3: difícil
            int tiempoPorPregunta = dificultad == 1 ? 5000 : (dificultad == 2 ? 7000 : 10000);
            timeRemaining = totalPreguntas * tiempoPorPregunta;

            // Iniciar el temporizador
            startTimer();

            // Mostrar la primera pregunta
            displayCurrentQuestion();
        } else {
            Toast.makeText(this, "No se recibieron preguntas", Toast.LENGTH_SHORT).show();
        }
        btnSiguiente.setEnabled(true);
        btnSiguiente.setOnClickListener(v -> {
            // Comprobar si se ha seleccionado una respuesta
            if (radioGroupOpciones.getCheckedRadioButtonId() == -1) {
                // Si no se seleccionó ninguna respuesta, incrementar sin responder
                unansweredCount++;
            } else {
                // Obtener la respuesta seleccionada
                boolean selectedAnswer = optionTrue.isChecked();

                // Comparar la respuesta seleccionada con la respuesta correcta
                if (selectedAnswer == preguntas.get(currentQuestionIndex).getCorrectAnswer()) {
                    correctAnswersCount++;
                } else {
                    incorrectAnswersCount++;
                }
            }

            // Avanzar a la siguiente pregunta
            currentQuestionIndex++;
            if (currentQuestionIndex < preguntas.size()) {
                displayCurrentQuestion();
            } else {
                // Fin del cuestionario, mostrar el resultado
                showResult();
            }

            // Limpiar la selección de radio para la siguiente pregunta
            radioGroupOpciones.clearCheck();
        });


        // Configurar el botón de volver a jugar
        btnVolverAJugar.setOnClickListener(v -> {
            // Reiniciar la actividad para comenzar un nuevo juego
            finish();
            startActivity(getIntent());
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvTiempoRestante.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                // Si se acabó el tiempo, mostrar resultados
                // Si se acabó el tiempo, marcar todas las preguntas no respondidas
                for (int i = currentQuestionIndex; i < preguntas.size(); i++) {
                    // Si la pregunta no ha sido respondida, la marcamos como sin respuesta
                    unansweredCount++;
                }
                showResult();
            }
        };
        countDownTimer.start();
    }

    private void displayCurrentQuestion() {
        // Mostrar la pregunta y las opciones
        Question currentQuestion = preguntas.get(currentQuestionIndex);
        tvPregunta.setText(currentQuestion.getQuestion());
        optionTrue.setText("True");
        optionFalse.setText("False");

        // Limpiar selección previa
        radioGroupOpciones.clearCheck();

        // Actualizar contador
        tvContador.setText("Pregunta " + (currentQuestionIndex + 1) + " de " + preguntas.size());
    }

    private void showResult() {
        // Detener el temporizador
        countDownTimer.cancel();

        // Ocultar las preguntas
        tvPregunta.setVisibility(View.INVISIBLE);
        tvContador.setVisibility(View.INVISIBLE);
        radioGroupOpciones.setVisibility(View.INVISIBLE);
        btnSiguiente.setVisibility(View.INVISIBLE);
        tvTiempoRestante.setVisibility(View.INVISIBLE);

        // Mostrar los resultados
        TextView tvCorrectas = findViewById(R.id.tvCorrectas);
        TextView tvIncorrectas = findViewById(R.id.tvIncorrectas);
        TextView tvSinResponder = findViewById(R.id.tvSinResponder);

        tvCorrectas.setText("Correctas: " + correctAnswersCount);
        tvIncorrectas.setText("Incorrectas: " + incorrectAnswersCount);
        tvSinResponder.setText("Sin responder: " + unansweredCount);

        // Mostrar la vista de resultados
        findViewById(R.id.llResultados).setVisibility(View.VISIBLE);
        btnVolverAJugar.setVisibility(View.VISIBLE); // Hacer visible el botón de volver a jugar
    }
}


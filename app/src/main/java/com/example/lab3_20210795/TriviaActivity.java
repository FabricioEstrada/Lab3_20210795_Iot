package com.example.lab3_20210795;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class TriviaActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    TriviaViewModel viewModel;
    private LinearLayout layoutHeader;
    private TextView tvPregunta, tvContador, tvTiempoRestante;
    private RadioGroup radioGroupOpciones;
    private Button btnSiguiente, btnVolverAJugar;
    private RadioButton optionTrue, optionFalse;

    private int tiempoPorPregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        viewModel = new ViewModelProvider(this).get(TriviaViewModel.class);

        // Inicializar vistas
        tvPregunta = findViewById(R.id.tvPregunta);
        tvContador = findViewById(R.id.tvContador);
        tvTiempoRestante = findViewById(R.id.tvTiempoRestante);
        radioGroupOpciones = findViewById(R.id.radioGroupOpciones);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnVolverAJugar = findViewById(R.id.btnVolverAJugar);
        optionTrue = findViewById(R.id.optionTrue);
        optionFalse = findViewById(R.id.optionFalse);
        layoutHeader = findViewById(R.id.layoutHeader);

        if (viewModel.preguntas == null) {
            // Primera vez entrando, recibimos preguntas
            Intent intent = getIntent();
            List<Question> preguntas = (List<Question>) intent.getSerializableExtra("preguntas");

            if (preguntas != null && !preguntas.isEmpty()) {
                viewModel.preguntas = preguntas;
                int dificultad = intent.getIntExtra("dificultad", 1);
                tiempoPorPregunta = dificultad == 1 ? 5000 : (dificultad == 2 ? 7000 : 10000);
                viewModel.timeRemaining = preguntas.size() * tiempoPorPregunta;

                startTimer();
                displayCurrentQuestion();
            } else {
                Toast.makeText(this, "No se recibieron preguntas", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Si ya existía (rotación u otro evento), solo restaurar
            if (viewModel.isGameFinished) {
                showResult();
            } else {
                startTimer();
                displayCurrentQuestion();
            }
        }

        btnSiguiente.setEnabled(true);
        btnSiguiente.setOnClickListener(v -> {
            if (radioGroupOpciones.getCheckedRadioButtonId() == -1) {
                viewModel.unansweredCount++;
            } else {
                boolean selectedAnswer = optionTrue.isChecked();
                if (selectedAnswer == viewModel.preguntas.get(viewModel.currentQuestionIndex).getCorrectAnswer()) {
                    viewModel.correctAnswersCount++;
                } else {
                    viewModel.incorrectAnswersCount++;
                }
            }

            viewModel.currentQuestionIndex++;
            if (viewModel.currentQuestionIndex < viewModel.preguntas.size()) {
                displayCurrentQuestion();
            } else {
                showResult();
            }
            radioGroupOpciones.clearCheck();
        });

        btnVolverAJugar.setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(viewModel.timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.timeRemaining = millisUntilFinished;
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvTiempoRestante.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                if (!viewModel.isGameFinished) {
                    for (int i = viewModel.currentQuestionIndex; i < viewModel.preguntas.size(); i++) {
                        viewModel.unansweredCount++;
                    }
                    showResult();
                }
            }
        }.start();
    }

    private void displayCurrentQuestion() {
        Question currentQuestion = viewModel.preguntas.get(viewModel.currentQuestionIndex);
        tvPregunta.setText(currentQuestion.getQuestion());
        optionTrue.setText("True");
        optionFalse.setText("False");

        radioGroupOpciones.clearCheck();
        tvContador.setText("Pregunta " + (viewModel.currentQuestionIndex + 1) + " de " + viewModel.preguntas.size());
    }

    private void showResult() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        viewModel.isGameFinished = true;

        // Ocultar vistas de preguntas
        tvPregunta.setVisibility(View.INVISIBLE);
        tvContador.setVisibility(View.INVISIBLE);
        radioGroupOpciones.setVisibility(View.INVISIBLE);
        btnSiguiente.setVisibility(View.INVISIBLE);
        tvTiempoRestante.setVisibility(View.INVISIBLE);
        layoutHeader.setVisibility(View.GONE);

        // Mostrar resultados
        TextView tvCorrectas = findViewById(R.id.tvCorrectas);
        TextView tvIncorrectas = findViewById(R.id.tvIncorrectas);
        TextView tvSinResponder = findViewById(R.id.tvSinResponder);

        tvCorrectas.setText("Correctas: " + viewModel.correctAnswersCount);
        tvIncorrectas.setText("Incorrectas: " + viewModel.incorrectAnswersCount);
        tvSinResponder.setText("Sin responder: " + viewModel.unansweredCount);

        findViewById(R.id.llResultados).setVisibility(View.VISIBLE);
        btnVolverAJugar.setVisibility(View.VISIBLE);
    }
}

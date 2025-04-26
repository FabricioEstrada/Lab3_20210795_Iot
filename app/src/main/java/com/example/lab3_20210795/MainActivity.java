package com.example.lab3_20210795;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20210795.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TriviaApi triviaApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializa TriviaApi antes de usarla
        triviaApi = new TriviaApi();  // Asegúrate de que TriviaApi tenga un constructor adecuado

        // Llamar a las funciones para configurar los spinners
        setupCategorySpinner();
        setupDifficultySpinner();

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Deshabilitar el botón de comenzar al inicio
        binding.btnComenzar.setEnabled(false);

        // Click en el botón de comprobar conexión
        binding.btnComprobarConexion.setOnClickListener(v -> {
            if (isInternetAvailable()) {
                Toast.makeText(this, "✅ ¡Conexión disponible!", Toast.LENGTH_SHORT).show();
                binding.btnComenzar.setEnabled(true);
            } else {
                Toast.makeText(this, "❌ Sin conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        });

        // Click en el botón de comenzar
        binding.btnComenzar.setOnClickListener(v -> {
            String categoriaSeleccionada = binding.spinnerCategoria.getSelectedItem().toString();
            String cantidadStr = binding.editTextCantidad.getText().toString();
            int cantidadPreguntas = Integer.parseInt(cantidadStr);
            String dificultadSeleccionada = binding.spinnerDificultad.getSelectedItem().toString();

            // Llamar a la API para obtener las preguntas
            triviaApi.getTriviaQuestions(
                    cantidadPreguntas,
                    getCategoryId(categoriaSeleccionada),
                    dificultadSeleccionada,
                    "boolean",  // Tipo de pregunta (true/false)
                    new TriviaApi.TriviaCallback() {
                        @Override
                        public void onSuccess(List<Question> questions) {
                            // Mostrar las preguntas en el Log o en la interfaz
                            Log.i("Trivia", "Preguntas obtenidas: " + questions.size());
                            // Aquí puedes actualizar la interfaz para mostrar las preguntas
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Mostrar el error en un Toast
                            Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });
    }

    private void setupCategorySpinner() {
        // Lista de categorías
        String[] categorias = {
                "Cultura General", "Libros", "Películas", "Música", "Computación", "Matemática", "Deportes", "Historia"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapter);
    }

    private void setupDifficultySpinner() {
        // Lista de dificultades
        String[] dificultades = {"easy", "medium", "hard"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dificultades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDificultad.setAdapter(adapter);
    }

    private int getCategoryId(String categoria) {
        // Mapea las categorías a sus respectivos IDs en la API
        switch (categoria) {
            case "Cultura General": return 9;
            case "Libros": return 10;
            case "Películas": return 11;
            case "Música": return 12;
            case "Computación": return 18;
            case "Matemática": return 19;
            case "Deportes": return 21;
            case "Historia": return 23;
            default: return 9; // Default to General Knowledge
        }
    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean tieneInternet = false;
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("msg-Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                    tieneInternet = true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("msg-Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                    tieneInternet = true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("msg-Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                    tieneInternet = true;
                }
            }
        }
        return tieneInternet;
    }
}

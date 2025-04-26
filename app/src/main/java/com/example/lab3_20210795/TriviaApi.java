package com.example.lab3_20210795;

import android.util.Log;

import com.example.lab3_20210795.TriviaApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaApi {

    private TriviaApiService triviaApiService;

    public TriviaApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        triviaApiService = retrofit.create(TriviaApiService.class);
    }

    // Método para obtener las preguntas
    public void getTriviaQuestions(int amount, int category, String difficulty, String type, final TriviaCallback callback) {
        Call<TriviaResponse> call = triviaApiService.getQuestions(amount, category, difficulty, type);

        Log.i("TriviaApi", "Realizando solicitud a la API con los parámetros: " +
                "amount=" + amount + ", category=" + category + ", difficulty=" + difficulty + ", type=" + type);

        // Realizamos la llamada asíncrona
        call.enqueue(new Callback<TriviaResponse>() {
            @Override
            public void onResponse(Call<TriviaResponse> call, Response<TriviaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si la respuesta es exitosa, obtenemos las preguntas
                    Log.i("TriviaApi", "Respuesta exitosa de la API. Número de preguntas obtenidas: " + response.body().getResults().size());
                    callback.onSuccess(response.body().getResults());
                } else {
                    // Si hubo un error en la respuesta
                    Log.e("TriviaApi", "Error en la respuesta de la API. Código de error: " + response.code());
                    callback.onFailure("Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TriviaResponse> call, Throwable t) {
                // Si ocurrió un error en la conexión o durante la solicitud
                callback.onFailure("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Interfaz para manejar las respuestas de la API
    public interface TriviaCallback {
        void onSuccess(List<Question> questions);
        void onFailure(String errorMessage);
    }
}

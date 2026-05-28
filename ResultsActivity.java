package com.Luis.Memorama;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private TextView txtPuntuacionFinal, txtMensajeFinal, txtTotalBotones, txtAciertos, txtFallos;
    private Button btnJugarNuevo, btnMenuPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Conectar elementos del XML
        txtPuntuacionFinal = findViewById(R.id.txtPuntuacionFinal);
        txtMensajeFinal = findViewById(R.id.txtMensajeFinal);
        txtTotalBotones = findViewById(R.id.txtTotalBotones);
        txtAciertos = findViewById(R.id.txtAciertos);
        txtFallos = findViewById(R.id.txtFallos);
        btnJugarNuevo = findViewById(R.id.btnJugarNuevo);
        btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal);

        // Obtener datos del Intent - AHORA CON LAS CLAVES CORRECTAS
        int paresEncontrados = getIntent().getIntExtra("Pares", 0);
        int totalPares = getIntent().getIntExtra("TOTAL_Pares", 0);

        // Calcular estadísticas
        int aciertos = paresEncontrados;
        int fallos = totalPares - aciertos;

        // Mostrar estadísticas
        txtPuntuacionFinal.setText("Pares encontrados: " + paresEncontrados + " / " + totalPares);
        txtTotalBotones.setText("Total de pares en el juego: " + totalPares);
        txtAciertos.setText("Pares acertados: " + aciertos);
        txtFallos.setText("Pares fallados: " + fallos);

        // Mostrar mensaje según la puntuación
        String mensaje = obtenerMensajeSegunPuntuacion(paresEncontrados, totalPares);
        txtMensajeFinal.setText(mensaje);

        // Botón Jugar de Nuevo
        btnJugarNuevo.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        // Botón Menú Principal
        btnMenuPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String obtenerMensajeSegunPuntuacion(int paresEncontrados, int totalPares) {
        double porcentaje = (double) paresEncontrados / totalPares * 100;

        if (porcentaje >= 90) {
            return "¡Perfecto! Encontraste todos los pares 🎯🏆";
        } else if (porcentaje >= 70) {
            return "¡Muy bien! Excelente memoria 💪";
        } else if (porcentaje >= 50) {
            return "¡Buen trabajo! Sigue practicando 👍";
        } else if (porcentaje >= 30) {
            return "¡Puedes hacerlo mejor! 😊";
        } else {
            return "¡Sigue intentándolo! La práctica hace al maestro 💪";
        }
    }
}

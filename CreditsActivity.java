package com.Luis.Memorama;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreditsActivity extends AppCompatActivity {

    private TextView txtNombre1, txtNombre2, txtInstitucion, txtFecha;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        // Conectar los elementos del XML con el código Java
        txtNombre1 = findViewById(R.id.txtNombre1);
        txtInstitucion = findViewById(R.id.txtInstitucion);
        txtFecha = findViewById(R.id.txtFecha);
        btnVolver = findViewById(R.id.btnVolver);

        // Establecer la información personalizada
        txtNombre1.setText("Vega Hernandez Luis Ricardo");
        txtInstitucion.setText("Facultad de Estudios Superiores Aragón");

        // Obtener y formatear la fecha actual
        String fechaActual = obtenerFechaActual();
        txtFecha.setText("Fecha: " + fechaActual);

        // Botón Volver - regresa al menú principal
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(CreditsActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra esta actividad
        });
    }

    ///Método para obtener la fecha actual formateada
    private String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
package com.Luis.Memorama;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    private TextView txtTiempo, txtPuntos, txtMensaje;
    private GridLayout tablero;
    private Button btnSalirJuego;

    private int tiempoRestante = 60;
    private int paresEncontrados = 0;
    private int totalPares = 8;
    private boolean juegoActivo = false;
    private Handler handler = new Handler();

    private ArrayList<Integer> valoresCartas;
    private ImageButton[] botonesCartas;
    private int[] imagenesAnimales;
    private int primerIndice = -1;
    private boolean esperandoComparacion = false;

    private Runnable temporizadorRunnable = new Runnable() {
        @Override
        public void run() {
            if (tiempoRestante > 0 && juegoActivo) {
                tiempoRestante--;
                txtTiempo.setText("Tiempo: " + tiempoRestante + "s");
                handler.postDelayed(this, 1000);
            } else if (tiempoRestante == 0 && juegoActivo) {
                terminarJuego();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtTiempo = findViewById(R.id.txtTiempo);
        txtPuntos = findViewById(R.id.txtPuntos);
        txtMensaje = findViewById(R.id.txtMensaje);
        tablero = findViewById(R.id.tableroCartas);
        btnSalirJuego = findViewById(R.id.btnSalirJuego);

        btnSalirJuego.setOnClickListener(v -> salirDelJuego());

        iniciarMemorama();
    }

    private void iniciarMemorama() {
        inicializarImagenes();
        generarCartas();
        agregarCartasAlTablero();

        txtMensaje.setText("Memorama\n¡Encuentra los pares!");
        handler.postDelayed(() -> {
            txtMensaje.setVisibility(View.GONE);
            juegoActivo = true;
            handler.post(temporizadorRunnable);
        }, 1500);
    }

    private void inicializarImagenes() {
        imagenesAnimales = new int[]{
                R.drawable.perro,
                R.drawable.gato,
                R.drawable.raton,
                R.drawable.hamster,
                R.drawable.conejo,
                R.drawable.zorro,
                R.drawable.oso,
                R.drawable.panda
        };
    }

    private void generarCartas() {
        valoresCartas = new ArrayList<>();
        for (int i = 0; i < totalPares; i++) {
            valoresCartas.add(i);
            valoresCartas.add(i);
        }
        Collections.shuffle(valoresCartas);
    }

    private void agregarCartasAlTablero() {
        int totalCartas = valoresCartas.size();
        botonesCartas = new ImageButton[totalCartas];

        tablero.removeAllViews();
        tablero.setColumnCount(4);
        tablero.setRowCount(4);

        for (int i = 0; i < totalCartas; i++) {
            ImageButton btn = new ImageButton(this);
            btn.setImageResource(R.drawable.reverso);
            btn.setBackgroundColor(0xFF1976D2);
            btn.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            btn.setPadding(4, 4, 4, 4);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);

            final int index = i;
            btn.setOnClickListener(v -> onCartaClick(index));

            tablero.addView(btn);
            botonesCartas[index] = btn;
        }
    }

    private void onCartaClick(int indice) {
        if (!juegoActivo || esperandoComparacion) return;
        if (primerIndice == indice) return;

        ImageButton carta = botonesCartas[indice];
        if (!carta.isEnabled()) return;

        int valorAnimal = valoresCartas.get(indice);
        carta.setImageResource(imagenesAnimales[valorAnimal]);
        carta.setBackgroundColor(0xFFFFA726);

        if (primerIndice == -1) {
            primerIndice = indice;
        } else {
            esperandoComparacion = true;
            final int segundoIndice = indice;

            handler.postDelayed(() -> {
                int valor1 = valoresCartas.get(primerIndice);
                int valor2 = valoresCartas.get(segundoIndice);

                if (valor1 == valor2) {
                    botonesCartas[primerIndice].setEnabled(false);
                    botonesCartas[segundoIndice].setEnabled(false);
                    botonesCartas[primerIndice].setBackgroundColor(0xFF4CAF50);
                    botonesCartas[segundoIndice].setBackgroundColor(0xFF4CAF50);
                    paresEncontrados++;
                    txtPuntos.setText("Pares: " + paresEncontrados);

                    if (paresEncontrados == totalPares) {
                        terminarJuego();
                    }
                } else {
                    botonesCartas[primerIndice].setImageResource(R.drawable.reverso);
                    botonesCartas[primerIndice].setBackgroundColor(0xFF1976D2);
                    botonesCartas[segundoIndice].setImageResource(R.drawable.reverso);
                    botonesCartas[segundoIndice].setBackgroundColor(0xFF1976D2);
                }

                primerIndice = -1;
                esperandoComparacion = false;
            }, 700);
        }
    }

    private void terminarJuego() {
        juegoActivo = false;
        handler.removeCallbacks(temporizadorRunnable);

        Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
        intent.putExtra("Pares", paresEncontrados);
        intent.putExtra("TOTAL_Pares", totalPares);
        startActivity(intent);
        finish();
    }

    private void salirDelJuego() {
        juegoActivo = false;
        handler.removeCallbacks(temporizadorRunnable);
        startActivity(new Intent(GameActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(temporizadorRunnable);
    }
}
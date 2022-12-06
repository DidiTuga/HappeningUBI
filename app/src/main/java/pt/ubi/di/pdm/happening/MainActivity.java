package pt.ubi.di.pdm.happening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // METER APLICAÇÂO EM FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //TIRAR A BARRA DE TITULO
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        // Inicializar os botoes
        Button x = findViewById(R.id.Btn_comecar);
        x.setOnClickListener(this);
        Button y = findViewById(R.id.Btn_ajudar);
        y.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.Btn_comecar: // Abre uma nova janela
                Intent Jan = new Intent(this, LoginActivity.class);
                startActivity(Jan);
                // ola
                break;
            case R.id.Btn_ajudar: // abre uma pagina web
                Intent Janela = new Intent(this, AjudaActivity.class);
                startActivity(Janela);
                finish();
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }
}
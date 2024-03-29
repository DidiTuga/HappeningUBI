package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Tirar a barra de cima
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
        switch (view.getId()) {
            case R.id.Btn_comecar: // Abre uma nova janela
                Intent Jan = new Intent(this, LoginActivity.class);
                startActivity(Jan);
                finish();
                break;
            case R.id.Btn_ajudar: // abre uma janela que vai explicar como funciona a aplicação
                Intent Janela = new Intent(this, AjudaActivity.class);
                startActivity(Janela);
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }
}
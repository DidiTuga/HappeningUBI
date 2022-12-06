package pt.ubi.di.pdm.happening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventosActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    TextView inicial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        // ver se esta logado
        mAuth = FirebaseAuth.getInstance();
        // inicializar botoes
        Button x = findViewById(R.id.Btn_deslogar);
        x.setOnClickListener(this);
        inicial = findViewById(R.id.Txt_inicial);
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){

            // voltar para o login se nao tiver logado
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            // se estiver logado
            inicial.setText("Bem vindo " + currentUser.getEmail());
        }

    }
    public void OnBackPressed(){
        // nao deixa voltar para tras
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_deslogar:
                mAuth.signOut();
                finish();
                Intent Jan = new Intent(this, MainActivity.class);
                startActivity(Jan);
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }
}
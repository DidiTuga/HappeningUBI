package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Variaveis edittexts
    private ProgressBar progressBar;
    private EditText nome, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        // Inicializar os botoes
        Button x = findViewById(R.id.Btn_entrar);
        x.setOnClickListener(this);
        TextView y = findViewById(R.id.Txt_registar);
        y.setOnClickListener(this);
        // Inicializar os edittexts
        nome = findViewById(R.id.Edt_nome);
        nome.setOnClickListener(this);
        password = findViewById(R.id.Edt_password);
        progressBar = findViewById(R.id.prog);
        progressBar.setVisibility(View.INVISIBLE);
        password.setOnClickListener(this);
        // ver se esta logado
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance(); // null se nao estiver logado


    }
    // Ver se esta logado
    public void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // user está logado
            Intent Jan = new Intent(this, EventosActivity.class);
            startActivity(Jan);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_entrar: // Vai tentar os dados do utilizador
                loginUser();
                break;
            case R.id.Txt_registar: // Vai para a activity de registar
                Intent Jan = new Intent(this, RegistarActivity.class);
                startActivity(Jan);
                finish();
                break;
            case R.id.Edt_nome: // Limpa o edittext
                nome.setText("");
                break;
            case R.id.Edt_password: // Limpa o edittext
                password.setText("");
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }


    // funcao para fazer login
    // Vai buscar os dados do utilizador e tenta fazer login
    // Se conseguir vai para a pagina principal se nao dá uma mensagem de erro
    public void loginUser(){
        String email = nome.getText().toString();
        String pass = password.getText().toString();
        if(email.isEmpty() || pass.isEmpty()){
            Uteis.MSG(getApplicationContext(), "Preencha todos os campos");
        }else{
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Intent Jan = new Intent(getApplicationContext(), EventosActivity.class);
                            startActivity(Jan);
                        }}, 1000);

                }else{
                    Uteis.MSG(getApplicationContext(), "Erro ao logar");
                }
            });
        }
    }
}
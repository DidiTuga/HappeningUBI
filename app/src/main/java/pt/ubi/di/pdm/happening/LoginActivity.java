package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Variaveis edittexts

    EditText nome, password;
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
        password.setOnClickListener(this);
        // ver se esta logado
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_entrar:
                loginUser();
                break;
            case R.id.Txt_registar:
                Intent Jan = new Intent(this, RegistarActivity.class);
                startActivity(Jan);
                break;
            case R.id.Edt_nome:
                nome.setText("");
                break;
            case R.id.Edt_password:
                password.setText("");
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }


    // funcao para fazer login
    public void loginUser(){
        String email = nome.getText().toString();
        String pass = password.getText().toString();
        if(email.isEmpty() || pass.isEmpty()){
            Uteis.MSG(getApplicationContext(), "Preencha todos os campos");
        }else{
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    Intent Jan = new Intent(this, EventosActivity.class);
                    startActivity(Jan);
                }else{
                    Uteis.MSG(getApplicationContext(), "Erro ao logar");
                }
            });
        }
    }
}
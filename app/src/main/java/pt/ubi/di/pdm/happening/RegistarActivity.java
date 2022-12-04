package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistarActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registar);
        // Inicializar os botoes
        Button x = findViewById(R.id.Btn_registar);
        x.setOnClickListener(this);
        Button y = findViewById(R.id.Btn_voltar);
        y.setOnClickListener(this);

        // Inicializar os edittexts
        email = findViewById(R.id.Edt_email1);
        email.setOnClickListener(this);
        password = findViewById(R.id.Edt_password1);
        password.setOnClickListener(this);

        // ver se esta logado
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_registar:
                createUser();
                break;
            case R.id.Btn_voltar:
                Intent Jan = new Intent(this, LoginActivity.class);
                startActivity(Jan);
                break;
            case R.id.Edt_email1:
                email.setText("");
                break;
            case R.id.Edt_password1:
                password.setText("");
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }

    // funcao para criar user
    public void createUser(){
        String email_ = email.getText().toString();
        String pass = password.getText().toString();
        if(email_.isEmpty() || pass.isEmpty()){
            Uteis.MSG(getApplicationContext(), "Preencha todos os campos");
        }else{
            mAuth.createUserWithEmailAndPassword(email_, pass).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    Uteis.MSG(getApplicationContext(), "Registado com sucesso");
                    Intent Jan = new Intent(this, LoginActivity.class);
                    startActivity(Jan);
                }else{
                    Uteis.MSG(getApplicationContext(), "Erro ao registar");
                }
            });
        }
    }
}
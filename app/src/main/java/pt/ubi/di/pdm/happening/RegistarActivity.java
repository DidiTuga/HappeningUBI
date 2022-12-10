package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistarActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password, nome, telemovel, idade;
    FirebaseAuth mAuth;
    String accountID;

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
        nome = findViewById(R.id.Edt_nome);
        nome.setOnClickListener(this);
        telemovel = findViewById(R.id.Edt_movel);
        telemovel.setOnClickListener(this);
        idade = findViewById(R.id.Edt_idade);
        idade.setOnClickListener(this);

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
                finish();
                break;
            case R.id.Edt_email1:
                email.setText("");
                break;
            case R.id.Edt_password1:
                password.setText("");
                break;
            case R.id.Edt_nome:
                nome.setText("");
                break;
            case R.id.Edt_movel:
                telemovel.setText("");
                break;
            case R.id.Edt_idade:
                idade.setText("");
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }

    // funcao para criar user
    public void createUser() {
        String email_ = email.getText().toString();
        String pass = password.getText().toString();
        if (email_.isEmpty() || pass.isEmpty()) {
            Uteis.MSG(getApplicationContext(), "Preencha todos os campos");
        } else {
            mAuth.createUserWithEmailAndPassword(email_, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Uteis.MSG(getApplicationContext(), "Registado com sucesso");
                    saveuser();
                    Intent Jan = new Intent(this, LoginActivity.class);
                    startActivity(Jan);
                    finish();
                } else {
                    String erro;
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma password que tenha no minimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Essa conta já existe";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email inválido";
                    } catch (Exception e) {
                        erro = "Erro ao registar";
                    }
                    Uteis.MSG(getApplicationContext(), erro);
                }
            });
        }
    }

    private void saveuser() {
        String nome_ = nome.getText().toString();
        String telemovel_ = telemovel.getText().toString();
        String idade_ = idade.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> users = new HashMap<>();
        users.put("nome", nome_);
        users.put("telemovel", telemovel_);
        users.put("idade", idade_);
        accountID = mAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users").document(accountID);
        docRef.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Uteis.MSG_Log("User saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Uteis.MSG_Log("Error saving user" + e);
            }
        });


    }
}
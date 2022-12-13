package pt.ubi.di.pdm.happening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private EditText email, password, telemovel, idade;
    private ProgressBar Pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        // ir buscar os dados do user
        Intent intent = getIntent();
        String nome = intent.getStringExtra("nome");
        TextView txt_nome = findViewById(R.id.Txt_nomeUser_edit);
        txt_nome.setText(nome);
        // buscar os edittexts
        email = findViewById(R.id.Edt_novoEmail);
        password = findViewById(R.id.Edt_novaPassword);
        telemovel = findViewById(R.id.Edt_novoTelefone);
        idade = findViewById(R.id.Edt_novaIdade);
        Pb = findViewById(R.id.Pb_editarUser);
        Pb.setVisibility(View.GONE);
        // botao para ouvir
        findViewById(R.id.Btn_editarUser).setOnClickListener(this);
        findViewById(R.id.Btn_apagarUser).setOnClickListener(this);

        // ir ver se esta logado
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        // ir buscar os dados do user e meter nos edittexts
        if (user != null) {// user está logado
            email.setText(user.getEmail());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<com.google.firebase.firestore.DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        com.google.firebase.firestore.DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            idade.setText(document.get("idade").toString());
                            telemovel.setText(document.get("telemovel").toString());

                        }
                    }
                }
            });
        } else { // user não está logado
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_editarUser:
                // guardar os dados
                // Mensagem de dialog a confirmar se quer mesmo editar
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#ffed5e'>Editar Perfil</font>"));
                builder.setMessage(Html.fromHtml("<font color='#fffbcb'>Tem a certeza que quer editar o seu perfil?</font>"));
                builder.setPositiveButton("Sim", (dialog, which) -> {
                    editarPerfil();
                });
                builder.setNegativeButton("Voltar", (dialog, which) -> {
                    // voltar para a pagina anterior
                    onBackPressed();
                });
                // Mudar a cor do botao (sim e nao) do dialog e o background
                AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alert.getWindow().setBackgroundDrawableResource(R.color.backgroud);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ff7e32"));
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#A80F0F"));
                    }
                });
                alert.show();
                break;
            case R.id.Btn_apagarUser:
                // apagar o user
                // Mensagem de dialog a confirmar se quer mesmo editar
                AlertDialog.Builder fim = new AlertDialog.Builder(this);
                fim.setTitle(Html.fromHtml("<font color='#ffed5e'>Editar Perfil</font>"));
                fim.setMessage(Html.fromHtml("<font color='#fffbcb'>Tem a certeza que quer apagar o seu perfil?</font>"));
                fim.setPositiveButton("Sim", (dialog, which) -> {
                    apagarUser();
                });
                fim.setNegativeButton("Voltar", (dialog, which) -> {
                    // voltar para a pagina anterior
                    onBackPressed();
                });
                // Mudar a cor do botao (sim e nao) do dialog e o background
                AlertDialog dialog = fim.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroud);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ff7e32"));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#A80F0F"));
                    }
                });
                dialog.show();

                break;
        }
    }
    // ApagarUser
    // Apaga o user da base de dados e do firebase auth
    private void apagarUser() {
        // apagar o user
        Pb.setVisibility(View.VISIBLE);
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // apagar o user da base de dados
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(user.getUid()).delete();
                    // voltar para a pagina de login
                    Uteis.MSG(getApplicationContext(), "Perfil apagado com sucesso!");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Pb.setVisibility(View.GONE);
                            Intent intent = new Intent(EditarPerfilActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                } else {
                    Uteis.MSG(EditarPerfilActivity.this, "Volte a fazer o login e tente novamente!");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(EditarPerfilActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 900);

                }
            }
        });
    }

    // Editar o perfil
    // Vai dar update primeiro a password e depois da update ao email e só depois vai dar update aos dados do user
    private void editarPerfil() {
        // editar o perfil
        // verificar se os campos estao preenchidos
        String email_user = email.getText().toString();
        String password_user = password.getText().toString();
        String telemovel_user = telemovel.getText().toString();
        String idade_user = idade.getText().toString();

        if (email_user.isEmpty()) {
            // email nao preenchido
            email.setError("Preencha o email!");
            email.requestFocus();
            return;
        }
        if (password_user.isEmpty()) {
            // password nao preenchida
            password.setError("Preencha a password!");
            password.requestFocus();
            return;
        }
        if (telemovel_user.isEmpty()) {
            // telemovel nao preenchido
            telemovel.setError("Preencha o telemovel!");
            telemovel.requestFocus();
            return;
        }
        if (idade_user.isEmpty()) {
            // idade nao preenchida
            idade.setError("Preencha a idade!");
            idade.requestFocus();
            return;
        }
        // atualizar password
        user.updatePassword(password_user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // atualizar email
                    user.updateEmail(email_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // atualizar os dados do user na base de dados
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").document(user.getUid()).update("idade", idade_user, "telemovel", telemovel_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // atualizar os dados do user na base de dados
                                            Uteis.MSG(getApplicationContext(), "Perfil editado com sucesso!");
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Pb.setVisibility(View.GONE);
                                                    Intent intent = new Intent(EditarPerfilActivity.this, PerfilActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }, 1000);
                                        } else {
                                            Uteis.MSG_Debug(task.getException().getMessage());
                                        }
                                    }
                                });
                            } else {
                                Uteis.MSG_Debug(task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    Uteis.MSG_Debug(task.getException().getMessage());
                }
            }
        });
    }
}

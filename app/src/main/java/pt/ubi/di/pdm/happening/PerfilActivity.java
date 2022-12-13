package pt.ubi.di.pdm.happening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    // TextViews
    private TextView nome, email, telefone, idade;
    // Strings
    private String nomeS, uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //tirar o titulo
        Objects.requireNonNull(getSupportActionBar()).hide();
        // inicializar textviews
        nome = findViewById(R.id.Txt_nomeUser);
        email = findViewById(R.id.Txt_emailUser);
        telefone = findViewById(R.id.Txt_teleUser);
        idade = findViewById(R.id.Txt_idaUser);
        // inicializar botao
        Button btn_editar = findViewById(R.id.Btn_altUser);
        btn_editar.setOnClickListener(this);
        // inicializar
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        // verificar se esta logado
        if (user != null) {
            // se estiver logado
            // obter dados do user
            String emailS = mAuth.getCurrentUser().getEmail();
            uid = mAuth.getCurrentUser().getUid();
            // ir buscar a base de dados users o documento com o id do user
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                // se conseguir ir buscar o documento
                // obter os dados do documento
                nomeS = documentSnapshot.getString("nome");
                String telemovelS = documentSnapshot.getString("telemovel");
                String idadeS = documentSnapshot.getString("idade");
                // colocar os dados nos textviews
                nome.setText("Nome: " +nomeS);
                email.setText("Email: "+emailS);
                telefone.setText("Telemovel: "+ telemovelS);
                idade.setText("Idade: "+idadeS);
            });
        }else {
            // se nao estiver logado
            // ir para a activity de login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Btn_altUser:
                // abrir activity de editar perfil
                Intent intent = new Intent(this, EditarPerfilActivity.class);
                intent.putExtra("nome", nomeS);
                startActivity(intent);
                break;
        }
    }
}
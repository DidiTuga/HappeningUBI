package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Evento_Separado extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private String nome, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_separado);
        mAuth = FirebaseAuth.getInstance();
        // ir buscar os dados do evento
        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
        // Mudar o título da action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Evento: " + nome);
        String descricao = intent.getStringExtra("descricao");
        String local = intent.getStringExtra("local");
        String data = intent.getStringExtra("data");
        link = intent.getStringExtra("link");
        String id = intent.getStringExtra("id_user");
        // inicializar os textviews/imagens/buttons
        TextView nome1 = findViewById(R.id.Txt_nomeEvento_separado);
        TextView descricao1 = findViewById(R.id.Txt_descricaoEvento_separado);
        TextView local1 = findViewById(R.id.Txt_localEvento_separado);
        TextView data1 = findViewById(R.id.Txt_dataEvento_separado);
        ImageView imagem1 = findViewById(R.id.Img_evento_separado);
        Button y = findViewById(R.id.Btn_ApagarEvento_separado);
        y.setVisibility(View.INVISIBLE);
        y.setOnClickListener(this);
        // colocar os dados nos textviews/imagens
        Picasso.with(this).load(link).into((imagem1));
        descricao1.setText("Descrição: " + descricao);
        local1.setText("Local: " + local);
        data1.setText("Data: " + data);
        nome1.setText("Nome: " + nome);
        // se o evento for do utilizador logado ou o admin, aparece o botao de apagar evento
        if (id.equals(mAuth.getCurrentUser().getUid()) || mAuth.getCurrentUser().getEmail().equals("WTwEe1wgTIXXUM76SiNvz2XdEOF2")) {
            y.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Ver se o utilizador está logado
    public void onStart() {
        super.onStart();
        // ver se esta logado
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_ApagarEvento_separado: // Vai apagar o evento e volta mas espera um 1.5s para atualizar a db
                // apagar o evento na base de dados e a imagem
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(link);
                if (storageReference != null) {
                    storageReference.delete();
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("eventos").document(nome).delete().addOnCompleteListener(task -> {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);

                });


                break;
        }
    }
}
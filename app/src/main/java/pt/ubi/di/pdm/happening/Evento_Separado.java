package pt.ubi.di.pdm.happening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class Evento_Separado extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_separado);
        mAuth = FirebaseAuth.getInstance();
        // ir buscar os dados do evento
        Intent intent = getIntent();
        String nome = intent.getStringExtra("nome");
        String descricao = intent.getStringExtra("descricao");
        String local = intent.getStringExtra("local");
        String data = intent.getStringExtra("data");
        String link = intent.getStringExtra("link");
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
        descricao1.setText(descricao);
        local1.setText(local);
        data1.setText(data);
        nome1.setText(nome);
        // se o evento for do utilizador logado, aparece o botao de apagar
        if (id.equals(mAuth.getCurrentUser().getUid())) {
            y.setVisibility(View.VISIBLE);
        }


    }
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
            case R.id.Btn_ApagarEvento_separado:
                // apagar o evento na base de dados e a imagem

                break;
        }
    }
}
package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EventosActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewInterface {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private StorageReference mStorageRef;
    private ProgressBar mProgressCircle;
    ArrayList<Evento> eventos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        // ver se esta logado
        mAuth = FirebaseAuth.getInstance();
        // inicializar botoes
        FloatingActionButton x = findViewById(R.id.Btn_adicionar);
        x.setOnClickListener(this);
        // inicializar recycler view
        mRecyclerView = findViewById(R.id.RvRecycler);
        // inicilizar db com os eventos e storage
        mStorageRef = FirebaseStorage.getInstance().getReference("imagens");
        getEventos();

    }
    // metodo para criar o menu
    public boolean onCreateOptionsMenu(Menu menu) {
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // se clicar nos buttons do menu
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                // Sair e voltar para o inicio
                mAuth.signOut();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                this.finish();
                return true;
            case R.id.idPerfil:
                // Ir para o perfil
                Uteis.MSG(this, "Perfil");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        }

    }

    // funcao para ir buscar os eventos
    private void getEventos() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String nome = document.getId();
                                String descricao = document.getString("Descricao");
                                String local = document.getString("Local");
                                String link = document.getString("ImagemLink");
                                Timestamp data = document.getTimestamp("Data");
                                String id_user = document.getString("Id_User");
                                // ir buscar o dia anterior para nao aparecer os eventos que ja passaram
                                Date data_atual = new Date();
                                data_atual.setDate(data_atual.getDate() - 1);
                                if(data != null && data.toDate().after(data_atual)){
                                    eventos.add(new Evento(nome, descricao, local, link, data, id_user));
                                }
                            }
                            mostrarEventos();
                        } else {
                            Uteis.MSG_Log("Error getting documents.");
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_adicionar:
                Intent Jan = new Intent(this, Ad_evento.class);
                startActivity(Jan);
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }
    // funcao para mostrar os eventos
    private void mostrarEventos(){
        // mostrar os eventos no recycler view
        EventoAdapter adapter = new EventoAdapter(this, eventos, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    // funcao para abrir o evento
    @Override
    public void onEventoClick(int position) {
        Intent intent = new Intent(this, Evento_Separado.class);
        // passar o evento para a outra activity
        intent.putExtra("nome", eventos.get(position).getNome());
        intent.putExtra("descricao", eventos.get(position).getDescricao());
        intent.putExtra("local", eventos.get(position).getLocal());
        intent.putExtra("link", eventos.get(position).getLink());
        Date date = eventos.get(position).getData().toDate();
        DateFormat formatter = DateFormat.getDateTimeInstance();
        String dateFormatted = formatter.format(date);
        intent.putExtra("data", dateFormatted);
        intent.putExtra("id_user", eventos.get(position).getId_user());
        startActivity(intent);
    }
}
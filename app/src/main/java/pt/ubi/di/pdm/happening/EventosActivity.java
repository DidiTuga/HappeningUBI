package pt.ubi.di.pdm.happening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EventosActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    // firestorage database
    private RecyclerView mRecyclerView;
    StorageReference mStorageRef;
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // inicilizar db com os eventos e storage
        mStorageRef = FirebaseStorage.getInstance().getReference("imagens");
        getEventos();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //adding a click listner for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.
                mAuth.signOut();
                //on below line we are opening our login activity.
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                this.finish();
                return true;
            case R.id.idPerfil:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "Perfil", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.

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
            // se estiver logado
            //Uteis.MSG(this, "Bem vindo " + currentUser.getEmail());
        }

    }
    private void getEventos() {
        // buscar os eventos da db
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
                                String link = document.getString("ImageLink");
                                Timestamp data = document.getTimestamp("Data");
                                //Uteis.MSG(EventosActivity.this, "nome: " + nome + " descricao: " + descricao + " local: " + local + " link: " + link + " data: " + data);
                                eventos.add(new Evento(nome, descricao, local, link, data));
                            }
                            // mostrar os eventos
                            mostrarEventos();
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    public void OnBackPressed(){
        // nao deixa voltar para tras
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_adicionar:
                Intent Jan = new Intent(this, Ad_evento.class);
                startActivity(Jan);
                finish();
                break;
            default:
                Uteis.MSG(getApplicationContext(), "Esqueceste do on click");
                break;
        }
    }
    private void mostrarEventos(){
        // mostrar os eventos no recycler view
        EventoAdapter adapter = new EventoAdapter(eventos, this);
        mRecyclerView.setAdapter(adapter);








    }
}
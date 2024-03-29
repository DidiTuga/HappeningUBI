package pt.ubi.di.pdm.happening;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdicionarEventoActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private FirebaseAuth mAuth;
    private String AccountID;
    private EditText descricao, local, nome, link;
    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private ProgressBar progressBar;
    private Uri imageUri;
    private String imageUrl;
    private ImageView img;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_evento);
        // Mudar o título da action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Criar novo evento");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroud_2)));

        // ver se esta logado
        mAuth = FirebaseAuth.getInstance();
        // inicializar botoes
        Button y = findViewById(R.id.Btn_date);
        Button x = findViewById(R.id.Btn_adEvento);
        Button z = findViewById(R.id.Btn_image);
        x.setOnClickListener(this);
        y.setOnClickListener(this);
        z.setOnClickListener(this);
        // img
        img = findViewById(R.id.Img_evento);
        // inicializar edittexts
        descricao = findViewById(R.id.Edt_desEvento);
        local = findViewById(R.id.Edt_geopoint);
        nome = findViewById(R.id.Edt_eventoNome);
        // progressbar
        progressBar = findViewById(R.id.Pb_eventoad);
        // Storage
        mStorageRef = FirebaseStorage.getInstance().getReference("imagens");

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
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
        switch (view.getId()) {
            case R.id.Btn_adEvento:
                criarEvento();

                break;
            case R.id.Btn_date:
                Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
                datePickerDialog.show();

                break;
            case R.id.Btn_image:
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, 100);
                break;
            default:
                Uteis.MSG(this, "Esta funcionalidade ainda não está disponível");
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Uteis.MSG_Log("Imagem selecionada com sucesso");
                img.setImageURI(data.getData());
                // upload da imagem para FireStorage
                imageUri = data.getData();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dayFinal = i2;
        monthFinal = i1 + 1;
        yearFinal = i;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;
        Uteis.MSG(this, dayFinal + "/" + monthFinal + "/" + yearFinal + " " + hourFinal + ":" + minuteFinal);
    }


    private void criarEvento() {
        String nomeEvento = nome.getText().toString();
        String descricaoEvento = descricao.getText().toString();
        String localEvento = local.getText().toString();
        if (nomeEvento.isEmpty()) {
            nome.setError("Nome do evento é obrigatório");
            nome.requestFocus();
            return;
        }
        if (descricaoEvento.isEmpty()) {
            descricao.setError("Descrição do evento é obrigatório");
            descricao.requestFocus();
            return;
        }
        if (localEvento.isEmpty()) {
            local.setError("Local do evento é obrigatório");
            local.requestFocus();
            return;
        }
        if (imageUri == null) {
            Uteis.MSG(this, "Selecione uma imagem");
            return;
        }

        // Timestamp com a data e hora do evento
        String data = dayFinal + "/" + monthFinal + "/" + yearFinal + " " + hourFinal + ":" + minuteFinal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        try {
            date = sdf.parse(data);
        } catch (ParseException e) {
            Uteis.MSG_Log(e.getMessage());
        }
        Timestamp timestamp = new Timestamp(date);
        // data != null
        if (dayFinal == 0) {
            Uteis.MSG(this, "Selecione uma data");
            return;
        }
        // upload da imagem para FireStorage
        String nomeImagem = System.currentTimeMillis() + "." + getFileExtension(imageUri);
        StorageReference filepath = mStorageRef.child(nomeImagem);
        // token de acess para a imagem


        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                Uteis.MSG_Log("Imagem carregada com sucesso");
                // obter o token de acesso
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        String token = uri.toString();
                        // criar evento
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> eventos = new HashMap<>();
                        AccountID = mAuth.getCurrentUser().getUid();
                        eventos.put("Id_User", AccountID);
                        eventos.put("Descricao", descricaoEvento);
                        eventos.put("Local", localEvento);
                        eventos.put("ImagemLink", token);
                        eventos.put("Data", timestamp);
                        DocumentReference docRef = db.collection("eventos").document(nomeEvento);
                        docRef.set(eventos).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Uteis.MSG_Log("Eventos saved");
                                progressBar.setVisibility(View.VISIBLE);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Uteis.MSG_Log("Error saving Evento" + e);
                            }
                        });
                    }
                });
            }

            public void onFailure(@NonNull Exception e) {
                Uteis.MSG_Log("Erro ao carregar a imagem");
            }
        });


    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
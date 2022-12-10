package pt.ubi.di.pdm.happening;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class Ad_evento extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private FirebaseAuth mAuth;
    private String AccountID;
    private EditText descricao, local, nome, link;
    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    private Uri imageUri;
    private String imageUrl;
    private ImageView img;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_evento);
        // ver se esta logado
        mAuth = FirebaseAuth.getInstance();
        // inicializar botoes
        Button y = findViewById(R.id.Btn_date);
        Button x = findViewById(R.id.Btn_adEvento);
        x.setOnClickListener(this);
        y.setOnClickListener(this);
        Button z = findViewById(R.id.Btn_image);
        z.setOnClickListener(this);
        img = findViewById(R.id.Img_evento);
        // inicializar edittexts
        descricao = findViewById(R.id.Edt_desEvento);
        local = findViewById(R.id.Edt_geopoint);
        nome = findViewById(R.id.Edt_eventoNome);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_adEvento:
                criarEvento();
                Intent i = new Intent(this, EventosActivity.class);
                startActivity(i);
                finish();
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
                Uteis.MSG(this, "Imagem selecionada com sucesso");
                img.setImageURI(data.getData());
                // upload da imagem para FireStorage
                imageUri = data.getData();
            }
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dayFinal = i2;
        monthFinal = i1;
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
        if (dayFinal == 0){
            Uteis.MSG(this, "Selecione uma data");
            return;
        }
        // upload da imagem para FireStorage
        StorageReference filepath = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uteis.MSG(Ad_evento.this, "Imagem carregada com sucesso");
            }
        });
        // ir buscar o link da imagem
       imageUrl = filepath.getDownloadUrl().toString();
        // criar evento
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> eventos = new HashMap<>();
        AccountID = mAuth.getCurrentUser().getUid();
        eventos.put("Id_User", AccountID);
        eventos.put("Descricao", descricaoEvento);
        eventos.put("Local", localEvento);
        eventos.put("ImagemLink", imageUrl);
        eventos.put("Data", timestamp);
        DocumentReference docRef = db.collection("eventos").document(nomeEvento);
        docRef.set(eventos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Uteis.MSG_Log("Eventos saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Uteis.MSG_Log("Error saving Evento"+ e.toString());
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
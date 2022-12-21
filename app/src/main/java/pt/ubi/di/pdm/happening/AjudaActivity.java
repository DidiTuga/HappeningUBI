package pt.ubi.di.pdm.happening;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AjudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ajuda");
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
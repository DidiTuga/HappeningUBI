package pt.ubi.di.pdm.happening;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AjudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent Jan = new Intent(this, MainActivity.class);
        startActivity(Jan);
        finish();
    }
}
package co.edu.unipiloto.transporteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Rol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        Button transportistaBtn = findViewById(R.id.transportista_btn);
        Button duenoCargaBtn = findViewById(R.id.dueno_carga_btn);

        transportistaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(RolConductor.class);
            }
        });

        duenoCargaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(RolDueno.class);
            }
        });
    }

    private void openMainActivity(Class<?> roleClass) {
        Intent intent = new Intent(this, roleClass);
        startActivity(intent);
    }
}



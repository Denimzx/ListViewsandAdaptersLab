package co.edu.unipiloto.transporteapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RolDueno extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dueno_carga);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    public void abrirFormulario (View view){
        Intent intent = new Intent(this, FormularioPubliCarga.class);
        startActivity(intent);
    }

    public void revisarPublicadas (View view){
        Intent intent = new Intent(this, Publicadas.class);
        startActivity(intent);
    }
    public void verEstados (View view){
        Intent intent = new Intent(this, Estados.class);
        startActivity(intent);
    }

    public void asignarCarga (View view){
        Intent intent = new Intent(this, Asignar.class);
        startActivity(intent);
    }
    public void solicitarUbi (View view){
        Intent intent = new Intent(this, SolicitarUbicacion.class);
        startActivity(intent);
    }





}

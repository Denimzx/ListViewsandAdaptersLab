package co.edu.unipiloto.transporteapp;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RolConductor extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);
    }
    public void revisarPublicaciones (View view){
        Intent intent = new Intent(this, RevisarPublicacionesActivity.class);
        startActivity(intent);
    }
    public void aplicarSolicitud (View view){
        Intent intent = new Intent(this, AplicarSolicitud.class);
        startActivity(intent);
    }
    public void enviarUbicacion (View view){
        Intent intent = new Intent(this, EnviarUbicacion.class);
        startActivity(intent);
    }
    public void verCargas (View view){
        Intent intent = new Intent(this, Cargas.class);
        startActivity(intent);
    }

}

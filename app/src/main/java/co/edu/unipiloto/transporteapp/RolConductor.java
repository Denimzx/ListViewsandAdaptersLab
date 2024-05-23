package co.edu.unipiloto.transporteapp;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RolConductor extends AppCompatActivity {

    private EditText edtIdCarga;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        // Inicializar EditText
        edtIdCarga = findViewById(R.id.edtIdCarga);
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

    public void verRuta(View view) {
        // Obtener el ID de carga ingresado por el usuario
        String idCargaStr = edtIdCarga.getText().toString().trim();

        if (!idCargaStr.isEmpty()) {
            // Convertir el ID de carga a entero
            int idCarga = Integer.parseInt(idCargaStr);

            // Crear un intent para iniciar ActivityRuta y pasar el ID de carga como extra
            Intent intent = new Intent(this, ActivityRuta.class);
            intent.putExtra("idCarga", idCarga);
            startActivity(intent);
        } else {
            // Mostrar un mensaje de error si el campo está vacío
            edtIdCarga.setError("Ingrese un ID de carga");
        }
    }
}

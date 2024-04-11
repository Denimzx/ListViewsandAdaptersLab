package co.edu.unipiloto.transporteapp;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

        public void abrirFormulario (View view){
            Intent intent = new Intent(this, FormularioPubliCarga.class);
            startActivity(intent);
        }



}


package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class FormularioPubliCarga extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_publi_carga);
        dbHelper = new DatabaseHelper(this);
    }

    public void publicarInformacion(View view) {
        EditText ubicacionEditText = findViewById(R.id.editUbicacion);
        EditText nombreCargaEditText = findViewById(R.id.editNombreCarga);
        EditText precioEditText = findViewById(R.id.editPrecio);
        EditText correoEditText = findViewById(R.id.editCorreo);
        EditText destinoEditText = findViewById(R.id.editDestino);
        EditText numeroEditText = findViewById(R.id.editNumero);


        String ubicacion = ubicacionEditText.getText().toString();
        String nombreCarga = nombreCargaEditText.getText().toString();
        String precio = precioEditText.getText().toString();
        String correo = correoEditText.getText().toString();
        String destino = destinoEditText.getText().toString();
        String numero = numeroEditText.getText().toString();


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_UBICACION, ubicacion);
        values.put(DatabaseHelper.COLUMN_NOMBRE_CARGA, nombreCarga);
        values.put(DatabaseHelper.COLUMN_PRECIO, precio);
        values.put(DatabaseHelper.COLUMN_CORREO, correo);
        values.put(DatabaseHelper.COLUMN_DESTINO, destino);
        values.put(DatabaseHelper.COLUMN_NUMERO, numero);
        values.put(DatabaseHelper.COLUMN_ESTADO, "No asignado");
        values.put(DatabaseHelper.COLUMN_OBSERVACION, "Ninguna");
        values.put(DatabaseHelper.COLUMN_MENSAJE, "Ninguno");
        values.put(DatabaseHelper.COLUMN_CONDUCTOR, "Ninguno");


        db.insert(DatabaseHelper.TABLE_PUBLICACIONES, null, values);

        dbHelper.close();

        Intent intent = new Intent(this, RolDueno.class);
        startActivity(intent);

        finish();
    }
}
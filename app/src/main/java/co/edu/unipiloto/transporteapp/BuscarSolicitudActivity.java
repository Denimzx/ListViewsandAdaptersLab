package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class BuscarSolicitudActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_solicitud);
        dbHelper = new DatabaseHelper(this);
    }

    public void buscarPublicacion(View view) {
        EditText idEditText = findViewById(R.id.editId);


        int id = Integer.parseInt(idEditText.getText().toString());

        // Buscar la publicación por su ID
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_UBICACION,
                DatabaseHelper.COLUMN_NOMBRE_CARGA,
                DatabaseHelper.COLUMN_PRECIO
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PUBLICACIONES,
                projection,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UBICACION));
            String nombreCarga = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE_CARGA));
            String precio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRECIO));

            mostrarDialogo(ubicacion, nombreCarga, precio, id);
        } else {
            mostrarDialogo("Error", "Publicación no encontrada.", "", Integer.parseInt("id"));
        }

        cursor.close();
        db.close();
    }
    private void mostrarDialogo(String titulo, String mensaje, String informacion, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(mensaje + "\n\n" + informacion)
                .setPositiveButton("Aceptar", (dialog, which) -> aceptarSolicitud(id))
                .show();
    }
    private void aceptarSolicitud(int id) {
        mostrarMensaje("Solicitud Aceptada");
    }
    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setPositiveButton("Aceptar", null)
                .show();
    }
}
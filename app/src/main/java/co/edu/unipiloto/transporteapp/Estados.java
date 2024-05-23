package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Estados extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        verificarCargasPendientes(); // Verificar cargas pendientes al iniciar la actividad

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        verEstado(); // Mostrar el estado inicial de las publicaciones
    }

    private void verificarCargasPendientes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Obtener el correo electrónico guardado en SharedPreferences
        String userEmail = sharedPreferences.getString("email", "");

        String[] projection = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_OBSERVACION};
        String selection = DatabaseHelper.COLUMN_CORREO + "=? AND " + DatabaseHelper.COLUMN_OBSERVACION + "=?";
        String[] selectionArgs = {userEmail, "pendiente confirmación entrega"};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PUBLICACIONES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Si hay cargas pendientes, mostrar un diálogo de confirmación
            int idCarga = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            mostrarDialogoConfirmacionEntrega(idCarga);
        }

        cursor.close();
        db.close();
    }

    private void mostrarDialogoConfirmacionEntrega(final int idCarga) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar entrega de carga");
        builder.setMessage("Se ha enviado una confirmación de entrega para la carga " + idCarga + ". ¿Verifica su entrega y confirma?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Actualizar la observación de la carga a "se ha enviado una confirmación de entrega"
                if (actualizarObservacion(idCarga, "se ha enviado una confirmación de entrega")) {
                    Toast.makeText(Estados.this, "Observación actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Estados.this, "Error al actualizar la observación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setCancelable(false); // Impide que el diálogo se cierre al presionar fuera de él

        builder.show();
    }

    private boolean actualizarObservacion(int idCarga, String observacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OBSERVACION, observacion);

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(idCarga)};

        int rowsAffected = db.update(DatabaseHelper.TABLE_PUBLICACIONES, values, selection, selectionArgs);

        db.close();

        return rowsAffected > 0;
    }

    private void verEstado() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_UBICACION,
                DatabaseHelper.COLUMN_NOMBRE_CARGA,
                DatabaseHelper.COLUMN_PRECIO,
                DatabaseHelper.COLUMN_CORREO,
                DatabaseHelper.COLUMN_DESTINO,
                DatabaseHelper.COLUMN_NUMERO,
                DatabaseHelper.COLUMN_ESTADO,
                DatabaseHelper.COLUMN_OBSERVACION,
                DatabaseHelper.COLUMN_MENSAJE,
                DatabaseHelper.COLUMN_CONDUCTOR
        };

        // Obtener el correo electrónico guardado en SharedPreferences
        String userEmail = sharedPreferences.getString("email", "");

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PUBLICACIONES,
                projection,
                DatabaseHelper.COLUMN_CORREO + "=?",
                new String[]{userEmail}, // Filtrar publicaciones por correo electrónico del usuario actual
                null,
                null,
                null
        );

        StringBuilder result = new StringBuilder();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UBICACION));
            String nombreCarga = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE_CARGA));
            String precio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRECIO));
            String correo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORREO));
            String destino = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINO));
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NUMERO));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ESTADO));
            String observacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OBSERVACION));
            String mensaje = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MENSAJE));
            String conductor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONDUCTOR));

            result.append("ID: ").append(id).append("\n");
            result.append("Ubicación: ").append(ubicacion).append("\n");
            result.append("Nombre de la carga: ").append(nombreCarga).append("\n");
            result.append("Precio: ").append(precio).append("\n");
            result.append("Correo: ").append(correo).append("\n");
            result.append("Destino: ").append(destino).append("\n");
            result.append("Numero: ").append(numero).append("\n");
            result.append("Estado: ").append(estado).append("\n");
            result.append("Observacion: ").append(observacion).append("\n");
            result.append("Mensaje: ").append(mensaje).append("\n");
            result.append("Conductor: ").append(conductor).append("\n\n");
        }

        cursor.close();
        db.close();

        TextView publicacionesTextView = findViewById(R.id.textPublicaciones);
        publicacionesTextView.setText(result.toString());
    }
}

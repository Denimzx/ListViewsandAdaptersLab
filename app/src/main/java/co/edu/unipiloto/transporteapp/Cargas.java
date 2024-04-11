package co.edu.unipiloto.transporteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Cargas extends AppCompatActivity {

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cargas);
        dbHelper = new DatabaseHelper(this);

        mostrarPublicaciones();
    }

    private void mostrarPublicaciones() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_UBICACION,
                DatabaseHelper.COLUMN_NOMBRE_CARGA,
                DatabaseHelper.COLUMN_PRECIO,
                DatabaseHelper.COLUMN_CORREO,
                DatabaseHelper.COLUMN_DESTINO,
                DatabaseHelper.COLUMN_NUMERO,
                DatabaseHelper.COLUMN_OBSERVACION,
                DatabaseHelper.COLUMN_MENSAJE
        };

        String selection = DatabaseHelper.COLUMN_ESTADO + "=? AND " + DatabaseHelper.COLUMN_CONDUCTOR + "=?";
        String[] selectionArgs = new String[]{"Asignado", email};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PUBLICACIONES,
                projection,
                selection,
                selectionArgs,
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
            String destino = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINO));
            String observacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OBSERVACION));
            String mensaje = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MENSAJE));

            result.append("ID: ").append(id).append("\n");
            result.append("Ubicación: ").append(ubicacion).append("\n");
            result.append("Nombre de la carga: ").append(nombreCarga).append("\n");
            result.append("Precio: ").append(precio).append("\n");
            result.append("Destino: ").append(destino).append("\n");
            result.append("Observación: ").append(observacion).append("\n");
            result.append("Mensaje: ").append(mensaje).append("\n");
        }

        cursor.close();
        db.close();

        TextView publicacionesTextView = findViewById(R.id.textPublicaciones);
        publicacionesTextView.setText(result.toString());
    }
}

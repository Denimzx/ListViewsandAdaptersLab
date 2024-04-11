package co.edu.unipiloto.transporteapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class Publicadas extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicadas);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        mostrarPublicaciones();
    }
    public void eliminarPublicada(View view) {
        EditText idEliminarEditText = findViewById(R.id.editEliminarId);
        String idEliminarString = idEliminarEditText.getText().toString();

        if (idEliminarString.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID de publicación para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        int idEliminar = Integer.parseInt(idEliminarString);

        // Obtener el correo electrónico guardado en SharedPreferences
        String userEmail = sharedPreferences.getString("email", "");

        // Crear una condición para incluir el correo electrónico del usuario en la eliminación
        String whereClause = DatabaseHelper.COLUMN_ID + "=? AND " + DatabaseHelper.COLUMN_CORREO + "=?";
        String[] whereArgs = new String[]{String.valueOf(idEliminar), userEmail};

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filasEliminadas = db.delete(DatabaseHelper.TABLE_PUBLICACIONES, whereClause, whereArgs);
        db.close();

        if (filasEliminadas > 0) {
            Toast.makeText(this, "Publicación eliminada exitosamente", Toast.LENGTH_SHORT).show();
            mostrarPublicaciones(); // Actualizar la lista de publicaciones después de la eliminación
        } else {
            Toast.makeText(this, "No se encontró ninguna publicación con el ID proporcionado para el usuario actual", Toast.LENGTH_SHORT).show();
        }
    }


    private void mostrarPublicaciones() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_UBICACION,
                DatabaseHelper.COLUMN_NOMBRE_CARGA,
                DatabaseHelper.COLUMN_PRECIO,
                DatabaseHelper.COLUMN_CORREO,
                DatabaseHelper.COLUMN_DESTINO,
                DatabaseHelper.COLUMN_NUMERO
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

            result.append("ID: ").append(id).append("\n");
            result.append("Ubicación: ").append(ubicacion).append("\n");
            result.append("Nombre de la carga: ").append(nombreCarga).append("\n");
            result.append("Precio: ").append(precio).append("\n");
            result.append("Correo: ").append(correo).append("\n");
            result.append("Destino: ").append(destino).append("\n");
            result.append("Numero: ").append(numero).append("\n\n");
        }

        cursor.close();
        db.close();

        TextView publicacionesTextView = findViewById(R.id.textPublicaciones);
        publicacionesTextView.setText(result.toString());

    }
}

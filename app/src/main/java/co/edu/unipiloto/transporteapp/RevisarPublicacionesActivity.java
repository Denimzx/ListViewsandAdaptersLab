package co.edu.unipiloto.transporteapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RevisarPublicacionesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados);
        dbHelper = new DatabaseHelper(this);

        mostrarPublicaciones();
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

        // Agregar una cláusula WHERE para filtrar las publicaciones por estado "No asignado"
        String selection = DatabaseHelper.COLUMN_ESTADO + "=?";
        String[] selectionArgs = new String[]{"No asignado"};

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
            //String correo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORREO));
            String destino = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINO));
            //String numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NUMERO));

            result.append("ID: ").append(id).append("\n");
            result.append("Ubicación: ").append(ubicacion).append("\n");
            result.append("Nombre de la carga: ").append(nombreCarga).append("\n");
            result.append("Precio: ").append(precio).append("\n");
            //result.append("Correo: ").append(correo).append("\n");
            result.append("Destino: ").append(destino).append("\n");
            //result.append("Numero: ").append(numero).append("\n\n");
        }

        cursor.close();
        db.close();

        TextView publicacionesTextView = findViewById(R.id.textPublicaciones);
        publicacionesTextView.setText(result.toString());
    }

}
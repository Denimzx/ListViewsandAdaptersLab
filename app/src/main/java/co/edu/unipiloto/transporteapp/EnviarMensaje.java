package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EnviarMensaje extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisar_publicaciones);
        dbHelper = new DatabaseHelper(this);
    }
    public void enviarMsj(View view) {
        enviarMensaje(view);
    }


    public void enviarMensaje(View view) {
        EditText editTextId = findViewById(R.id.editId);
        EditText editTextMensaje = findViewById(R.id.editMensaje);

        String id = editTextId.getText().toString();
        String mensaje = editTextMensaje.getText().toString();

        // Verificar si se ingresó un ID y un mensaje
        if (!id.isEmpty() && !mensaje.isEmpty()) {
            // Actualizar el mensaje de la publicación en la base de datos
            int idPublicacion = Integer.parseInt(id);
            if (actualizarMensaje(idPublicacion, mensaje)) {
                Toast.makeText(this, "Mensaje actualizado para la publicación con ID " + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró ninguna publicación con el ID especificado", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Mostrar un mensaje de error si falta ingresar algún campo
            Toast.makeText(this, "Por favor ingrese el ID y el mensaje", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean actualizarMensaje(int idPublicacion, String mensaje) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MENSAJE, mensaje);

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(idPublicacion)};

        int rowsAffected = db.update(DatabaseHelper.TABLE_PUBLICACIONES, values, selection, selectionArgs);

        db.close();

        return rowsAffected > 0;
    }
}


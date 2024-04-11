package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.database.Cursor;

public class Asignar extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
    }

    public void asignarCarga(View view) {
        // Obtener el correo electrónico del usuario almacenado en SharedPreferences
        String userEmail = sharedPreferences.getString("email", "");

        // Obtener el ID de la publicación ingresado por el usuario
        EditText idEditText = findViewById(R.id.editId);
        int idPublicacion = Integer.parseInt(idEditText.getText().toString());

        // Verificar si hay una publicación con el ID proporcionado y el mismo correo electrónico en la base de datos
        if (!existePublicacion(idPublicacion, userEmail)) {
            Toast.makeText(this, "No se encontró ninguna publicación con el ID proporcionado o no pertenece al usuario actual", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el correo electrónico del conductor ingresado por el usuario
        EditText conductorEditText = findViewById(R.id.editConductor);
        String correoConductor = conductorEditText.getText().toString();

        // Actualizar la base de datos para cambiar el estado de la publicación a "Asignado" y asignar el conductor
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ESTADO, "Asignado");
        values.put(DatabaseHelper.COLUMN_CONDUCTOR, correoConductor);

        int filasActualizadas = db.update(DatabaseHelper.TABLE_PUBLICACIONES, values,
                DatabaseHelper.COLUMN_ID + "=? AND " + DatabaseHelper.COLUMN_ESTADO + "=?",
                new String[]{String.valueOf(idPublicacion), "No asignado"});

        db.close();

        if (filasActualizadas > 0) {
            Toast.makeText(this, "Carga asignada exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró ninguna publicación con el ID proporcionado o la carga ya está asignada", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean existePublicacion(int idPublicacion, String correoUsuario) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PUBLICACIONES +
                " WHERE " + DatabaseHelper.COLUMN_ID + " = ? AND " +
                DatabaseHelper.COLUMN_CORREO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idPublicacion), correoUsuario});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }
}

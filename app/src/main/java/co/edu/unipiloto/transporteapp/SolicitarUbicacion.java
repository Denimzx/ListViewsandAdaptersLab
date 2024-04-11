package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;


public class SolicitarUbicacion extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editTextIdPublicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_ubicacion);

        // Inicializar el helper de la base de datos
        dbHelper = new DatabaseHelper(this);

        // Referencia al EditText del ID de la publicación
        editTextIdPublicacion = findViewById(R.id.editIdPublicacion);
    }

    public void solicitarUbicacion(View view) {
        // Obtener el ID de la publicación desde el EditText
        String idPublicacion = editTextIdPublicacion.getText().toString().trim();

        // Verificar si el ID de la publicación no está vacío
        if (!idPublicacion.isEmpty()) {
            // Obtener una instancia de la base de datos en modo escritura
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Crear un objeto ContentValues para almacenar los valores a actualizar
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_OBSERVACION, "Se ha solicitado la ubicación");

            // Crear la cláusula WHERE para especificar la publicación a actualizar
            String selection = DatabaseHelper.COLUMN_ID + "=?";
            String[] selectionArgs = {idPublicacion};

            // Actualizar la publicación en la base de datos
            int rowsAffected = db.update(DatabaseHelper.TABLE_PUBLICACIONES, values, selection, selectionArgs);

            // Cerrar la conexión a la base de datos
            db.close();

            // Verificar si se actualizó correctamente al menos una fila
            if (rowsAffected > 0) {
                Toast.makeText(this, "Se ha solicitado la ubicación", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró la publicación con el ID especificado", Toast.LENGTH_SHORT).show();
            }

        } else {
             Toast.makeText(this, "Ingrese el ID de la publicación", Toast.LENGTH_SHORT).show();
        }
    }
}

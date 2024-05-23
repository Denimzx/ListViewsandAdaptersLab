package co.edu.unipiloto.transporteapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Cargas extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "DeliveryNotificationChannel";

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cargas);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        mostrarPublicaciones();
    }

    public void enviarMensajeCon(View view) {
        EditText editTextId = findViewById(R.id.editIdMensajeCon);
        EditText editTextMensaje = findViewById(R.id.editMensajeCon);

        String id = editTextId.getText().toString();
        String mensaje = editTextMensaje.getText().toString();

        // Verificar si se ingresó un ID y un mensaje
        if (!id.isEmpty() && !mensaje.isEmpty()) {
            int idPublicacion = Integer.parseInt(id);
            if (actualizarMensaje(idPublicacion, mensaje)) {
                // Cambiar la observación en la base de datos
                if (cambiarObservacion(idPublicacion, "El conductor ha enviado un mensaje")) {
                    Toast.makeText(this, "Mensaje enviado y observación actualizada para la publicación con ID " + id, Toast.LENGTH_SHORT).show();
                    editTextId.setText("");
                    editTextMensaje.setText("");
                    recreate();
                } else {
                    Toast.makeText(this, "Error al actualizar la observación", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No se encontró ninguna publicación con el ID especificado", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Mostrar un mensaje de error si falta ingresar algún campo
            Toast.makeText(this, "Por favor ingrese el ID y el mensaje", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean cambiarObservacion(int idPublicacion, String observacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OBSERVACION, observacion);

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(idPublicacion)};

        int rowsAffected = db.update(DatabaseHelper.TABLE_PUBLICACIONES, values, selection, selectionArgs);

        db.close();

        return rowsAffected > 0;
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
    public void entregar(View view) {
        EditText editTextIdCarga = findViewById(R.id.editIdCargacon);
        String idCargaStr = editTextIdCarga.getText().toString();

        if (!idCargaStr.isEmpty()) {
            int idCarga = Integer.parseInt(idCargaStr);

            // Mostrar un diálogo de confirmación
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar entrega de carga");
            builder.setMessage("¿Estás seguro de que quieres confirmar la entrega de la carga " + idCarga + "?");

            builder.setPositiveButton("Sí", (dialog, which) -> {
                // Actualizar el estado en la base de datos
                if (actualizarEstadoEntrega(idCarga)) {
                    // Notificar la entrega
                    notificarEntrega(idCarga);
                    Toast.makeText(Cargas.this, "Entrega confirmada para la carga " + idCarga, Toast.LENGTH_SHORT).show();
                    editTextIdCarga.setText("");
                    recreate(); // Recargar la actividad para reflejar los cambios
                } else {
                    Toast.makeText(Cargas.this, "Error al confirmar la entrega", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("No", null);
            builder.show();
        } else {
            Toast.makeText(this, "Por favor ingrese el ID de la carga", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean actualizarEstadoEntrega(int idCarga) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OBSERVACION, "pendiente confirmación entrega");

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(idCarga)};

        int rowsAffected = db.update(DatabaseHelper.TABLE_PUBLICACIONES, values, selection, selectionArgs);

        db.close();

        return rowsAffected > 0;
    }

    private void notificarEntrega(int idCarga) {
        // Crear un Intent para abrir la actividad cuando se toque la notificación
        Intent intent = new Intent(this, Cargas.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Reemplaza con tu propio icono
                .setContentTitle("Entrega de Carga")
                .setContentText("Se ha marcado como entregada la carga " + idCarga)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Establecer el intent pendiente
                .setAutoCancel(true);

        // Obtener el servicio NotificationManager y mostrar la notificación
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }




}

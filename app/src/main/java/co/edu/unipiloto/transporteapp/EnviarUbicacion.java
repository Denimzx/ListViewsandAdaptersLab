package co.edu.unipiloto.transporteapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.content.Context;
import android.location.LocationManager;
import android.location.Location;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import android.content.ContentValues;






public class EnviarUbicacion extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private static final String TAG = "EnviarUbicacion";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    private String[] obtenerUbicacion() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitud = location.getLatitude();
                    double longitud = location.getLongitude();
                    return new String[]{String.valueOf(latitud), String.valueOf(longitud)};
                }
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Error al obtener la ubicación: " + e.getMessage());
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_ubicacion);
        dbHelper = new DatabaseHelper(this);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de ubicación otorgado, puedes acceder a la ubicación
                obtenerUbicacion();
            } else {
                // Permiso de ubicación denegado, manejar la situación adecuadamente
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void enviarCorreo(View view) {
        EditText editTextIdPublicacion = findViewById(R.id.editTextIdPublicacion);
        String idPublicacion = editTextIdPublicacion.getText().toString();

        if (idPublicacion.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID de publicación", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idPublicacion);

        // Buscar el correo electrónico asociado al ID de la publicación
        String correo = obtenerCorreoPorId(id);

        if (correo != null) {
            // Actualizar la observación de la publicación
            boolean observacionActualizada = actualizarObservacion(id, "Se ha enviado la ubicación");
            if (observacionActualizada) {
                // Enviar correo electrónico en un hilo secundario
                new EnviarCorreoTask().execute(correo);
                Toast.makeText(EnviarUbicacion.this, "Se ha enviado la ubicación", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo actualizar la observación de la publicación", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se encontró ningún correo asociado al ID de la publicación", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean actualizarObservacion(int id, String nuevaObservacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OBSERVACION, nuevaObservacion);

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        int rowsAffected = db.update(
                DatabaseHelper.TABLE_PUBLICACIONES,
                values,
                selection,
                selectionArgs);

        db.close();

        return rowsAffected > 0;
    }


    private String obtenerCorreoPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_CORREO};
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PUBLICACIONES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String correo = null;
        if (cursor.moveToFirst()) {
            correo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORREO));
        }

        cursor.close();
        db.close();

        return correo;
    }

    private class EnviarCorreoTask extends AsyncTask<String, Void, Boolean> {

        private String ubicacionLink;

        @Override
        protected Boolean doInBackground(String... params) {
            String correo = params[0];
            String[] coordenadas = obtenerUbicacion();
            if (coordenadas != null) {
                ubicacionLink = "http://maps.google.com/maps?q=" + coordenadas[0] + "," + coordenadas[1];
            } else {
                ubicacionLink = null;
            }

            // Resto del código para enviar el correo electrónico...
            if (ubicacionLink != null) {
                return enviarCorreoElectronico(correo, ubicacionLink);
            } else {
                // Si no se pudo obtener la ubicación, enviar el correo sin el enlace
                return enviarCorreoElectronico(correo, null);
            }
        }



        private boolean enviarCorreoElectronico(String correo, String ubicacionLink) {
            // Configuración del servidor de correo saliente (SMTP)
            final String username = "sebastiansalazar5108@gmail.com"; // Reemplaza con tu dirección de correo
            final String password = "dmds oyrz weni jdyb"; // Reemplaza con tu contraseña de correo

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
            props.put("mail.smtp.port", "587"); // Puerto SMTP de Gmail

            // Crear una nueva sesión de correo electrónico con autenticación
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                // Crear un nuevo mensaje de correo electrónico
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username)); // Establecer el remitente

                // Establecer el destinatario
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));

                // Establecer el asunto y el cuerpo del correo electrónico
                message.setSubject("Ubicación Actual");
                if (ubicacionLink != null) {
                    message.setText("Haz clic en el siguiente enlace para ver mi ubicación actual: " + ubicacionLink);
                } else {
                    message.setText("No se pudo obtener la ubicación actual.");
                }

                // Enviar el mensaje de correo electrónico
                Transport.send(message);

                return true;
            } catch (MessagingException e) {
                Log.e(TAG, "Error al enviar el correo electrónico: " + e.getMessage());
                return false;
            }
        }



    }

}

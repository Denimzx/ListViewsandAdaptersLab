package co.edu.unipiloto.transporteapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AplicarSolicitud extends AppCompatActivity {

    private EditText editIdPublicacion, editNombreCompleto, editExperiencia, editCorreo, editIdentificacion, editNumeroCel, editDireccion, editAdicionalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicar_solicitud);

        // Inicializar vistas
        editIdPublicacion = findViewById(R.id.editIdPublicacion);
        editNombreCompleto = findViewById(R.id.editNombreCompleto);
        editExperiencia = findViewById(R.id.editExperiencia);
        editCorreo = findViewById(R.id.editCorreo);
        editIdentificacion = findViewById(R.id.editIdentificacion);
        editNumeroCel = findViewById(R.id.editNumeroCel);
        editDireccion = findViewById(R.id.editDireccion);
        editAdicionalInfo = findViewById(R.id.editAdicionalInfo);
    }

    public void aplicar(View view) {
        // Obtener los datos ingresados en el formulario
        String idPublicacion = editIdPublicacion.getText().toString();
        String nombreCompleto = editNombreCompleto.getText().toString();
        String experiencia = editExperiencia.getText().toString();
        String correo = editCorreo.getText().toString();
        String identificacion = editIdentificacion.getText().toString();
        String numeroCel = editNumeroCel.getText().toString();
        String direccion = editDireccion.getText().toString();
        String adicionalInfo = editAdicionalInfo.getText().toString();

        // Validar campos obligatorios
        if (idPublicacion.isEmpty() || nombreCompleto.isEmpty() || experiencia.isEmpty() || correo.isEmpty() || identificacion.isEmpty() || numeroCel.isEmpty() || direccion.isEmpty() || adicionalInfo.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enviar correo electrónico
        new SendMailTask().execute(idPublicacion, nombreCompleto, experiencia, correo, identificacion, numeroCel, direccion, adicionalInfo);
    }

    private class SendMailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // Configurar propiedades para la conexión SMTP
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Configurar la sesión
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication("sebastiansalazar5108@gmail.com", "dmds oyrz weni jdyb");
                }
            });

            try {
                // Crear un nuevo mensaje de correo
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("tu_correo@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params[3]));
                message.setSubject("Han aplicado a tu publicación");
                message.setText("Han aplicado a tu publicación con los siguientes detalles:\n\n" +
                        "ID de publicación: " + params[0] + "\n" +
                        "Nombre completo: " + params[1] + "\n" +
                        "Experiencia: " + params[2] + "\n" +
                        "Correo: " + params[3] + "\n" +
                        "Identificación: " + params[4] + "\n" +
                        "Número celular: " + params[5] + "\n" +
                        "Dirección: " + params[6] + "\n" +
                        "Información adicional: " + params[7] + "\n\n" +
                        "Recuerda que si quieres aceptar a este conductor debes asignarlo en la aplicación con el correo que te propocionó " +
                        "en este caso asigna tu carga al correo " + params[3] + " asegúrate de no dejar ningún espacio a lahora de asignarlo " +
                        "y copiarlo bien y exactamente como está" );

                // Enviar el mensaje
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(AplicarSolicitud.this, "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}


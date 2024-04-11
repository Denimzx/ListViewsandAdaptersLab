package co.edu.unipiloto.transporteapp;

import static co.edu.unipiloto.transporteapp.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignUp;

    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_sign_up);

        etEmail = findViewById(id.etEmail);
        etPassword = findViewById(id.etPassword);
        btnSignUp = findViewById(id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SignUpActivity", "Botón de registro clickeado");
                signUpUser(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
    }


    private void signUpUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(signupActivity.this, loginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(signupActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

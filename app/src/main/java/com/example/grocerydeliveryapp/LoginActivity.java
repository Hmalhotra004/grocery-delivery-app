package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

  public EditText etEmail, etPassword;
  public Button btnLogin;
  public TextView tvSignUp;

  private FirebaseAuth firebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    firebaseAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    if (currentUser != null) {
      startActivity(new Intent(LoginActivity.this, MainActivity.class));
      finish();
    }

    etEmail = findViewById(R.id.et_email);
    etPassword = findViewById(R.id.et_password);
    btnLogin = findViewById(R.id.btn_login);
    tvSignUp = findViewById(R.id.tv_signup);

    btnLogin.setOnClickListener(v -> login());
    tvSignUp.setOnClickListener(v -> openSignUpPage());
  }

  private void login() {
    String email = etEmail.getText().toString().trim();
    String password = etPassword.getText().toString().trim();

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
      return;
    }

    firebaseAuth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, task -> {
        if (task.isSuccessful()) {
          Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(LoginActivity.this, MainActivity.class));
          finish();
        } else {
          Toast.makeText(LoginActivity.this, "Invalid Credentials.", Toast.LENGTH_SHORT).show();
        }
      });
  }

  private void openSignUpPage() {
    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
    startActivity(intent);
  }
}

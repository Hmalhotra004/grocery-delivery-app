package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

  public EditText etEmail, etPassword, etConfirmPassword,etName;
  public Button btnSignUp;
  public TextView tvLogin;

  private FirebaseAuth firebaseAuth;
  private FirebaseFirestore db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    firebaseAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    etName = findViewById(R.id.et_name);
    etEmail = findViewById(R.id.et_email);
    etPassword = findViewById(R.id.et_password);
    etConfirmPassword = findViewById(R.id.et_confirm_password);
    btnSignUp = findViewById(R.id.btn_signup);
    tvLogin = findViewById(R.id.tv_login);

    btnSignUp.setOnClickListener(v -> signUp());
    tvLogin.setOnClickListener(v -> openLoginPage());
  }

  private void signUp() {
    String name = etName.getText().toString().trim();
    String email = etEmail.getText().toString().trim();
    String password = etPassword.getText().toString().trim();
    String confirmPassword = etConfirmPassword.getText().toString().trim();

    if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
      Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
      return;
    }

    if (!password.equals(confirmPassword)) {
      Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
      return;
    }

    firebaseAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, task -> {
        if (task.isSuccessful()) {
          String userId = firebaseAuth.getCurrentUser().getUid();

          // Save user data to Firestore
          Map<String, Object> user = new HashMap<>();
          user.put("name",name);
          user.put("email", email);
          user.put("userId", userId);

          db.collection("users").document(userId).set(user)
            .addOnSuccessListener(aVoid -> {
              Toast.makeText(SignupActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(SignupActivity.this, MainActivity.class));
              finish();
            })
            .addOnFailureListener(e -> {
              Toast.makeText(SignupActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            });

        } else {
          Toast.makeText(SignupActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }
      });
  }

  private void openLoginPage() {
    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
    startActivity(intent);
  }
}

package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

public class ChangePasswordActivity extends AppCompatActivity {

  public Button save;
  public EditText old, newP, confirmP;
  public FirebaseAuth firebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_change_password);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    save = findViewById(R.id.saveBtnPass);
    old = findViewById(R.id.oldPass);
    newP = findViewById(R.id.newPass);
    confirmP = findViewById(R.id.conPass);

    firebaseAuth = FirebaseAuth.getInstance();

    save.setOnClickListener(v -> {
      String oldPassword = old.getText().toString().trim();
      String newPassword = newP.getText().toString().trim();
      String confirmPassword = confirmP.getText().toString().trim();

      if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
        Toast.makeText(ChangePasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
        return;
      }

      if (!newPassword.equals(confirmPassword)) {
        Toast.makeText(ChangePasswordActivity.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
        return;
      }

      FirebaseUser user = firebaseAuth.getCurrentUser();
      if (user != null && user.getEmail() != null) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

        // Re-authenticate the user
        user.reauthenticate(credential).addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            // Update the password
            user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
              if (updateTask.isSuccessful()) {
                Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
              } else {
                Toast.makeText(ChangePasswordActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                Log.e("ChangePassword", "Error updating password", updateTask.getException());
              }
            });
          } else {
            Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
            Log.e("ChangePassword", "Re-authentication failed", task.getException());
          }
        });
      } else {
        Toast.makeText(ChangePasswordActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
      }
    });
  }
}

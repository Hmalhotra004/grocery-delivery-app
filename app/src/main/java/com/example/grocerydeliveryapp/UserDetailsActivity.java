package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {

  private Button save;
  private EditText name, phone;
  private FirebaseAuth firebaseAuth;
  private FirebaseFirestore firestore;
  private LinearLayout userDetailsForm;
  private ProgressBar userDetailsLoader;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_user_details);

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    firebaseAuth = FirebaseAuth.getInstance();
    firestore = FirebaseFirestore.getInstance();

    save = findViewById(R.id.saveBtnD);
    phone = findViewById(R.id.phoneD);
    name = findViewById(R.id.nameD);
    userDetailsForm = findViewById(R.id.userDetailsForm);
    userDetailsLoader = findViewById(R.id.userDetailsLoader);

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    if (currentUser != null) {
      String userId = currentUser.getUid();

      DocumentReference userRef = firestore.collection("users").document(userId);

      userRef.get().addOnSuccessListener(documentSnapshot -> {
        if (documentSnapshot.exists()) {
          String userName = documentSnapshot.getString("name");
          String userPhone = documentSnapshot.getString("phone");

          if (userName != null) {
            name.setText(userName);
          }
          if (userPhone != null) {
            phone.setText(userPhone);
          }

          // Show form and hide loader
          showLoading(false);
        } else {
          Log.e("UserDetails", "Document does not exist");
          showLoading(false);
        }
      }).addOnFailureListener(e -> {
        Log.e("UserDetails", "Error retrieving document", e);
        showLoading(false);
      });

      save.setOnClickListener(v -> {
        String updatedName = name.getText().toString().trim();
        String updatedPhone = phone.getText().toString().trim();

        if (updatedName.isEmpty() || updatedPhone.isEmpty()) {
          Toast.makeText(UserDetailsActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
          return;
        }

        if (!updatedPhone.matches("\\d{10}")) {
          Toast.makeText(UserDetailsActivity.this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
          return;
        }

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", updatedName);
        updatedData.put("phone", updatedPhone);

        userRef.update(updatedData)
          .addOnSuccessListener(aVoid -> Toast.makeText(UserDetailsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
          .addOnFailureListener(e -> {
            Log.e("UserDetails", "Error updating document", e);
            Toast.makeText(UserDetailsActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
          });
      });
    }
  }

  private void showLoading(boolean isLoading) {
    userDetailsLoader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    userDetailsForm.setVisibility(isLoading ? View.GONE : View.VISIBLE);
  }
}

package com.example.grocerydeliveryapp;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPhoneActivity extends AppCompatActivity {

  public EditText phone;
  public Button save;
  public FirebaseAuth firebaseAuth;
  public FirebaseFirestore firestore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_add_phone);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    save = (Button) findViewById(R.id.addPhoneBtn);
    phone = (EditText) findViewById(R.id.phoneAddEt);

    // Initialize FirebaseAuth and Firestore
    firebaseAuth = FirebaseAuth.getInstance();
    firestore = FirebaseFirestore.getInstance();

    save.setOnClickListener(v -> {
      String phoneNumber = phone.getText().toString().trim();

      // Validate phone number
      if (phoneNumber.isEmpty()) {
        Toast.makeText(AddPhoneActivity.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
        return;
      }
      if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d{10}")) {
        Toast.makeText(AddPhoneActivity.this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
        return;
      }

      // Get the current user's UID
      String userId = firebaseAuth.getCurrentUser().getUid();
      DocumentReference userRef = firestore.collection("users").document(userId);

      // Create a map for the phone number
      Map<String, Object> userData = new HashMap<>();
      userData.put("phone", phoneNumber);

      // Update the Firestore document
      userRef.update(userData)
        .addOnSuccessListener(aVoid -> {
          Toast.makeText(AddPhoneActivity.this, "Phone number added successfully", Toast.LENGTH_SHORT).show();

          // Redirect to the MainActivity
          Intent intent = new Intent(AddPhoneActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        })
        .addOnFailureListener(e -> {
          Log.e("AddPhoneActivity", "Error updating phone number", e);
          Toast.makeText(AddPhoneActivity.this, "Failed to add phone number", Toast.LENGTH_SHORT).show();
        });
    });
  }
}

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {

  public Button save;
  public EditText name, phone;
  public FirebaseAuth firebaseAuth;
  public FirebaseFirestore firestore;

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
        } else {
          Log.e("UserDetails", "Document does not exist");
        }
      }).addOnFailureListener(e -> Log.e("UserDetails", "Error retrieving document", e));

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
          .addOnSuccessListener(aVoid -> {
            Toast.makeText(UserDetailsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
          })
          .addOnFailureListener(e -> {
            Log.e("UserDetails", "Error updating document", e);
            Toast.makeText(UserDetailsActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
          });
      });
    }
  }
}

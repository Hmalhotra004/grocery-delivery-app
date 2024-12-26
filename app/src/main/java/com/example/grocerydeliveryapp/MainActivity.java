package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.grocerydeliveryapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

  public FirebaseAuth firebaseAuth;
  public FirebaseFirestore firestore;

  ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    EdgeToEdge.enable(this);
    setContentView(binding.getRoot());
    replaceFragment(new HomeFragment());

    firebaseAuth = FirebaseAuth.getInstance();
    firestore = FirebaseFirestore.getInstance();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    if (currentUser == null) {
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
      finish();
      return;
    }

    String userId = currentUser.getUid();
    DocumentReference userRef = firestore.collection("users").document(userId);
    userRef.get().addOnSuccessListener(documentSnapshot -> {
      if (documentSnapshot.exists()) {
        String userPhone = documentSnapshot.getString("phone");

        if (userPhone == null || userPhone.isEmpty()) {
          Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
          startActivity(intent);
          finish();
        }
      } else {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
      }
    }).addOnFailureListener(e -> {
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
    });

    binding.bottomNavigationView.setOnItemSelectedListener(item -> {
      int id = item.getItemId();

      if (id == R.id.home) {
        replaceFragment(new HomeFragment());
      } else if (id == R.id.user) {
        replaceFragment(new ProfileFragment());
      } else if (id == R.id.orders) {
        replaceFragment(new ordersFragment());
      } else if (id == R.id.cart) {
        replaceFragment(new CartFragment());
      } else {
        return false;
      }
      return true;
    });

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  private void replaceFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frame_layout, fragment);
    fragmentTransaction.commit();
  }
}

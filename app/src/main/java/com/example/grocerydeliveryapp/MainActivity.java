package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

  public FirebaseAuth firebaseAuth;
  public Button logoutBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);

    logoutBtn = findViewById(R.id.logout_btn);

    firebaseAuth = FirebaseAuth.getInstance();

    if (firebaseAuth.getCurrentUser() == null) {
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
      finish();
    }


    // Handle window insets for edge-to-edge layout
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    logoutBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
      }
    });
  }
}

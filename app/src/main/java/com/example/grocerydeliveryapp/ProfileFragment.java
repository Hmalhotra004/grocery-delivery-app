package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  public FirebaseAuth firebaseAuth;
  public TextView account;
  public Button logout,orders;

  public ProfileFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view =  inflater.inflate(R.layout.fragment_profile, container, false);

    firebaseAuth = FirebaseAuth.getInstance();
    account = (TextView) view.findViewById(R.id.account);
    logout = (Button) view.findViewById(R.id.logoutBtn);
    orders = (Button) view.findViewById(R.id.ordersBtn);

    String email = firebaseAuth.getCurrentUser().getEmail();

    account.setText(email);

    orders.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new ordersFragment());
        fragmentTransaction.commit();

        // Update the bottom navigation selection
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
          mainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.orders);
        }
      }
    });

    logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        if (getActivity() != null) {
          getActivity().finish();
        }
      }
    });

    return view;
  }
}
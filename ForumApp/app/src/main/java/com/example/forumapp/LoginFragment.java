package com.example.forumapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    String TAG = "debug";


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextEmail, editTextPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle("Login");

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);

        view.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onClick: ");
                    new LoginAsync().execute(email, password);

                }

            }
        });

        view.findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromLoginToCreateAccount();
            }
        });


        return view;
    }

    class LoginAsync extends AsyncTask<String, Boolean, com.example.forumapp.DataServices.AuthResponse> {

        @Override
        protected com.example.forumapp.DataServices.AuthResponse doInBackground(String... strings) {
            String email = strings[0];
            String password = strings[1];
            try {
                Log.d(TAG, "doInBackground: " + com.example.forumapp.DataServices.login(email, password));
                return com.example.forumapp.DataServices.login(email, password);
            } catch (com.example.forumapp.DataServices.RequestException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(com.example.forumapp.DataServices.AuthResponse authResponse) {
            super.onPostExecute(authResponse);

            if (authResponse != null) {
                Log.d(TAG, "onPostExecute: True");
                mListener.fromLoginToForum(authResponse);
            }else{
                Toast.makeText(getActivity(), "Email or password is incorrect.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onPostExecute: FALSE");
            }

        }
    }

    LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (LoginListener) context;
    }

    interface LoginListener {
        void fromLoginToForum(com.example.forumapp.DataServices.AuthResponse authResponse);

        void fromLoginToCreateAccount();
    }

}
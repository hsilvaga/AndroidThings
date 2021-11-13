package com.example.forumapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.midtermapp.CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountFragment extends Fragment {

    public CreateAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextName2, editTextEmail2, editTextPassword2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        getActivity().setTitle("Create Account");

        editTextName2 = view.findViewById(R.id.editTextName2);
        editTextEmail2 = view.findViewById(R.id.editTextEmail2);
        editTextPassword2 = view.findViewById(R.id.editTextPassword2);

        view.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName2.getText().toString();
                String email = editTextEmail2.getText().toString();
                String password = editTextPassword2.getText().toString();

                if(name.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Name", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    new RegisterAsync().execute(name, email, password);
                }
            }
        });

        view.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromRegisterToLogin();
            }
        });


        return view;
    }

    class RegisterAsync extends AsyncTask<String, String, com.example.forumapp.DataServices.AuthResponse> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected com.example.forumapp.DataServices.AuthResponse doInBackground(String... strings) {
            String name = strings[0];
            String email = strings[1];
            String password = strings[2];

            try {
                return  com.example.forumapp.DataServices.register(name, email, password);
            } catch (com.example.forumapp.DataServices.RequestException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(com.example.forumapp.DataServices.AuthResponse authResponse) {
            super.onPostExecute(authResponse);
            if(authResponse != null) {
                mListener.fromRegisterToForum(authResponse);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (RegisterListener) context;
    }

    RegisterListener mListener;

    interface RegisterListener{
        void fromRegisterToForum(com.example.forumapp.DataServices.AuthResponse authResponse);
        void fromRegisterToLogin();
    }
}
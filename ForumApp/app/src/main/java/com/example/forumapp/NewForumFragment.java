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

public class NewForumFragment extends Fragment {

    private static final String ARG_PARAM_TOKEN = "ARG_PARAM_TOKEN";

    private String mToken;

    public NewForumFragment() {
        // Required empty public constructor
    }

    public static com.example.forumapp.NewForumFragment newInstance(String token) {

        Bundle args = new Bundle();
        Log.d("debug", "newInstance: ");
        com.example.forumapp.NewForumFragment fragment = new com.example.forumapp.NewForumFragment();
        args.putString(ARG_PARAM_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mToken = getArguments().getString(ARG_PARAM_TOKEN);
        }
    }

    EditText editTextForumTitle, editTextForumDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        getActivity().setTitle("New Forum" );

        editTextForumTitle = view.findViewById(R.id.editTextForumTitle);
        editTextForumDesc = view.findViewById(R.id.editTextForumDesc);

        view.findViewById(R.id.buttonSubmit1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextForumTitle.getText().toString();
                String desc = editTextForumDesc.getText().toString();

                if(title.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Title", Toast.LENGTH_SHORT).show();
                } else if(desc.isEmpty()) {
                    Toast.makeText(getActivity(), "Description is empty", Toast.LENGTH_SHORT).show();
                } else {
                    new NewForumAsync().execute(title, desc, mToken);
                    mListener.doneCreateForum();
                }

            }
        });

        view.findViewById(R.id.buttonCancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.doneCreateForum();
            }
        });

        return view;
    }

    class NewForumAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String title = strings[0];
            String desc = strings[1];
            String token = strings[2];

            try {
                com.example.forumapp.DataServices.createForum(token, title, desc);
            } catch (com.example.forumapp.DataServices.RequestException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            }

            return null;
        }
    }

    NewForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mListener = (NewForumListener) context;
    }

    interface NewForumListener{
        void doneCreateForum();
    }
}
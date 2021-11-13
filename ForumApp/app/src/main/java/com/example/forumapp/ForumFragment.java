package com.example.forumapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ForumFragment extends Fragment {

    private static final String ARG_PARAM_AUTH_RES = "ARG_PARAM_AUTH_RES";

    private com.example.forumapp.DataServices.AuthResponse mAuthResponse;
    String TAG = "debug";

    public ForumFragment() {
        // Required empty public constructor
    }

    public static com.example.forumapp.ForumFragment newInstance(com.example.forumapp.DataServices.AuthResponse authResponse) {
        com.example.forumapp.ForumFragment fragment = new com.example.forumapp.ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_AUTH_RES, authResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthResponse = (com.example.forumapp.DataServices.AuthResponse) getArguments().getSerializable(ARG_PARAM_AUTH_RES);
        }
    }

    RecyclerView recyclerView;
    ArrayList<com.example.forumapp.DataServices.Forum> forums = new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    ForumsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        getActivity().setTitle("Forums List");

        //setup recycler view
        recyclerView = view.findViewById(R.id.forumRecyclerView);
        adapter = new ForumsAdapter();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreateView: Executing Forum Async");
        (new ForumsAsync()).execute(mAuthResponse.getToken());

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.FromForumToLogin();
            }
        });

        view.findViewById(R.id.buttonNewForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.FromForumToNewForum();
            }
        });

        return view;
    }


    //Gets all forums data asynchronously and populates forums arraylist
    class ForumsAsync extends AsyncTask<String, String, ArrayList<com.example.forumapp.DataServices.Forum>> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<com.example.forumapp.DataServices.Forum> doInBackground(String... strings) {
            String token = strings[0];
            
            try {
                Log.d(TAG, "doInBackground: Retrieving forums");
                return com.example.forumapp.DataServices.getAllForums(token);
            } catch (com.example.forumapp.DataServices.RequestException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<com.example.forumapp.DataServices.Forum> forumArrayList) {
            super.onPostExecute(forumArrayList);
            if(forumArrayList != null) {
                Log.d(TAG, "onPostExecute: displaying forums\n" + forumArrayList);
                forums.clear();
                forums.addAll(forumArrayList);
                adapter.notifyDataSetChanged();
            }else {
                Log.d(TAG, "onPostExecute: Forums array is empty");
            }
        }
    }

    class DeleteAsync extends AsyncTask<Void, String, Boolean> {
        long mForumId;
        String mToken;

        public DeleteAsync(long forumId, String token) {
            this.mForumId = forumId;
            this.mToken = token;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                com.example.forumapp.DataServices.deleteForum(mToken, mForumId);
                return true;
            } catch (com.example.forumapp.DataServices.RequestException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);
            if(isSuccessful) {
                new ForumsAsync().execute(mToken);
            }
        }
    }

    class LikeUnlikeAsync extends AsyncTask<Void, Void, Boolean> {
        long mForumId;
        String mToken;
        Boolean mLike;

        public LikeUnlikeAsync(long forumId, String token, Boolean like) {
            this.mForumId = forumId;
            this.mToken = token;
            this.mLike = like;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(mLike) {
                try {
                    com.example.forumapp.DataServices.likeForum(mToken, mForumId);
                    return true;
                } catch (com.example.forumapp.DataServices.RequestException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    com.example.forumapp.DataServices.unLikeForum(mToken, mForumId);
                    return true;
                } catch (com.example.forumapp.DataServices.RequestException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);

            if(isSuccessful) {
                new ForumsAsync().execute(mToken); //reloads list
            }

        }
    }


    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumViewHolder> {

        @NonNull
        @Override
        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_list_item, parent, false);



            view.findViewById(R.id.imageViewLike).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Like Clicked: ");
                }
            });
            return new ForumViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
            com.example.forumapp.DataServices.Forum forum = forums.get(position);
            holder.setupForumItem(forum);

        }

        @Override
        public int getItemCount() {
            return forums.size();
        }


        class ForumViewHolder extends RecyclerView.ViewHolder{
            TextView textViewTitle, textViewDescription, textViewLikesTime, textViewOwner;
            ImageView imageViewLike, imageViewDelete;
            com.example.forumapp.DataServices.Forum mForum;


            public ForumViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewOwner = itemView.findViewById(R.id.textViewOwner);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
                textViewLikesTime = itemView.findViewById(R.id.textViewLikesTime);

                imageViewLike = itemView.findViewById((R.id.imageViewLike));
                imageViewDelete = itemView.findViewById(R.id.imageViewDelete);

            }

            public void setupForumItem(com.example.forumapp.DataServices.Forum forum) {
                this.mForum = forum;
                Log.d(TAG, "setupForumItem: ");
                textViewTitle.setText(forum.getTitle());

                String description200 = forum.getDescription().substring(0, Math.min(200, forum.getDescription().length()));
                textViewDescription.setText(description200);
                textViewOwner.setText(forum.getCreatedBy().getName());

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewLikesTime.setText(forum.getLikedBy().size() + " Likes | " + formatter.format(forum.getCreatedAt()));

                if(forum.getCreatedBy().uid == mAuthResponse.account.uid) {
                    imageViewDelete.setVisibility(View.VISIBLE);
                    imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DeleteAsync(mForum.getForumId(), mAuthResponse.getToken()).execute(); //gets passed to constructor
                        }
                    });
                } else {
                    imageViewDelete.setVisibility(View.INVISIBLE);
                }

                if(forum.getLikedBy().contains(mAuthResponse.getAccount())) {
                    imageViewLike.setImageResource(R.drawable.like_favorite);

                } else {
                    imageViewLike.setImageResource(R.drawable.like_not_favorite);

                }

                imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(forum.getLikedBy().contains(mAuthResponse.getAccount())) {//if forum is liked by current account
                            new LikeUnlikeAsync(mForum.getForumId(), mAuthResponse.getToken(), false).execute();
                        }else {
                            new LikeUnlikeAsync(mForum.getForumId(), mAuthResponse.getToken(), true).execute();
                        }
                    }
                });

            }
        }
    }




    ForumListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mListener = (ForumListener) context;
    }

    interface ForumListener{
        void FromForumToDetails(com.example.forumapp.DataServices.Forum forum);
        void FromForumToNewForum();
        void FromForumToLogin();
    }
}
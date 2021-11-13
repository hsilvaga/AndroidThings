package com.example.forumapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements com.example.forumapp.LoginFragment.LoginListener, com.example.forumapp.CreateAccountFragment.RegisterListener, com.example.forumapp.ForumFragment.ForumListener
                                                                , com.example.forumapp.NewForumFragment.NewForumListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rootView, new com.example.forumapp.LoginFragment())
                .commit();
    }

    com.example.forumapp.DataServices.AuthResponse mAuthResponse;

    @Override
    public void fromLoginToForum(com.example.forumapp.DataServices.AuthResponse authResponse) {
        this.mAuthResponse = authResponse;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, com.example.forumapp.ForumFragment.newInstance(mAuthResponse))
                .commit();
    }

    @Override
    public void fromLoginToCreateAccount() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new com.example.forumapp.CreateAccountFragment())
                .commit();
    }

    @Override
    public void fromRegisterToForum(com.example.forumapp.DataServices.AuthResponse authResponse) {
        this.mAuthResponse = authResponse;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, com.example.forumapp.ForumFragment.newInstance(authResponse))
                .commit();
    }

    @Override
    public void fromRegisterToLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new com.example.forumapp.LoginFragment())
                .commit();
    }

    @Override
    public void FromForumToDetails(com.example.forumapp.DataServices.Forum forum) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new com.example.forumapp.NewForumFragment())
                .commit();
    }

    @Override
    public void FromForumToNewForum() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, com.example.forumapp.NewForumFragment.newInstance(this.mAuthResponse.getToken()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void FromForumToLogin() {
        mAuthResponse = null; //logout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new com.example.forumapp.LoginFragment())
                .commit();
    }

    @Override
    public void doneCreateForum() {
        getSupportFragmentManager().popBackStack();
    }
}
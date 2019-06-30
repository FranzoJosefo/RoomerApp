package com.franciscoolivero.android.roomerapp.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.franciscoolivero.android.roomerapp.MainActivity;
import com.franciscoolivero.android.roomerapp.Profile.ProfileActivity;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1; //codigo asignado al sign in
    private final String TAG = getClass().getSimpleName();
    /**
     * CLIENT ID CHANGE FROM DEBUG TO RELEASE WHEN GENERATING SIGNED APK/BUNDLE
     */
    private final String CLIENT_ID_DEBUG = getString(R.string.client_id_gsi_debug);
    private final String CLIENT_ID_RELEASE = getString(R.string.client_id_gsi_release);
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
//    @BindView(R.id.sign_in_button)
//    Buttton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setEnabled(true);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID_RELEASE)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

            //TODO Call Roomer API getUsersbyToken with account.getEmail
            //TODO Call Roomer API getFilterbyToken
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        //TODO Call Roomer API getUsersbyToken with account.getEmail
        //TODO Call Roomer API getFilterbyToken
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("account", account);
//        startActivity(intent);
//        finish();
        Intent intent = new Intent(this, ProfileActivity.class);
        if (account != null) {
            intent.putExtra("account", account);
            startActivity(intent);
            finish();
        } else {
            //TODO
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signInButton.setEnabled(false);
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if(account!=null){
                updateUI(account);
            }
        } catch (ApiException e) {
            signInButton.setEnabled(true);
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
package sembarang.userprofileapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.lang.ref.WeakReference;

import okhttp3.FormBody;
import sembarang.userprofileapp.global.Global;
import sembarang.userprofileapp.global.Url;
import sembarang.userprofileapp.model.LoginResponse;
import sembarang.userprofileapp.util.PreferencesManager;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(this, username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void openResetPasswordScreen(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;
        private String mPassword;
        private String errorMessage = "";
        private WeakReference<LoginActivity> loginActivityWeakReference;

        UserLoginTask(LoginActivity loginActivity, String username, String password) {
            loginActivityWeakReference = new WeakReference<>(loginActivity);
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // attempt authentication against a network service.
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("username", mUsername);
            builder.add("password", mPassword);

            String response = RequestHelper.postData(
                    Url.LOGIN,
                    builder.build()
            );
            final LoginResponse loginResponse =
                    JSON.parseObject(response, LoginResponse.class);

            if (loginResponse == null) {
                errorMessage = "Response tidak valid";
                return false;
            }

            LoginActivity loginActivity =
                    loginActivityWeakReference.get();
            if (loginActivity != null) {
                if (loginResponse.status.equals("success")) {
                    PreferencesManager.putString(
                            loginActivity,
                            Global.SHARED_PREFERENCES_KEY_USER,
                            JSON.toJSONString(loginResponse.user)
                    );
                    return true;
                } else {
                    errorMessage = loginResponse.message;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            LoginActivity loginActivity =
                    loginActivityWeakReference.get();
            if (loginActivity == null) {
                return;
            }
            loginActivity.mAuthTask = null;
            loginActivity.showProgress(false);

            if (success) {
                Intent intent = new Intent(
                        loginActivity,
                        ProfileActivity.class
                );
                loginActivity.startActivity(intent);
                loginActivity.finish();
            } else {
                Toast.makeText(
                        loginActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                ).show();
            }
        }

        @Override
        protected void onCancelled() {
            LoginActivity loginActivity =
                    loginActivityWeakReference.get();
            if (loginActivity == null) {
                return;
            }
            loginActivity.mAuthTask = null;
            loginActivity.showProgress(false);
        }
    }
}


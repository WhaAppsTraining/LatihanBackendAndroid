package sembarang.userprofileapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.lang.ref.WeakReference;

import okhttp3.FormBody;
import sembarang.userprofileapp.global.Url;
import sembarang.userprofileapp.model.BaseResponse;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }

    public void resetPassword(View view) {
        EditText editTextUsername = findViewById(R.id.et_username);
        EditText editTextNewPassword = findViewById(R.id.et_new_password);
        new ResetPasswordTask(this,
                editTextUsername.getText().toString(),
                editTextNewPassword.getText().toString()
        ).execute();
    }

    public static class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;
        private String mNewPassword;
        private String errorMessage = "";
        private String successMessage = "";
        private WeakReference<ResetPasswordActivity> resetPasswordActivityWeakReference;

        ResetPasswordTask(ResetPasswordActivity resetPasswordActivity, String username, String newPassword) {
            resetPasswordActivityWeakReference = new WeakReference<>(resetPasswordActivity);
            mUsername = username;
            mNewPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("username", mUsername);
            builder.add("password", mNewPassword);

            String response = RequestHelper.postData(
                    Url.RESET_PASSWORD,
                    builder.build()
            );
            final BaseResponse baseResponse =
                    JSON.parseObject(response, BaseResponse.class);

            if (baseResponse == null) {
                errorMessage = "Response tidak valid";
                return false;
            }

            ResetPasswordActivity resetPasswordActivity = resetPasswordActivityWeakReference.get();
            if (resetPasswordActivity != null) {
                if (baseResponse.status.equals("success")) {
                    successMessage = baseResponse.message;
                    return true;
                } else {
                    errorMessage = baseResponse.message;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            ResetPasswordActivity resetPasswordActivity =
                    resetPasswordActivityWeakReference.get();
            if (resetPasswordActivity == null) {
                return;
            }

            if (success) {
                resetPasswordActivity.finish();
                Toast.makeText(
                        resetPasswordActivity,
                        successMessage,
                        Toast.LENGTH_LONG
                ).show();
            } else {
                Toast.makeText(
                        resetPasswordActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}

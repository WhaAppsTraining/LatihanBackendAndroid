package sembarang.userprofileapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import sembarang.userprofileapp.model.UserModel;

/**
 * @author hendrawd on 13/05/18
 */
public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserModel currentUser = getIntent().getParcelableExtra("user");

        EditText etName = findViewById(R.id.et_name);
        EditText etDescription = findViewById(R.id.et_description);

        etName.setText(currentUser.name);
        etDescription.setText(currentUser.description);
    }
}

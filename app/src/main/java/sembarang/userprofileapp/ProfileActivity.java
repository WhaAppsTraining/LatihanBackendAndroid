package sembarang.userprofileapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import sembarang.userprofileapp.global.Global;
import sembarang.userprofileapp.model.UserModel;
import sembarang.userprofileapp.util.PreferencesManager;

/**
 * @author hendrawd on 13/05/18
 */
public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userJsonObject = PreferencesManager.getString(
                this,
                Global.SHARED_PREFERENCES_KEY_USER,
                ""
        );
        UserModel currentUser = JSON.parseObject(userJsonObject, UserModel.class);

        EditText etName = findViewById(R.id.et_name);
        EditText etDescription = findViewById(R.id.et_description);

        etName.setText(currentUser.name);
        etDescription.setText(currentUser.description);
    }
}

package sembarang.userprofileapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author hendrawd on 13/05/18
 */
public class UserModel implements Parcelable {
    public String id;
    public String username;
    public String name;
    public String password;
    public String description;
    @JSONField(name = "access_key")
    public String accessKey;
    @JSONField(name = "created_at")
    public String createdAt;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.description);
        dest.writeString(this.accessKey);
        dest.writeString(this.createdAt);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.name = in.readString();
        this.password = in.readString();
        this.description = in.readString();
        this.accessKey = in.readString();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
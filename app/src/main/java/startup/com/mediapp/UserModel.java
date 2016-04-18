package startup.com.mediapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshil on 16/04/2016.
 */
public class UserModel implements Parcelable {
    private String id;
    private String name;
    private String img_url;
    private String mobile_no;
    private String addr_id;

    public UserModel(){

    }

    public UserModel(Parcel in){
        id = in.readString();
        name = in.readString();
        img_url = in.readString();
        mobile_no = in.readString();
        addr_id = in.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAddr_id() {
        return addr_id;
    }

    public void setAddr_id(String addr_id) {
        this.addr_id = addr_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(img_url);
        dest.writeString(mobile_no);
        dest.writeString(addr_id);
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>()
    {
        public UserModel createFromParcel(Parcel in)
        {
            return new UserModel(in);
        }
        public UserModel[] newArray(int size)
        {
            return new UserModel[size];
        }
    };

}

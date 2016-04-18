package startup.com.mediapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshil on 11/04/2016.
 */
public class ItemModel implements Parcelable{
    private String id;
    private String name;
    private String brand_name;
    private String price;
    private String description;
    private String img_url;
    private int quantity;
    private int is_added;

    public ItemModel(){

    }

    public ItemModel(Parcel in){
        id = in.readString();
        name = in.readString();
        brand_name = in.readString();
        price = in.readString();
        description = in.readString();
        img_url = in.readString();
        quantity = in.readInt();
        is_added = in.readInt();
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public int is_added() {
        return is_added;
    }

    public void setIs_added(int is_added) {
        this.is_added = is_added;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(brand_name);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(img_url);
        dest.writeInt(quantity);
        dest.writeInt(is_added);
    }

    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>()
    {
        public ItemModel createFromParcel(Parcel in)
        {
            return new ItemModel(in);
        }
        public ItemModel[] newArray(int size)
        {
            return new ItemModel[size];
        }
    };
}

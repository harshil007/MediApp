package startup.com.mediapp;

/**
 * Created by Harshil on 16/04/2016.
 */
public class SellerModel {
    private String id;
    private String name;
    private String img_url;
    private String locality;

    public SellerModel(String id, String name, String img_url, String locality) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.locality = locality;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}

package startup.com.mediapp;

/**
 * Created by Pursnani Kapil on 03-May-16.
 */
public class SellerOrderItemInfo {


    protected String oid;
    protected String name;
    protected String amount;
    protected String date;
    protected String arrival_time;
    protected String address;
    protected String mobile;
    protected String product;
    protected String quntity;
    protected String prize;


    public SellerOrderItemInfo(){

    }

    public SellerOrderItemInfo(String oid, String name, String amount, String date, String arrival_time, String address, String mobile, String product, String quntity, String prize) {
        this.oid = oid;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.arrival_time = arrival_time;
        this.address = address;
        this.mobile = mobile;
        this.product = product;
        this.quntity = quntity;
        this.prize = prize;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuntity() {
        return quntity;
    }

    public void setQuntity(String quntity) {
        this.quntity = quntity;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}

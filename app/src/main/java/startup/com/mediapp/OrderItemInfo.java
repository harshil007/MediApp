package startup.com.mediapp;

/**
 * Created by Pursnani Kapil on 17-Apr-16.
 */
public class OrderItemInfo {

    protected String oid;
    protected String amount;
    protected String status;
    protected String date;
    protected String arrival_time;
    protected String p_id;
    protected String quantity;
    protected String c_id;



    public OrderItemInfo() {
    }

    public OrderItemInfo(String oid, String amount, String status, String date, String arrival_time, String p_id, String quantity, String c_id) {
        this.oid = oid;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.arrival_time = arrival_time;
        this.p_id = p_id;
        this.quantity = quantity;
        this.c_id = c_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}

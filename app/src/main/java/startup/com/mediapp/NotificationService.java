package startup.com.mediapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Harshil on 27/04/2016.
 */
public class NotificationService extends GcmListenerService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String user_id;
    String order_id;
    private RequestQueue mQueue;
    private String ORDER_URL = "http://mediapp.netai.net/get_order.php";
    String p_id, quant, amt, status, arr_time, u_name, u_img_url, u_mob, u_addr_id;


    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);


        //Here is called even app is not running.
        //create your notification here.
        Log.i("Sender", from);
        String msg = data.getString("message");
        //Toast.makeText(ChatService.this, msg, Toast.LENGTH_SHORT).show();
        if (msg != null) {
            int index = msg.indexOf(" ");
            if (index != -1)
                user_id = msg.substring(0, index);
            order_id = msg.substring(msg.indexOf(" ") + 1, msg.length());
        }
        volley_fetch();



    }

    private void volley_fetch() {
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        JSONObject son = new JSONObject();
        try {
            son.put("o_id", order_id);
            son.put("u_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(ORDER_URL, son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONArray p_id_array, quant_array, amt_array, status_array, arr_time_array, u_name_array, u_img_url_array, u_mob_array, u_addr_id_array;

                try {
                    p_id_array = response.getJSONArray(0);
                    quant_array = response.getJSONArray(1);
                    amt_array = response.getJSONArray(2);
                    status_array = response.getJSONArray(3);
                    arr_time_array = response.getJSONArray(4);
                    u_name_array = response.getJSONArray(5);
                    u_img_url_array = response.getJSONArray(6);
                    u_mob_array = response.getJSONArray(7);
                    u_addr_id_array = response.getJSONArray(8);

                    p_id = p_id_array.getString(0);
                    quant = quant_array.getString(0);
                    amt = amt_array.getString(0);
                    status = status_array.getString(0);
                    arr_time = arr_time_array.getString(0);
                    u_name = u_name_array.getString(0);
                    u_img_url = u_img_url_array.getString(0);
                    u_mob = u_mob_array.getString(0);
                    u_addr_id = u_addr_id_array.getString(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendNotification();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                volley_fetch();
            }
        });

        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);

    }


    private void sendNotification() {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);




        Intent intent = new Intent(this, SellerOrdersActivity.class)
                //.addFlags(PendingIntent.FLAG_UPDATE_CURRENT)
                .putExtra("p_id", p_id)
                .putExtra("quant",quant)
                .putExtra("amt",amt)
                .putExtra("status",status)
                .putExtra("arr_time",arr_time)
                .putExtra("u_name",u_name)
                .putExtra("u_img_url",u_img_url)
                .putExtra("u_mob",u_mob)
                .putExtra("u_addr_id",u_addr_id)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent pushmsg = new Intent("noti_order")
                .putExtra("p_id", p_id)
                .putExtra("quant",quant)
                .putExtra("amt",amt)
                .putExtra("status",status)
                .putExtra("arr_time",arr_time)
                .putExtra("u_name",u_name)
                .putExtra("u_img_url",u_img_url)
                .putExtra("u_mob",u_mob)
                .putExtra("u_addr_id",u_addr_id);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushmsg);


        PendingIntent contentIntent = PendingIntent.getActivity(this, new Random().nextInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("New order")
                        .setSmallIcon(R.drawable.icon_leaf)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("from "+u_name))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText("from "+u_name);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}

package startup.com.mediapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pursnani Kapil on 17-Apr-16.
 */
public class Login {

    String e,p,url;
    Context c;
    String u;
    SharedPreferences sharedPref;



    public  Login(String e, String p, String url, Context c, String u){
        this.c = c;
        this.e = e;
        this.p = p;
        this.url = url;
        this.u = u;

        sharedPref = c.getSharedPreferences(c.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    }



    public void SignIn(){

        RequestQueue rq = CustomVolleyRequestQueue.getInstance(c)
                .getRequestQueue();

        Log.d("URL", url);

        JSONObject insert = new JSONObject();
        try {
            insert.put("email", e);
            insert.put("password", p);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest req = new JsonArrayRequest(url, insert,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());


                        String uname="";
                        String imgUrl="";
                        int success;
                        String msg;
                        String id,name,img_url;
                        JSONArray flag;


                        try {
                            // Parsing json array response
                            // loop through each json objectString jsonRespons = response.toString();
                            // dialog.dismiss();
                            Log.d("Response", response.toString());
                            flag = response.getJSONArray(0);

                            success=flag.getInt(0);
                            msg=flag.getString(1);
                            if(success==1) {


                                // Saving username and Password as a Shared Preference
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(c.getString(R.string.Flag), "true");
                                editor.putString(c.getString(R.string.Email), e);
                                editor.putString(c.getString(R.string.Password), p);
                                if(u.equals("Seller")) {
                                    editor.putString(c.getString(R.string.User), "Seller");
                                }else if(u.equals("Customer")) {
                                    editor.putString(c.getString(R.string.User), "Customer");
                                }
                                editor.commit();

                                id = response.getJSONArray(1).getString(0);
                                name = response.getJSONArray(2).getString(0);
                                img_url = response.getJSONArray(3).getString(0);
                                Toast.makeText(c, "Success", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(c,MainCategory.class);
                                i.putExtra("name", name);
                                i.putExtra("img_url",img_url);
                                i.putExtra("email", e);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                c.startActivity(i);


                            }
                            else{
                                // pDialog.dismiss();
                                Toast.makeText(c, msg,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }



                        } catch (JSONException e) {
                            //  dialog.dismiss();
                            // pDialog.dismiss();
                            e.printStackTrace();
                            VolleyLog.e("Volley", "Error : " + e.getMessage());
                            Toast.makeText(c,
                                    "Error : " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("VolleyTest", e.getMessage());
                            return;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                //   dialog.dismiss();

                //pDialog.dismiss();

                VolleyLog.e("Volley", "ErrorV: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(c,
                        "Network Problem!!", Toast.LENGTH_SHORT).show();
                return;

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rq.add(req);

    }


}
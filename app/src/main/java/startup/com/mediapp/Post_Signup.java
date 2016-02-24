package startup.com.mediapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Harshil on 17/02/2016.
 */
public class Post_Signup extends AppCompatActivity {

    String encodedString;
    String fileName="";
    private static int RESULT_LOAD_IMG = 1;
    private static final String TAG = "PostSignupActivity";
    ImageView imgv;
    Button skip,browse,submit;

    ProgressDialog pDialog;

    InputStream is=null;

    EditText et_home,et_soc,et_loc,et_city,et_state,et_pin,et_shop_num;

    public static int type;

    public String cid,sid,name,email,password,mobile,addr_id,image_url;

    public String num,s_name,locality,city,state,pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_signup_customer);


        //LinearLayout ll = (LinearLayout)findViewById(R.id.post_ll);
        //LayoutInflater inflater = (LayoutInflater)this.getSystemService
        //        (Context.LAYOUT_INFLATER_SERVICE);
        //ll = (LinearLayout)inflater.inflate(R.id.ll2);

        CustFragment frag = null;
        Fragment sfrag = null;

        type=getIntent().getExtras().getInt("type");
        setup();
        if(type==0){
          /*  frag = new CustFragment();
            if(frag!=null){
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.post_ll,frag);
                ft.commit();
            }*/
            cid=getIntent().getExtras().getString("cid");
        }
        else if(type==1){
            /*sfrag = new SellFragment();
            if(sfrag!=null){
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.post_ll,sfrag);
                ft.commit();
            }*/
            name=getIntent().getExtras().getString("name");
            email=getIntent().getExtras().getString("email");
            password=getIntent().getExtras().getString("password");
            mobile=getIntent().getExtras().getString("mobile");
            sid=getIntent().getExtras().getString("sid");
        }
        else{
            final AlertDialog a = new AlertDialog.Builder(getApplicationContext()).create();
            a.setMessage("Please try again");
            a.setTitle("Error");
            a.show();
            a.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

        }

        ActionBar a;
        a = getActionBar();
        if(a!=null){
            a.show();
        }

    }



    private void setup(){

        if(type==0){
            skip = (Button)findViewById(R.id.b_skip);

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            browse = (Button)findViewById(R.id.b_browse);
            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadImagefromGallery();
                }
            });
            submit = (Button)findViewById(R.id.b_submit);


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(type);
                }
            });


        }


        et_home=(EditText)findViewById(R.id.input_home_no);
        et_soc=(EditText)findViewById(R.id.input_s_name);
        et_loc=(EditText)findViewById(R.id.input_locality);
        et_city=(EditText)findViewById(R.id.input_city);
        et_state=(EditText)findViewById(R.id.input_state);
        et_pin=(EditText)findViewById(R.id.input_pincode);

        TextInputLayout et_no = (TextInputLayout)findViewById(R.id.et_no);
        TextInputLayout et_nm = (TextInputLayout)findViewById(R.id.et_nm);



/*
        if(type==1){
            et_shop_num=(EditText)findViewById(R.id.input_shop_num);
            et_no.setHint("Shop number");
            et_nm.setHint("Shop name");
            Button browse1 = (Button)findViewById(R.id.b_browse1);
            browse1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadImagefromGallery();
                }
            });

            Button submit1 = (Button)findViewById(R.id.b_submit1);


            submit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(type);
                }
            });
        }*/


    }

    private void submit(int type) {
        int s = (int)(Math.random()*1000);
        sid = "s_"+s;

        num = et_home.getText().toString();
        s_name = et_soc.getText().toString();
        locality = et_loc.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        pincode = et_pin.getText().toString();
        image_url="";
        String image_name="";

        pDialog = new ProgressDialog(Post_Signup.this);
        if(type==0){
            image_name=cid;
        }
        else if(type==1){
            image_name=sid;
        }
        image_url="http://mediapp.netai.net/Images/"+image_name;

        new FTP().execute(image_name, is);
        if(is!=null){
            pDialog.setMessage("Uploading image...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        final int t = type;
        JSONObject insert = new JSONObject();
        String url="";
        if(type==0){
            url = "http://mediapp.netai.net/update_cust.php";
            pDialog.setMessage("Updating profile...");
            try {
                insert.put("cid",cid);
                insert.put("image_url",image_url);
                insert.put("addr_id",cid);
                insert.put("home_no",num);
                insert.put("soc_name",s_name);
                insert.put("locality",locality);
                insert.put("city",city);
                insert.put("state",state);
                insert.put("pincode",pincode);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type==1){
            url = "http://mediapp.netai.net/insert_sell.php";
            pDialog.setMessage("Creating account...");

            try {
                insert.put("sid",sid);
                insert.put("email", email);
                insert.put("password", password);
                insert.put("name",name);
                insert.put("mobile", mobile);
                insert.put("image_url",image_url);
                insert.put("addr_id",sid);
                insert.put("shop_no",num);
                insert.put("shop_name",s_name);
                insert.put("locality",locality);
                insert.put("city",city);
                insert.put("state",state);
                insert.put("pincode",pincode);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{

        }
        if(is==null){
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }



        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(url, insert,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            // Parsing json array response
                            // loop through each json objectString jsonRespons = response.toString();
                            int jsonResponse;
                            jsonResponse = response.getInt("success");

                            pDialog.dismiss();

                            if(jsonResponse==1){
                                if(t==1){
                                    Toast.makeText(getApplicationContext(),
                                            "Account created successfully!",
                                            Toast.LENGTH_LONG).show();
                                }
                                else if(t==0){
                                    Toast.makeText(getApplicationContext(),
                                            "Success!",
                                            Toast.LENGTH_LONG).show();
                                }
                                Intent i = new Intent(Post_Signup.this,LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            }
                            else{
                                if(t==1){
                                    Toast.makeText(getApplicationContext(),
                                            "Account cannot be created! Try again",
                                            Toast.LENGTH_LONG).show();
                                }
                                if(t==0){
                                    Toast.makeText(getApplicationContext(),
                                            "Error! Try again",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                            // Log.d("VolleyTest", person.toString());


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            VolleyLog.d(TAG, "ErrorJ: " + e.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    "ErrorJ: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("VolleyTest", e.getMessage());
                        }
                        pDialog.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "ErrorV: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

            }
        });

        rq.add(req);


    }



    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }



    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            imgv = (ImageView) findViewById(R.id.image_profile);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            String fileNameSegments[] = picturePath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];

            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(picturePath, options);


           /* Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            imgv.setVisibility(View.VISIBLE);
            imgv.setImageBitmap(myImg);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, 0);*/
            imgv.setVisibility(View.VISIBLE);
            imgv.setImageBitmap(bm);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.PNG, 70, stream); // convert Bitmap to ByteArrayOutputStream
            is = new ByteArrayInputStream(stream.toByteArray());

        }
    }



    class FTP extends AsyncTask<Object,Void,Void> {


        @Override
        protected Void doInBackground(Object... params) {
            String name = (String) params[0];
            InputStream inputstream = (InputStream) params[1];
            FTPClient ftp = new FTPClient();

            if(inputstream==null){
                return null;
            }

            try {
                ftp.connect("mediapp.netai.net");
                if(!ftp.login("a1277353", "WhySoSerious7"))
                {
                    ftp.logout();
                    return null;
                }
                int reply = ftp.getReplyCode();
                //FTPReply stores a set of constants for FTP reply codes.
                if (!FTPReply.isPositiveCompletion(reply))
                {
                    ftp.disconnect();
                    return null;
                }
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                ftp.changeWorkingDirectory("/public_html/Images");
                ftp.storeFile(name,inputstream);
                ftp.logout();
                ftp.disconnect();


            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }
    }



}

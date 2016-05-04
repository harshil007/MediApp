package startup.com.mediapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class PostSignupCust extends AppCompatActivity implements View.OnClickListener{

    Button browse,skip,submit;
    EditText et_home,et_soc,et_loc,et_city,et_state,et_pin;
    ProgressDialog pDialog;
    ImageView imgv;
    InputStream is=null;

    static int REQUEST_CAMERA=10;
    static int SELECT_IMAGE=110;

    String fileName="";
    private static int RESULT_LOAD_IMG = 1;
    private static final String TAG = "PostSignupCustActivity";

    public String cid,name,email,addr_id,image_url;
    public String num,s_name,locality,city,state,pincode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_signup_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_post_cust);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setup();
        cid=getIntent().getExtras().getString("cid");
    }

    public void setup(){
        browse = (Button) findViewById(R.id.b_browse_cust);
        skip = (Button) findViewById(R.id.b_skip);
        submit = (Button) findViewById(R.id.b_submit_cust);
        et_home=(EditText)findViewById(R.id.input_home_no);
        et_soc=(EditText)findViewById(R.id.input_s_name);
        et_loc=(EditText)findViewById(R.id.input_locality);
        et_city=(EditText)findViewById(R.id.input_city);
        et_state=(EditText)findViewById(R.id.input_state);
        et_pin=(EditText)findViewById(R.id.input_pincode);
        imgv = (ImageView) findViewById(R.id.image_profile_cust);

        browse.setOnClickListener(PostSignupCust.this);
        skip.setOnClickListener(PostSignupCust.this);
        submit.setOnClickListener(PostSignupCust.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.b_browse_cust:
                selectImage();
                break;

            case R.id.b_skip:
                finish();
                break;

            case R.id.b_submit_cust:
                submit();
                break;
        }
    }

    private void submit() {


        num = et_home.getText().toString();
        s_name = et_soc.getText().toString();
        locality = et_loc.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        pincode = et_pin.getText().toString();
        image_url="";
        String image_name="";

        pDialog = new ProgressDialog(PostSignupCust.this);
        image_name=cid+".png";
        image_url="http://mediapp.netai.net/Images/profile_pics/"+image_name;

        new FTP().execute(image_name, is);
        if(is!=null){
            pDialog.setMessage("Uploading image...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject insert = new JSONObject();
            String url = "http://mediapp.netai.net/update_cust.php";
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
                                Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(PostSignupCust.this,LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            }
                            else{


                                    Toast.makeText(getApplicationContext(),
                                            "Error! Try again",
                                            Toast.LENGTH_LONG).show();


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
/*
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }*/


    // When Image is selected from Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imgv.setVisibility(View.VISIBLE);
        imgv.setImageBitmap(thumbnail);




        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            String path=destination.getAbsolutePath();
            String name=destination.getName();




            try {
                convert(thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void onSelectFromGalleryResult(Intent data) {

        String[] projection = { MediaStore.MediaColumns.DATA };
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String selectedImagePath = cursor.getString(columnIndex);
        cursor.close();


        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        try {
            convert(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgv.setVisibility(View.VISIBLE);
        imgv.setImageBitmap(bm);
    }


    private void convert(Bitmap bitmap) throws Exception {
        if (bitmap == null) {
            return;
        }

        Bitmap bitmap1 = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap1.compress(Bitmap.CompressFormat.PNG, 70, stream); // convert Bitmap to ByteArrayOutputStream
        is = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
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
                ftp.changeWorkingDirectory("/public_html/Images/profile_pics");
                ftp.storeFile(name,inputstream);
                ftp.logout();
                ftp.disconnect();


            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PostSignupCust.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_IMAGE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



}

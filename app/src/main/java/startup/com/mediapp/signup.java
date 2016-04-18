package startup.com.mediapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class signup extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    ProgressDialog pDialog;
    String encodedString;
    String fileName = "";
    private static int RESULT_LOAD_IMG = 1;
    ImageView imgv;
    String eAdd;
    String pwd;
    String uname;
    int flag = 0;

    public String name, password, email, mobile, confirm;

    private static String cid;

    EditText _nameText, _emailText, _passwordText, _confirmpasswordText, _mobileText;

    EditText et, pt, unam;

    public int type;

    RadioGroup rg;
    RadioButton rb_cust, rb_sell;
    Button _signupButton;

    TextView _loginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //ButterKnife.inject(this);


        setup();


    }

    private void setup() {
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _confirmpasswordText = (EditText) findViewById(R.id.confirm_password);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        //rg = (RadioGroup)findViewById(R.id.rg_signup);
        rb_sell = (RadioButton) findViewById(R.id.rb_sell);
        rb_cust = (RadioButton) findViewById(R.id.rb_cust);


        et = (EditText) findViewById(R.id.input_email);
        unam = (EditText) findViewById(R.id.input_name);
        pt = (EditText) findViewById(R.id.input_password);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_cust:
                if (!checked) {
                    rb_cust.toggle();
                    rb_sell.toggle();
                }

                break;
            case R.id.rb_sell:
                if (!checked) {
                    rb_cust.toggle();
                    rb_sell.toggle();
                }

                break;
        }
    }


    public static boolean isEmailValid(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


    public void uploadData() {

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String mobile = _mobileText.getText().toString();
        int c = (int) (Math.random() * 1000);
        cid = "c_" + c;


        String url = "http://mediapp.netai.net/insert_cust.php";
        final String TAG = MainActivity.class.getSimpleName();
        flag = 0;

        JSONObject insert = new JSONObject();
        try {
            insert.put("cid", cid);
            insert.put("email", email);
            insert.put("password", password);
            insert.put("name", name);
            insert.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
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

                            if (jsonResponse == 1) {

                                Toast.makeText(getApplicationContext(),
                                        "Account created successfully!",
                                        Toast.LENGTH_LONG).show();
                                Intent i = new Intent(signup.this, PostSignupCust.class);
                                i.putExtra("cid", cid);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Account cannot be created! Try again",
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
                error.printStackTrace();
                pDialog.dismiss();

            }
        });

        rq.add(req);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }


        if (rb_cust.isChecked()) {
            type = 0;
            pDialog = new ProgressDialog(signup.this);
            pDialog.setMessage("Creating account...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();


            // TODO: Implement your own signup logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();
                            // onSignupFailed();
                        }
                    }, 3000);
        } else if (rb_sell.isChecked()) {
            type = 1;
            int s = (int) (Math.random() * 1000);
            String sid = "s_" + s;
            Intent i = new Intent(signup.this, PostSignupSell.class);

            i.putExtra("name", name);
            i.putExtra("email", email);
            i.putExtra("password", password);
            i.putExtra("mobile", mobile);
            i.putExtra("sid", sid);
            startActivity(i);
        }

        _signupButton.setEnabled(false);


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        eAdd = et.getText().toString();
        pwd = pt.getText().toString();
        uname = unam.getText().toString();
// hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pt.getWindowToken(), 0);
        uploadData();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Form incomplete or incorrect!", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        confirm = _confirmpasswordText.getText().toString();
        mobile = _mobileText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!(confirm.equals(password))) {
            _confirmpasswordText.setError("Must be same as the password given above");
            valid = false;
        } else {
            _confirmpasswordText.setError(null);
        }

        if (mobile.isEmpty()) {
            _mobileText.setError("This field is compulsory");
            valid = false;
        } else if (mobile.length() != 10) {
            _mobileText.setError("Mobile number must be of 10 digits");
            valid = false;
        } else {
            _mobileText.setError(null);
        }


        return valid;
    }


    public void onBackPressed() {
        // do something on back.
        finish();
    }

}

package startup.com.mediapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "MESSAGE";
    public final static String EXTRA_MESSAGE3 = "MESSAGE3";
    private static final int REQUEST_SIGNUP = 0;

    private String myIP;
    private static String TAG = MainActivity.class.getSimpleName();
    private Intent intent1;
    private Intent intent2;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView _signupLink;

    private Button signIn_cust,signIn_sell;
    private Button register;

    private String emAddr;
    private String pass;

    protected ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        setup();



    }

    private void setup(){
        // Set up the login form.
        _signupLink=(TextView)findViewById(R.id.link_signup);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);


        signIn_cust = (Button) findViewById(R.id.login_button_cust);
        signIn_sell = (Button) findViewById(R.id.login_button_sell);
        signIn_cust.setOnClickListener(this);
        signIn_sell.setOnClickListener(this);





        _signupLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button_cust :
                attemptLogin(true);
                break;
            case R.id.login_button_sell :
                attemptLogin(false);
                break;
            case R.id.link_signup :
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), signup.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                break;
        }
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(Boolean f) {
        if (mAuthTask != null) {
            return;
        }




        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if(email.equals("admin@mediapp.com") && password.equals("admin")){
                Toast.makeText(getApplicationContext(),"Admin login",Toast.LENGTH_LONG);
                Intent i = new Intent(LoginActivity.this,MainCategory.class);
                startActivity(i);
                finish();
            }
            else{
                if(f){
                    myIP= "http://mediapp.netai.net/login_cust.php";
                }
                else {
                    myIP = "http://mediapp.netai.net/login_sell.php";
                }



                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                //showProgress(true);
                //intent1 = new Intent(LoginActivity.this,BuyMeds.class);
                // startActivity(intent1);

                pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Attempting login...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setCancelable(false);
                pDialog.setTitle("Signing in");
                pDialog.show();



                emAddr=mEmailView.getText().toString();
                pass=mPasswordView.getText().toString();
                SignIn();
                //startActivity(intent1);

                mAuthTask = new UserLoginTask(email, password);
                mAuthTask.execute((Void) null);
            }


        }
    }

    public void SignIn(){

        RequestQueue rq = Volley.newRequestQueue(this);
       // final download down =new download();

        String url = myIP;
        Log.d("URL", url);

        JSONObject insert = new JSONObject();
        try {
            insert.put("email", emAddr);
            insert.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, insert,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        String uname="";
                        String imgUrl="";
                        try {
                            // Parsing json array response
                            // loop through each json objectString jsonRespons = response.toString();
                         // dialog.dismiss();

                            int success = response.getInt("success");

                            if(success==1) {
                                pDialog.dismiss();
                                Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                Intent i = new Intent(LoginActivity.this,MainCategory.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                pDialog.dismiss();
                                Toast.makeText(getBaseContext(), "Invalid e-mail address or Password !!",
                                        Toast.LENGTH_LONG).show();
                            }



                        } catch (JSONException e) {
                          //  dialog.dismiss();
                            pDialog.dismiss();
                            e.printStackTrace();
                            VolleyLog.e(TAG, "Error : " + e.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    "Error : " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("VolleyTest", e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
             //   dialog.dismiss();

                pDialog.dismiss();

                VolleyLog.e(TAG, "ErrorV: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Network Problem!!", Toast.LENGTH_SHORT).show();

            }
        });

        rq.add(req);
    }


    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;


            if (success) {
               // finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
    public void onBackPressed() {
        // do something on back.
      finish();
    }
}


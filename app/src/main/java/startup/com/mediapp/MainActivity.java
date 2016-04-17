package startup.com.mediapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {


    private static String TAG = MainActivity.class.getName();
    private static long SLEEP_TIME = 3500;    // Sleep for some time
    private SharedPreferences sharedPref;
    private String e,p,url,user;
    View view;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.activity_splash_screen);

        /*LinearLayout splash = (LinearLayout) findViewById(R.id.splash);
        Bitmap bckimage = BitmapFactory.decodeResource(this.getResources(),R.drawable.mediapp);
        bckimage = Bitmap.createScaledBitmap(bckimage,view.getWidth(),splash.getHeight(),true);
        Drawable d = new BitmapDrawable(getResources(), bckimage);
        splash.setBackground(d);*/

        // Start timer and launch main activity
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME);
                sharedPref = getApplicationContext().
                        getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE );
                String defaultValue = getResources().getString(R.string.Default);
                String flag = sharedPref.getString(getString(R.string.Flag), defaultValue);
                Log.d("Flag", flag);

                user = sharedPref.getString(getString(R.string.User), defaultValue);
                e = sharedPref.getString(getString(R.string.Email), defaultValue);
                p = sharedPref.getString(getString(R.string.Password), defaultValue);

                Log.d("user", user);
                Log.d("Flag", e);
                Log.d("Flag", p);
                if(!flag.equals("false") &&  !user.equals("false") && !e.equals("false") && !p.equals("false")){


                    if(user.equals("Customer")) {

                        url = "http://mediapp.netai.net/login_cust.php";
                    }

                    else if(user.equals("Seller")){

                        url = "http://mediapp.netai.net/login_sell.php";
                    }
                    Login l = new Login(e,p,url,getApplicationContext(),user);
                    l.SignIn();

                }
                else{
                    // Start main activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();

                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }



        }
    }
}
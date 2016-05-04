package startup.com.mediapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PillChooser extends AppCompatActivity {

    EditText pill;
    Button choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_chooser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pill = (EditText)findViewById(R.id.pill_chooser);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(pill.getText())) {

                    Intent i = new Intent(PillChooser.this, Dosage.class);
                    Bundle b = new Bundle();
                    b.putString("pill", pill.getText().toString());
                    i.putExtras(b);
                    startActivity(i);
                }
                else{pill.setError("Enter pill name");}
                return;
            }
        });
    }

}

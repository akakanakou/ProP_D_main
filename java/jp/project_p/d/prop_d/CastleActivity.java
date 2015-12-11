package jp.project_p.d.prop_d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

public class CastleActivity extends Activity {
    public int castle_current_HP = 100;
    public ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.castle_b_bef);
        bar = (ProgressBar)findViewById(R.id.progressBar);
        bar.setMax(100);
        bar.setProgress(castle_current_HP);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void onClickShotBtn(View v){
        /*Intent intent = new Intent(this, CastleActivity2.class);
        startActivity(intent);*/
        castle_current_HP -= 20;
        bar.setProgress(castle_current_HP);
        if(castle_current_HP == 0){
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("key.StringData", "(^p^)"); //(^p^)の意味は全くない。何でもいい(^q^)
            setResult(RESULT_OK, data);
            finish();
            /*Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);*/
        }

    }

}

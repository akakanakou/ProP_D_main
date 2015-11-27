package jp.project_p.d.prop_d;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.KeyEvent;
import android.widget.EditText;

public class TestActivity extends AppCompatActivity implements OnClickListener {

    private static Button btn_ok = null;
    private static EditText latitude_editText = null;    //緯度
    private static EditText longitude_editText = null;    //経度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_ok=(Button)findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void onClick(View v) {
        if (v.equals(btn_ok)) {
            latitude_editText = (EditText) findViewById(R.id.latitude);
            longitude_editText = (EditText) findViewById(R.id.longitude);
            // 返すデータ(Intent&Bundle)の作成
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putDouble("key.latitude", Double.parseDouble(latitude_editText.getText().toString()));
            bundle.putDouble("key.longitude", Double.parseDouble(longitude_editText.getText().toString()));
            data.putExtras(bundle);

            // setResult() で bundle を載せた
            // 送るIntent dataをセットする

            // 第一引数は…Activity.RESULT_OK,
            // Activity.RESULT_CANCELED など
            setResult(RESULT_OK, data);

            // finish() で終わらせて
            // Intent data を送る
            finish();
        }
    }
}

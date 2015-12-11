package jp.project_p.d.prop_d;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class SwingActivity extends AppCompatActivity
        implements SensorEventListener {
    // センサーマネージャ
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    // 値を保存するかどうか判定するのに使うチェックボックス
    //private CheckBox doSave;
    // 値を保存するのに使う
    //FileOutputStream out;

    //スイング開始時に押されるボタン
    Button swingBtn;
    //ボタンが押され、取得可能かどうかを判定する変数
    boolean canSave;

    // 加速度の取得間隔（ms）
    int interval;

    //加速度を測定するかどうか
    boolean doGetAcc;

    //maxYを取得後、値が一定以上プラスになったらminYを取得可能にする
    boolean canGetMinY;

    // 現在時刻
    long currentTime;
    // 前回処理を行った時刻
    long lastTime;
    // 最高到達点保存時刻
    long timeArrivingMaxY;
    // 最低到達点保存時刻
    long timeArrivingMinY;

    // 今回の加速度
    double currentAcc;
    // 前回の加速度
    double lastAcc;
    // 今回の速度
    double currentVel;
    // 初速度（前回の速度）
    double lastVel;

    // 現在の高さ（計算式では距離）
    double currentY;
    // 最高到達点
    double maxY;
    // 最低到達点
    double minY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swing);
        // センサーマネージャのインスタンスを取得
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // 加速度センサーの取得
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // チェックボックスのインスタンス取得
        //doSave = (CheckBox)findViewById(R.id.doSave);
        //ボタンのインスタンス取得
        swingBtn = (Button)findViewById(R.id.btn_swing);

        //保存先のファイルを開き、値の解説を挟む
        /*try{
            out = openFileOutput( "test.txt", MODE_PRIVATE );
            String str = "画面を上にしたとき" + "\n" + "X:左から右 Y:下から上 Z:裏から表" + "\n"
                    + "X:右辺が下だとマイナス" + "\n" + "Y:頭が下だとマイナス" + "\n" + "Z:画面が下だとマイナス" + "\n";
            out.write(str.getBytes());
        }catch( IOException e ){
            e.printStackTrace();
        }*/

        paramReset();

    }

    //変数の値をリセットする
    private void paramReset(){
        //ボタンが押され、取得可能かどうかを判定する変数
        canSave = false;

        // 加速度の取得間隔（ms）
        interval = 20;

        //加速度を測定するかどうか
        doGetAcc = false;

        //maxYを取得後、値が一定以上プラスになったらminYを取得可能にする
        canGetMinY = false;

        // 現在時刻
        currentTime = 0;
        // 前回処理を行った時刻
        lastTime = 0;
        // 最高到達点保存時刻
        timeArrivingMaxY = 0;
        // 最低到達点保存時刻
        timeArrivingMinY = 0;

        // 今回の加速度
        currentAcc = 0.0;
        // 前回の加速度
        lastAcc = 0.0;
        // 今回の速度
        currentVel = 0.0;
        // 初速度（前回の速度）
        lastVel = 0.0;

        // 現在の高さ（計算式では距離）
        currentY = 0.0;
        // 最高到達点
        maxY = 0.0;
        // 最低到達点
        minY = 0.0;
    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    //ShowDataボタンのクリックイベントハンドラー
    /*public void onClickShowData(View v){
        //改めて測定する場合に備えて変数をリセット
        paramReset();

        //データの区切りがわかるようにスペースを挟む
        try{
            String str = "---------------------\n";
            out.write(str.getBytes());
        }catch( IOException e ){
            e.printStackTrace();
        }

        Intent intent = new Intent(this, ShowDataActivity.class);
        startActivity(intent);
    }*/

    //SwingBtnのクリックイベントハンドラー
    public void onClickSwingBtn(View v){
        if(canSave != true){
            canSave = true;
            swingBtn.setText(R.string.btn_swinging);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO 自動生成されたメソッド・スタブ

        //チェックが入っているときのみ値を取得可能
        //if(doSave.isChecked() == true) {
        //ボタンが押され、canSaveがtrueになれば取得可能
        if(canSave){

            //端末が真横に傾いたら測定開始
            if(doGetAcc == false && event.values[0] > 9.8 || event.values[0] < -9.8){
                doGetAcc = true;
            }

            //maxY取得後、すぐにminYを取得してしまわないよう、
            //ある程度、Yの加速度がプラスへ傾いたことを確認する
            if(maxY != 0.0 && canGetMinY == false && event.values[1] > 3.0){
                canGetMinY = true;
            }

            if(doGetAcc) {

                //現在時刻の取得
                currentTime = System.currentTimeMillis();

                //前回の処理からinterval(ms)経過していたら処理に入る
                if ((currentTime - lastTime) >= interval) {
                    //処理時刻の更新
                    lastTime = currentTime;

                    //EditTextに書き込む文字列を作成するのに必要
                    //StringBuilder strBuild = new StringBuilder();

                    //Yの加速度
                    currentAcc = event.values[1];

                    //現在までの移動距離の合計を求める
                    //加速度は現在のモノと前回のモノの差を利用
                    currentY += lastVel + ((currentAcc - lastAcc) / 2.0);


                    //速度を求める　次回の計算に利用
                    //加速度は現在のモノと前回のモノの差を利用
                    currentVel = lastVel + (currentAcc - lastAcc);

                    //速度と加速度の更新
                    lastAcc = currentAcc;
                    lastVel = currentVel;

                    //最高到達点、最低到達点の保存
                    //ceil(double a) currentAccが-0.5とかの場合に0と見なせる
                    //引数の値以上で、計算上の整数と等しい、
                    // 最小の(負の無限大にもっとも近い) double値を返します。
                    //	floor(double a) currentAccが0.5とかの場合に0と見なせる
                    //引数の値以下で、計算上の整数と等しい、
                    // 最大の(正の無限大にもっとも近い) double値を返します。
                    if(maxY == 0.0 &&
                            Math.ceil(currentAcc) == 0.0){
                        maxY = currentY;
                        timeArrivingMaxY = currentTime;
                    } else if(canGetMinY == true && minY == 0.0 &&
                            Math.floor(currentAcc) == 0.0){
                        minY = currentY;
                        timeArrivingMinY = currentTime;
                    }

                    if(minY != 0.0){
                        //最高到達点から最低到達点までの速度を求める
                        double v = Math.abs(maxY) / ((timeArrivingMinY - timeArrivingMaxY) / 5.0);

                        //速度から移動先の座標を計算し、MapsActivityに戻す
                        // 返すデータ(Intent&Bundle)の作成
                        //latitude : 緯度
                        //longitude : 経度
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("key.latitude", v);
                        bundle.putDouble("key.longitude", v);
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

                    //strBuild.append("Y: " + event.values[1] + "\n\n");
                    /*if(maxY != 0.0){
                        strBuild.append("maxY: " + maxY + "\n");
                        strBuild.append("timeArrivingMaxY: " + timeArrivingMaxY + "\n");
                    }
                    if(minY != 0.0){
                        strBuild.append("minY: " + minY + "\n");
                        strBuild.append("timeArrivingMinY: " + timeArrivingMinY + "\n\n");
                        //最高到達点から最低到達点までの速度を秒単位で求める
                        double v = Math.abs(maxY) / ((timeArrivingMinY - timeArrivingMaxY) / 1000.0);
                        strBuild.append("V: " + v + "\n");
                    }*/


                    //データの書き込み
                    /*try {
                        out.write(strBuild.toString().getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    TextView txt01 = (TextView) findViewById(R.id.txt01);
                    txt01.setText(strBuild.toString());*/
                }
            }
        }

    }
}
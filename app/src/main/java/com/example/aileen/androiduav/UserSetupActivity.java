package com.example.aileen.androiduav;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserSetupActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        EditText editText = findViewById(R.id.edit_bluetooth);
        editText.setText(UAVApplication.BLUETOOTHVALUE);

        //确认
        findViewById(R.id.info_confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserSetupActivity.this, MainActivity.class);
                EditText editText = findViewById(R.id.edit_bluetooth); //获取editText
                i.putExtra("BluetoothValue", editText.getText().toString());
                startActivity(i);
                finish();
            }
        });

        //取消
        findViewById(R.id.info_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

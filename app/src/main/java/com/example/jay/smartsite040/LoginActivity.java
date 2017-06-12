package com.example.jay.smartsite040;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author zcy
 *
 */
public class LoginActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); 
        
        Button loginBtn = (Button)findViewById(R.id.login_in);
        loginBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
                //这个是直接跳转到AwesomeActivity
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
//                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        
        Button cancel;
        cancel=(Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击退出
            	System.exit(0);
            }
        });
    }

}

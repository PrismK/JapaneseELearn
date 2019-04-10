package com.prismk.japaneseelearn.guidepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.LoginActivity;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        ParallxContainer container = (ParallxContainer) findViewById(R.id.parallax_container);
        container.setUp(
                new int[]{
                        R.layout.view_intro_1,
                        R.layout.view_intro_2,
                        R.layout.view_intro_3,
                        R.layout.view_intro_4,
                        R.layout.view_intro_5,
                        R.layout.view_login
                }
        );

        View rl_login = findViewById(R.id.rl_login);
        rl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //设置动画
        ImageView iv_man = (ImageView) findViewById(R.id.iv_man);
        iv_man.setBackgroundResource(R.drawable.man_run);
        container.setIv_man(iv_man);

    }
}

package cn.scxingm.taglayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String[] btns  = new String[]{
            "付款了的", "dsafgdsag", "士大夫感到撒", "dsagddasg",
            "包顺丰快递v了撒", "sdagdsag", "ndlkzsv", "dsagdsa",
            "付款了的", "dsafgdsag", "fg", "dsagddasg",
            "包顺丰快递v了撒", "sdagdsag", "ndlkzsv", "dsagdsa"
    };

    private TagLayout tagLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagLayout = (TagLayout) findViewById(R.id.tagLayout);

        initData();
    }

    private void initData() {
        for (int i = 0; i < btns.length; i ++){
            Button btn = new Button(this);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
            );

            btn.setText(btns[i]);
            tagLayout.addView(btn, params);
        }
    }
}

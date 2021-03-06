package com.github.snowdream.core.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.getkeepsafe.relinker.sample.Native;
import com.github.snowdream.core.lang.ILoadListener;
import com.github.snowdream.core.lang.System;
import com.github.snowdream.core.task.Cancellable;
import com.github.snowdream.core.task.Task;
import com.github.snowdream.core.widget.Toast;
import com.github.snowdream.core.widget.ToastBean;
import com.github.snowdream.image.IImage;
import com.github.snowdream.net.DefaultHttpResponseParser;
import com.github.snowdream.net.HttpResponseCallback;
import com.github.snowdream.net.IHttp;
import com.github.snowdream.net.IHttpRequest;
import com.github.snowdream.support.v4.app.FragmentActivity;
import com.github.snowdream.toybricks.ToyBricks;
import com.github.snowdream.util.DensityUtil;
import org.jetbrains.annotations.NotNull;

import static com.github.snowdream.core.widget.ToastBean.LENGTH_LONG;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);



        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        final Integer[] i = {0};

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IKotlinText ktext = ToyBricks.getImplementation(IKotlinText.class);
                //
                //Snackbar.make(view,ktext.getText(), Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                i[0]++;

                //Snackbar.make(fab, Native.helloJni(), Snackbar.LENGTH_LONG)
                //    .setAction("Action", null).show();

                final float PADDING_21 = DensityUtil.applyDimension(getApplicationContext(),
                    TypedValue.COMPLEX_UNIT_DIP, 21F);
                final float PADDING_14 = DensityUtil.applyDimension(getApplicationContext(),
                    TypedValue.COMPLEX_UNIT_DIP, 14F);

                final RelativeLayout layout = new RelativeLayout(getApplicationContext());
                layout.setPadding((int)PADDING_21, (int)PADDING_14, (int)PADDING_21, (int)PADDING_14);
                layout.setBackgroundResource(R.drawable.bg_toast);

                RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                TextView textView = new TextView(getApplicationContext());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setText("" + i[0]);

                layout.addView(textView, params);

                int gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

                switch (i[0] % 4) {
                    case 0:
                        gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                        break;
                    case 1:
                        gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        break;
                    case 2:
                        gravity = Gravity.START | Gravity.CENTER_VERTICAL;
                        break;
                    case 3:
                        gravity = Gravity.END | Gravity.CENTER_VERTICAL;
                        break;
                    default:
                        gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                        break;
                }

                final ToastBean bean = new ToastBean(getApplicationContext(), layout, LENGTH_LONG);
                bean.setGravity(gravity, 0, 0);

                Toast.show(bean);

                IHttpRequest<String> request = ToyBricks.getImplementation(IHttpRequest.class);
                request
                    .addUrl("http://httpbin.org/get")
                    .addHttpResponseParser(new DefaultHttpResponseParser())
                    .get();

                IHttp httpclient = ToyBricks.getImplementation(IHttp.class);
                httpclient.start(MainActivity.this, request, new HttpResponseCallback<String>(){
                    @Override
                    public void onSuccess(boolean isOnUiThread, String s) {
                        Log.i("MainActivity",s);
                    }
                });

                ImageView imageView = (ImageView)findViewById(R.id.imageview);
                IImage iImage = ToyBricks.getImplementation(IImage.class);
                iImage.load(MainActivity.this,imageView,"https://www.baidu.com/img/bd_logo1.png",R.mipmap.ic_launcher,R.mipmap.ic_launcher);
            }
        });

        System.loadLibrary(this, "hellojni", new ILoadListener() {
            @Override
            public void success() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(fab, Native.helloJni(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    }
                });
            }

            @Override
            public void failure(@NotNull Throwable t) {
                Snackbar.make(fab, t.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

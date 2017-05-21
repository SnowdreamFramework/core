package com.github.snowdream.core.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.getkeepsafe.relinker.sample.Native;
import com.github.snowdream.core.lang.ILoadListener;
import com.github.snowdream.core.lang.System;
import com.github.snowdream.kotlin.helloworld.IKotlinText;
import com.github.snowdream.toybricks.ToyBricks;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IKotlinText ktext = ToyBricks.getImplementation(IKotlinText.class);
                //
                //Snackbar.make(view,ktext.getText(), Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Snackbar.make(fab, Native.helloJni(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
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
                Snackbar.make(fab,  t.getMessage(), Snackbar.LENGTH_LONG)
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

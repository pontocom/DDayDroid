package eu.netmust.dreamdestinationaday;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    //protected ImageView frontImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*
        frontImage = (ImageView) findViewById(R.id.imageView);

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        ImageLoader.getInstance().displayImage("http://whenonearth.net/wp-content/uploads/2014/08/Aogashima-Island-above.jpg", frontImage, new ImageLoadingListener() {
            ProgressDialog pDialog;

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.i("DDDay", "Started loading the image...");
                pDialog = new ProgressDialog(SplashActivity.this);
                pDialog.setMessage("Loading Image ....");
                pDialog.show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.i("DDDay", "Finished loading the image...");
                pDialog.dismiss();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        */

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_out);

                // termina a activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

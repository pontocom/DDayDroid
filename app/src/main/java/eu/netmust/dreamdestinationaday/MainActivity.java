package eu.netmust.dreamdestinationaday;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    ImageView defaultLocationView;
    Bitmap bitmap;

    TextView POInameView, POIdescView;

    private static String uniqueID = null;
    private static final String USER_UNIQUE_ID = "USER_UNIQUE_ID";

    static final String APPNAME = "DreamDestinationADay";

    private static boolean hasFirstImage = false;
    static final String SERVICE_URL = "http://192.168.2.52:3000/ddday";

    String POIname, POIdetails, POIlat, POIlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_in);


        /* check if it is the first time that the application is run:
        if it is this, then it is necessary to verify if a unique identifier already exists or not
         */
        if(uniqueID == null) {
            // it doesn't exist, lets try to read it from shared preferences
            SharedPreferences sp = getApplicationContext().getSharedPreferences(USER_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sp.getString(USER_UNIQUE_ID, null);
            // if it doesn't exist on the shared preferences file, then create a new one!!!
            if(uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(USER_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }


        // add the UP action button on the ActionBar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        defaultLocationView = (ImageView) findViewById(R.id.defaultImageView);
        POInameView = (TextView) findViewById(R.id.POInameView);
        POIdescView = (TextView) findViewById(R.id.POIdescriptionView);

        // check if we already have a first image!!!
        if(!hasFirstImage) {
            //ok, we need to get the first image, contacting the service and after, displaying the image
            // Load a remote image
            new getDreamDestWeb().execute(SERVICE_URL);
            hasFirstImage = true;
        } else {
            defaultLocationView.setImageBitmap(bitmap);
        }

        defaultLocationView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new getDreamDestWeb().execute(SERVICE_URL);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        getMenuInflater().inflate(R.menu.activity_main_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle presses on the action bar items
        switch (id) {
            case R.id.action_location:
                Intent locationIntent = new Intent(this, LocationActivity.class);
                Bundle locationInfo = new Bundle();
                locationInfo.putString("POINAME", POIname);
                locationInfo.putString("POIDETAILS", POIdetails);
                locationInfo.putString("POILAT", POIlat);
                locationInfo.putString("POILNG", POIlng);
                locationIntent.putExtras(locationInfo);
                startActivity(locationIntent);
                return true;
            case R.id.action_refresh:
                new getDreamDestWeb().execute(SERVICE_URL);
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(this, ShareActivity.class);
                Bundle shareInfo = new Bundle();
                shareInfo.putString("POINAME", POIname);
                shareInfo.putString("POIDETAILS", POIdetails);
                shareInfo.putString("POILAT", POIlat);
                shareInfo.putString("POILNG", POIlng);
                shareIntent.putExtras(shareInfo);
                startActivity(shareIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        ProgressDialog pDialog;

        String fileName = "tempImage.png";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();

                FileOutputStream fileOutStream = openFileOutput(fileName, MODE_PRIVATE);
                fileOutStream.write(b);
                fileOutStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                defaultLocationView.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class getDreamDestWeb extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog ddialog;
        JSONObject response = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ddialog = new ProgressDialog(MainActivity.this);
            ddialog.setMessage("Getting remote data ...");
            ddialog.show();
        }

        protected JSONObject doInBackground(String... args) {
            try {
                HttpClient httpc = new DefaultHttpClient();
                HttpGet hget = new HttpGet(args[0]);
                Log.i(APPNAME, "Calling -> " + args[0]);

                String res = httpc.execute(hget, new BasicResponseHandler());
                Log.i(APPNAME, "Full response = " + res);

                response = new JSONObject(res);

            } catch (Exception e) {
                Log.e(APPNAME, "An exception has occured in the connection - " + e.toString());
                return null;
            }
            return response;
        }

        protected void onPostExecute(JSONObject jObj) {
            // TODO: do something with the data!!!
            ddialog.dismiss();
            try {
                POIname = jObj.getString("POIname");
                POInameView.setText(POIname);
                POIdetails = jObj.getString("POIdetails");
                POIdescView.setText(POIdetails);

                POIlat = jObj.getString("POIlat");
                POIlng = jObj.getString("POIlng");

                new LoadImage().execute(jObj.getString("POIimageURL"));

            } catch (Exception e) {
                Log.e(APPNAME, "An exception has occured in JSON parsing - " + e.toString());
            }
        }
    }

}

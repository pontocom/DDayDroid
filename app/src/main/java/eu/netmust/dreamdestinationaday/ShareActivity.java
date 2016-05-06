package eu.netmust.dreamdestinationaday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

import java.io.File;
import java.util.Arrays;


public class ShareActivity extends ActionBarActivity {

    Switch sFB;
    Switch sTW;

    String POIname, POIdetails, POIlat, POIlng;
    String shareText;

    EditText shareTextView;
    ImageView bitmapView;

    Bitmap bitmap;

    Button shareBtn;

    static final String APPNAME = "DreamDestinationADay";

    String fileName = "tempImage.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);

        shareBtn = (Button) findViewById(R.id.button2);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if(data!=null) {
            POIname = data.getString("POINAME");
            POIdetails = data.getString("POIDETAILS");
            POIlat = data.getString("POILAT");
            POIlng = data.getString("POILNG");
        }


        shareText = "This is my dream destination for today: " + POIname + ", " + POIdetails + " #dreamdestdday #netmust).";

        sFB = (Switch) findViewById(R.id.switchFB);
        sTW = (Switch) findViewById(R.id.switchTW);

        shareTextView = (EditText) findViewById(R.id.shareText);
        shareTextView.setText(shareText);

        bitmapView = (ImageView) findViewById(R.id.imagePOIView);

        File filePath = getFileStreamPath(fileName);
        Drawable d = Drawable.createFromPath(filePath.toString());

        bitmapView.setImageDrawable(d);

        sFB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    Log.i("ShareActivity", "Facebook = OFF ");
                } else {
                    Log.i("ShareActivity", "Facebook = ON ");
                    Session.openActiveSession(ShareActivity.this, true, new Session.StatusCallback() {

                        @Override
                        public void call(Session session, SessionState sessionState, Exception e) {
                            if(session.isOpened()) {
                                Request.newMeRequest(session, new Request.GraphUserCallback() {
                                    @Override
                                    public void onCompleted(GraphUser graphUser, Response response) {
                                        if(graphUser!=null) {
                                            Log.i("ShareActivity","User ID "+ graphUser.getId());
                                            Log.i("ShareActivity","Email "+ graphUser.asMap().get("email"));
                                        }
                                    }
                                }).executeAsync();
                            }
                        }
                    });
                }
            }
        });

        sTW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    Log.i("ShareActivity", "Twitter = OFF ");
                } else {
                    Log.i("ShareActivity", "Twitter = ON ");
                }
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(APPNAME, "Share button pressed!!!");

                Session.openActiveSession(ShareActivity.this, true, new Session.StatusCallback() {

                    @Override
                    public void call(Session session, SessionState sessionState, Exception e) {
                        if(session.isOpened()) {

                            Log.i(APPNAME, "Build share dialog!!!");
                            Log.i(APPNAME, "Message: " + shareTextView.getText().toString());


                        }
                    }
                });
            }
        });


        /*

        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setOnErrorListener(new LoginButton.OnErrorListener() {
            @Override
            public void onError(FacebookException e) {
                Log.i("ShareActivity", "Error -> " + e.getMessage());
            }
        });

        //authButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        authButton.setPublishPermissions(Arrays.asList("publish_actions"));
        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                if(session.isOpened()) {
                    Log.i("ShareActivity", "Access Token = " + session.getAccessToken());

                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser, Response response) {
                            if(graphUser != null) {
                                Log.i("ShareActivity","User ID "+ graphUser.getId());
                                Log.i("ShareActivity","Email "+ graphUser.asMap().get("email"));
                            }
                        }
                    }).executeAsync();
                }
            }
        });

        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}

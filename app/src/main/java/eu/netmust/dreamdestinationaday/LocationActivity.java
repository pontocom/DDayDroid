package eu.netmust.dreamdestinationaday;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationActivity extends ActionBarActivity {

    /** Local variables **/
    GoogleMap googleMap;

    String POIname, POIdetails, POIlat, POIlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if(data!=null) {
            POIname = data.getString("POINAME");
            POIdetails = data.getString("POIDETAILS");
            POIlat = data.getString("POILAT");
            POIlng = data.getString("POILNG");
        }

        createMapView();
        addMarker(POIname, POIdetails, Double.parseDouble(POIlat), Double.parseDouble(POIlng));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
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

    /**
     * Initialises the mapview
     */
    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                } else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    googleMap.setMyLocationEnabled(true);

                    UiSettings mapSettings;
                    mapSettings = googleMap.getUiSettings();
                    mapSettings.setCompassEnabled(true);
                    mapSettings.setMyLocationButtonEnabled(true);
                    mapSettings.setZoomControlsEnabled(true);

                }
            }
        } catch (NullPointerException exception){
            Log.e("dreamdestinationaday", exception.toString());
        }
    }

    /**
     * Adds a marker to the map
     */
    private void addMarker(String POIname, String POIdetail, double POIlat, double POIlng){

        /** Make sure that the map has been initialised **/
        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(POIlat, POIlng))
                            .title(POIname)
                            .snippet(POIdetail)
                            .draggable(false)
            );
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(POIlat, POIlng))
                    .zoom(5)
                    .build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

}

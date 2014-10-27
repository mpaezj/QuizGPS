package com.mpaezj.mauriciopaezgps;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
        	getSupportFragmentManager().beginTransaction().
        		add(R.id.main,new expo()).commit();
        	
        }
    }
    public static class expo extends Fragment implements LocationListener{
    	private LocationManager mLocationManager;
    	private View rootView;
    	private String TAG = expo.class.getSimpleName(); 
    	protected Button mButton, b2;
    	protected TextView tv1, tv2, tv3;
    	protected boolean mStarted;
    	protected double la=0;
    	protected double lo=0;
    	
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater,
    			ViewGroup container,
    			Bundle savedInstanceState) {
    		rootView = inflater.inflate(R.layout.container, container, false);
    		mButton = (Button) rootView.findViewById(R.id.button1);
    		b2 = (Button) rootView.findViewById(R.id.button2);
    		b2.setVisibility(View.INVISIBLE);
    		tv1 = (TextView) rootView.findViewById(R.id.textView1);
			tv2 = (TextView) rootView.findViewById(R.id.textView2);
			tv3 = (TextView) rootView.findViewById(R.id.textView3);
			mButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
						if(mStarted) {
							stopCapturing();
							Toast.makeText(rootView.getContext(), "Detenido", Toast.LENGTH_SHORT).show();
							mStarted = !mStarted;
							mButton.setText("Comenzar");
						} else {
							startCapturing();
							b2.setVisibility(View.VISIBLE);
						}
										
				}
			});   
			b2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					la=Double.parseDouble(tv1.getText().toString());
					lo=Double.parseDouble(tv2.getText().toString());
					
										
				}
			});
    		
    		return rootView;
    	}

		@Override
		public void onLocationChanged(Location location) {
			try {
				tv1.setText(location.getLatitude()+"");
				tv2.setText(location.getLongitude()+"");
				if(la!=0)
				{
					Location ini = new Location("");
					ini.setLongitude(lo);
					ini.setLatitude(la);
					double dist = ini.distanceTo(location);
					if(dist<5){
						tv3.setText("Se encuentra a menos de 5 metros del punto guardado, exactamente "+dist);
					}else{
						tv3.setText("distacia: "+ dist);
					}
					
					
				}
			}catch(Exception e){
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
		}
		

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		public void startCapturing() {
			boolean gps_enabled = true;
			mLocationManager = (LocationManager) rootView.getContext().getSystemService(Context.LOCATION_SERVICE);

			try {
				gps_enabled = mLocationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception ex) {			
			}
			
			Log.d(TAG, "gps_enabled " + gps_enabled);
			if (!gps_enabled ) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						getActivity());
				dialog.setMessage("GPS no esta habilitado!").setPositiveButton("OK", null);
				dialog.show();

			} else {
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
				Toast.makeText(getActivity(), "Comenzado", Toast.LENGTH_SHORT)
						.show();
				mStarted = !mStarted;
				mButton.setText("Detener");
			}
		}
		
		public void stopCapturing() {
			b2.setVisibility(View.INVISIBLE);
			if (mLocationManager != null) {
				mLocationManager.removeUpdates(this);
			}
		}
    	
    }
}

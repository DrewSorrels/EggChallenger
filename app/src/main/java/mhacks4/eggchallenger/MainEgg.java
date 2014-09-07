package mhacks4.eggchallenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.openxc.measurements.AmbientTemp;
import com.openxc.measurements.EngCoolTemp;
import com.openxc.measurements.Measurement;

public class MainEgg extends Activity {
    private double insideTemp;
    private double engineTemp;
    private final static String TAG = "VehicleManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_egg);

        double time;
        time = calcTime();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_egg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private double calcTime(){
        // Estimate mass of egg (AA Large egg): 57g
        int mass = 57;
        // Egg must reach between 144 and 158° F
        double t1 = 144;
        // Specific heat of egg: 3.18 J/g C
        double specificHeat = 3.18;

        // Read temperature inside.

        AmbientTemp.Listener mAmbientTemp =
            new AmbientTemp.Listener() {
                public void receive(Measurement measurement) {
                    final AmbientTemp atemp = (AmbientTemp) measurement;
                    insideTemp = Double.parseDouble(atemp.toString());
                }
            };
        // Read temperature of engine block
        EngCoolTemp.Listener mEngCoolTemp =
            new EngCoolTemp.Listener() {
                public void receive(Measurement measurement) {
                     EngCoolTemp cooltemp =
                            (EngCoolTemp) measurement;
                    engineTemp = Double.parseDouble(cooltemp.toString());
                }
            };

        // Calculate delta t to be difference in temp of 144 and insideTemp
        double dt = t1 - insideTemp;

        // Volume: 0.021978
        double v = 0.021978;
        // Thermal Conductivity of egg: 0.475545
        double tc = 0.475545;
        // Thickness of egg estimated to be 0.01m
        double thickness = 0.01;
        double surfaceArea = v/thickness;

        double dtEngineEgg = Math.abs(insideTemp - engineTemp);

        // Rate of transfer joules/s and heat joules
        double rateOfTransfer = tc * surfaceArea * dtEngineEgg / thickness;
        double q = mass * specificHeat * dt;

        return q / rateOfTransfer;
        // Sources:
        // http://www.sciencedirect.com/science/article/pii/S0260877405001330
        // http://www.answers.com/Q/The_Density_of_an_egg
        // http://www.howmuchisin.com/produce_converters/volume-of-an-egg
    }
}

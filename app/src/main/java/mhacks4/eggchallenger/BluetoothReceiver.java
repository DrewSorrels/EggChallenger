package mhacks4.eggchallenger;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mhacks4.eggchallenger.MainEgg;


public class BluetoothReceiver extends BroadcastReceiver {
    private final static String TAG = BluetoothReceiver.class.getSimpleName();

    private final boolean isVehicleInterface(BluetoothDevice device) {
        return device != null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice bluetoothDevice = (BluetoothDevice)
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if(isVehicleInterface(bluetoothDevice)) {
            if(intent.getAction().compareTo(
                        BluetoothDevice.ACTION_ACL_CONNECTED) == 0){
                context.startService(new Intent(context, MainEgg.class));
            } else {
                // Intentionally does nothing
            }
        }
    }
}

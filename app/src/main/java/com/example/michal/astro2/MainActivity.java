package com.example.michal.astro2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michal.astro2.data.Atmosphere;
import com.example.michal.astro2.data.Channel;
import com.example.michal.astro2.data.Item;
import com.example.michal.astro2.data.Location;
import com.example.michal.astro2.service.WeatherServiceCallback;
import com.example.michal.astro2.service.YahooWeatherService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements WeatherServiceCallback {

    TextView tvTemp,tvCondition,tvLocation, tvLongitude, tvLatitude, tvTime, tvPressure, tvWindSpeed, tvWindDirection, tvHumidity, tvVisiblity, tvForecastTemp, tvForecastDate, tvForecastCondition, tvForecastTemp2, tvForecastDate2, tvForecastCondition2, tvForecastTemp3, tvForecastDate3, tvForecastCondition3, tvForecastTemp4, tvForecastDate4, tvForecastCondition4;
    ImageView image;
    EditText textInput;
    Button button, buttonSwap, buttonSave, buttonClear;
    boolean connection,cf = true;
    YahooWeatherService service;
    ProgressDialog dialog;
    String memory = "";
    String city = "";
    List<String> list = new ArrayList<String>();
    Spinner spinner;
    Context context;
    Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvCondition = (TextView) findViewById(R.id.tvCondition);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvPressure = (TextView) findViewById(R.id.tvPressure);
        tvVisiblity = (TextView) findViewById(R.id.tvVisibility);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvWindDirection = (TextView) findViewById(R.id.tvWindDirection);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);
        tvForecastDate = (TextView) findViewById(R.id.tvForecastDate);
        tvForecastTemp = (TextView) findViewById(R.id.tvForecastTemp);
        tvForecastCondition = (TextView) findViewById(R.id.tvForecastCondition);
        tvForecastDate2 = (TextView) findViewById(R.id.tvForecastDate2);
        tvForecastTemp2 = (TextView) findViewById(R.id.tvForecastTemp2);
        tvForecastCondition2 = (TextView) findViewById(R.id.tvForecastCondition2);
        tvForecastDate3 = (TextView) findViewById(R.id.tvForecastDate3);
        tvForecastTemp3 = (TextView) findViewById(R.id.tvForecastTemp3);
        tvForecastCondition3 = (TextView) findViewById(R.id.tvForecastCondition3);
        tvForecastDate4 = (TextView) findViewById(R.id.tvForecastDate4);
        tvForecastTemp4 = (TextView) findViewById(R.id.tvForecastTemp4);
        tvForecastCondition4 = (TextView) findViewById(R.id.tvForecastCondition4);
        image = (ImageView) findViewById(R.id.image);
        textInput = (EditText) findViewById(R.id.textInput);
        button = (Button) findViewById(R.id.button);
        buttonSwap = (Button) findViewById(R.id.buttonSwap);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        spinner = (Spinner) findViewById(R.id.spinner);

        context = this;

        refreshConnection();

        memory = readFromFile("config.txt");

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Update();
            }
        });

        buttonSwap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cf = !cf;
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                memory += city;
                writeToFile(memory, "config.txt");
                memory = readFromFile("config.txt");
                spinner.setSelection(list.size() - 1);
                writeToFile(channel.data.toString(), city + ".txt");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                memory = "";
                writeToFile(memory, "config.txt");
                memory = readFromFile("config.txt");
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textInput.setText(spinner.getSelectedItem().toString());
                Update();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }
    private boolean executeCommand(){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue "+mExitValue);
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return false;
    }

    private void refreshConnection() {
        Thread t = new Thread() {

            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(executeCommand()) {
                                    connection = true;
                                }
                                else {
                                    connection = false;
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    @Override
    public void serviceSucces(Channel chanel) {
        dialog.hide();
        channel = chanel;
        if(list.contains(city)) {
            writeToFile(channel.data.toString(), textInput.getText().toString() + ".txt");
        }
        setAll(chanel);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void go(String text, boolean cf) {
        char cfc = 'c';
        if(cf) {
            cfc = 'c';
        } else {
            cfc = 'f';
        }
        if(connection) {
            service = new YahooWeatherService(this);
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.show();
            service.refreshWeather(text,cfc);
        }
        else {
            if(list.contains(textInput.getText().toString())) {
                JSONObject json = null;
                try {
                    json = new JSONObject(readFromJSON(textInput.getText().toString() + ".txt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Channel channel = new Channel();
                channel.populate(json);
                setAll(channel);
            }

        }
    }

    private void writeToFile(String data, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        int prev = 0;
        list.clear();
        for(int j = 1; j < ret.length(); j++) {
            if(ret.charAt(j) == Character.toUpperCase(ret.charAt(j))) {
                String text = ret.substring(prev,j);
                if(!list.contains(text)) {
                    list.add(text);
                }
                prev = j;
            }
            if(j == ret.length() - 1 && ret.length() > 0) {
                String text = ret.substring(prev);
                if(!list.contains(text)) {
                    list.add(text);
                }
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        return ret;
    }

    private String readFromJSON(String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void Update() {
        String test = textInput.getText().toString();
        if(list.contains(test)) {
            try {
                JSONObject json = new JSONObject(readFromJSON(test + ".txt"));
                Channel channel = new Channel();
                channel.populate(json);
                Item item = channel.getItem();
                SimpleDateFormat sdf = new SimpleDateFormat("HH");
                String currentTime = sdf.format(new Date());
                String time;
                if (item.getPubDate().contains("AM")) {
                    time = item.getPubDate().substring(item.getPubDate().indexOf("AM") - 6, item.getPubDate().indexOf("AM") - 4);
                    if (Integer.parseInt(time) == 12) {
                        time = "00";
                    }
                } else {
                    time = item.getPubDate().substring(item.getPubDate().indexOf("PM") - 6, item.getPubDate().indexOf("PM") - 4);
                    time = "" + (Integer.parseInt(time) + 12);
                }
                if (!time.equals(currentTime)) {
                    go(test, cf);
                } else {
                    Toast.makeText(context, "No need", Toast.LENGTH_SHORT).show();
                    connection = false;
                    go(test,cf);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            go(test, cf);
        }
    }

    private void setAll(Channel chanel) {
        Item item = chanel.getItem();
        Location location = chanel.getLocation();
        city = location.getCity();
        Atmosphere atmosphere = chanel.getAtmosphere();
        tvLocation.setText(location.getCity());
        tvLongitude.setText(Double.toString(item.getLongitude()));
        tvLatitude.setText(Double.toString(item.getLatitude()));
        tvTime.setText(item.getPubDate());
        tvPressure.setText(Double.toString(atmosphere.getPressure()) + chanel.getUnits().getPressure());
        tvTemp.setText(item.getCondition().getTemperature() + "\u00B0" + chanel.getUnits().getTemperature());
        tvCondition.setText(item.getCondition().getDescription());
        Picasso.with(this).load(item.getDescription().substring(item.getDescription().indexOf("src=") + 5,item.getDescription().indexOf("/>") - 1)).resize(500,500).into(image);
        tvWindSpeed.setText("Speed " + chanel.getWind().getSpeed());
        tvWindDirection.setText("Direction " + chanel.getWind().getDirection());
        tvHumidity.setText("Humidity " + chanel.getAtmosphere().getHumidity());
        tvVisiblity.setText("Visbility " + chanel.getAtmosphere().getVisibility());
        tvForecastDate.setText(chanel.getItem().getForecast1().getDay() + ", " + chanel.getItem().getForecast1().getDate());
        tvForecastTemp.setText(chanel.getItem().getForecast1().getLow() + "\u00B0" + chanel.getUnits().getTemperature() + " - " +chanel.getItem().getForecast1().getHigh() + "\u00B0" + chanel.getUnits().getTemperature());
        tvForecastCondition.setText(chanel.getItem().getForecast1().getText());
        if(isTablet() && getRotation()) {
            tvForecastDate2.setText(chanel.getItem().getForecast2().getDay() + ", " + chanel.getItem().getForecast2().getDate());
            tvForecastTemp2.setText(chanel.getItem().getForecast2().getLow() + "\u00B0" + chanel.getUnits().getTemperature() + " - " +chanel.getItem().getForecast2().getHigh() + "\u00B0" + chanel.getUnits().getTemperature());
            tvForecastCondition2.setText(chanel.getItem().getForecast2().getText());
            tvForecastDate3.setText(chanel.getItem().getForecast3().getDay() + ", " + chanel.getItem().getForecast3().getDate());
            tvForecastTemp3.setText(chanel.getItem().getForecast3().getLow() + "\u00B0" + chanel.getUnits().getTemperature() + " - " +chanel.getItem().getForecast3().getHigh() + "\u00B0" + chanel.getUnits().getTemperature());
            tvForecastCondition3.setText(chanel.getItem().getForecast3().getText());
            tvForecastDate4.setText(chanel.getItem().getForecast4().getDay() + ", " + chanel.getItem().getForecast2().getDate());
            tvForecastTemp4.setText(chanel.getItem().getForecast4().getLow() + "\u00B0" + chanel.getUnits().getTemperature() + " - " +chanel.getItem().getForecast4().getHigh() + "\u00B0" + chanel.getUnits().getTemperature());
            tvForecastCondition4.setText(chanel.getItem().getForecast4().getText());
        }
        if(!isTablet() && !getRotation()) {
            tvForecastDate2.setText(chanel.getItem().getForecast2().getDay() + ", " + chanel.getItem().getForecast2().getDate());
            tvForecastTemp2.setText(chanel.getItem().getForecast2().getLow() + "\u00B0" + chanel.getUnits().getTemperature() + " - " +chanel.getItem().getForecast2().getHigh() + "\u00B0" + chanel.getUnits().getTemperature());
            tvForecastCondition2.setText(chanel.getItem().getForecast2().getText());
        }

    }

    public boolean getRotation(){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return true;
            case Surface.ROTATION_90:
                return false;
            case Surface.ROTATION_180:
                return true;
            default:
                return false;
        }
    }

    public boolean isTablet() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        if(screenInches > 6) {
            return true;
        }
        else {
            return false;
        }
    }
}

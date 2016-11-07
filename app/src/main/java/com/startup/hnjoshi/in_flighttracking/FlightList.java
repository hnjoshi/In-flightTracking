package com.startup.hnjoshi.in_flighttracking;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by hnjoshi on 11/5/2016.
 */

public class FlightList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] departCode;
    private final String[] arrivalCode;
//    private final String[] departAirport;
//    private final String[] arrivalAirport;

    public FlightList(Activity context, String[] departCode, String[] arrivalCode) {
        super(context, R.layout.list_single, departCode);
        this.context = context;
        this.departCode = departCode;
        this.arrivalCode = arrivalCode;
//        this.departAirport = departAirport;
//        this.arrivalAirport = arrivalAirport;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, true);
        TextView titleTextView = (TextView)rowView.findViewById(R.id.titleTextView);
//        TextView departTextView = (TextView)rowView.findViewById(R.id.DepartTextView);
//        TextView arrivalTextView = (TextView)rowView.findViewById(R.id.ArrivalTextView);

        titleTextView.setText(departCode[position] +" - "+ arrivalCode[position]);
//        departTextView.setText(departAirport[position]);
//        arrivalTextView.setText(arrivalAirport[position]);

        return rowView;
    }
}

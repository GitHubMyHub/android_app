package com.toolbardemo.mytestapp.widgets;

import android.app.DatePickerDialog;

import android.content.Context;

import android.util.Log;

import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Locale;


/**
 * Created by V-Windows on 06.03.2018.
 *
 */

public class MyEditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ImageButton btnCalender;
    private Calendar myCalendar;
    private Context context;
    private EditText editText;

    public MyEditTextDatePicker(ImageButton btnCalender, Context context){
        Log.d("MyEditTextDatePicker", "MyEditTextDatePicker");
        this.btnCalender = btnCalender;
        this.context = context;
        myCalendar = Calendar.getInstance();
        btnCalender.setOnClickListener(this);
    }

    public void setDurabilityField(EditText editText){
        this.editText = editText;
    }

    @Override
    public void onClick(View view) {

        new DatePickerDialog(context, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Log.d("MyEditTextDatePicker", "onDateSet");

        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.GERMANY);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = sdformat.format(myCalendar.getTime());

        Log.d("onDateSet", date);

        editText.setText(date);

    }

}

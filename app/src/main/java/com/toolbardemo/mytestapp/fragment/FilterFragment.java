package com.toolbardemo.mytestapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.toolbardemo.mytestapp.DAO.FilterDAO;
import com.toolbardemo.mytestapp.MainActivity;
import com.toolbardemo.mytestapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements View.OnClickListener {

    private static final String SWITCH_1 = "SWITCH_1";
    private static final String SWITCH_2 = "SWITCH_2";
    private static final String SWITCH_3 = "SWITCH_3";
    private static final String SWITCH_4 = "SWITCH_4";
    private static final String SWITCH_5 = "SWITCH_5";
    private static final String SWITCH_6 = "SWITCH_6";
    private static final String SWITCH_7 = "SWITCH_7";
    private static final String SWITCH_8 = "SWITCH_8";
    private static final String SWITCH_9 = "SWITCH_9";

    public static final String TAG = "FilterFragment";

    FilterDAO filterDAO;


    SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    @BindView(R.id.switch1) Switch switch1;
    @BindView(R.id.switch2) Switch switch2;
    @BindView(R.id.switch3) Switch switch3;
    @BindView(R.id.switch4) Switch switch4;
    @BindView(R.id.switch5) Switch switch5;
    @BindView(R.id.switch6) Switch switch6;
    @BindView(R.id.switch7) Switch switch7;
    @BindView(R.id.switch8) Switch switch8;
    @BindView(R.id.switch9) Switch switch9;
    @BindView(R.id.textViewOption1) TextView textViewOption1;
    @BindView(R.id.textViewOption2) TextView textViewOption2;
    @BindView(R.id.textViewOption3) TextView textViewOption3;
    @BindView(R.id.textViewOption4) TextView textViewOption4;
    @BindView(R.id.textViewOption5) TextView textViewOption5;
    @BindView(R.id.textViewOption6) TextView textViewOption6;
    @BindView(R.id.textViewOption7) TextView textViewOption7;
    @BindView(R.id.textViewOption8) TextView textViewOption8;
    @BindView(R.id.textViewOption9) TextView textViewOption9;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterDAO = new FilterDAO();

        loadPrefs();

        /*Log.d("spermissionCheck- Write", Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("spermissionCheck- Read", Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("spermissionCheck- Read", Manifest.permission.INTERNET);*/


        /*checkWrite.setChecked(getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        checkRead.setChecked(getPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        checkInternet.setChecked(getPermission(Manifest.permission.INTERNET));*/

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");

        if(view instanceof Switch) {

            if (view.getId() == R.id.switch1) {
                savePrefs(SWITCH_1, switch1.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption1.getText().toString(), switch1.isChecked());
            } else if (view.getId() == R.id.switch2) {
                savePrefs(SWITCH_2, switch2.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption2.getText().toString(), switch2.isChecked());
            } else if (view.getId() == R.id.switch3) {
                savePrefs(SWITCH_3, switch3.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption3.getText().toString(), switch3.isChecked());
            } else if (view.getId() == R.id.switch4) {
                savePrefs(SWITCH_4, switch4.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption4.getText().toString(), switch4.isChecked());
            } else if (view.getId() == R.id.switch5) {
                savePrefs(SWITCH_5, switch5.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption5.getText().toString(), switch5.isChecked());
            } else if (view.getId() == R.id.switch6) {
                savePrefs(SWITCH_6, switch6.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption6.getText().toString(), switch6.isChecked());
            } else if (view.getId() == R.id.switch7) {
                savePrefs(SWITCH_7, switch7.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption7.getText().toString(), switch7.isChecked());
            } else if (view.getId() == R.id.switch8) {
                savePrefs(SWITCH_8, switch8.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption8.getText().toString(), switch8.isChecked());
            } else if (view.getId() == R.id.switch9) {
                savePrefs(SWITCH_9, switch9.isChecked());
                filterDAO.setFilter(MainActivity.mProfile.getUid(), textViewOption9.getText().toString(), switch9.isChecked());
            }
        }

    }

    private boolean getPermission(String permission){

        if(ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED){
            Log.d("Permission", "JA");
            return true;
        }else{
            Log.d("Permission", "NEIN");
            return false;
        }

    }

    private void loadPrefs(){
        Log.d(TAG, "loadPrefs");

        sharedPref = getActivity().getSharedPreferences("prefsData", Context.MODE_PRIVATE);

        boolean swValue1 = sharedPref.getBoolean(SWITCH_1, false);
        boolean swValue2 = sharedPref.getBoolean(SWITCH_2, false);
        boolean swValue3 = sharedPref.getBoolean(SWITCH_3, false);
        boolean swValue4 = sharedPref.getBoolean(SWITCH_4, false);
        boolean swValue5 = sharedPref.getBoolean(SWITCH_5, false);
        boolean swValue6 = sharedPref.getBoolean(SWITCH_6, false);
        boolean swValue7 = sharedPref.getBoolean(SWITCH_7, false);
        boolean swValue8 = sharedPref.getBoolean(SWITCH_8, false);
        boolean swValue9 = sharedPref.getBoolean(SWITCH_9, false);

        switch1.setChecked(swValue1);
        switch2.setChecked(swValue2);
        switch3.setChecked(swValue3);
        switch4.setChecked(swValue4);
        switch5.setChecked(swValue5);
        switch6.setChecked(swValue6);
        switch7.setChecked(swValue7);
        switch8.setChecked(swValue8);
        switch9.setChecked(swValue9);

    }


    private void savePrefs(String key, boolean value){
        Log.d(TAG, "savePrefs");

        sharedPref = getActivity().getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        edit = sharedPref.edit();
        //Log.d(CHECKBOX_TORCH, Boolean.toString(value));
        edit.putBoolean(key, value);
        edit.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_single_filter, container, false);

        ButterKnife.bind(this, view);

        switch1.setOnClickListener(this);
        switch2.setOnClickListener(this);
        switch3.setOnClickListener(this);
        switch4.setOnClickListener(this);
        switch5.setOnClickListener(this);
        switch6.setOnClickListener(this);
        switch7.setOnClickListener(this);
        switch8.setOnClickListener(this);
        switch9.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

}

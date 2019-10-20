package com.toolbardemo.mytestapp.fragment;


import android.Manifest;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.image.FileCache;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final String CHECKBOX_TORCH = "CHECKBOX_TORCH";

    CheckBox checkBoxTorch;

    SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CheckBox checkWrite  = view.findViewById(R.id.checkBoxWrite);
        CheckBox checkRead  = view.findViewById(R.id.checkBoxRead);
        CheckBox checkInternet  = view.findViewById(R.id.checkBoxInternet);

        checkBoxTorch = getActivity().findViewById(R.id.checkBoxTorch);
        loadPrefs();

        Log.d("spermissionCheck- Write", Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("spermissionCheck- Read", Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("spermissionCheck- Read", Manifest.permission.INTERNET);


        checkWrite.setChecked(getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        checkRead.setChecked(getPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        checkInternet.setChecked(getPermission(Manifest.permission.INTERNET));


        checkBoxTorch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrefs(CHECKBOX_TORCH, checkBoxTorch.isChecked());
            }
        });


        Button buttonClear = getActivity().findViewById(R.id.buttonClearFolder);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileCache fileCache = new FileCache(getActivity());
                fileCache.clear();
                Toast.makeText(getActivity(), R.string.toast_clear_cache, Toast.LENGTH_SHORT).show();
            }
        });


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
        sharedPref = getActivity().getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        boolean cbTorchValue = sharedPref.getBoolean(CHECKBOX_TORCH, false);

        Log.d("LoadPrefs", Boolean.toString(cbTorchValue));

        checkBoxTorch.setChecked(cbTorchValue);

    }


    private void savePrefs(String key, boolean value){
        sharedPref = getActivity().getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        edit = sharedPref.edit();
        Log.d(CHECKBOX_TORCH, Boolean.toString(value));
        edit.putBoolean(key, value);
        edit.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settings, container, false);
        return inflater.inflate(R.layout.list_single_settings, container, false);
    }

}

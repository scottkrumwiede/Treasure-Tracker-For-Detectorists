
package com.mdtt.scott.treasuretrackerfordetectorists;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class AddCladInfoFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private EditText cladAmountEditText;
    private EditText cladLocationFoundEditText;
    private TextView cladDateFoundTextView;
    private Spinner cladCurrencySpinner;
    private int cladCurrencyPositionSelected;
    private ArrayList<String> currencyList;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int year;
    private int month;
    private int day;
    private InputMethodManager inputManager;

    public AddCladInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_clad_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DecimalFormat df = new DecimalFormat("#0.00");

        cladAmountEditText = view.findViewById(R.id.clad_amount_edittext);

        cladAmountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus)
                {
                    String oldValue = cladAmountEditText.getText().toString();
                    if(!oldValue.isEmpty())
                    {
                        double num = Double.valueOf(oldValue);
                        if(num < 0.01)
                        {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Amount must be at least 0.01", Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String newValue = df.format(num);
                            cladAmountEditText.setText(newValue);
                        }
                    }
                }
            }

        });

        cladLocationFoundEditText = view.findViewById(R.id.cladLocationFoundEditText);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        String date = month + 1 + "/" + day + "/" + year;

        cladDateFoundTextView = view.findViewById(R.id.cladFoundTextView);
        cladDateFoundTextView.setText(date);
        cladDateFoundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int newYear, int newMonth, int newDay) {

                //Log.d("TEST", "onDateSet: mm/dd/yyy: " + newMonth + "/" + newDay + "/" + newYear);

                year = newYear;
                month = newMonth;
                day = newDay;

                String date = newMonth + 1 + "/" + newDay + "/" + newYear;
                cladDateFoundTextView.setText(date);
            }
        };

        Button placePickerButton = view.findViewById(R.id.placePickerButton);
        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cladLocationFoundEditText.requestFocus();

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }
                else
                {
                    LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location != null)
                    {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();

                        Geocoder gCoder = new Geocoder(getContext());
                        List<Address> addresses = null;
                        try {
                            addresses = gCoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null && addresses.size() > 0) {
                            cladLocationFoundEditText.setText(addresses.get(0).getAddressLine(0));
                        }
                    }
                    else
                    {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Could not get current location.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cladCurrencySpinner = view.findViewById(R.id.clad_currency_spinner);
        cladCurrencyPositionSelected = cladCurrencySpinner.getSelectedItemPosition();
        currencyList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.clad_currency_array)));

        ArrayAdapter<String> currencySpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, currencyList);
        currencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cladCurrencySpinner.setAdapter(currencySpinnerAdapter);
        cladCurrencySpinner.setSelection(cladCurrencyPositionSelected);
        currencySpinnerAdapter.notifyDataSetChanged();

        cladCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(cladCurrencyPositionSelected != position)
                {
                    final int previousCurrency = cladCurrencyPositionSelected;
                    //Log.d("test", "starting currencySpinner because:\ncladCurrencyPositionSelected= "+cladCurrencyPositionSelected+"\nposition= "+position);
                    if(parentView.getItemAtPosition(position).toString().equals("Custom..."))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Custom Currency:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String task = String.valueOf(taskEditText.getText());
                                        if(task.isEmpty())
                                        {
                                            cladCurrencySpinner.setSelection(previousCurrency);
                                            cladCurrencyPositionSelected = previousCurrency;
                                            return;
                                        }
                                        while(currencyList.size() > 5)
                                        {
                                            currencyList.remove(currencyList.size()-1);
                                        }
                                        currencyList.add(task);
                                        currencyList.add("Custom...");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currencyList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        cladCurrencySpinner.setAdapter(spinnerAdapter);
                                        cladCurrencySpinner.setSelection(currencyList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(),"Custom currency added!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cladCurrencySpinner.setSelection(previousCurrency);
                                        cladCurrencyPositionSelected = previousCurrency;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cladCurrencySpinner.setSelection(previousCurrency);
                                cladCurrencyPositionSelected = previousCurrency;
                            }
                        });
                    }
                }
                //Log.d("test", "coinCountrySelected="+cladCurrencyPositionSelected+" changed to "+position);
                cladCurrencyPositionSelected = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getContext(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveClad() {
        cladLocationFoundEditText.requestFocus();

        if(cladAmountEditText.getText().toString().isEmpty())
        {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Enter an amount first.", Snackbar.LENGTH_SHORT).show();
            cladAmountEditText.requestFocus();
        }
        else if(Double.valueOf(cladAmountEditText.getText().toString())<0.01)
        {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Amount must be at least 0.01", Snackbar.LENGTH_SHORT).show();
            cladAmountEditText.requestFocus();
        }
        else
        {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm?")
                    .setMessage("Add this clad now?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            double cladAmount = Double.valueOf(cladAmountEditText.getText().toString());

                            Clad clad = new Clad(0, cladCurrencySpinner.getSelectedItem().toString(), cladAmount, cladLocationFoundEditText.getText().toString(),cladDateFoundTextView.getText().toString());

                            MySQliteHelper helper = new MySQliteHelper(getContext());
                            long result = helper.addClad(clad);
                            if(result == -1)
                            {
                                Toast.makeText(getContext(), "Failed to add to database!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "New clad added!", Toast.LENGTH_SHORT).show();
                                getActivity().setResult(Activity.RESULT_OK,null);
                                getActivity().finish();
                            }
                        }
                    }).create().show();
        }
    }
}


package com.mdtt.scott.treasuretrackerfordetectorists;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Objects;


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
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().invalidateOptionsMenu();
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

        cladAmountEditText.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus)
            {
                String oldValue = cladAmountEditText.getText().toString();
                if(!oldValue.isEmpty())
                {
                    if(!oldValue.equals("."))
                    {
                        double num = Double.parseDouble(oldValue);
                        if(num < 0.01)
                        {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Amount must be at least 0.01", Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String newValue = df.format(num);
                            cladAmountEditText.setText(newValue);
                        }
                    }
                    else
                    {

                        cladAmountEditText.setText(R.string.Zero_CladAmountEditText);
                    }
                }
            }
        });

        cladLocationFoundEditText = view.findViewById(R.id.cladLocationFoundEditText);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        month++;

        String date = month + "/" + day + "/" + year;

        String[] splitDate = date.split("/");
        if(splitDate[0].length() == 1)
        {
            splitDate[0] = "0"+splitDate[0];
        }
        if(splitDate[1].length() == 1)
        {
            splitDate[1] = "0"+splitDate[1];
        }

        date = splitDate[0]+"/"+splitDate[1]+"/"+splitDate[2];

        cladDateFoundTextView = view.findViewById(R.id.cladFoundTextView);
        cladDateFoundTextView.setText(date);
        cladDateFoundTextView.setOnClickListener(view12 -> {

            DatePickerDialog dialog = new DatePickerDialog(requireActivity(),
                    android.R.style.Theme_DeviceDefault_Light_Dialog,
                    mDateSetListener,
                    year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();
        });

        mDateSetListener = (datePicker, newYear, newMonth, newDay) -> {

            //Log.d("TEST", "onDateSet: mm/dd/yyy: " + newMonth + "/" + newDay + "/" + newYear);

            year = newYear;
            month = newMonth;
            day = newDay;

            newMonth++;

            String date1 = newMonth + "/" + newDay + "/" + newYear;

            String[] splitDate1 = date1.split("/");
            if(splitDate1[0].length() == 1)
            {
                splitDate1[0] = "0"+ splitDate1[0];
            }
            if(splitDate1[1].length() == 1)
            {
                splitDate1[1] = "0"+ splitDate1[1];
            }

            date1 = splitDate1[0]+"/"+ splitDate1[1]+"/"+ splitDate1[2];

            cladDateFoundTextView.setText(date1);
        };

        Button placePickerButton = view.findViewById(R.id.placePickerButton);
        placePickerButton.setOnClickListener(view1 -> {

            cladLocationFoundEditText.requestFocus();

            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
        });

        cladCurrencySpinner = view.findViewById(R.id.clad_currency_spinner);
        cladCurrencyPositionSelected = cladCurrencySpinner.getSelectedItemPosition();
        currencyList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.clad_currency_array)));

        ArrayAdapter<String> currencySpinnerAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, currencyList);
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
                    if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        taskEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
                        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                                .setTitle("Custom Currency:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", (dialog13, which) -> {
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
                                    currencyList.add("Custom…");
                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, currencyList);
                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    cladCurrencySpinner.setAdapter(spinnerAdapter);
                                    cladCurrencySpinner.setSelection(currencyList.size()-2);
                                    spinnerAdapter.notifyDataSetChanged();
                                })
                                .setNegativeButton("Cancel", (dialog12, which) -> {
                                    cladCurrencySpinner.setSelection(previousCurrency);
                                    cladCurrencyPositionSelected = previousCurrency;
                                })
                                .create();
                        dialog.show();
                        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(dialog1 -> {
                            cladCurrencySpinner.setSelection(previousCurrency);
                            cladCurrencyPositionSelected = previousCurrency;
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
        String selectedCurrency = cladCurrencySpinner.getSelectedItem().toString().toUpperCase();
        if(!selectedCurrency.startsWith("("))
        {
            selectedCurrency = "("+selectedCurrency;
        }
        if(!selectedCurrency.endsWith(")"))
        {
            selectedCurrency = selectedCurrency + ")";
        }

        if(cladAmountEditText.getText().toString().isEmpty())
        {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Enter an amount first.", Snackbar.LENGTH_SHORT).show();
            cladAmountEditText.requestFocus();
        }
        else if(Double.parseDouble(cladAmountEditText.getText().toString())<0.01)
        {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Amount must be at least 0.01", Snackbar.LENGTH_SHORT).show();
            cladAmountEditText.requestFocus();
        }
        else
        {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            final String finalSelectedCurrency = selectedCurrency;
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm?")
                    .setMessage("Add this clad now?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {

                        double cladAmount = Double.parseDouble(cladAmountEditText.getText().toString());

                        String oldDate = cladDateFoundTextView.getText().toString();
                        String[] splitDate = oldDate.split("/");
                        String cladFoundDate = splitDate[2]+"/"+splitDate[0]+"/"+splitDate[1];

                        Clad clad = new Clad(0, finalSelectedCurrency, cladAmount, cladLocationFoundEditText.getText().toString(),cladFoundDate);

                        MySQliteHelper helper = new MySQliteHelper(getContext());
                        long result = helper.addClad(clad);
                        if(result == -1)
                        {
                            Toast.makeText(getContext(), "Failed to add to database!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "New clad added!", Toast.LENGTH_SHORT).show();
                            requireActivity().setResult(Activity.RESULT_OK,null);
                            getActivity().finish();
                        }
                    }).create().show();
        }
    }
}

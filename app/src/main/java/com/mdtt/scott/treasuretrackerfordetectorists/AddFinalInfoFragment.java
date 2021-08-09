package com.mdtt.scott.treasuretrackerfordetectorists;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class AddFinalInfoFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private Bundle bundle;
    private TextView treasureDateFoundTextView;
    private EditText treasureLocationFoundEditText, treasureInfoEditText, collectionNameEditText;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int year;
    private int month;
    private int day;
    private int id;

    public AddFinalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        id = Objects.requireNonNull(bundle).getInt("id");
        requireActivity().invalidateOptionsMenu();

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_final_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        treasureDateFoundTextView = view.findViewById(R.id.treasureDateFoundTextView);
        treasureLocationFoundEditText = view.findViewById(R.id.treasureLocationEditText);
        treasureInfoEditText = view.findViewById(R.id.treasureInfoEditText);
        collectionNameEditText = view.findViewById(R.id.collectionNameEditText);
        TextView collectionNameTextView = view.findViewById(R.id.collectionNameLabel);

        if (Objects.requireNonNull(bundle.getString("type")).equals("collection")) {
            collectionNameTextView.setVisibility(View.VISIBLE);
            collectionNameEditText.setVisibility(View.VISIBLE);
            treasureLocationFoundEditText.setHint("Include a location for your collection");
            treasureInfoEditText.setHint("Include any additional info about your collection");
        }

        month++;

        String date = month + "/" + day + "/" + year;

        String[] splitDate = date.split("/");
        if (splitDate[0].length() == 1) {
            splitDate[0] = "0" + splitDate[0];
        }
        if (splitDate[1].length() == 1) {
            splitDate[1] = "0" + splitDate[1];
        }

        date = splitDate[0] + "/" + splitDate[1] + "/" + splitDate[2];
        treasureDateFoundTextView.setText(date);

        if (id != 0) {
            String[] split = Objects.requireNonNull(bundle.getString("treasureDateFound")).split("/");
            //if year is first, we will format to mm/dd/yyyy instead
            if (split[0].length() == 4) {
                date = split[1] + "/" + split[2] + "/" + split[0];
                bundle.putString("treasureDateFound", date);
            }
        }

        //If the user had previously filled out info here but then backed up to a previous fragment before returning
        if (bundle.containsKey("treasureDateFound")) {
            repopulateInfo();
        }

        treasureDateFoundTextView.setOnClickListener(view12 -> {

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
            if (splitDate1[0].length() == 1) {
                splitDate1[0] = "0" + splitDate1[0];
            }
            if (splitDate1[1].length() == 1) {
                splitDate1[1] = "0" + splitDate1[1];
            }

            date1 = splitDate1[0] + "/" + splitDate1[1] + "/" + splitDate1[2];

            treasureDateFoundTextView.setText(date1);
        };

        Button getCurrentLocationButton = view.findViewById(R.id.placePickerButton);
        getCurrentLocationButton.setOnClickListener(view1 -> {

            if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d("mytag", "We are attempting to request permission now!");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location == null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
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
                        treasureLocationFoundEditText.setText(addresses.get(0).getAddressLine(0));
                    }
                    else
                    {
                        Snackbar.make(requireActivity().findViewById(android.R.id.content), "Could not get current location.", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), "Could not get current location.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void repopulateInfo() {

        treasureDateFoundTextView.setText(bundle.getString("treasureDateFound"));
        treasureLocationFoundEditText.setText(bundle.getString("treasureLocationFound"));
        treasureInfoEditText.setText(bundle.getString("treasureInfo"));

        if (Objects.requireNonNull(bundle.getString("type")).equals("collection")) {
            collectionNameEditText.setText(bundle.getString("treasureName"));
        }
    }

    private void renameTempImages(final String timeAtAdd) {
        ContextWrapper cw = new ContextWrapper(requireActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getFilesDir();
        File subDir = new File(directory, "imageDir");
        boolean isSubDirCreated = subDir.exists();
        if (!isSubDirCreated)
            isSubDirCreated = subDir.mkdir();
        if(isSubDirCreated)
        {
            final String prefix = "temp_" + timeAtAdd;

            File[] files = subDir.listFiles((directory12, name) -> name.startsWith(prefix));
            //Log.d("TEST", "The number of temp_images found was: "+files.length);

            //Log.d("TEST", "Removing temp prefix from images at path: "+subDir.getPath());
            for (File file : files) {

                //remove temp_ chars from prefix string
                String newName = file.getName().substring(5);
                File newFile = new File(subDir, newName);
                //rename file from temp to permanent
                file.renameTo(newFile);
            }

            final String prefix2 = "edit_" + timeAtAdd;

            File[] files2 = subDir.listFiles((directory1, name) -> name.startsWith(prefix2));
            for (File file : files2) {

                //delete file that was saved in saveImage method
                file.delete();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                LocationManager lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location == null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
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
                        treasureLocationFoundEditText.setText(addresses.get(0).getAddressLine(0));
                    }
                    else
                    {
                        Snackbar.make(requireActivity().findViewById(android.R.id.content), "Could not get current location.", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), "Could not get current location.", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveTreasure() {
        final String type = bundle.getString("type");
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm?")
                .setMessage("Add this "+type+" now?")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, (arg0, arg1) -> {

                    String timeAtAdd = bundle.getString("timeAtAdd"); //treasurephotopath
                    String treasureName;
                    if (Objects.requireNonNull(type).equals("collection"))
                    {
                        treasureName = collectionNameEditText.getText().toString();
                    } else {
                        treasureName = bundle.getString("treasureName");
                    }
                    String coinCountry = bundle.getString("coinCountry");
                    String coinType = bundle.getString("coinType");
                    String treasureSeries = bundle.getString("treasureSeries");
                    String treasureYear = bundle.getString("treasureYear");
                    String coinMint = bundle.getString("coinMint");
                    String treasureMaterial = bundle.getString("treasureMaterial");
                    String treasureWeight = bundle.getString("treasureWeight");
                    String treasureWeightUnit = bundle.getString("treasureWeightUnit");
                    if (treasureWeightUnit == null)
                    {
                        treasureWeightUnit = "grams";
                    }

                    String oldDate = treasureDateFoundTextView.getText().toString();
                    String[] splitDate = oldDate.split("/");
                    String treasureFoundDate = splitDate[2]+"/"+splitDate[0]+"/"+splitDate[1];

                    String treasureLocationFound = treasureLocationFoundEditText.getText().toString();
                    String treasureInfo = treasureInfoEditText.getText().toString().trim();
                    //Log.d("TEST", type + " " + timeAtAdd + " " + coinCountry + " " + coinType + " " + treasureSeries + " " + treasureYear + " " + coinMint + " " + treasureMaterial);

                    Treasure treasure = new Treasure(0, type, coinCountry, coinType, treasureSeries, treasureName, treasureYear, coinMint, treasureMaterial, treasureWeight, treasureLocationFound, treasureFoundDate, treasureInfo, timeAtAdd, treasureWeightUnit);

                    MySQliteHelper helper = new MySQliteHelper(getContext());
                    long result = helper.addTreasure(treasure);
                    if(result == -1)
                    {
                        Toast.makeText(getActivity(), "Failed to add to database!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "New "+type+" added!", Toast.LENGTH_SHORT).show();

                        renameTempImages(timeAtAdd);
                    }

                    requireActivity().setResult(Activity.RESULT_OK,null);
                    getActivity().finish();

                }).create().show();
    }

    public void addToBundle() {
        bundle.putString("treasureDateFound", treasureDateFoundTextView.getText().toString());
        bundle.putString("treasureLocationFound", treasureLocationFoundEditText.getText().toString());
        bundle.putString("treasureInfo", treasureInfoEditText.getText().toString().trim());

        if(Objects.requireNonNull(bundle.getString("type")).equals("collection"))
        {
            bundle.putString("treasureName", collectionNameEditText.getText().toString());
        }
    }

    public void editTreasure() {
        final String type = bundle.getString("type");
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm?")
                .setMessage("Edit this "+type+" now?")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, (arg0, arg1) -> {

                    int treasureId = bundle.getInt("id");
                    String timeAtAdd = bundle.getString("timeAtAdd"); //treasurephotopath
                    String treasureName;
                    if(Objects.requireNonNull(type).equals("collection"))
                    {
                        treasureName = collectionNameEditText.getText().toString();
                    }
                    else
                    {
                        treasureName = bundle.getString("treasureName");
                    }
                    String coinCountry = bundle.getString("coinCountry");
                    String coinType = bundle.getString("coinType");
                    String treasureSeries = bundle.getString("treasureSeries");
                    String treasureYear = bundle.getString("treasureYear");
                    String coinMint = bundle.getString("coinMint");
                    String treasureMaterial = bundle.getString("treasureMaterial");
                    String treasureWeight = bundle.getString("treasureWeight");
                    String treasureWeightUnit = bundle.getString("treasureWeightUnit");

                    String oldDate = treasureDateFoundTextView.getText().toString();
                    String[] splitDate = oldDate.split("/");
                    String treasureFoundDate = splitDate[2]+"/"+splitDate[0]+"/"+splitDate[1];

                    String treasureLocationFound = treasureLocationFoundEditText.getText().toString();
                    String treasureInfo = treasureInfoEditText.getText().toString().trim();
                    //Log.d("TEST", type + " " + timeAtAdd + " " + coinCountry + " " + coinType + " " + treasureSeries + " " + treasureYear + " " + coinMint + " " + treasureMaterial);

                    Treasure treasure = new Treasure(treasureId, type, coinCountry, coinType, treasureSeries, treasureName, treasureYear, coinMint, treasureMaterial, treasureWeight, treasureLocationFound, treasureFoundDate, treasureInfo, timeAtAdd, treasureWeightUnit);

                    MySQliteHelper helper = new MySQliteHelper(getContext());
                    long result = helper.editTreasure(treasure);
                    if(result == -1)
                    {
                        Toast.makeText(getActivity(), "Failed to edit database!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), type+" successfully edited!", Toast.LENGTH_SHORT).show();

                        renameTempImages(timeAtAdd);
                    }

                    requireActivity().setResult(Activity.RESULT_OK,null);
                    getActivity().finish();

                }).create().show();
    }
}
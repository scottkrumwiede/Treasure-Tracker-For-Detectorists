package com.mdtt.scott.treasuretrackerfordetectorists;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FilenameFilter;
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

    public AddFinalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_final_info, container, false);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        treasureDateFoundTextView = view.findViewById(R.id.treasureDateFoundTextView);
        treasureLocationFoundEditText = view.findViewById(R.id.treasureLocationEditText);
        treasureInfoEditText = view.findViewById(R.id.treasureInfoEditText);
        collectionNameEditText = view.findViewById(R.id.collectionNameEditText);
        TextView collectionNameTextView = view.findViewById(R.id.collectionNameLabel);

        if(Objects.requireNonNull(bundle.getString("type")).equals("collection"))
        {
            collectionNameTextView.setVisibility(View.VISIBLE);
            collectionNameEditText.setVisibility(View.VISIBLE);
            treasureLocationFoundEditText.setHint("Include a location for your collection");
            treasureInfoEditText.setHint("Include any additional info about your collection");

        }

        month++;

        String date = month + "/" + day + "/" + year;

        String splitDate[] = date.split("/");
        if(splitDate[0].length() == 1)
        {
            splitDate[0] = "0"+splitDate[0];
        }
        if(splitDate[1].length() == 1)
        {
            splitDate[1] = "0"+splitDate[1];
        }

        date = splitDate[0]+"/"+splitDate[1]+"/"+splitDate[2];
        treasureDateFoundTextView.setText(date);

        //If the user had previously filled out info here but then backed up to a previous fragment before returning
        if(bundle.containsKey("treasureDateFound"))
        {
            repopulateInfo();
        }

        treasureDateFoundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
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

                newMonth++;

                String date = newMonth + "/" + newDay + "/" + newYear;

                String splitDate[] = date.split("/");
                if(splitDate[0].length() == 1)
                {
                    splitDate[0] = "0"+splitDate[0];
                }
                if(splitDate[1].length() == 1)
                {
                    splitDate[1] = "0"+splitDate[1];
                }

                date = splitDate[0]+"/"+splitDate[1]+"/"+splitDate[2];

                treasureDateFoundTextView.setText(date);
            }
        };

        Button getCurrentLocationButton = view.findViewById(R.id.placePickerButton);
        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Objects.requireNonNull(getContext()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
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
                            treasureLocationFoundEditText.setText(addresses.get(0).getAddressLine(0));
                        }
                    }
                    else
                    {
                        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "Could not get current location.", Snackbar.LENGTH_SHORT).show();

                    }
                }
            }
        });

        return view;
    }

    private void repopulateInfo() {

        treasureDateFoundTextView.setText(bundle.getString("treasureDateFound"));
        treasureLocationFoundEditText.setText(bundle.getString("treasureLocationFound"));
        treasureInfoEditText.setText(bundle.getString("treasureInfo"));

        if(Objects.requireNonNull(bundle.getString("type")).equals("collection"))
        {
            collectionNameEditText.setText(bundle.getString("collectionName"));
        }
    }

    private void renameTempImages(final String timeAtAdd)
    {
        ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(getActivity()).getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getFilesDir();
        File subDir = new File(directory, "imageDir");
        if( !subDir.exists() )
            subDir.mkdir();
        final String prefix = "temp_" + timeAtAdd;

        File [] files = subDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String name) {
                return name.startsWith(prefix);
            }
        });
        //Log.d("TEST", "The number of temp_images found was: "+files.length);

        //Log.d("TEST", "Removing temp prefix from images at path: "+subDir.getPath());
        for (File file : files) {

            //remove temp_ chars from prefix string
            String newName = file.getName().substring(5);
            File newFile = new File(subDir, newName);
            //rename file from temp to permanent
            file.renameTo(newFile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                LocationManager lm = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
            } else {
                Toast.makeText(getActivity(), "Cannot request location without permissions!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveTreasure() {
        final String type = bundle.getString("type");
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle("Confirm?")
                .setMessage("Add this "+type+" now?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

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

                        String oldDate = treasureDateFoundTextView.getText().toString();
                        String[] splitDate = oldDate.split("/");
                        String treasureFoundDate = splitDate[2]+"/"+splitDate[0]+"/"+splitDate[1];

                        String treasureLocationFound = treasureLocationFoundEditText.getText().toString();
                        String treasureInfo = treasureInfoEditText.getText().toString().trim();
                        //Log.d("TEST", type + " " + timeAtAdd + " " + coinCountry + " " + coinType + " " + treasureSeries + " " + treasureYear + " " + coinMint + " " + treasureMaterial);

                        Treasure treasure = new Treasure(0, type, coinCountry, coinType, treasureSeries, treasureName, treasureYear, coinMint, treasureMaterial, treasureWeight, treasureLocationFound, treasureFoundDate, treasureInfo, timeAtAdd);

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

                        Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK,null);
                        getActivity().finish();

                    }
                }).create().show();
    }

    public void addToBundle() {
        bundle.putString("treasureDateFound", treasureDateFoundTextView.getText().toString());
        bundle.putString("treasureLocationFound", treasureLocationFoundEditText.getText().toString());
        bundle.putString("treasureInfo", treasureInfoEditText.getText().toString().trim());

        if(Objects.requireNonNull(bundle.getString("type")).equals("collection"))
        {
            bundle.putString("collectionName", collectionNameEditText.getText().toString());
        }
    }
}
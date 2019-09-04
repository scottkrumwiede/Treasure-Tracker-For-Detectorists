package com.mdtt.scott.treasuretrackerfordetectorists;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddJewelryInfoFragment extends Fragment {

    Bundle bundle;
    Spinner jewelryMaterialSpinner;
    EditText jewelryNameEditText, jewelrySeriesEditText, jewelryYearEditText, jewelryWeightEditText;
    private int jewelryMaterialSelected;
    private ArrayList<String> jewelryMaterialList;

    public AddJewelryInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        jewelryMaterialList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.jewelry_material_array)));
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
        View view = inflater.inflate(R.layout.fragment_add_jewelry_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jewelryMaterialSpinner = (Spinner) view.findViewById(R.id.jewelryMaterialSpinner);
        jewelryNameEditText = (EditText) view.findViewById(R.id.jewelryNameEditText);
        jewelrySeriesEditText = (EditText) view.findViewById(R.id.jewelrySeriesEditText);
        jewelryYearEditText = (EditText) view.findViewById(R.id.jewelryYearEditText);
        jewelryWeightEditText = (EditText) view.findViewById(R.id.jewelryWeightEditText);

        Log.d("test", "on view created we're here");

        jewelryMaterialSelected = jewelryMaterialSpinner.getSelectedItemPosition();
        Log.d("test", "Material: " + jewelryMaterialSelected);

        ArrayAdapter<String> jewelryMaterialSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, jewelryMaterialList);
        jewelryMaterialSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jewelryMaterialSpinner.setAdapter(jewelryMaterialSpinnerAdapter);
        jewelryMaterialSpinner.setSelection(jewelryMaterialSelected);
        jewelryMaterialSpinnerAdapter.notifyDataSetChanged();

        jewelryMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(jewelryMaterialSelected != position)
                {
                    final int previousMaterial = jewelryMaterialSelected;
                    Log.d("test", "starting jewelryMaterialSpinner because:\njewelryMaterialSelected= "+jewelryMaterialSelected+"\nposition= "+position);
                    if(parentView.getItemAtPosition(position).toString().equals("Custom..."))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Custom Material:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String task = String.valueOf(taskEditText.getText());
                                        if(task.isEmpty())
                                        {
                                            jewelryMaterialSpinner.setSelection(previousMaterial);
                                            jewelryMaterialSelected = previousMaterial;
                                            return;
                                        }
                                        while(jewelryMaterialList.size() > 9)
                                        {
                                            jewelryMaterialList.remove(jewelryMaterialList.size()-1);
                                        }
                                        jewelryMaterialList.add(task);
                                        jewelryMaterialList.add("Custom...");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, jewelryMaterialList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        jewelryMaterialSpinner.setAdapter(spinnerAdapter);
                                        jewelryMaterialSpinner.setSelection(jewelryMaterialList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        jewelryMaterialSpinner.setSelection(previousMaterial);
                                        jewelryMaterialSelected = previousMaterial;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                jewelryMaterialSpinner.setSelection(previousMaterial);
                                jewelryMaterialSelected = previousMaterial;
                            }
                        });
                    }
                }
                Log.d("test", "jewelryMaterialSelected="+jewelryMaterialSelected+" changed to "+position);
                jewelryMaterialSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nextButtonClicked() {
        bundle.putString("treasureName", jewelryNameEditText.getText().toString());
        bundle.putString("treasureSeries", jewelrySeriesEditText.getText().toString());
        bundle.putString("treasureYear", jewelryYearEditText.getText().toString());
        bundle.putString("treasureMaterial", jewelryMaterialSpinner.getSelectedItem().toString());
        bundle.putString("treasureWeight", jewelryWeightEditText.getText().toString());
        ((AddActivity) getActivity()).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
    }
}
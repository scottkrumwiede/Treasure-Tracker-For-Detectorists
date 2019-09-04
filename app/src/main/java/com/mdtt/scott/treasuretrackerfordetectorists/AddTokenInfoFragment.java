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
public class AddTokenInfoFragment extends Fragment {

    Bundle bundle;
    Spinner tokenMaterialSpinner;
    EditText tokenNameEditText, tokenSeriesEditText, tokenYearEditText;
    private int tokenMaterialSelected;
    private ArrayList<String> tokenMaterialList;

    public AddTokenInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        tokenMaterialList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.token_material_array)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_token_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenMaterialSpinner = (Spinner) view.findViewById(R.id.tokenMaterialSpinner);
        tokenNameEditText = (EditText) view.findViewById(R.id.tokenNameEditText);
        tokenSeriesEditText = (EditText) view.findViewById(R.id.tokenSeriesEditText);
        tokenYearEditText = (EditText) view.findViewById(R.id.tokenYearEditText);

        Log.d("test", "on view created we're here");

        tokenMaterialSelected = tokenMaterialSpinner.getSelectedItemPosition();
        Log.d("test", "Material: " + tokenMaterialSelected);

        ArrayAdapter<String> tokenMaterialSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tokenMaterialList);
        tokenMaterialSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tokenMaterialSpinner.setAdapter(tokenMaterialSpinnerAdapter);
        tokenMaterialSpinner.setSelection(tokenMaterialSelected);
        tokenMaterialSpinnerAdapter.notifyDataSetChanged();

        tokenMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(tokenMaterialSelected != position)
                {
                    final int previousMaterial = tokenMaterialSelected;
                    Log.d("test", "starting tokenMaterialSpinner because:\ntokenMaterialSelected= "+tokenMaterialSelected+"\nposition= "+position);
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
                                            tokenMaterialSpinner.setSelection(previousMaterial);
                                            tokenMaterialSelected = previousMaterial;
                                            return;
                                        }
                                        while(tokenMaterialList.size() > 5)
                                        {
                                            tokenMaterialList.remove(tokenMaterialList.size()-1);
                                        }
                                        tokenMaterialList.add(task);
                                        tokenMaterialList.add("Custom...");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tokenMaterialList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        tokenMaterialSpinner.setAdapter(spinnerAdapter);
                                        tokenMaterialSpinner.setSelection(tokenMaterialList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tokenMaterialSpinner.setSelection(previousMaterial);
                                        tokenMaterialSelected = previousMaterial;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                tokenMaterialSpinner.setSelection(previousMaterial);
                                tokenMaterialSelected = previousMaterial;
                            }
                        });
                    }
                }
                Log.d("test", "relicMaterialSelected="+tokenMaterialSelected+" changed to "+position);
                tokenMaterialSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nextButtonClicked() {
        bundle.putString("treasureName", tokenNameEditText.getText().toString());
        bundle.putString("treasureSeries", tokenSeriesEditText.getText().toString());
        bundle.putString("treasureYear", tokenYearEditText.getText().toString());
        bundle.putString("treasureMaterial", tokenMaterialSpinner.getSelectedItem().toString());
        ((AddActivity) getActivity()).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
    }
}

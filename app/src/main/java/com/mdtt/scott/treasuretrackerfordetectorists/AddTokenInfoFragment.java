package com.mdtt.scott.treasuretrackerfordetectorists;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class AddTokenInfoFragment extends Fragment {

    private Bundle bundle;
    private Spinner tokenMaterialSpinner;
    private EditText tokenNameEditText;
    private EditText tokenSeriesEditText;
    private EditText tokenYearEditText;
    private int tokenMaterialSelected;
    private ArrayList<String> tokenMaterialList;

    public AddTokenInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        tokenMaterialList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.token_material_array)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_token_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenMaterialSpinner = view.findViewById(R.id.tokenMaterialSpinner);
        tokenNameEditText = view.findViewById(R.id.tokenNameEditText);
        tokenSeriesEditText = view.findViewById(R.id.tokenSeriesEditText);
        tokenYearEditText = view.findViewById(R.id.tokenYearEditText);

        //Log.d("test", "on view created we're here");
        ArrayAdapter<String> tokenMaterialSpinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, tokenMaterialList);

        //If the user had previously filled out info here but then backed up to a previous fragment before returning
        if(bundle.containsKey("treasureName"))
        {
            repopulateInfo();
        }
        //first entry into this fragment
        else
        {
            tokenMaterialSelected = tokenMaterialSpinner.getSelectedItemPosition();
        }

        //Log.d("test", "Material: " + tokenMaterialSelected);

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
                    //Log.d("test", "starting tokenMaterialSpinner because:\ntokenMaterialSelected= "+tokenMaterialSelected+"\nposition= "+position);
                    if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
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
                                        tokenMaterialList.add("Custom…");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, tokenMaterialList);
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
                        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                tokenMaterialSpinner.setSelection(previousMaterial);
                                tokenMaterialSelected = previousMaterial;
                            }
                        });
                    }
                }
                //Log.d("test", "relicMaterialSelected="+tokenMaterialSelected+" changed to "+position);
                tokenMaterialSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nextButtonClicked() {
        addToBundle();
        ((AddActivity) Objects.requireNonNull(getActivity())).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
    }

    public void addToBundle() {
        bundle.putString("treasureName", tokenNameEditText.getText().toString());
        bundle.putString("treasureSeries", tokenSeriesEditText.getText().toString());
        bundle.putString("treasureYear", tokenYearEditText.getText().toString());
        bundle.putString("treasureMaterial", tokenMaterialSpinner.getSelectedItem().toString());
    }

    private void repopulateInfo() {
        //set maker
        tokenNameEditText.setText(bundle.getString("treasureName"));
        //set design
        tokenSeriesEditText.setText(bundle.getString("treasureSeries"));
        //set year
        tokenYearEditText.setText(bundle.getString("treasureYear"));
        //check if material is in list, otherwise add. then set spinner to position.
        if(tokenMaterialList.contains(bundle.getString("treasureMaterial")))
        {
            tokenMaterialSelected = tokenMaterialList.indexOf(bundle.getString("treasureMaterial"));
        }
        else
        {
            while(tokenMaterialList.size() > 10)
            {
                tokenMaterialList.remove(tokenMaterialList.size()-1);
            }
            tokenMaterialList.add(bundle.getString("treasureMaterial"));
            tokenMaterialList.add("Custom…");
            tokenMaterialSelected = tokenMaterialList.size()-2;
        }
    }
}

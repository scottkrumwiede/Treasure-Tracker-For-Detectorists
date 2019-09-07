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


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("ALL")
public class AddRelicInfoFragment extends Fragment {

    private Bundle bundle;
    private Spinner relicMaterialSpinner;
    private EditText relicNameEditText;
    private EditText relicSeriesEditText;
    private EditText relicYearEditText;
    private int relicMaterialSelected;
    private ArrayList<String> relicMaterialList;

    public AddRelicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        relicMaterialList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.relic_material_array)));
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
        return inflater.inflate(R.layout.fragment_add_relic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        relicMaterialSpinner = view.findViewById(R.id.relicMaterialSpinner);
        relicNameEditText = view.findViewById(R.id.relicNameEditText);
        relicSeriesEditText = view.findViewById(R.id.relicSeriesEditText);
        relicYearEditText = view.findViewById(R.id.relicYearEditText);

        //Log.d("test", "on view created we're here");

        relicMaterialSelected = relicMaterialSpinner.getSelectedItemPosition();
        //Log.d("test", "Material: " + relicMaterialSelected);

        ArrayAdapter<String> relicMaterialSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, relicMaterialList);
        relicMaterialSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relicMaterialSpinner.setAdapter(relicMaterialSpinnerAdapter);
        relicMaterialSpinner.setSelection(relicMaterialSelected);
        relicMaterialSpinnerAdapter.notifyDataSetChanged();

        relicMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(relicMaterialSelected != position)
                {
                    final int previousMaterial = relicMaterialSelected;
                    //Log.d("test", "starting relicMaterialSpinner because:\nrelicMaterialSelected= "+relicMaterialSelected+"\nposition= "+position);
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
                                            relicMaterialSpinner.setSelection(previousMaterial);
                                            relicMaterialSelected = previousMaterial;
                                            return;
                                        }
                                        while(relicMaterialList.size() > 4)
                                        {
                                            relicMaterialList.remove(relicMaterialList.size()-1);
                                        }
                                        relicMaterialList.add(task);
                                        relicMaterialList.add("Custom...");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, relicMaterialList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        relicMaterialSpinner.setAdapter(spinnerAdapter);
                                        relicMaterialSpinner.setSelection(relicMaterialList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        relicMaterialSpinner.setSelection(previousMaterial);
                                        relicMaterialSelected = previousMaterial;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                relicMaterialSpinner.setSelection(previousMaterial);
                                relicMaterialSelected = previousMaterial;
                            }
                        });
                    }
                }
                //Log.d("test", "relicMaterialSelected="+relicMaterialSelected+" changed to "+position);
                relicMaterialSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nextButtonClicked() {
        bundle.putString("treasureName", relicNameEditText.getText().toString());
        bundle.putString("treasureSeries", relicSeriesEditText.getText().toString());
        bundle.putString("treasureYear", relicYearEditText.getText().toString());
        bundle.putString("treasureMaterial", relicMaterialSpinner.getSelectedItem().toString());
        ((AddActivity) getActivity()).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
    }
}
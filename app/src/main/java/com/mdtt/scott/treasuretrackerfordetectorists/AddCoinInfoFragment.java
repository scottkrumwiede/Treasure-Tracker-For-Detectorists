
package com.mdtt.scott.treasuretrackerfordetectorists;


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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class AddCoinInfoFragment extends Fragment {

    private Bundle bundle;
    private Spinner coinCountrySpinner;
    private Spinner coinTypeSpinner;
    private Spinner coinSeriesSpinner;
    private Spinner coinMintSpinner;
    private Spinner coinMaterialSpinner;
    private EditText coinYearEditText;
    private EditText customTypeEditText;
    private EditText customDesignEditText;
    private EditText customMintEditText;
    private int coinCountrySelected, coinTypeSelected, coinSeriesSelected, coinMintSelected, coinMaterialSelected;
    private ArrayList<String> countriesList, coinTypeList, coinSeriesList, coinMintList, coinMaterialList;
    private int coinSeriesListSize;

    public AddCoinInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

        countriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.countries_array)));
        coinTypeList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_type_array)));
        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_penny_series_array)));
        coinSeriesListSize = coinSeriesList.size();
        coinMintList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_mint_array)));
        coinMaterialList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.coin_material_array)));
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
        return inflater.inflate(R.layout.fragment_add_coin_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coinCountrySpinner = view.findViewById(R.id.coinCountrySpinner);
        coinTypeSpinner = view.findViewById(R.id.coinTypeSpinner);
        coinSeriesSpinner = view.findViewById(R.id.coinSeriesSpinner);
        coinYearEditText = view.findViewById(R.id.coinYearEditText);
        coinMintSpinner = view.findViewById(R.id.coinMintSpinner);
        coinMaterialSpinner = view.findViewById(R.id.coinMaterialSpinner);

        customTypeEditText = view.findViewById(R.id.customTypeEditText);
        customDesignEditText = view.findViewById(R.id.customDesignEditText);
        customMintEditText = view.findViewById(R.id.customMintEditText);

        //If the user had previously filled out info here but then backed up to a previous fragment before returning
        if(bundle.containsKey("addCoinInfoType"))
        {
           repopulateInfo();
        }
        //first entry into this fragment
        else
        {
            //Log.d("test", "on view created we're here");
            coinCountrySelected = coinCountrySpinner.getSelectedItemPosition();
            //Log.d("test", "Country: "+coinCountrySelected);
            coinTypeSelected = coinTypeSpinner.getSelectedItemPosition();
            //Log.d("test", "Type: "+coinTypeSelected);
            coinSeriesSelected = coinSeriesSpinner.getSelectedItemPosition();
            //Log.d("test", "Series: "+coinSeriesSelected);
            coinMintSelected = coinMintSpinner.getSelectedItemPosition();
            //Log.d("test", "Mint: "+coinMintSelected);
            coinMaterialSelected = coinMaterialSpinner.getSelectedItemPosition();
            //Log.d("test", "Material: "+coinMaterialSelected);
        }

        ArrayAdapter<String> countrySpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, countriesList);
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinCountrySpinner.setAdapter(countrySpinnerAdapter);
        coinCountrySpinner.setSelection(coinCountrySelected);
        countrySpinnerAdapter.notifyDataSetChanged();

        ArrayAdapter<String> coinTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinTypeList);
        coinTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinTypeSpinner.setAdapter(coinTypeSpinnerAdapter);
        coinTypeSpinner.setSelection(coinTypeSelected);
        coinTypeSpinnerAdapter.notifyDataSetChanged();

        //Log.d("TEST",coinSeriesList.get(coinSeriesList.size()-2));
        ArrayAdapter<String> coinSeriesSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinSeriesList);
        coinSeriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinSeriesSpinner.setAdapter(coinSeriesSpinnerAdapter);
        coinSeriesSpinner.setSelection(coinSeriesSelected);
        coinSeriesSpinnerAdapter.notifyDataSetChanged();

        ArrayAdapter<String> coinMintSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinMintList);
        coinMintSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinMintSpinner.setAdapter(coinMintSpinnerAdapter);
        coinMintSpinner.setSelection(coinMintSelected);
        coinMintSpinnerAdapter.notifyDataSetChanged();

        ArrayAdapter<String> coinMaterialSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinMaterialList);
        coinMaterialSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinMaterialSpinner.setAdapter(coinMaterialSpinnerAdapter);
        coinMaterialSpinner.setSelection(coinMaterialSelected);
        coinMaterialSpinnerAdapter.notifyDataSetChanged();

        coinCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(coinCountrySelected != position)
                    {
                        final int previousCountry = coinCountrySelected;
                        //Log.d("test", "starting countrySpinner because:\ncoinCountrySelected= "+coinCountrySelected+"\nposition= "+position);
                        if(parentView.getItemAtPosition(position).toString().equals("United States"))
                        {

                            customTypeEditText.setVisibility(View.GONE);
                            customMintEditText.setVisibility(View.GONE);
                            coinTypeSpinner.setVisibility(View.VISIBLE);
                            coinMintSpinner.setVisibility(View.VISIBLE);
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinTypeList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            coinTypeSpinner.setAdapter(spinnerAdapter);
                            coinTypeSpinner.setSelection(coinTypeSelected);
                            spinnerAdapter.notifyDataSetChanged();
                            if(!(coinTypeList.size() > 7 && coinTypeSelected == 6))
                            {
                                customDesignEditText.setVisibility(View.GONE);
                                coinSeriesSpinner.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                customDesignEditText.setText("");
                                customDesignEditText.requestFocus();
                            }
                        }
                        else if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
                        {
                            final EditText taskEditText = new EditText(getContext());
                            AlertDialog dialog = new AlertDialog.Builder(getContext())
                                    .setTitle("Custom Country:")
                                    .setView(taskEditText)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity(), "Custom added!", Toast.LENGTH_SHORT).show();
                                            String task = String.valueOf(taskEditText.getText());
                                            if(task.isEmpty())
                                            {
                                                coinCountrySpinner.setSelection(previousCountry);
                                                coinCountrySelected = previousCountry;
                                                return;
                                            }
                                            while(countriesList.size() > 1)
                                            {
                                                countriesList.remove(countriesList.size()-1);
                                            }
                                            countriesList.add(task);
                                            countriesList.add("Custom…");
                                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, countriesList);
                                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            coinCountrySpinner.setAdapter(spinnerAdapter);
                                            coinCountrySpinner.setSelection(countriesList.size()-2);
                                            spinnerAdapter.notifyDataSetChanged();

                                            customDesignEditText.setVisibility(View.VISIBLE);
                                            customTypeEditText.setVisibility(View.VISIBLE);
                                            customMintEditText.setVisibility(View.VISIBLE);
                                            customDesignEditText.setText("");
                                            customTypeEditText.setText("");
                                            customMintEditText.setText("");
                                            coinTypeSpinner.setVisibility(View.GONE);
                                            coinSeriesSpinner.setVisibility(View.GONE);
                                            coinMintSpinner.setVisibility(View.GONE);
                                            customTypeEditText.requestFocus();

                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            coinCountrySpinner.setSelection(previousCountry);
                                            coinCountrySelected = previousCountry;
                                        }
                                    })
                                    .create();
                            dialog.show();
                            dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    coinCountrySpinner.setSelection(previousCountry);
                                    coinCountrySelected = previousCountry;
                                }
                            });
                        }
                        else
                        {
                            if(customDesignEditText.getVisibility() == View.VISIBLE)
                            {
                                customDesignEditText.setText("");
                            }
                            customDesignEditText.setVisibility(View.VISIBLE);
                            customTypeEditText.setVisibility(View.VISIBLE);
                            customMintEditText.setVisibility(View.VISIBLE);
                            coinTypeSpinner.setVisibility(View.GONE);
                            coinSeriesSpinner.setVisibility(View.GONE);
                            coinMintSpinner.setVisibility(View.GONE);
                            if(customTypeEditText.getText().toString().equals(""))
                            {
                                customTypeEditText.requestFocus();
                            }
                            else
                            {
                                customDesignEditText.requestFocus();
                            }
                        }
                    }
                    //Log.d("test", "coinCountrySelected="+coinCountrySelected+" changed to "+position);
                    coinCountrySelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }

        });

        coinTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(coinTypeSelected != position)
                {
                    final int previousType = coinTypeSelected;
                    //Log.d("test", "starting coinTypeSpinner because:\ncoinTypeSelected= "+coinTypeSelected+"\nposition= "+position);
                    ArrayAdapter<String> spinnerAdapter;
                    if(parentView.getItemAtPosition(position).toString().equals("1-cent (Penny)"))
                    {
                        customDesignEditText.setVisibility(View.GONE);
                        coinSeriesSpinner.setVisibility(View.VISIBLE);

                        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_penny_series_array)));
                        coinSeriesListSize = coinSeriesList.size();

                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.uscoin_penny_series_array));
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else if(parentView.getItemAtPosition(position).toString().equals("5-cent (Nickel)"))
                    {
                        customDesignEditText.setVisibility(View.GONE);
                        coinSeriesSpinner.setVisibility(View.VISIBLE);

                        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_nickel_series_array)));
                        coinSeriesListSize = coinSeriesList.size();

                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.uscoin_nickel_series_array));
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else if(parentView.getItemAtPosition(position).toString().equals("10-cent (Dime)"))
                    {
                        customDesignEditText.setVisibility(View.GONE);
                        coinSeriesSpinner.setVisibility(View.VISIBLE);

                        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_dime_series_array)));
                        coinSeriesListSize = coinSeriesList.size();

                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.uscoin_dime_series_array));
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else if(parentView.getItemAtPosition(position).toString().equals("25-cent (Quarter)"))
                    {
                        customDesignEditText.setVisibility(View.GONE);
                        coinSeriesSpinner.setVisibility(View.VISIBLE);

                        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_quarter_series_array)));
                        coinSeriesListSize = coinSeriesList.size();

                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.uscoin_quarter_series_array));
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else if(parentView.getItemAtPosition(position).toString().equals("50-cent (Half dollar)"))
                    {
                        customDesignEditText.setVisibility(View.GONE);
                        coinSeriesSpinner.setVisibility(View.VISIBLE);

                        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_halfdollar_series_array)));
                        coinSeriesListSize = coinSeriesList.size();

                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.uscoin_halfdollar_series_array));
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else if(parentView.getItemAtPosition(position).toString().equals("100-cent (Dollar)"))
                    {
                        customDesignEditText.setVisibility(View.GONE);
                        coinSeriesSpinner.setVisibility(View.VISIBLE);

                        coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_dollar_series_array)));
                        coinSeriesListSize = coinSeriesList.size();

                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.uscoin_dollar_series_array));
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Custom Denomination:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Custom added!", Toast.LENGTH_SHORT).show();
                                        String task = String.valueOf(taskEditText.getText());
                                        if(task.isEmpty())
                                        {
                                            coinTypeSpinner.setSelection(previousType);
                                            coinTypeSelected = previousType;
                                            return;
                                        }
                                        while(coinTypeList.size() > 6)
                                        {
                                            coinTypeList.remove(coinTypeList.size()-1);
                                        }
                                        coinTypeList.add(task);
                                        coinTypeList.add("Custom…");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinTypeList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        coinTypeSpinner.setAdapter(spinnerAdapter);
                                        coinTypeSpinner.setSelection(coinTypeList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                        customDesignEditText.setVisibility(View.VISIBLE);
                                        customDesignEditText.setText("");
                                        customDesignEditText.requestFocus();
                                        coinSeriesSpinner.setVisibility(View.GONE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        coinTypeSpinner.setSelection(previousType);
                                        coinTypeSelected = previousType;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                coinTypeSpinner.setSelection(previousType);
                                coinTypeSelected = previousType;
                            }
                        });
                    }
                    else
                    {
                        customDesignEditText.setVisibility(View.VISIBLE);
                        coinSeriesSpinner.setVisibility(View.GONE);
                    }
                }

                //Log.d("test", "coinTypeSelected="+coinTypeSelected+" changed to "+position);
                coinTypeSelected = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }

        });

        coinSeriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(coinSeriesSelected != position)
                {
                    final int previousSeries = coinSeriesSelected;
                    //Log.d("test", "starting coinSeriesSpinner because:\ncoinSeriesSelected= "+coinSeriesSelected+"\nposition= "+position);
                    if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Custom Design/Series:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Custom added!", Toast.LENGTH_SHORT).show();
                                        String task = String.valueOf(taskEditText.getText());
                                        if(task.isEmpty())
                                        {
                                            coinSeriesSpinner.setSelection(previousSeries);
                                            coinSeriesSelected = previousSeries;
                                            return;
                                        }
                                        while(coinSeriesList.size() > coinSeriesListSize-1)
                                        {
                                            coinSeriesList.remove(coinSeriesList.size()-1);
                                        }
                                        coinSeriesList.add(task);
                                        coinSeriesList.add("Custom…");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinSeriesList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        coinSeriesSpinner.setAdapter(spinnerAdapter);
                                        coinSeriesSpinner.setSelection(coinSeriesList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        coinSeriesSpinner.setSelection(previousSeries);
                                        coinSeriesSelected = previousSeries;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                coinSeriesSpinner.setSelection(previousSeries);
                                coinSeriesSelected = previousSeries;
                            }
                        });
                    }
                }
                //Log.d("test", "coinSeriesSelected="+coinSeriesSelected+" changed to "+position);
                coinSeriesSelected = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }

        });

        coinMintSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(coinMintSelected != position)
                {
                    final int previousMint = coinMintSelected;
                    //Log.d("test", "starting coinMintSpinner because:\ncoinMintSelected= "+coinMintSelected+"\nposition= "+position);
                    if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Custom Mint:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Custom added!", Toast.LENGTH_SHORT).show();
                                        String task = String.valueOf(taskEditText.getText());
                                        if(task.isEmpty())
                                        {
                                            coinMintSpinner.setSelection(previousMint);
                                            coinMintSelected = previousMint;
                                            return;
                                        }
                                        while(coinMintList.size() > 10)
                                        {
                                            coinMintList.remove(coinMintList.size()-1);
                                        }
                                        coinMintList.add(task);
                                        coinMintList.add("Custom…");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinMintList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        coinMintSpinner.setAdapter(spinnerAdapter);
                                        coinMintSpinner.setSelection(coinMintList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        coinMintSpinner.setSelection(previousMint);
                                        coinMintSelected = previousMint;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                coinMintSpinner.setSelection(previousMint);
                                coinMintSelected = previousMint;
                            }
                        });
                    }
                }
                //Log.d("test", "coinMintSelected="+coinMintSelected+" changed to "+position);
                coinMintSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });

        coinMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(coinMaterialSelected != position)
                {
                    final int previousMaterial = coinMaterialSelected;
                    //Log.d("test", "starting coinMaterialSpinner because:\ncoinMaterialSelected= "+coinMaterialSelected+"\nposition= "+position);
                    if(parentView.getItemAtPosition(position).toString().equals("Custom…"))
                    {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Custom Material:")
                                .setView(taskEditText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Custom added!", Toast.LENGTH_SHORT).show();
                                        String task = String.valueOf(taskEditText.getText());
                                        if(task.isEmpty())
                                        {
                                            coinMaterialSpinner.setSelection(previousMaterial);
                                            coinMaterialSelected = previousMaterial;
                                            return;
                                        }
                                        while(coinMaterialList.size() > 10)
                                        {
                                            coinMaterialList.remove(coinMaterialList.size()-1);
                                        }
                                        coinMaterialList.add(task);
                                        coinMaterialList.add("Custom…");
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, coinMaterialList);
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        coinMaterialSpinner.setAdapter(spinnerAdapter);
                                        coinMaterialSpinner.setSelection(coinMaterialList.size()-2);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        coinMaterialSpinner.setSelection(previousMaterial);
                                        coinMaterialSelected = previousMaterial;
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                coinMaterialSpinner.setSelection(previousMaterial);
                                coinMaterialSelected = previousMaterial;
                            }
                        });
                    }
                }
                //Log.d("test", "coinMaterialSelected="+coinMaterialSelected+" changed to "+position);
                coinMaterialSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getActivity(), "NOTHING added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void repopulateInfo() {
        //check type
        //custom country,type,series,mint
        if(bundle.getString("addCoinInfoType").equals("1"))
        {
            //add custom country to countriesList
            while(countriesList.size() > 1)
            {
                countriesList.remove(countriesList.size()-1);
            }
            countriesList.add(bundle.getString("coinCountry"));
            countriesList.add("Custom…");
            coinCountrySelected = countriesList.size()-2;

            //set custom edit texts to stored values
            customTypeEditText.setText(bundle.getString("coinType"));
            customDesignEditText.setText(bundle.getString("treasureSeries"));
            customMintEditText.setText(bundle.getString("coinMint"));

            //show custom edit texts and hide default
            customDesignEditText.setVisibility(View.VISIBLE);
            customTypeEditText.setVisibility(View.VISIBLE);
            customMintEditText.setVisibility(View.VISIBLE);
            coinTypeSpinner.setVisibility(View.GONE);
            coinSeriesSpinner.setVisibility(View.GONE);
            coinMintSpinner.setVisibility(View.GONE);

        }
        //custom type,design
        else if(bundle.get("addCoinInfoType").equals("2"))
        {
            Log.d("myTag", "we are in type2");
            //add custom type to typeList
            while(coinTypeList.size() > 6)
            {
                Log.d("myTag", "we are in remove cointype!");

                coinTypeList.remove(coinTypeList.size()-1);
            }
            coinTypeList.add(bundle.getString("coinType"));
            coinTypeList.add("Custom…");
            coinTypeSelected = coinTypeList.size()-2;

            //set custom edit texts to stored values
            customDesignEditText.setText(bundle.getString("treasureSeries"));

            //show custom edit texts and hide default
            customDesignEditText.setVisibility(View.VISIBLE);
            coinSeriesSpinner.setVisibility(View.GONE);

            //check if mint is in list, otherwise add and then set to position
            if(coinMintList.contains(bundle.getString("coinMint")))
            {
                coinMintSelected = coinMintList.indexOf(bundle.getString("coinMint"));
            }
            else
            {
                while(coinMintList.size() > 10)
                {
                    coinMintList.remove(coinMintList.size()-1);
                }
                coinMintList.add(bundle.getString("coinMint"));
                coinMintList.add("Custom…");
                coinMintSelected = coinMintList.size()-2;
            }
        }
        else
        {
            //set coinTypeSelected to correct position
            coinTypeSelected = coinTypeList.indexOf(bundle.getString("coinType"));
            if(bundle.getString("coinType").equals("1-cent (Penny"))
            {
                coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_penny_series_array)));
                coinSeriesListSize = coinSeriesList.size();
            }
            else if(bundle.getString("coinType").equals("5-cent (Nickel)"))
            {
                coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_nickel_series_array)));
                coinSeriesListSize = coinSeriesList.size();
            }
            else if(bundle.getString("coinType").equals("10-cent (Dime)"))
            {
                coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_dime_series_array)));
                coinSeriesListSize = coinSeriesList.size();
            }
            else if(bundle.getString("coinType").equals("25-cent (Quarter)"))
            {
                coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_quarter_series_array)));
                coinSeriesListSize = coinSeriesList.size();
            }
            else if(bundle.getString("coinType").equals("50-cent (Half dollar)"))
            {
                coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_halfdollar_series_array)));
                coinSeriesListSize = coinSeriesList.size();
            }
            else if(bundle.getString("coinType").equals("100-cent (Dollar)"))
            {
                coinSeriesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.uscoin_dollar_series_array)));
                coinSeriesListSize = coinSeriesList.size();
            }

            //check if series is in list, otherwise add and then set to position
            if(coinSeriesList.contains(bundle.getString("treasureSeries")))
            {
                coinSeriesSelected = coinSeriesList.indexOf(bundle.getString("treasureSeries"));
            }
            else
            {
                while(coinSeriesList.size() > coinSeriesListSize-1)
                {
                    coinSeriesList.remove(coinSeriesList.size()-1);
                }
                coinSeriesList.add(bundle.getString("treasureSeries"));
                coinSeriesList.add("Custom…");
                coinSeriesSelected = coinSeriesList.size()-2;
            }

            //check if mint is in list, otherwise add and then set to position
            if(coinMintList.contains(bundle.getString("coinMint")))
            {
                coinMintSelected = coinMintList.indexOf(bundle.getString("coinMint"));
            }
            else
            {
                while(coinMintList.size() > 10)
                {
                    coinMintList.remove(coinMintList.size()-1);
                }
                coinMintList.add(bundle.getString("coinMint"));
                coinMintList.add("Custom…");
                coinMintSelected = coinMintList.size()-2;
            }
        }

        //set yearedittext to value
        coinYearEditText.setText(bundle.getString("treasureYear"));
        //check if material is in list, otherwise add and then set to position
        if(coinMaterialList.contains(bundle.getString("treasureMaterial")))
        {
            coinMaterialSelected = coinMaterialList.indexOf(bundle.getString("treasureMaterial"));
        }
        else
        {
            while(coinMaterialList.size() > 10)
            {
                coinMaterialList.remove(coinMaterialList.size()-1);
            }
            coinMaterialList.add(bundle.getString("treasureMaterial"));
            coinMaterialList.add("Custom…");
            coinMaterialSelected = coinMaterialList.size()-2;
        }
    }

    public void nextButtonClicked() {
        addToBundle();
        ((AddActivity) getActivity()).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
    }

    public void addToBundle() {
        //type 1 add - custom country - therefore type, series, and mint will all be custom
        if(customTypeEditText.isShown())
        {
            bundle.putString("coinType", customTypeEditText.getText().toString());
            bundle.putString("treasureSeries", customDesignEditText.getText().toString());
            bundle.putString("coinMint", customMintEditText.getText().toString());
            bundle.putString("addCoinInfoType", "1");
        }
        //type 2 add - custom type - therefore design and mint will be custom
        else if(customDesignEditText.isShown())
        {
            bundle.putString("coinType", coinTypeSpinner.getSelectedItem().toString());
            bundle.putString("treasureSeries", customDesignEditText.getText().toString());
            bundle.putString("coinMint", coinMintSpinner.getSelectedItem().toString());
            bundle.putString("addCoinInfoType", "2");
        }
        //type 3 add - neither custom type or design - therefore mint will not be custom
        else
        {
            bundle.putString("coinType", coinTypeSpinner.getSelectedItem().toString());
            bundle.putString("treasureSeries", coinSeriesSpinner.getSelectedItem().toString());
            bundle.putString("coinMint", coinMintSpinner.getSelectedItem().toString());
            bundle.putString("addCoinInfoType", "3");
        }

        //always added regardless of type
        bundle.putString("coinCountry", coinCountrySpinner.getSelectedItem().toString());
        bundle.putString("treasureYear", coinYearEditText.getText().toString());
        bundle.putString("treasureMaterial", coinMaterialSpinner.getSelectedItem().toString());
    }
}

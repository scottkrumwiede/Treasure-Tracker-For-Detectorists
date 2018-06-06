package com.mdtt.scott.treasuretrackerfordetectorists;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRelicFragment extends Fragment {

    //Button addCoinButton, addTokenButton, addJewelryButton, addRelicButton, addCladButton;

    public AddRelicFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Add a new relic:");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_relic, container, false);
        /*
        addCoinButton = (Button) view.findViewById(R.id.chooseAddCoin);

        addCoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment, new AddCoinFragment(), "NewFragmentTag");
                //ft.addToBackStack(null);
                ft.commit();
            }
        });
        */
        return view;
    }
}
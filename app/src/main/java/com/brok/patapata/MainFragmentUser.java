package com.brok.patapata;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainFragmentUser extends Fragment {
    public  String value;
    public Button button;
  Spinner mySpinner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_fragment_user, container, false);


        return myView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mySpinner = (Spinner) getView().findViewById(R.id.spinner2);
        button = (Button) getView().findViewById(R.id.litre_btn);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Litres));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
                break;

            case 1:
            value = "1000";
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("value",value);
            Bundle extras = new Bundle();
            extras.putString("status", "1000 Litres");
            intent.putExtras(extras);
            startActivity(intent);
            break;
            case 2:
                value = "2500";
                Intent intent1 = new Intent(getActivity(), MapsActivity.class);
                intent1.putExtra("value",value);
                Bundle extras1 = new Bundle();
                extras1.putString("status", "2500 Litres");
                intent1.putExtras(extras1);
                startActivity(intent1);
                break;
            case 3:
                value = "5000";
                Intent intent2 = new Intent(getActivity(), MapsActivity.class);
                intent2.putExtra("value",value);
                Bundle extras2 = new Bundle();
                extras2.putString("status", "5000 Litres");
                intent2.putExtras(extras2);
                startActivity(intent2);
                break;
            case 4:
                value = "10000";
                Intent intent3 = new Intent(getActivity(), MapsActivity.class);
                intent3.putExtra("value",value);
                Bundle extras3 = new Bundle();
                extras3.putString("status", "10000 Litres");
                intent3.putExtras(extras3);
                startActivity(intent3);
                break;

        }

       // Intent intent = new Intent(getActivity(), MapsActivity.class);
                   //intent.putExtra("VALUE", value);
                 // startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
            }


}

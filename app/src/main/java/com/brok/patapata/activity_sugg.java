package com.brok.patapata;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_sugg extends Fragment {

    private TextView textViewResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View myView = inflater.inflate(R.layout.activity_sugg, container, false);


        textViewResult = (TextView)myView.findViewById(R.id.text_view_result);
        /*mToolbar.setTitle(getString(R.string.app_name));
        /mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ListView listView = findViewById(R.id.cnames);
        */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api api = retrofit.create(api.class);

        Call<List<sugg>> call = api.getSugg();

        call.enqueue(new Callback<List<sugg>>() {
            @Override
           /* public void onResponse(Call<List<sugg>> call, Response<List<sugg>> response) {

                List<sugg> suggs = response.body();

                String[] cnames = new String[suggs.size()];

                for(int i=0; i<suggs.size(); i++)
                {
                    cnames[i] =   suggs.get(i).getCname();
                }
                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,cnames));

            }*/
            public void onResponse(Call<List<sugg>> call, Response<List<sugg>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<sugg> posts = response.body();

                for(sugg post : posts){
                    String content = "";
                    content += "Company Name: " + post.getCname() + "\n";
                    content += "Email: " + post.getCemail() + "\n";
                    content += "Phone: " + post.getMobileNo() + "\n\n";

                    textViewResult.append(content);
                }

            }
            @Override
            public void onFailure(Call<List<sugg>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage (), Toast.LENGTH_SHORT).show();;
            }
        });
        return myView;
    }
}

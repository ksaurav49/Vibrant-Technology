package htkc.ebaba.ecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.ProbResponse;
import htkc.ebaba.ecom.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class problem extends AppCompatActivity {
    String name,order_id;
    TextView textView,t;
    TextInputEditText textInputEditText;
    Button button;
    ProgressDialog progressDialog;
    int f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView=findViewById(R.id.u);
        t=findViewById(R.id.t);
        textInputEditText=findViewById(R.id.pdescr);
        button=findViewById(R.id.pdsub);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report Problem");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
        progressDialog = new ProgressDialog(problem.this);
        progressDialog.setMessage("Please wait..");
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        order_id=intent.getStringExtra("order_id");
        Log.d("name",name);
        Log.d("order",order_id);
        textView.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f=0;
                if(textInputEditText.getText().toString().isEmpty()){

                    Toast.makeText(problem.this, "Please provide proper Description!!", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    String des=textInputEditText.getText().toString();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl( ApiURL.BASE_URL)
                            .addConverterFactory( GsonConverterFactory.create())
                            .build();
                    ApiService apiService = retrofit.create(ApiService.class);

                    Call<ProbResponse> call = apiService.prob(order_id,des);
                    call.enqueue(new Callback<ProbResponse>() {
                        @Override
                        public void onResponse(Call<ProbResponse> call, Response<ProbResponse> response) {
                            progressDialog.hide();
                            if(response.body().getSuccess().equalsIgnoreCase("yes")){
                                try {
                                    new SweetAlertDialog(problem.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Your problem is registered!")
                                            .setContentText("our executive will contact you!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    problem.super.onBackPressed();
                                                }
                                            })
                                            .show();
                                }catch(Exception e){

                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ProbResponse> call, Throwable t) {
                            progressDialog.hide();
                            Toast.makeText(problem.this, "Network error! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(problem.this, ""+textInputEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            // Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

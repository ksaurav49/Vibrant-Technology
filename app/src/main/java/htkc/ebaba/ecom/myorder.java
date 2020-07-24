package htkc.ebaba.ecom;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import htkc.ebaba.ecom.adapter.MyOrderAdapter;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.MyOrderResponse;
import htkc.ebaba.ecom.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class myorder extends AppCompatActivity {
    private List<MyOrderResponse> myOrderResponses;
    private MyOrderAdapter myOrderAdapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
        progressDialog = new ProgressDialog(myorder.this);
        progressDialog.setMessage("Loading!!");
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(myorder.this);
        recyclerView.setLayoutManager(layoutManager);
        myOrderAdapter= new MyOrderAdapter(myorder.this,myOrderResponses);
        recyclerView.setAdapter(myOrderAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        String uid=SharedPrefManager.getInstance(this).getUser().getId();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ApiURL.BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<MyOrderResponse>> call = apiService.myorder(uid);
        call.enqueue(new Callback<List<MyOrderResponse>>() {
            @Override
            public void onResponse(Call<List<MyOrderResponse>> call, Response<List<MyOrderResponse>> response) {
                progressDialog.dismiss();
                myOrderResponses=response.body();


                myOrderAdapter.setMyOrderResponses(myOrderResponses);
            }

            @Override
            public void onFailure(Call<List<MyOrderResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(myorder.this, "Error!!", Toast.LENGTH_SHORT).show();
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

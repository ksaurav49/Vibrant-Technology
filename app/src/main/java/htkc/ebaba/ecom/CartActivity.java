package htkc.ebaba.ecom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import htkc.ebaba.ecom.adapter.CartAdapter;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.CartResponse;
import htkc.ebaba.ecom.response.DeliveryResponse;
import htkc.ebaba.ecom.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {
    private List<CartResponse> cartList;
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    AppCompatButton checkout;
    ProgressBar progressBar;
    TextView tot,delivery;
    int total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initToolbar();
        initComponent();

        Intent intent = getIntent();
        String cart_id = intent.getStringExtra("cart_id");
        String quantity = intent.getStringExtra("qntty");


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
    }

    private void initComponent() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        cartAdapter= new CartAdapter(CartActivity.this,cartList);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        tot=findViewById(R.id.tot);
        delivery = findViewById(R.id.deli);
        checkout = findViewById(R.id.checkout);
        String uid = SharedPrefManager.getInstance(CartActivity.this).getUser().getId();
progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ApiURL.BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<CartResponse>> call = apiService.getCart(uid);
        call.enqueue( new Callback<List<CartResponse>>() {
            @Override
            public void onResponse(Call<List<CartResponse>> call, Response<List<CartResponse>> response) {

    try {
        if(response.body().get(0).getCimage() == null){
            progressBar.setVisibility(View.GONE);
            new SweetAlertDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Item in cart!")
                    .setContentText("Add some to order!!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            CartActivity.super.onBackPressed();
                        }
                    })
                    .show();
        }
    if (!response.body().isEmpty()) {
        for (CartResponse c : response.body()) {
            total = total + (Integer.parseInt(c.getCprice()) * Integer.parseInt(c.getCqntty()));
            Call<DeliveryResponse> call1 = apiService.delivery();
            call1.enqueue(new Callback<DeliveryResponse>() {
                @Override
                public void onResponse(Call<DeliveryResponse> call, Response<DeliveryResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if(total < Integer.parseInt(response.body().getMin())){
                        Log.d("total before",""+total);
                        Log.d("Minimum",""+response.body().getMin());
                        total+=Integer.parseInt(response.body().getCharges());
                        Log.d("total After",""+total);
                        tot.setText("RS. "+total);

                        delivery.setText("RS. "+response.body().getCharges());
                        Toast.makeText(CartActivity.this, "Get Free delivery on order above ₹"+response.body().getMin(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this,"Connection Failed...",Toast.LENGTH_LONG ).show();
                }
            });

        }
        tot.setText("RS. " + total);
    }else{
        progressBar.setVisibility(View.GONE);
        new SweetAlertDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("No Item in cart!")
                .setContentText("Add some to order!!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        CartActivity.super.onBackPressed();
                    }
                })
                .show();
    }
    //Toast.makeText(CartActivity.this, ""+total, Toast.LENGTH_SHORT).show();
    //tot.setText(total);


    cartList = response.body();

    cartAdapter.setProductList(cartList);
}catch (Exception e){

}

            }

            @Override
            public void onFailure(Call<List<CartResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this,"Connection Failed...",Toast.LENGTH_LONG ).show();
            }
        } );



checkout.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(progressBar.isAnimating()){
            Toast.makeText(CartActivity.this, "Error!! Reload the cart", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(CartActivity.this, order.class);
            intent.putExtra("total",""+total);
            //Toast.makeText(CartActivity.this, ""+total, Toast.LENGTH_SHORT).show();
            startActivity(intent);
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

package htkc.ebaba.ecom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.OrderResponse;
import htkc.ebaba.ecom.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class order extends AppCompatActivity implements PaymentResultListener {
    ImageView imageView;
    CheckBox cod,online;
    TextInputEditText house,area,city,pincode;
    TextView paymnt,proceed;
    String total;
    String u_id,hno,cty,ara,pin;
    ProgressDialog progressDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Checkout.preload(order.this);
        //imageView = findViewById(R.id.img);
        cod=findViewById(R.id.cash);
        online=findViewById(R.id.online);
        house=findViewById(R.id.house);
        area=findViewById(R.id.area);
        city=findViewById(R.id.city);
        pincode=findViewById(R.id.pin);
        paymnt=findViewById(R.id.payt);
        proceed=findViewById(R.id.proceed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment");

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
        Intent intent=getIntent();
        total=intent.getStringExtra("total");
        u_id= SharedPrefManager.getInstance(order.this).getUser().getId();
        paymnt.setText(total);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
//                startActivity(intent);
//            }
//        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(online.isChecked()){
                    online.setChecked(false);
                }
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cod.isChecked()){
                    cod.setChecked(false);
                }
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hno=house.getText().toString();
                ara=area.getText().toString();
                cty=city.getText().toString();
                pin=pincode.getText().toString();
                if(hno.isEmpty() || ara.isEmpty() || cty.isEmpty() || pin.isEmpty()){
                    Toast.makeText(order.this, "Please fill the deleviring address!!!", Toast.LENGTH_SHORT).show();
                }else if(total.equalsIgnoreCase("")){
                    Toast.makeText(order.this, "No Items to proceed!!", Toast.LENGTH_SHORT).show();
                }
                else {


                    if (online.isChecked()) {
                        startPayment();
                    } else if(cod.isChecked()) {
                        progressDialog=new ProgressDialog(order.this);
                        progressDialog.setMessage("Working...!!");
                        progressDialog.show();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiURL.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiService apiService = retrofit.create(ApiService.class);

                        Call<OrderResponse> call = apiService.order(u_id, "cod", hno, ara, cty, pin, total);
                        call.enqueue(new Callback<OrderResponse>() {
                            @Override
                            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                progressDialog.dismiss();
                                if (response.body().getSuccess().equalsIgnoreCase("yes")) {

                                    new SweetAlertDialog(order.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("ThanK You!")
                                            .setContentText("Order has been placed!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    startActivity(new Intent(order.this, HomeActivity.class));
                                                }
                                            })
                                            .show();
                                }else{

                                    new SweetAlertDialog(order.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("ThanK You!")
                                            .setContentText("Order has been placed!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    startActivity(new Intent(order.this, HomeActivity.class));
                                                }
                                            })
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                new SweetAlertDialog(order.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("ThanK You!")
                                        .setContentText("Order has been placed!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                startActivity(new Intent(order.this, HomeActivity.class));
                                            }
                                        })
                                        .show();
                            }
                        });
                    }else{
                        Toast.makeText(order.this, "Please select a transaction mode!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void startPayment(){
        progressDialog=new ProgressDialog(order.this);
        progressDialog.setMessage("Processing...!!");
        progressDialog.show();
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_wiHZuAKDtyfbUb");
        checkout.setImage(R.drawable.logo_small);

        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Vibrent");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");


            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            int a = Integer.parseInt(total)*100;
            options.put("amount", ""+a);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentSuccess(String s) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ApiURL.BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<OrderResponse> call = apiService.order(u_id,"online",hno,ara,cty,pin,total);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if(response.body().getSuccess().equalsIgnoreCase("yes")){
                    progressDialog.dismiss();
                    new SweetAlertDialog(order.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("ThanK You!")
                            .setContentText("Order has been placed!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(order.this, HomeActivity.class));
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                progressDialog.dismiss();
                new SweetAlertDialog(order.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("ThanK You!")
                        .setContentText("Order has been placed!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                startActivity(new Intent(order.this, HomeActivity.class));
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        progressDialog.dismiss();
        Toast.makeText(this, "Error "+s, Toast.LENGTH_SHORT).show();
    }
}

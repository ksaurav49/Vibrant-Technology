package htkc.ebaba.ecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.OrderResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forgot extends AppCompatActivity {

    TextView mobile;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        mobile = findViewById(R.id.mo);
        button = findViewById(R.id.forgotpass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mo = mobile.getText().toString();
                if(mo.length() == 0){
                    Toast.makeText(Forgot.this, "Enter number", Toast.LENGTH_SHORT).show();
                }else{
                    ProgressDialog progressDialog = new ProgressDialog(Forgot.this);
                    progressDialog.setMessage("Please wait");
                    progressDialog.show();
                    //String s1= SharedPrefManager.getInstance(Forgot.this).getUser().getId();
                    Retrofit retrofit=new Retrofit.Builder()
                            .baseUrl( ApiURL.BASE_URL)
                            .addConverterFactory( GsonConverterFactory.create())
                            .build();
                    ApiService apiService=retrofit.create(ApiService.class);
                    Call<OrderResponse> call=apiService.forgot(mo);
                    call.enqueue(new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            progressDialog.dismiss();
                            if(response.body().getSuccess().equalsIgnoreCase("yes")) {
                                Toast.makeText(Forgot.this, "Password is sent to your inbox", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Forgot.this,UserLogin.class));
                            }else{
                                Toast.makeText(Forgot.this, "User Invalid", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Forgot.this,UserLogin.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(Forgot.this, "Error!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Forgot.this,UserLogin.class));
                        }
                    });
                }
            }
        });
    }
}

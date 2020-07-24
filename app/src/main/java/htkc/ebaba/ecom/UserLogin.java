package htkc.ebaba.ecom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogin extends AppCompatActivity
{
    TextInputEditText mymob,mypwd;
    Button btn1;
    private ProgressDialog mProgress;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        mymob=findViewById(R.id.mob);
        mypwd=findViewById(R.id.password);
        btn1=findViewById(R.id.signin);
        forgot = findViewById(R.id.forgot);

        mProgress =new ProgressDialog(this);
        String titleId="Processing...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");



        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            startActivity( new Intent( UserLogin.this, HomeActivity.class ) );
            finish();
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLogin.this,Forgot.class));
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1,s2;
                s1=mymob.getText().toString();
                s2=mypwd.getText().toString();

                mProgress.show();

                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl( ApiURL.BASE_URL)
                        .addConverterFactory( GsonConverterFactory.create())
                        .build();
                ApiService apiService=retrofit.create(ApiService.class);
                Call<UserResponse> call=apiService.login(s1,s2);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        //Toast.makeText(UserLogin.this, "AAA gayaa", Toast.LENGTH_SHORT).show();

                        if(!response.body().getError())
                        {
                            String muname=response.body().getUname();
                            String mmobile=response.body().getMobile();
                            String mid=response.body().getId();

                                // set the shared preference and go to home
                            SharedPrefManager.getInstance(UserLogin.this).userLogin(muname,mmobile,mid);

                            mProgress.dismiss();
                            Toast.makeText( UserLogin.this, "Successfully Loged In...!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UserLogin.this, HomeActivity.class));
                            finish();
                        }
                        else
                        {
                            mProgress.dismiss();
                            Toast.makeText( UserLogin.this, "Username Or Password is Incorrect...!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        mProgress.dismiss();
                        Toast.makeText( UserLogin.this, "Connection Failled...!", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }

    public void skipSignIn(View view) {
        startActivity( new Intent( UserLogin.this, HomeActivity.class ) );
        finish();
    }

    public void btn_signup(View view) {
        startActivity( new Intent( UserLogin.this, UserRegister.class ) );
        finish();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder( UserLogin.this);
        builder.setMessage("Are you sure want to do this ?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UserLogin.super.onBackPressed();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}

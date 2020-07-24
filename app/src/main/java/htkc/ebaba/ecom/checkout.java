package htkc.ebaba.ecom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import htkc.ebaba.ecom.utils.Tools;

public class checkout extends AppCompatActivity {
    ImageView imageView;
    CheckBox cod,online;
    TextInputEditText house,area,city,pincode;
    TextView paymnt,proceed;
    String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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

}

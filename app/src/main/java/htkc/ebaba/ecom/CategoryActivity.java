package htkc.ebaba.ecom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import htkc.ebaba.ecom.adapter.SubCategoryAdapter;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.CategoryRetResponse;
import htkc.ebaba.ecom.response.SubCategoryRetResponse;
import htkc.ebaba.ecom.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryActivity extends AppCompatActivity  implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private List<SubCategoryRetResponse> subCategoryList;
    private SliderLayout Slider;
    private SubCategoryAdapter subCategoryAdapter;
    private boolean is_account_mode = false;

    private RecyclerView recyclerView;
    ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category );

        initToolbar();
        initComponent();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(CategoryActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        subCategoryAdapter= new SubCategoryAdapter(CategoryActivity.this,subCategoryList);
//        recyclerView.setAdapter(subCategoryAdapter);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager( CategoryActivity.this );
        recyclerView.setLayoutManager( layoutManager );
        subCategoryAdapter= new SubCategoryAdapter(CategoryActivity.this,subCategoryList);
        recyclerView.setAdapter( subCategoryAdapter );
        recyclerView.setLayoutManager( new GridLayoutManager( this, 2 ) );
        recyclerView.addItemDecoration( new SpacingItemDecoration( 2, Tools.dpToPx( this, 8 ), true ) );
        recyclerView.setHasFixedSize( true );
        recyclerView.setNestedScrollingEnabled( false );
        spinner = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        String cat_ids = intent.getStringExtra("my_cat_id");
        spinner.setVisibility(View.VISIBLE);
        //Log.d( "bhh",cat_ids );
        //Toast.makeText( this, cat_ids, Toast.LENGTH_SHORT ).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ApiURL.BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Slider = (SliderLayout) findViewById(R.id.sliderss);


        Call<List<CategoryRetResponse>> call1 = apiService.getCategory();
        call1.enqueue(new Callback<List<CategoryRetResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryRetResponse>> call, Response<List<CategoryRetResponse>> response) {
                List<CategoryRetResponse> obj = response.body();
                HashMap<String, String> url_maps = new HashMap<String, String>();
                for (CategoryRetResponse h : obj) {

                    String picpath = ApiURL.BASE_URL  + h.getCat_img();
                    Log.d("Complete", picpath);
                    //Picasso.get().load(picpath).into(img1);

                    url_maps.put(h.getCat_name(), picpath);
                }
                for (String name : url_maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(CategoryActivity.this);
                    //initialize a SliderLayout
                    textSliderView
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit);


                    Slider.addSlider(textSliderView);


                }

                Slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                Slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                Slider.setCustomAnimation(new DescriptionAnimation());
                Slider.setDuration(4000);
                // Slider.addOnPageChangeListener(this);


            }

            @Override
            public void onFailure(Call<List<CategoryRetResponse>> call, Throwable t) {

            }

        });

        Call<List<SubCategoryRetResponse>> call = apiService.getSubCategory(cat_ids);
        call.enqueue(new Callback<List<SubCategoryRetResponse>>() {
            @Override
            public void onResponse(Call<List<SubCategoryRetResponse>> call, Response<List<SubCategoryRetResponse>> response) {
                spinner.setVisibility(View.GONE);
                if(response.body().get(0).getImg() == null){
                    new SweetAlertDialog(CategoryActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Item!")
                            .setContentText("Choose another category!!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    CategoryActivity.super.onBackPressed();
                                }
                            })
                            .show();
                }else{
                    List<SubCategoryRetResponse> obj = response.body();

                    subCategoryList=response.body();

                    subCategoryAdapter.setCategoryList(subCategoryList);
                }

            }

            @Override
            public void onFailure(Call<List<SubCategoryRetResponse>> call, Throwable t) {
                spinner.setVisibility(View.GONE);
            }

        });

    }


    private void initComponent() {


    }


    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_900);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart_setting, menu);
        return true;
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

    @Override
    public void onSliderClick(BaseSliderView slider) {

       // Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void openCart(MenuItem item) {

        if(!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            startActivity( new Intent( CategoryActivity.this, UserLogin.class ) );
            finish();
        }
        else {
            startActivity( new Intent( CategoryActivity.this, CartActivity.class ) );
        }
    }
    public void logt(MenuItem item) {

        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            SharedPrefManager.getInstance(this).logout();
            startActivity( new Intent( CategoryActivity.this, UserLogin.class ) );
            finish();
        }
    }
}
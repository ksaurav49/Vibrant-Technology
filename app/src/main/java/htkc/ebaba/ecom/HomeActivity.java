package htkc.ebaba.ecom;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;

import htkc.ebaba.ecom.adapter.CategoryAdapter;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.CategoryRetResponse;
import htkc.ebaba.ecom.response.SliderRetResponse;
import htkc.ebaba.ecom.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity  implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private ActionBar actionBar;
    private Toolbar toolbar;
    private List<CategoryRetResponse> categoryList;
    private CategoryAdapter categoryAdapter;
    private DrawerLayout drawer;
    private View navigation_header;
    private TextView logout;
    private boolean is_account_mode = false;

    private RecyclerView recyclerView;
    private SliderLayout Slider;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home );

        initToolbar();
        initComponent();
        initNavigationMenu();

        Slider = (SliderLayout) findViewById(R.id.slider);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ApiURL.BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<SliderRetResponse>> call = apiService.getSlider();
        call.enqueue(new Callback<List<SliderRetResponse>>() {
            @Override
            public void onResponse(Call<List<SliderRetResponse>> call, Response<List<SliderRetResponse>> response) {
                List<SliderRetResponse> obj = response.body();
                HashMap<String, String> url_maps = new HashMap<String, String>();
                for (SliderRetResponse h : obj) {

                    String picpath = ApiURL.BASE_URL  + h.getSimage();
                    Log.d("Complete", picpath);
                    //Picasso.get().load(picpath).into(img1);

                    url_maps.put(h.getSname(), picpath);
                }
                for (String name : url_maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(getApplicationContext());
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
            public void onFailure(Call<List<SliderRetResponse>> call, Throwable t) {

            }

        });

    }

    private void initComponent() {

        recyclerView = findViewById( R.id.recyclerView );
        spinner = findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager( HomeActivity.this );
        recyclerView.setLayoutManager( layoutManager );
        categoryAdapter = new CategoryAdapter( HomeActivity.this, categoryList );
        recyclerView.setAdapter( categoryAdapter );
        recyclerView.setLayoutManager( new GridLayoutManager( this, 2 ) );
        recyclerView.addItemDecoration( new SpacingItemDecoration( 2, Tools.dpToPx( this, 8 ), true ) );
        recyclerView.setHasFixedSize( true );
        recyclerView.setNestedScrollingEnabled( false );
        spinner.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ApiURL.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
        ApiService apiService = retrofit.create( ApiService.class );

        Call<List<CategoryRetResponse>> call = apiService.getCategory();
        call.enqueue( new Callback<List<CategoryRetResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryRetResponse>> call, Response<List<CategoryRetResponse>> response) {
                spinner.setVisibility(View.GONE);
                categoryList = response.body();

                categoryAdapter.setProductList( categoryList );
            }

            @Override
            public void onFailure(Call<List<CategoryRetResponse>> call, Throwable t) {
                spinner.setVisibility(View.GONE);
            }

        } );
    }

    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.pink_600));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar.setTitle("Home");
    }



    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };
        if(!SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn())
        {
            Menu menu = nav_view.getMenu();
            // find MenuItem you want to change
            MenuItem log = menu.findItem(R.id.log);
            log.setTitle("Login");

        }else{
            Menu menu = nav_view.getMenu();
            // find MenuItem you want to change
            MenuItem log = menu.findItem(R.id.log);
            log.setTitle("Logout");

        }

        nav_view.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.hom:
                        // Handle menu click
                        startActivity( new Intent( HomeActivity.this,HomeActivity.class ) );
                        break;
                        //finish();
                    case R.id.about:
                        // Handle menu click
                        Intent intent = new Intent(HomeActivity.this, About.class);
                        startActivity(intent);
                        //Toast.makeText(HomeActivity.this, "About", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.share:
                        // Handle menu click
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case R.id.contact:
                        // Handle menu click
                        String phone = "+917000715346";
                        Intent inten = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(inten);
//                        Toast.makeText(HomeActivity.this, "contact", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.myorder:
                        // Handle settings click
                        if(!SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn()) {
                            startActivity(new Intent(HomeActivity.this, UserLogin.class));
                        }else{
                            startActivity(new Intent(HomeActivity.this, myorder.class));
                        }
                        break;
                    case R.id.log:
                        // Handle settings click
                        if(!SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn())
                        {
                            Menu menu = nav_view.getMenu();
                            // find MenuItem you want to change
                            MenuItem log = menu.findItem(R.id.log);
                            log.setTitle("Login");
                            startActivity( new Intent( HomeActivity.this, UserLogin.class ) );

                        }
                        else {
                            SharedPrefManager.getInstance(HomeActivity.this).logout();
                            startActivity( new Intent( HomeActivity.this, UserLogin.class ) );
                        }

                        break;
                    default:
                        return false;
                }
                return false;
            }
        } );

        navigation_header = nav_view.getHeaderView(0);

    }


    private void updateCounter(NavigationView nav) {
        if (is_account_mode) return;

        Menu m = nav.getMenu();


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
            //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder( HomeActivity.this);
            builder.setMessage("Are you sure want to do this ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            HomeActivity.super.onBackPressed();

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
            startActivity( new Intent( HomeActivity.this, UserLogin.class ) );
            finish();
        }
        else {
            startActivity( new Intent( HomeActivity.this, CartActivity.class ) );
        }
    }
    public void logt(MenuItem item) {

        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            SharedPrefManager.getInstance(this).logout();
            startActivity( new Intent( HomeActivity.this, UserLogin.class ) );
            finish();
        }
    }

}
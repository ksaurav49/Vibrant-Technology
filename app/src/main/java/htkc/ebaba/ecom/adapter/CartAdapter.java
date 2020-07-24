package htkc.ebaba.ecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import htkc.ebaba.ecom.CartActivity;
import htkc.ebaba.ecom.R;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.response.CartResponse;
import htkc.ebaba.ecom.response.UpdateCartResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyviewHolder>
{
    Context context;
    List<CartResponse> cartList;

    public CartAdapter(Context context, List<CartResponse> cartResponseList)
    {
        this.context = context;
        this.cartList = cartResponseList;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try{
            View view= LayoutInflater.from(context).inflate( R.layout.cart_row_item,parent,false);
            return new MyviewHolder(view);
        }catch (Exception e){
            Toast.makeText(context, "No item yet!!!", Toast.LENGTH_SHORT).show();
            return new MyviewHolder(null);
        }


    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        holder.cpname.setText(cartList.get(position).getCpname());
        holder.tv_qty.setText(cartList.get(position).getCqntty());
        holder.cid.setText(cartList.get(position).getCid());
        holder.cprice.setText(cartList.get(position).getCprice());
        holder.fab_qty_sub.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.tv_qty.getText().toString());
                if (qty > 0) {
                    qty--;
                    holder.tv_qty.setText(qty + "");

                    String cids = holder.cid.getText().toString();
                    String qtty = holder.tv_qty.getText().toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl( ApiURL.BASE_URL)
                            .addConverterFactory( GsonConverterFactory.create())
                            .build();
                    ApiService apiService = retrofit.create(ApiService.class);

                    Call<UpdateCartResponse> call = apiService.updateCart(cids,qtty);
                    call.enqueue( new Callback<UpdateCartResponse>() {
                        @Override
                        public void onResponse(Call<UpdateCartResponse> call, Response<UpdateCartResponse> response) {

                            Intent intent = new Intent(context, CartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);

                        }

                        @Override
                        public void onFailure(Call<UpdateCartResponse> call, Throwable t) {
                            Intent intent = new Intent(context, CartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    } );
                }
            }
        });

        holder.fab_qty_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.tv_qty.getText().toString());
                if (qty < 10) {
                    qty++;
                    holder.tv_qty.setText(qty + "");String cids = holder.cid.getText().toString();
                    String qtty = holder.tv_qty.getText().toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl( ApiURL.BASE_URL)
                            .addConverterFactory( GsonConverterFactory.create())
                            .build();
                    ApiService apiService = retrofit.create(ApiService.class);

                    Call<UpdateCartResponse> call = apiService.updateCart(cids,qtty);
                    call.enqueue( new Callback<UpdateCartResponse>() {
                        @Override
                        public void onResponse(Call<UpdateCartResponse> call, Response<UpdateCartResponse> response) {

                            Intent intent = new Intent(context, CartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<UpdateCartResponse> call, Throwable t) {
                            Intent intent = new Intent(context, CartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);

                        }
                    } );
                }
            }
        });
        // picasso ka code
        String picname = cartList.get(position).getCimage();
        String picpath = ApiURL.BASE_URL  + picname;
        Log.d("Complete", picpath);

        Glide.with(holder.pimg)
                .load(picpath)
        .into(holder.pimg);

    }

    @Override
    public int getItemCount() {
        if(cartList!= null){
            return cartList.size();
        }

        return 0;
    }


    public void setProductList(List<CartResponse> cartList) {
        this.cartList=cartList;
        notifyDataSetChanged();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder
    {
        TextView cpname,cid,cprice;
        ImageView pimg;
        TextView tv_qty;
        ImageButton fab_qty_sub,fab_qty_add;
        public MyviewHolder(View itemView)
        {
            super(itemView);
            cpname = (TextView)itemView.findViewById(R.id.pname);
            cprice = (TextView)itemView.findViewById(R.id.price);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            cid = (TextView)itemView.findViewById(R.id.cid);
            pimg=(ImageView)itemView.findViewById(R.id.pimg);
            fab_qty_sub=(ImageButton)itemView.findViewById(R.id.fab_qty_sub);
            fab_qty_add=(ImageButton)itemView.findViewById(R.id.fab_qty_add);

        }
    }
}

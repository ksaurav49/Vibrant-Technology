package htkc.ebaba.ecom.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import htkc.ebaba.ecom.R;
import htkc.ebaba.ecom.api.ApiService;
import htkc.ebaba.ecom.api.ApiURL;
import htkc.ebaba.ecom.myorder;
import htkc.ebaba.ecom.problem;
import htkc.ebaba.ecom.response.MyOrderResponse;
import htkc.ebaba.ecom.response.ProbResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyviewHolder> {
    Context context;
    List<MyOrderResponse> myOrderResponses;

    public MyOrderAdapter(Context context, List<MyOrderResponse> myOrderResponses) {
        this.context = context;
        this.myOrderResponses = myOrderResponses;
    }

    @NonNull
    @Override
    public MyOrderAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate( R.layout.myorder_list,parent,false);
        return new MyOrderAdapter.MyviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.MyviewHolder holder, int position) {

        holder.name.setText(myOrderResponses.get(position).getOrder_id());
        //Log.d("order_id::::",myOrderResponses.get(position).getOrder_id());
        //Toast.makeText(context, ""+myOrderResponses.get(position).getOrder_id(), Toast.LENGTH_SHORT).show();
        holder.item.setText(myOrderResponses.get(position).getProduct());
        holder.date.setText(myOrderResponses.get(position).getDate());
        holder.amnt.setText(myOrderResponses.get(position).getSum());
        if(myOrderResponses.get(position).getStatus().equalsIgnoreCase("ordered")){
            holder.stat.setText("Order Placed");
        }else{
            holder.stat.setText(myOrderResponses.get(position).getStatus());
        }
        if(myOrderResponses.get(position).getStatus().equalsIgnoreCase("cancelled")
            || myOrderResponses.get(position).getStatus().equalsIgnoreCase("delivered")
                || myOrderResponses.get(position).getStatus().equalsIgnoreCase("shipped")){
            holder.cancel.setVisibility(View.GONE);
        }else{
            holder.cancel.setVisibility(View.VISIBLE);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.progressDialog.setMessage("Please Wait!");
                holder.progressDialog.show();
                String oid = holder.name.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl( ApiURL.BASE_URL)
                        .addConverterFactory( GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<ProbResponse> call = apiService.cancel(oid);
                call.enqueue(new Callback<ProbResponse>() {
                    @Override
                    public void onResponse(Call<ProbResponse> call, Response<ProbResponse> response) {
                        holder.progressDialog.dismiss();
                        if(response.body().getSuccess().equalsIgnoreCase("yes")){
                            Toast.makeText(context, "Order Cancled", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, myorder.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProbResponse> call, Throwable t) {
                        holder.progressDialog.dismiss();
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.odd.setText(myOrderResponses.get(position).getO_id());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oid=holder.odd.getText().toString();
                Intent intent = new Intent(context, problem.class);
                //Toast.makeText( context, catids, Toast.LENGTH_SHORT ).show();
                intent.putExtra( "order_id",oid );
                intent.putExtra( "name",holder.item.getText().toString() );
                context.startActivity(intent);
                //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(myOrderResponses!= null){
            return myOrderResponses.size();
        }

        return 0;
    }

    public void setMyOrderResponses(List<MyOrderResponse> myOrderResponses) {
        this.myOrderResponses = myOrderResponses;
        notifyDataSetChanged();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView item,date,amnt,stat,name,odd;
        Button button,cancel;
        ProgressDialog progressDialog;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            item=itemView.findViewById(R.id.oitem);
            date=itemView.findViewById(R.id.odt);
            amnt=itemView.findViewById(R.id.oamt);
            stat=itemView.findViewById(R.id.ostat);
            name=itemView.findViewById(R.id.osname);
            button=itemView.findViewById(R.id.prob);
            odd=itemView.findViewById(R.id.odd);
            cancel = itemView.findViewById(R.id.cancel);
            progressDialog = new ProgressDialog(context);

        }
    }
}

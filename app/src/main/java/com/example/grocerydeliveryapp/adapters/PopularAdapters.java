package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.PopularModel;

import java.util.List;

public class PopularAdapters extends RecyclerView.Adapter<PopularAdapters.ViewHolder> {

  private Context context;
  private List<PopularModel> popularModelList;

  public PopularAdapters(Context context, List<PopularModel> popularModelList) {
    this.context = context;
    this.popularModelList = popularModelList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Glide.with(context).load(popularModelList.get(position).getImgUrl1()).into(holder.popImg1);
    Glide.with(context).load(popularModelList.get(position).getImgUrl2()).into(holder.popImg2);
    Glide.with(context).load(popularModelList.get(position).getImgUrl3()).into(holder.popImg3);
    Glide.with(context).load(popularModelList.get(position).getImgUrl4()).into(holder.popImg4);
    holder.name.setText(popularModelList.get(position).getName());
  }

  @Override
  public int getItemCount() {
    return popularModelList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView popImg1,popImg2,popImg3,popImg4;
    TextView name;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      popImg1 = (ImageView) itemView.findViewById(R.id.popImg1);
      popImg2 = (ImageView) itemView.findViewById(R.id.popImg2);
      popImg3 = (ImageView) itemView.findViewById(R.id.popImg3);
      popImg4 = (ImageView) itemView.findViewById(R.id.popImg4);
      name = (TextView) itemView.findViewById(R.id.popName);
    }
  }
}

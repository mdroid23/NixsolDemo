package com.nixsol.mahesh.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nixsol.mahesh.R;
import com.nixsol.mahesh.model.response.Fact;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FactListAdapter extends RecyclerView.Adapter<FactListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Fact> factArrayList;
    private LayoutInflater mInflater;

    final LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
    final LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);

    public FactListAdapter(Context mContext, ArrayList<Fact> facts) {
        this.mContext = mContext;
        this.factArrayList = facts;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_fact_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Fact model = factArrayList.get(holder.getAdapterPosition());
        holder.textTitle.setText(model.getTitle());

        if (TextUtils.isEmpty(model.getDescription())) {
            holder.textDescription.setVisibility(View.INVISIBLE);
        } else {
            holder.textDescription.setText(model.getDescription());
            holder.textDescription.setVisibility(View.VISIBLE);
        }

        //default weight for child layout image and text description
        childParam1.weight = 0.7f;
        childParam2.weight = 0.3f;
        holder.textDescription.setLayoutParams(childParam1);
        holder.imageView.setLayoutParams(childParam2);

        if (!TextUtils.isEmpty(model.getImageHref())) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(model.getImageHref())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.imageView.setVisibility(View.GONE);
                            setWhenImageNotPresent(holder);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.imageView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        } else {
            setWhenImageNotPresent(holder);
            holder.imageView.setVisibility(View.GONE);
        }


    }

    private void setWhenImageNotPresent(ViewHolder holder) {
        childParam1.weight = 1f;
        childParam2.weight = 0f;
        holder.textDescription.setLayoutParams(childParam1);
        holder.imageView.setLayoutParams(childParam2);
        holder.imageView.requestLayout();
        holder.textDescription.requestLayout();
    }

    @Override
    public int getItemCount() {
        return factArrayList.size();
    }

    public void setItems(ArrayList<Fact> facts) {
        this.factArrayList = facts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.texTitle)
        TextView textTitle;
        @BindView(R.id.textDescription)
        TextView textDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

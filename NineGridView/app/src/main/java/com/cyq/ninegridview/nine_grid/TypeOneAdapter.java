package com.cyq.ninegridview.nine_grid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cyq.ninegridview.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Create by 陈扬齐
 * Create on 2019-07-07
 * description:
 */
public class TypeOneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_ONE = 1;
    private final int ITEM_TYPE_TOW = 2;
    private final int ITEM_TYPE_THREE = 3;

    private Context mContext;
    private List<String> paths;


    @Override
    public int getItemViewType(int position) {
        if (paths.size() == 1) {
            return ITEM_TYPE_ONE;
        } else if (paths.size() == 2 || paths.size() == 4) {
            return ITEM_TYPE_TOW;
        } else if (paths.size() <= 9) {
            return ITEM_TYPE_THREE;
        }
        return 0;
    }

    public TypeOneAdapter(Context mContext, List<String> paths) {
        this.mContext = mContext;
        this.paths = paths;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == ITEM_TYPE_ONE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_img_auto_size,
                    viewGroup,
                    false);
            return new TypeOneViewHolder(view);
        } else if (type == ITEM_TYPE_TOW) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_img_square, viewGroup,
                    false);
            return new TypeTowViewHolder(view);
        } else if (type == ITEM_TYPE_THREE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_img_square, viewGroup,
                    false);
            return new TypeThreeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof TypeOneViewHolder) {
            Glide.with(mContext).load(paths.get(i)).placeholder(R.mipmap.ic_launcher)
                    .apply(bitmapTransform(new RoundedCornersTransformation(30, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(((TypeOneViewHolder) holder).imageView);
        } else if (holder instanceof TypeTowViewHolder) {
            Glide.with(mContext).load(paths.get(i)).placeholder(R.mipmap.ic_launcher)
                    .apply(bitmapTransform(new RoundedCornersTransformation(30, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(((TypeTowViewHolder) holder).squareImageView);
        } else if (holder instanceof TypeThreeViewHolder) {
            Glide.with(mContext).load(paths.get(i)).placeholder(R.mipmap.ic_launcher)
                    .apply(bitmapTransform(new RoundedCornersTransformation(30, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(((TypeThreeViewHolder) holder).squareImageView);
        }

    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    class TypeOneViewHolder extends RecyclerView.ViewHolder {
        AutoSizeImageView imageView;

        public TypeOneViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_auto_size);
        }
    }

    class TypeTowViewHolder extends RecyclerView.ViewHolder {
        SquareImageView squareImageView;

        public TypeTowViewHolder(@NonNull View itemView) {
            super(itemView);
            squareImageView = itemView.findViewById(R.id.iv_square);
        }
    }

    class TypeThreeViewHolder extends RecyclerView.ViewHolder {
        SquareImageView squareImageView;

        public TypeThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            squareImageView = itemView.findViewById(R.id.iv_square);
        }
    }
}
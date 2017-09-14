package com.onespringday.earnmoney.Inquiry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onespringday.earnmoney.R;

import java.util.List;

/**
 * Created by yoseong on 2017-08-06.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ProductItem> data;

    public ProductListAdapter(Context context, List<ProductItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.product_cardview_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        final ProductItem productItem = data.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;

        // 상세 정보 설정
        cardViewHolder.productName.setText(productItem.getName());
        cardViewHolder.productSellingPrice.setText("정상판매가: "+productItem.getSellingPrice()+"원");
        cardViewHolder.productUnitPrice.setText("한국원가: "+productItem.getUnitPrice()+"원");
        cardViewHolder.productProfit.setText("이익금: "+productItem.getProfit()+"원");
        cardViewHolder.productColor.setText("색상: "+productItem.getColor());
        cardViewHolder.productSize.setText("사이즈: "+productItem.getSize());

        // glide 라이브러리를 사용해서 이미지 로딩 속도를 현저하게 줄여 RecyvlerView의 스크롤 속도 느려짐 해결
        Glide.with(context).load(productItem.getPhoto()).into(cardViewHolder.productPhoto);

        cardViewHolder.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_detail = new Intent(context, ProductDetailActivity.class);
                go_to_detail.putExtra("Member_Id", productItem.getMember_Id());
                go_to_detail.putExtra("Korea_Name", productItem.getName());
                go_to_detail.putExtra("Color", productItem.getColor());
                go_to_detail.putExtra("Size", productItem.getSize());
                context.startActivity(go_to_detail);
            }
        });

        cardViewHolder.productCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * product_card_view layout 에 있는 아이템 불러오기
     */
    private class CardViewHolder extends RecyclerView.ViewHolder {

        // product_card_view layout 에 있는 text, image view 선언
        private ImageView productPhoto;
        private TextView productName;
        private TextView productSellingPrice;
        private TextView productUnitPrice;
        private TextView productProfit;
        private TextView productColor;
        private TextView productSize;
        private CardView productCardView;

        public CardViewHolder(View view) {
            super(view);
            productPhoto = (ImageView) view.findViewById(R.id.product_photo);
            productName = (TextView) view.findViewById(R.id.product_name);
            productSellingPrice = (TextView) view.findViewById(R.id.product_selling_price);
            productUnitPrice = (TextView) view.findViewById(R.id.product_unit_price);
            productProfit = (TextView) view.findViewById(R.id.product_profit);
            productColor = (TextView) view.findViewById(R.id.product_color);
            productSize = (TextView) view.findViewById(R.id.product_size);
            productCardView = (CardView) view.findViewById(R.id.product_cardview);
        }
    }
}

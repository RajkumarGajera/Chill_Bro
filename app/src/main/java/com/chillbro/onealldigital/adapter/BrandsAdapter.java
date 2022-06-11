package com.chillbro.onealldigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import com.chillbro.onealldigital.R;
import com.chillbro.onealldigital.model.Brand;
import com.chillbro.onealldigital.interfaces.ClickHome;
import com.chillbro.onealldigital.model.getbrand;

public class BrandsAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ClickHome clickInterface;
    ArrayList<Brand> brands;

    public BrandsAdapter(Context context, ArrayList<Brand> brands, ClickHome clickInterface) {
        this.context = context;
        this.brands = brands;
        this.clickInterface = clickInterface;
    }

    @Override
    public int getCount() {
        return brands.size() /6 == 0 ? brands.size() /6 : brands.size() /6 + 1 ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout)o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.brand_layut,container,false);
        ImageView image_slide_1 = view.findViewById(R.id.image_slide_1);
        ImageView image_slide_2 = view.findViewById(R.id.image_slide_2);
        ImageView image_slide_3 = view.findViewById(R.id.image_slide_3);
        ImageView image_slide_4 = view.findViewById(R.id.image_slide_4);
        ImageView image_slide_5 = view.findViewById(R.id.image_slide_5);
        ImageView image_slide_6 = view.findViewById(R.id.image_slide_6);

        CardView brand_1 = view.findViewById(R.id.brand_1);
        CardView brand_2 = view.findViewById(R.id.brand_2);
        CardView brand_3 = view.findViewById(R.id.brand_3);
        CardView brand_4 = view.findViewById(R.id.brand_4);
        CardView brand_5 = view.findViewById(R.id.brand_5);
        CardView brand_6 = view.findViewById(R.id.brand_6);

        int currentBrandNum = Math.min(brands.size() - (position) * 6, 6);

        if (currentBrandNum < 6) brand_6.setVisibility(View.INVISIBLE);
        if (currentBrandNum < 5) brand_5.setVisibility(View.INVISIBLE);
        if (currentBrandNum < 4) brand_4.setVisibility(View.INVISIBLE);
        if (currentBrandNum < 3) brand_3.setVisibility(View.INVISIBLE);
        if (currentBrandNum < 2) brand_2.setVisibility(View.INVISIBLE);


        Picasso.get().load(brands.get(position*6).getImage()).into(image_slide_1);
        if (currentBrandNum > 1) {
            Picasso.get().load(brands.get(position*6+1).getImage()).into(image_slide_2);
        }

        if (currentBrandNum > 2) {
            Picasso.get().load(brands.get(position*6+2).getImage()).into(image_slide_3);
        }

        if (currentBrandNum > 3) {
            Picasso.get().load(brands.get(position*6+3).getImage()).into(image_slide_4);
        }

        if (currentBrandNum > 4) {
            Picasso.get().load(brands.get(position*6+4).getImage()).into(image_slide_5);
        }

        if (currentBrandNum > 5) {
            Picasso.get().load(brands.get(position*6+5).getImage()).into(image_slide_6);

        }

        brand_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position*6 + currentBrandNum <= brands.size())
                {
                    clickInterface.onClickBrand(brands.get(position*6));
                }
            }
        });

        brand_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position*6 + currentBrandNum <= brands.size())
                {
                    clickInterface.onClickBrand(brands.get(position*6 +1));
                }
            }
        });

        brand_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position*6 + currentBrandNum <= brands.size())
                {
                    clickInterface.onClickBrand(brands.get(position*6 + 2));
                }
            }
        });

        brand_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position*6 + currentBrandNum <= brands.size())
                {
                    clickInterface.onClickBrand(brands.get(position*6 + 3));
                }
            }
        });

        brand_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position*6 + currentBrandNum <= brands.size())
                {
                    clickInterface.onClickBrand(brands.get(position*6 + 4));
                }
            }
        });

        brand_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position*6 + currentBrandNum <= brands.size())
                {
                    getbrand getbrand = new getbrand(brands.get(position*6+5).getId().toString());
                    getbrand.setBrand_ID(brands.get(position*6+5).getId().toString());
                    clickInterface.onClickBrand(brands.get(position*6 + 5));
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object );
    }
}

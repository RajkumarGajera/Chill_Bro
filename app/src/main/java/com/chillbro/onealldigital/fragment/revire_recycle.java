package com.chillbro.onealldigital.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillbro.onealldigital.R;
import com.chillbro.onealldigital.adapter.seeallreviewAdapter;
import com.chillbro.onealldigital.model.Reviews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class revire_recycle extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Reviews> list;
    seeallreviewAdapter seeallreviewAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    View root;
    Activity activity;
    ImageView imageMenu;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.recycleview, container, false);

        recyclerView = root.findViewById(R.id.recyccleview);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        seeallreviewAdapter = new seeallreviewAdapter(getContext(), activity, list, R.layout.lyt_reviews, "sub_review", revire_recycle.this);
        recyclerView.setAdapter(seeallreviewAdapter);

        myRef.child("Review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Reviews reviews = dataSnapshot1.getValue(Reviews.class);
                        list.add(reviews);
//                        list.add(dataSnapshot1.getKey());
//                        list.add(reviews.getname());
//                        list.add(reviews.getRate());
//                        list.add(reviews.getRe());
                    }
                }
                System.out.println(list);
                seeallreviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return root;
    }
//    @SuppressLint("SetTextI18n")
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.toolbar_cart).setVisible(true);
//        menu.findItem(R.id.toolbar_notification).setVisible(true);
//        menu.findItem(R.id.toolbar_search).setVisible(false);
//        menu.findItem(R.id.toolbar_cart).setIcon(ApiConfig.buildCounterDrawable(Constant.TOTAL_CART_ITEM, activity));
//
//        if (fm.getBackStackEntryCount() > 0) {
//
//            drawerToggle.onDrawerClosed(drawer_layout);
//
//            bottomNavigationView.setVisibility(View.GONE);
//            imageMenu.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.img_back_arrow, activity.getTheme()));
//            imageMenu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    fm.popBackStack();
//                }
//            });
//            lockDrawer();
//
//        } else {
//            if (session.getBoolean(Constant.IS_USER_LOGIN)) {
//            } else {
//            }
//            bottomNavigationView.setVisibility(View.GONE);
//            imageMenu.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_menu, activity.getTheme()));
//            imageMenu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    drawer_layout.openDrawer(GravityCompat.START);
//                }
//            });
//            unLockDrawer();
//        }
//
//        invalidateOptionsMenu();
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    public void lockDrawer() {
//        ((DrawerLayout) findViewById(R.id.drawer_layout)).requestDisallowInterceptTouchEvent(true);
//        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
//    }
//
//    public void unLockDrawer() {
//        ((DrawerLayout) findViewById(R.id.drawer_layout)).requestDisallowInterceptTouchEvent(false);
//        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//    }
    
}




//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recycleview);
//
//        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//
//        imageMenu = findViewById(R.id.image_menu);
//        imageMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer_layout.openDrawer(GravityCompat.START);
//            }
//        });
//
//        recyclerView = findViewById(R.id.recyccleview);
//        list = new ArrayList<>();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        seeallreviewAdapter = new seeallreviewAdapter(this,list);
//        recyclerView.setAdapter(seeallreviewAdapter);
//
//        myRef.child("Review").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        Reviews reviews = dataSnapshot1.getValue(Reviews.class);
//                        list.add(reviews);
////                        list.add(dataSnapshot1.getKey());
////                        list.add(reviews.getname());
////                        list.add(reviews.getRate());
////                        list.add(reviews.getRe());
//                    }
//                }
//                System.out.println(list);
//                seeallreviewAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//}

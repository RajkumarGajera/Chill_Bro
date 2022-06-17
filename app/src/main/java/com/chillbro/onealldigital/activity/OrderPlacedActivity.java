package com.chillbro.onealldigital.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import com.chillbro.onealldigital.R;
import com.chillbro.onealldigital.fragment.CartFragment;
import com.chillbro.onealldigital.fragment.NotificationFragment;
import com.chillbro.onealldigital.fragment.SearchFragment;
import com.chillbro.onealldigital.helper.ApiConfig;
import com.chillbro.onealldigital.helper.Constant;
import com.chillbro.onealldigital.helper.Session;
import com.chillbro.onealldigital.helper.VolleyCallback;

public class OrderPlacedActivity extends AppCompatActivity {

    Activity activity;
    ProgressBar progressBar;
//    Button btnShopping, btnSummary;
    LottieAnimationView lottieAnimationView;
    private TextView tvOrderId;
    private Menu menu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        tvOrderId = findViewById(R.id.tv_order_id);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey(Constant.ORDERID)) {

            Intent intent = getIntent();
            String orderId = intent.getStringExtra(Constant.ORDERID);

            tvOrderId.setText("ORDER ID - "+orderId);
        }

        activity = this;
        Session session = new Session(activity);
        progressBar = findViewById(R.id.progressBar);
//        btnShopping = findViewById(R.id.btnShopping);
//        btnSummary = findViewById(R.id.btnSummary);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
//        setHasOptionsMenu(true);

        RemoveAllItemFromCart(activity, session);

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> finish());
    }

    public void RemoveAllItemFromCart(final Activity activity, Session session) {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.REMOVE_FROM_CART, Constant.GetVal);
        params.put(Constant.USER_ID, session.getData(Constant.ID));

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(Constant.ERROR)) {
                            ApiConfig.getCartItemCount(activity, session);
                            Constant.CartValues.clear();
                            progressBar.setVisibility(View.GONE);
                            lottieAnimationView.playAnimation();

                            /*btnSummary.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MainActivity.homeClicked = false;
                                    MainActivity.categoryClicked = false;
                                    MainActivity.favoriteClicked = false;
                                    MainActivity.trackingClicked = false;
                                    MainActivity.active = null;
                                    startActivity(new Intent(activity, MainActivity.class).putExtra(Constant.FROM, "tracker").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                            });

                            btnShopping.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(activity, MainActivity.class).putExtra(Constant.FROM, "").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                                }
                            });*/

                        }
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }
        }, activity, Constant.CART_URL, params, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.toolbar_cart).setIcon(ApiConfig.buildCounterDrawable(Constant.TOTAL_CART_ITEM, activity));
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.toolbar_cart).setVisible(true);
        menu.findItem(R.id.toolbar_notification).setVisible(true);
        menu.findItem(R.id.toolbar_search).setVisible(false);
        menu.findItem(R.id.toolbar_cart).setIcon(ApiConfig.buildCounterDrawable(Constant.TOTAL_CART_ITEM, activity));

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_cart:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("LOAD_FRAG", "CART");
                startActivity(intent);
                finish();
                break;
            case R.id.toolbar_notification:
                Intent intentNotify = new Intent(this, MainActivity.class);
                intentNotify.putExtra("LOAD_FRAG", "NOTIFICATION");
                startActivity(intentNotify);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
//        MainActivity.toolbar.setVisibility(View.GONE);
        lottieAnimationView.setAnimation("confetti.json");
//        activity.invalidateOptionsMenu();
//        hideKeyboard();
    }

    /*public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*@Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.toolbar_cart).setVisible(false);
        menu.findItem(R.id.toolbar_sort).setVisible(false);
        menu.findItem(R.id.toolbar_search).setVisible(false);
    }*/

}
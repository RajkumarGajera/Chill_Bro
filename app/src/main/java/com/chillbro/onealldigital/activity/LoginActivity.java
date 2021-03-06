package com.chillbro.onealldigital.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.chillbro.onealldigital.R;
import com.chillbro.onealldigital.helper.ApiConfig;
import com.chillbro.onealldigital.helper.Constant;
import com.chillbro.onealldigital.helper.DatabaseHelper;
import com.chillbro.onealldigital.helper.Session;
import com.chillbro.onealldigital.helper.Utils;
import com.chillbro.onealldigital.helper.VolleyCallback;
import com.chillbro.onealldigital.ui.PinView;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class LoginActivity extends AppCompatActivity {

    LinearLayout lytOTP;
    EditText edtResetPass, edtResetCPass, edtRefer, edtloginpassword, edtLoginMobile, edtname, edtemail, edtpsw, edtcpsw, edtMobileVerify;
    AppCompatButton btnLogin;
    CountryCodePicker edtCountryCodePicker;
    PinView pinViewOTP;
    TextView tvMobile, /*tvWelcome,*/ tvTimer, tvResend, tvForgotPass, tvPrivacyPolicy;
    ScrollView lytSignUp, lytVerify, lytResetPass;
    Session session;
    CheckBox chPrivacy;
    Animation animShow, animHide;
    private TextView tvAccount;
    private Button btnRegLogin;

    ConstraintLayout lytLogin;
    ////Firebase
    String phoneNumber, firebase_otp = "", otpFor = "";
    boolean resendOTP = false;
    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    DatabaseHelper databaseHelper;
    Activity activity;
    boolean timerOn;
    ImageView img;
    RelativeLayout lytWebView;
    String from, mobile, countryCode;
    WebView webView;
    ProgressDialog dialog;
    boolean forMultipleCountryUse = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;
        databaseHelper = new DatabaseHelper(activity);

        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);

        from = getIntent().getStringExtra(Constant.FROM);

        session = new Session(getApplicationContext());
        chPrivacy = findViewById(R.id.chPrivacy);
//        tvWelcome = findViewById(R.id.tvWelcome);
        edtCountryCodePicker = findViewById(R.id.edtCountryCodePicker);
        edtResetPass = findViewById(R.id.edtResetPass);
        edtResetCPass = findViewById(R.id.edtResetCPass);
        edtloginpassword = findViewById(R.id.edtloginpassword);
        edtLoginMobile = findViewById(R.id.edtLoginMobile);
        lytLogin = findViewById(R.id.lytLogin);
        lytResetPass = findViewById(R.id.lytResetPass);
        lytOTP = findViewById(R.id.lytOTP);
        pinViewOTP = findViewById(R.id.pinViewOTP);
        btnLogin = findViewById(R.id.btnLogin);
        edtMobileVerify = findViewById(R.id.edtMobileVerify);
        lytVerify = findViewById(R.id.lytVerify);
        lytSignUp = findViewById(R.id.lytSignUp);
        edtname = findViewById(R.id.edtname);
        edtemail = findViewById(R.id.edtemail);
        tvMobile = findViewById(R.id.tvMobile);
        edtpsw = findViewById(R.id.edtpsw);
        edtcpsw = findViewById(R.id.edtcpsw);
        edtRefer = findViewById(R.id.edtRefer);
        tvResend = findViewById(R.id.tvResend);
        tvTimer = findViewById(R.id.tvTimer);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacy);
        img = findViewById(R.id.img);
        lytWebView = findViewById(R.id.lytWebView);
        webView = findViewById(R.id.webView);

        tvAccount = findViewById(R.id.tv_no_account);
        btnRegLogin = findViewById(R.id.btn_register);
        btnRegLogin.setOnClickListener(v -> {
            String btnType = btnRegLogin.getText().toString();
            if (btnType.equals("LOGIN")){
                btnRegLogin.setText("REGISTER");
                tvAccount.setText(R.string.don_t_have_account);
                otpFor = "exist_user";
                lytLogin.setVisibility(View.VISIBLE);
                lytLogin.setAnimation(animShow);
                lytSignUp.setVisibility(View.GONE);
                lytVerify.setVisibility(View.GONE);
                lytOTP.setVisibility(View.GONE);
                lytResetPass.setVisibility(View.GONE);
            } else {
                tvAccount.setText(R.string.already_have_account);
                btnRegLogin.setText("LOGIN");
                otpFor = "new_user";
                edtMobileVerify.setText("");
                edtMobileVerify.setEnabled(true);
                lytOTP.setVisibility(View.GONE);
                lytVerify.setVisibility(View.VISIBLE);
                lytVerify.startAnimation(animShow);
                lytLogin.setVisibility(View.GONE);
            }
        });

        tvForgotPass.setText(underlineSpannable(getString(R.string.forgottext)));
//        edtLoginMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);

        edtloginpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
        edtpsw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
        edtcpsw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
        edtResetPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
        edtResetCPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);

        Utils.setHideShowPassword(edtpsw);
        Utils.setHideShowPassword(edtcpsw);
        Utils.setHideShowPassword(edtloginpassword);
        Utils.setHideShowPassword(edtResetPass);
        Utils.setHideShowPassword(edtResetCPass);

        lytResetPass.setVisibility(View.GONE);
        lytLogin.setVisibility(View.VISIBLE);
        lytVerify.setVisibility(View.GONE);
        lytSignUp.setVisibility(View.GONE);
        lytOTP.setVisibility(View.GONE);
        lytWebView.setVisibility(View.GONE);

//        tvWelcome.setText(getString(R.string.welcome) + getString(R.string.app_name));

        edtCountryCodePicker.setCountryForNameCode("IN");
//        forMultipleCountryUse = false;

        if (from != null) {
            switch (from) {
                case "drawer":
                case "checkout":
                case "tracker":
                    lytLogin.setVisibility(View.VISIBLE);
                    lytLogin.startAnimation(animShow);
                    new Handler().postDelayed(() -> edtLoginMobile.requestFocus(), 1500);
                    break;
                case "refer":
                    otpFor = "new_user";
                    lytVerify.setVisibility(View.VISIBLE);
                    lytVerify.startAnimation(animShow);
                    new Handler().postDelayed(() -> edtMobileVerify.requestFocus(), 1500);
                    break;
                default:
                    lytVerify.setVisibility(View.GONE);
                    lytResetPass.setVisibility(View.GONE);
                    lytVerify.setVisibility(View.GONE);
                    lytLogin.setVisibility(View.GONE);
                    lytSignUp.setVisibility(View.VISIBLE);
                    tvMobile.setText("+91"+mobile);
                    edtRefer.setText(Constant.FRND_CODE);
                    break;
            }
        } else {
            lytVerify.setVisibility(View.GONE);
            lytResetPass.setVisibility(View.GONE);
            lytVerify.setVisibility(View.GONE);
            lytLogin.setVisibility(View.VISIBLE);
            lytSignUp.setVisibility(View.GONE);
        }
        StartFirebaseLogin();
        PrivacyPolicy();
    }

    public void generateOTP() {
        dialog = ProgressDialog.show(activity, "", getString(R.string.please_wait), true);
        session.setData(Constant.COUNTRY_CODE, countryCode);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.TYPE, Constant.VERIFY_USER);
        params.put(Constant.MOBILE, "+91" + mobile);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject object = new JSONObject(response);
                    phoneNumber = ("+91" + mobile);
                    if (otpFor.equals("new_user")) {
                        if (!object.getBoolean(Constant.ERROR)) {
                            dialog.dismiss();
                            setSnackBar(getString(R.string.alert_register_num1) + getString(R.string.app_name) + getString(R.string.alert_register_num2), getString(R.string.btn_ok), from);
                        } else {
                            sentRequest(phoneNumber);
                        }
                    } else if (otpFor.equals("exist_user")) {
                        if (!object.getBoolean(Constant.ERROR)) {
                            Constant.U_ID = object.getString(Constant.ID);
                            sentRequest(phoneNumber);
                        } else {
                            dialog.dismiss();
                            setSnackBar(getString(R.string.alert_not_register_num1) + getString(R.string.app_name) + getString(R.string.alert_not_register_num2), getString(R.string.btn_ok), from);
                        }
                    }
                } catch (JSONException ignored) {

                }
            }
        }, activity, Constant.REGISTER_URL, params, false);
    }

    public void sentRequest(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallback)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                //System.out.println ("====verification complete call  " + phoneAuthCredential.getSmsCode ());
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                setSnackBar(e.getLocalizedMessage(), getString(R.string.btn_ok), Constant.FAILED);
            }

            @Override
            public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                dialog.dismiss();
                firebase_otp = s;
                pinViewOTP.requestFocus();
                if (resendOTP) {
                    Toast.makeText(getApplicationContext(), getString(R.string.otp_resend_alert), Toast.LENGTH_SHORT).show();
                } else {
                    edtMobileVerify.setEnabled(false);
                    edtCountryCodePicker.setCcpClickable(false);
//                    btnVerify.setText(getString(R.string.verify_otp));
                    lytOTP.setVisibility(View.VISIBLE);
                    lytOTP.startAnimation(animShow);
                    new CountDownTimer(120000, 1000) {
                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {
                            timerOn = true;
                            // Used for formatting digit to be in 2 digits only
                            NumberFormat f = new DecimalFormat("00");
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            tvTimer.setText(f.format(min) + ":" + f.format(sec));
                        }

                        public void onFinish() {
                            resendOTP = false;
                            timerOn = false;
                            tvTimer.setVisibility(View.GONE);
                            img.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary));
                            tvResend.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                            tvResend.setOnClickListener(v -> {
                                resendOTP = true;
                                sentRequest("+91"+ mobile);

                                new CountDownTimer(120000, 1000) {
                                    @SuppressLint("SetTextI18n")
                                    public void onTick(long millisUntilFinished) {

                                        tvTimer.setVisibility(View.VISIBLE);
                                        img.setColorFilter(ContextCompat.getColor(activity, R.color.gray));
                                        tvResend.setTextColor(activity.getResources().getColor(R.color.gray));

                                        timerOn = true;
                                        // Used for formatting digit to be in 2 digits only
                                        NumberFormat f = new DecimalFormat("00");
                                        long min = (millisUntilFinished / 60000) % 60;
                                        long sec = (millisUntilFinished / 1000) % 60;
                                        tvTimer.setText(f.format(min) + ":" + f.format(sec));
                                    }

                                    public void onFinish() {
                                        resendOTP = false;
                                        timerOn = false;
                                        tvTimer.setVisibility(View.GONE);
                                        img.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary));
                                        tvResend.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                                        tvResend.setOnClickListener(v1 -> {
                                            resendOTP = true;
                                            sentRequest("+91"+mobile);
                                        });
                                    }
                                }.start();
                            });
                        }
                    }.start();
                }
            }
        };
    }

    public void ForgotPassword() {
        String reset_psw = edtResetPass.getText().toString();
        String reset_c_psw = edtResetCPass.getText().toString();

        if (ApiConfig.CheckValidattion(reset_psw, false, false)) {
            edtResetPass.requestFocus();
            edtResetPass.setError(getString(R.string.enter_new_pass));
        } else if (ApiConfig.CheckValidattion(reset_c_psw, false, false)) {
            edtResetCPass.requestFocus();
            edtResetCPass.setError(getString(R.string.enter_confirm_pass));
        } else if (!reset_psw.equals(reset_c_psw)) {
            edtResetCPass.requestFocus();
            edtResetCPass.setError(getString(R.string.pass_not_match));
        } else if (ApiConfig.isConnected(activity)) {
            final Map<String, String> params = new HashMap<>();
            params.put(Constant.TYPE, Constant.FORGOT_PASSWORD_MOBILE);
            params.put(Constant.PASSWORD, reset_c_psw);
            //params.put(Constant.USER_ID, session.getData(Constant.ID));
            params.put(Constant.MOBILE, "+91"+mobile);
            ApiConfig.RequestToVolley((result, response) -> {
                if (result) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean(Constant.ERROR)) {
                            setSnackBar(getString(R.string.msg_reset_pass_success), getString(R.string.btn_ok), "forgot_password");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, activity, Constant.REGISTER_URL, params, true);
        }
    }

    public void UserLogin(String mobile, String password) {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.LOGIN, Constant.GetVal);
        params.put(Constant.MOBILE, "+91"+mobile);
        params.put(Constant.PASSWORD, password);
        params.put(Constant.FCM_ID, "" + session.getData(Constant.FCM_ID));
        ApiConfig.RequestToVolley((result, response) -> {

            //System.out.println ("============login res " + response);
            if (result) {
                try {
                    JSONObject objectbject = new JSONObject(response);
                    if (!objectbject.getBoolean(Constant.ERROR)) {
                        StartMainActivity(objectbject.getJSONArray(Constant.DATA).getJSONObject(0), password);
                    }
                    Toast.makeText(activity, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.LOGIN_URL, params, true);
    }


    public void setSnackBar(String message, String action, final String type) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, view -> {
            if (type.equals("forgot_password")) {
                try {
                    lytResetPass.setVisibility(View.GONE);
                    lytResetPass.startAnimation(animHide);
                    Thread.sleep(500);
                    lytVerify.setVisibility(View.GONE);
                    lytVerify.startAnimation(animHide);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            snackbar.dismiss();
        });

        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }

    @SuppressLint("SetTextI18n")
    public void OTP_Varification(String otptext) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(firebase_otp, otptext);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        if (otpFor.equals("new_user")) {
                            tvMobile.setText ("+91" + mobile);
                            lytSignUp.setVisibility(View.VISIBLE);
                            lytSignUp.startAnimation(animShow);
                        }
                        if (otpFor.equals("exist_user")) {
                            lytResetPass.setVisibility(View.VISIBLE);
                            lytResetPass.startAnimation(animShow);
                            System.out.println("lytResetPass.getVisibility() : " + lytResetPass.getVisibility() + ", " + View.VISIBLE + ", " + View.GONE);
                        }
                    } else {
                        //verification unsuccessful.. display an error message
                        String message = "Something is wrong, we will fix it soon...";
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }
                        pinViewOTP.requestFocus();
                        pinViewOTP.setError(message);
                    }
                });
    }

    public void UserSignUpSubmit(String name, String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.TYPE, Constant.REGISTER);
        params.put(Constant.NAME, name);
        params.put(Constant.EMAIL, email);
        params.put(Constant.MOBILE, "+91"+mobile);
        params.put(Constant.PASSWORD, password);
        params.put(Constant.COUNTRY_CODE, session.getData(Constant.COUNTRY_CODE));
        params.put(Constant.FCM_ID, "" + session.getData(Constant.FCM_ID));
        params.put(Constant.FRIEND_CODE, edtRefer.getText().toString().trim());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject objectbject = new JSONObject(response);
                    if (!objectbject.getBoolean(Constant.ERROR)) {
                        StartMainActivity(objectbject, password);
                    }
                    Toast.makeText(activity, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.REGISTER_URL, params, true);
    }

    public void OnBtnClick(View view) {
        int id = view.getId();
        hideKeyboard(activity, view);
        if (id == R.id.tvSignUp) {
            otpFor = "new_user";
            edtMobileVerify.setText("");
            edtMobileVerify.setEnabled(true);
            edtCountryCodePicker.setCcpClickable(forMultipleCountryUse);
            lytOTP.setVisibility(View.GONE);
            lytVerify.setVisibility(View.VISIBLE);
            lytVerify.startAnimation(animShow);
        } else if (id == R.id.tvForgotPass) {
            otpFor = "exist_user";
            edtMobileVerify.setText("");
            edtMobileVerify.setEnabled(true);
            edtCountryCodePicker.setCcpClickable(forMultipleCountryUse);
            lytOTP.setVisibility(View.GONE);
            lytLogin.setVisibility(View.GONE);
            lytVerify.setVisibility(View.VISIBLE);
            lytVerify.startAnimation(animShow);
        } else if (id == R.id.btnResetPass) {
            hideKeyboard(activity, view);
            ForgotPassword();
        } else if (id == R.id.btnLogin) {
            mobile = edtLoginMobile.getText().toString();
            final String password = edtloginpassword.getText().toString();

            if (ApiConfig.CheckValidattion(mobile, false, false)) {
                edtLoginMobile.requestFocus();
                edtLoginMobile.setError(getString(R.string.enter_mobile_no));
            } else if (ApiConfig.CheckValidattion(mobile, false, true)) {
                edtLoginMobile.requestFocus();
                edtLoginMobile.setError(getString(R.string.enter_valid_mobile_no));
            } else if (ApiConfig.CheckValidattion(password, false, false)) {
                edtloginpassword.requestFocus();
                edtloginpassword.setError(getString(R.string.enter_pass));
            } else if (ApiConfig.isConnected(activity)) {
                UserLogin(mobile, password);
            }
        } else if (id == R.id.btnVerify) {
            if (lytOTP.getVisibility() == View.GONE) {
                hideKeyboard(activity, view);
                mobile = edtMobileVerify.getText().toString().trim();
                countryCode = edtCountryCodePicker.getSelectedCountryCode();
                if (ApiConfig.CheckValidattion(mobile, false, false)) {
                    edtMobileVerify.requestFocus();
                    edtMobileVerify.setError(getString(R.string.enter_mobile_no));
                } else if (ApiConfig.CheckValidattion(mobile, false, true)) {
                    edtMobileVerify.requestFocus();
                    edtMobileVerify.setError(getString(R.string.enter_valid_mobile_no));
                } else if (ApiConfig.isConnected(activity)) {
                    generateOTP();
                }
            } else {
                String otptext = pinViewOTP.getText().toString().trim();
                if (ApiConfig.CheckValidattion(otptext, false, false)) {
                    pinViewOTP.requestFocus();
                    pinViewOTP.setError(getString(R.string.enter_otp));
                } else {
                    OTP_Varification(otptext);
                }
            }

        } else if (id == R.id.btnRegister) {
            String name = edtname.getText().toString().trim();
            String email = "" + edtemail.getText().toString().trim();
            final String password = edtpsw.getText().toString().trim();
            String cpassword = edtcpsw.getText().toString().trim();
            if (ApiConfig.CheckValidattion(name, false, false)) {
                edtname.requestFocus();
                edtname.setError(getString(R.string.enter_name));
            } else if (ApiConfig.CheckValidattion(email, false, false)) {
                edtemail.requestFocus();
                edtemail.setError(getString(R.string.enter_email));
            } else if (ApiConfig.CheckValidattion(email, true, false)) {
                edtemail.requestFocus();
                edtemail.setError(getString(R.string.enter_valid_email));
            } else if (ApiConfig.CheckValidattion(password, false, false)) {
                edtcpsw.requestFocus();
                edtpsw.setError(getString(R.string.enter_pass));
            } else if (ApiConfig.CheckValidattion(cpassword, false, false)) {
                edtcpsw.requestFocus();
                edtcpsw.setError(getString(R.string.enter_confirm_pass));
            } else if (!password.equals(cpassword)) {
                edtcpsw.requestFocus();
                edtcpsw.setError(getString(R.string.pass_not_match));
            } /*else if (!chPrivacy.isChecked()) {
                Toast.makeText(activity, getString(R.string.alert_privacy_msg), Toast.LENGTH_LONG).show();
            }*/ else if (ApiConfig.isConnected(activity)) {
                UserSignUpSubmit(name, email, password);
            }
        } else if (id == R.id.tvResend) {
            resendOTP = true;
            sentRequest(mobile);

        } else if (id == R.id.imgVerifyClose) {
            lytOTP.setVisibility(View.GONE);
            lytVerify.setVisibility(View.GONE);
            lytVerify.startAnimation(animHide);
            edtMobileVerify.setText("");
            edtMobileVerify.setEnabled(true);
            edtCountryCodePicker.setCcpClickable(forMultipleCountryUse);
            pinViewOTP.setText("");
        } else if (id == R.id.imgResetPasswordClose) {
            edtResetPass.setText("");
            edtResetCPass.setText("");
            lytResetPass.setVisibility(View.GONE);
            lytResetPass.startAnimation(animHide);
        } else if (id == R.id.imgSignUpClose) {
            lytSignUp.setVisibility(View.GONE);
            lytSignUp.startAnimation(animHide);
            tvMobile.setText("");
            edtname.setText("");
            edtemail.setText("");
            edtpsw.setText("");
            edtcpsw.setText("");
            edtRefer.setText("");
        } else if (id == R.id.imgWebViewClose) {
            lytWebView.setVisibility(View.GONE);
            lytWebView.startAnimation(animHide);
        }

    }

    public void StartMainActivity(JSONObject objectbject, String password) {
        try {
            new Session(activity).createUserLoginSession(objectbject.getString(Constant.PROFILE)
                    , session.getData(Constant.FCM_ID),
                    objectbject.getString(Constant.USER_ID),
                    objectbject.getString(Constant.NAME),
                    objectbject.getString(Constant.EMAIL),
                    objectbject.getString(Constant.MOBILE),
                    password,
                    objectbject.getString(Constant.REFERRAL_CODE));

            ApiConfig.AddMultipleProductInCart(session, activity, databaseHelper.getDataCartList());
            ApiConfig.getCartItemCount(activity, session);

            ArrayList<String> favorites = databaseHelper.getFavourite();
            for (int i = 0; i < favorites.size(); i++) {
                ApiConfig.AddOrRemoveFavorite(activity, session, favorites.get(i), true);
            }

            databaseHelper.DeleteAllFavoriteData();
            databaseHelper.DeleteAllOrderData();

            ApiConfig.getWalletBalance(activity, session);
            session.setData(Constant.COUNTRY_CODE, objectbject.getString(Constant.COUNTRY_CODE));

            MainActivity.homeClicked = false;
            MainActivity.categoryClicked = false;
            MainActivity.favoriteClicked = false;
            MainActivity.trackingClicked = false;

            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.FROM, "");
            if (from != null && from.equals("checkout")) {
                intent.putExtra("total", ApiConfig.StringFormat("" + Constant.FLOAT_TOTAL_AMOUNT));
                intent.putExtra(Constant.FROM, "checkout");
            } else if (from != null && from.equals("tracker")) {
                intent.putExtra(Constant.FROM, "tracker");
            }
            startActivity(intent);

            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public SpannableString underlineSpannable(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        return spannableString;
    }

    public void GetContent(final String type, final String key) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.SETTINGS, Constant.GetVal);
        params.put(type, Constant.GetVal);

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                if (result) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean(Constant.ERROR)) {

                            String privacyStr = obj.getString(key);
                            webView.setVerticalScrollBarEnabled(true);
                            webView.loadDataWithBaseURL("", privacyStr, "text/html", "UTF-8", "");
                        } else {
                            Toast.makeText(getContext(), obj.getString(Constant.MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                    }
                }
            }
        }, activity, Constant.SETTING_URL, params, false);
    }

    public void PrivacyPolicy() {
        tvPrivacyPolicy.setClickable(true);
        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        String message = getString(R.string.msg_privacy_terms);
        String s2 = getString(R.string.terms_conditions);
        String s1 = getString(R.string.privacy_policy);
        final Spannable wordtoSpan = new SpannableString(message);

        wordtoSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                GetContent(Constant.GET_PRIVACY, "privacy");
                try {
                    Thread.sleep(500);
                    lytWebView.setVisibility(View.VISIBLE);
                    lytWebView.startAnimation(animShow);
                } catch (Exception ignored) {

                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                ds.isUnderlineText();
            }
        }, message.indexOf(s1), message.indexOf(s1) + s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                GetContent(Constant.GET_TERMS, "terms");
                try {
                    Thread.sleep(500);
                    lytWebView.setVisibility(View.VISIBLE);
                    lytWebView.startAnimation(animShow);
                } catch (Exception ignored) {

                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                ds.isUnderlineText();
            }
        }, message.indexOf(s2), message.indexOf(s2) + s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrivacyPolicy.setText(wordtoSpan);
    }

    public void hideKeyboard(Activity activity, View root) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(root.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
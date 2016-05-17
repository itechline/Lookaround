package lar.com.lookaround;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.view.LayoutInflater;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.restapi.SoapService;
import lar.com.lookaround.util.LoginUtil;
import lar.com.lookaround.util.SettingUtil;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    ViewFlipper viewFlip, viewFlipFirstSettings;
    private int mCurrentLayoutState, mCurrentLayoutStateFirstSettings;

    private static final int REGISTRATION = 1;
    private static final int LOGIN = 0;
    private static final int FIRSTSETTINGS = 2;

    private EditText emailEditText;
    private EditText passEditText;
    private EditText firstnameEditText;
    private EditText lastnameEditText;

    private EditText emailCompanyEditText;
    private EditText passCompanyEditText;
    private EditText companyNameEditText;
    boolean isCompany = false;

    private EditText forgottenMail;

    //private RadioGroup radioGroup;
    //private RadioButton individual, estateagency;
    LayoutInflater inflater;

    CallbackManager callbackManager;
    List<String> permissionNeeds= Arrays.asList("user_photos", "email", "user_friends");
    String email_FB;
    String first_name_FB;
    String last_name_FB;
    String user_fb_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View login, regist, firstsettings, firstsettings_page_one, firstsettings_page_two, firstsettings_page_three;
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        login = inflater.inflate(R.layout.content_login, null);
        regist = inflater.inflate(R.layout.content_registration, null);
        firstsettings = inflater.inflate(R.layout.content_first_settings, null);
        firstsettings_page_one = inflater.inflate(R.layout.content_first_setting_page_1, null);
        firstsettings_page_two = inflater.inflate(R.layout.content_first_setting_page_2, null);
        firstsettings_page_three = inflater.inflate(R.layout.content_first_setting_page_3, null);


        viewFlip = (ViewFlipper) findViewById(R.id.mainViewFlipper);
        viewFlip.addView(login, LOGIN);
        viewFlip.addView(regist, REGISTRATION);
        viewFlip.addView(firstsettings, FIRSTSETTINGS);

        viewFlipFirstSettings = (ViewFlipper) findViewById(R.id.viewFlipperFirstSettings);
        viewFlipFirstSettings.addView(firstsettings_page_one, 0);
        viewFlipFirstSettings.addView(firstsettings_page_two, 1);
        viewFlipFirstSettings.addView(firstsettings_page_three, 2);
    }

    int whichPage = 0;

    public void nextPageFirstSetting(View view) {
        if (whichPage < 3) {
            whichPage += 1;
            switchLayoutToFirstSettings(whichPage);
        }

    }

    public void prewPageFirstSetting(View view) {
        if (whichPage > 0) {
            whichPage -= 1;
            switchLayoutToFirstSettings(whichPage);
        }

    }

    public void pageSetter() {

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected String generateRandomPass() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public void fb_login(final View view) {
        launchRingDialog(view);
        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        try {
                                            user_fb_id = object.getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        try {
                                            URL image_value = new URL("http://graph.facebook.com/"+ user_fb_id+ "/picture?type=large");
                                            Bitmap bmp = null;
                                            try {
                                                bmp = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            //profile_pic.setImageBitmap(bmp);
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }

                                        // Application code
                                        try {
                                            email_FB = object.getString("email");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            first_name_FB = object.getString("first_name");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            last_name_FB = object.getString("last_name");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        final String passw_fb = generateRandomPass();
                                        launchRingDialog(view);
                                        LoginUtil.sendRegistration(getBaseContext(), new SoapObjectResult() {
                                            @Override
                                            public void parseRerult(Object result) {
                                                if ((boolean) result) {
                                                    ringProgressDialog.dismiss();
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    ringProgressDialog.dismiss();
                                                    showAlert(view, "Hiba történt!");
                                                }

                                            }
                                        }, first_name_FB, last_name_FB, email_FB, passw_fb, "maganyszemely");


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email, location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        showAlert(view, "Hiba Történt!");
                    }
                });
    }



    public void openBlankPage(View view) {
        startActivity(new Intent(LoginActivity.this, BlankPageActivity.class));
    }

    /*public void loadRegistration() {
        //viewFlip.setDisplayedChild(REGISTRATION);

        radioGroup = (RadioGroup) findViewById(R.id.indiOrEstateGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton2) {
                    Toast.makeText(getApplicationContext(), "choice: Silent",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton) {
                    Toast.makeText(getApplicationContext(), "choice: Sound",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAlert(View view, String message) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("HIBA")
                .create();
        myAlert.show();
    }

    public void showLogin(View view) {
        switchLayoutTo(LOGIN);
    }

    public void sendRegistration(final View view) {
        boolean isAbleToJoin = true;
        if (!isCompany) {
            emailEditText = (EditText) findViewById(R.id.mail);
            passEditText = (EditText) findViewById(R.id.passw);
            firstnameEditText = (EditText) findViewById(R.id.keresztNev);
            lastnameEditText = (EditText) findViewById(R.id.vezetekNev);

            String firstName = firstnameEditText.getText().toString();
            String lastName = lastnameEditText.getText().toString();
            final String passw = passEditText.getText().toString();
            final String email = emailEditText.getText().toString();

            if (!isValidName(firstName)) {
                firstnameEditText.setError("Hibás név!");
                firstnameEditText.invalidate();
                isAbleToJoin=false;
            }

            if (!isValidName(lastName)) {
                lastnameEditText.setError(("Hibás név!"));
                lastnameEditText.invalidate();
                isAbleToJoin=false;
            }

            if (!isValidPassword(passw)) {
                passEditText.setError("Hibás jelszó!");
                passEditText.invalidate();
                isAbleToJoin=false;
            }

            if (!isValidEmail(email)) {
                emailEditText.setError("Hibás Email");
                emailEditText.invalidate();
                isAbleToJoin=false;
            }

            if (isAbleToJoin) {
                launchRingDialog(view);
                LoginUtil.sendRegistration(this, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        if ((boolean) result) {
                            //launchRingDialog(view);
                            //sendLoginAfterReg(view, email, passw);
                            ringProgressDialog.dismiss();
                            switchLayoutTo(FIRSTSETTINGS);
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //finish();
                        } else {
                            ringProgressDialog.dismiss();
                            showAlert(view, "Hibás Adatok!");
                        }

                    }
                }, firstName, lastName, email, passw, "maganyszemely");
            }

            //SettingUtil.setToken(this, firstName);

        } else {
            emailCompanyEditText = (EditText) findViewById(R.id.mail2);
            passCompanyEditText = (EditText) findViewById(R.id.passw2);
            companyNameEditText = (EditText) findViewById(R.id.entry_company);

            final String passwCompany = passCompanyEditText.getText().toString();
            final String emailCompany = emailCompanyEditText.getText().toString();
            String companyName = companyNameEditText.getText().toString();

            if(!isValidName(companyName)) {
                companyNameEditText.setError("Hibás Cégnév!");
                companyNameEditText.invalidate();
                isAbleToJoin=false;
            }

            if (!isValidPassword(passwCompany)) {
                passCompanyEditText.setError("Hibás jelszó!");
                passCompanyEditText.invalidate();
                isAbleToJoin=false;
            }

            if (!isValidEmail(emailCompany)) {
                emailCompanyEditText.setError("Hibás Email");
                emailCompanyEditText.invalidate();
                isAbleToJoin=false;
            }

            if (isAbleToJoin) {
                launchRingDialog(view);
                LoginUtil.sendRegistration(this, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        if ((boolean) result) {
                            //launchRingDialog(view);
                            ringProgressDialog.dismiss();
                            switchLayoutTo(FIRSTSETTINGS);
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //finish();
                        } else {
                            ringProgressDialog.dismiss();
                            showAlert(view, "Hibás Adatok!");

                        }

                    }
                }, companyName, " ", emailCompany, passwCompany, "ingatlanos");
            }

        }
    }



    public void sendLogin(final View view) {
        emailEditText = (EditText) findViewById(R.id.mailLogin);
        passEditText = (EditText) findViewById(R.id.passwLogin);
        boolean isAbleToJoin = true;

        final String passw = passEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (!isValidPassword(passw)) {
            passEditText.setError("Hibás jelszó!");
            passEditText.invalidate();
            isAbleToJoin = false;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Hibás Email");
            emailEditText.invalidate();
            isAbleToJoin = false;
        }

        if(isAbleToJoin) {
            //launchRingDialog(view);
            LoginUtil.login(this, new SoapObjectResult() {
                @Override
                public void parseRerult(Object result) {
                    if ((boolean) result) {
                        //ringProgressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        //ringProgressDialog.dismiss();
                        showAlert(view, "Hibás felhasználónév vagy jelszó!");
                    }

                }
            }, email, passw);
        }
    }

    ProgressDialog ringProgressDialog;
    public void launchRingDialog(View view) {
        ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...", "Logging In ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(10000);
                } catch (Exception e) {

                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }

    public void setIndividual(View view) {
        isCompany = false;
        RelativeLayout layone = (RelativeLayout) findViewById(R.id.relative_individual);
        layone.setVisibility(View.VISIBLE);
        RelativeLayout laytwo = (RelativeLayout) findViewById(R.id.relative_agency);
        laytwo.setVisibility(View.INVISIBLE);
    }

    public void setEstateagency(View view) {
        isCompany = true;
        RelativeLayout layone= (RelativeLayout) findViewById(R.id.relative_individual);
        layone.setVisibility(View.INVISIBLE);
        RelativeLayout laytwo= (RelativeLayout) findViewById(R.id.relative_agency);
        laytwo.setVisibility(View.VISIBLE);
    }

    public void showForgottenPass(View view) {
        RelativeLayout layone= (RelativeLayout) findViewById(R.id.Layout_Login);
        layone.setVisibility(View.INVISIBLE);
        RelativeLayout laytwo= (RelativeLayout) findViewById(R.id.Forgotten_Pass_Layout);
        laytwo.setVisibility(View.VISIBLE);
        LinearLayout layFooter = (LinearLayout) findViewById(R.id.login_linear_footer);
        layFooter.setVisibility(View.INVISIBLE);
    }

    public void sendForgottenPass(View view) {
        forgottenMail = (EditText) findViewById(R.id.forgottenMailLogin);

        String forgottenMailString = forgottenMail.getText().toString();

        if (!isValidEmail(forgottenMailString)) {
            forgottenMail.setError("Hibás Email");
            forgottenMail.invalidate();
        } else {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Új jelszavát elküldtük email címére!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RelativeLayout layone = (RelativeLayout) findViewById(R.id.Layout_Login);
                            layone.setVisibility(View.VISIBLE);
                            RelativeLayout laytwo = (RelativeLayout) findViewById(R.id.Forgotten_Pass_Layout);
                            laytwo.setVisibility(View.INVISIBLE);
                            LinearLayout layFooter = (LinearLayout) findViewById(R.id.login_linear_footer);
                            layFooter.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    })
                    .create();
            myAlert.show();
        }
    }

    public void showRegistration(View view) {
        switchLayoutTo(REGISTRATION);
    }

    @Override
    public void onBackPressed() {
        if (viewFlip.getDisplayedChild() == LOGIN) {
            switchLayoutTo(REGISTRATION);
        }
        if (viewFlip.getDisplayedChild() == REGISTRATION) {
            switchLayoutTo(LOGIN);
            RelativeLayout layone= (RelativeLayout) findViewById(R.id.Layout_Login);
            layone.setVisibility(View.VISIBLE);
            RelativeLayout laytwo= (RelativeLayout) findViewById(R.id.Forgotten_Pass_Layout);
            laytwo.setVisibility(View.INVISIBLE);
            LinearLayout layFooter = (LinearLayout) findViewById(R.id.login_linear_footer);
            layFooter.setVisibility(View.VISIBLE);
        }
    }


    private  boolean isValidName(String name) {
        if(name.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }

    public void switchLayoutTo(int switchTo){
        while(mCurrentLayoutState != switchTo){
            if(mCurrentLayoutState > switchTo){
                mCurrentLayoutState--;
                viewFlip.setInAnimation(inFromLeftAnimation());
                viewFlip.setOutAnimation(outToRightAnimation());
                viewFlip.setDisplayedChild(switchTo);
            } else {
                mCurrentLayoutState++;
                viewFlip.setInAnimation(inFromRightAnimation());
                viewFlip.setOutAnimation(outToLeftAnimation());
                viewFlip.setDisplayedChild(switchTo);
            }
        };
    }

    public void switchLayoutToFirstSettings(int switchTo){
        while(mCurrentLayoutStateFirstSettings != switchTo){
            if(mCurrentLayoutStateFirstSettings > switchTo){
                mCurrentLayoutStateFirstSettings--;
                viewFlipFirstSettings.setInAnimation(inFromLeftAnimation());
                viewFlipFirstSettings.setOutAnimation(outToRightAnimation());
                viewFlipFirstSettings.setDisplayedChild(switchTo);
            } else {
                mCurrentLayoutStateFirstSettings++;
                viewFlipFirstSettings.setInAnimation(inFromRightAnimation());
                viewFlipFirstSettings.setOutAnimation(outToLeftAnimation());
                viewFlipFirstSettings.setDisplayedChild(switchTo);
            }
        };
    }

    protected Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(300);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    protected Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(300);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    protected Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(300);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    protected Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(300);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}

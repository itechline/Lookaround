package lar.com.lookaround;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lar.com.lookaround.restapi.ProfileImageUploadService;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.ImageUtil;
import lar.com.lookaround.util.LoginUtil;
import lar.com.lookaround.util.ScalingUtilities;
import lar.com.lookaround.util.SettingUtil;

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

    Bitmap fb_profile_pic = null;


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
    private double lat = 0d;
    private double lng = 0d;

    public void nextPageFirstSetting(final View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (whichPage < 3) {
            whichPage += 1;

            EditText phone = (EditText) findViewById(R.id.first_setting_phone_edittext);
            EditText varos = (EditText) findViewById(R.id.first_setting_city_edittext);
            switch (whichPage) {
                case 1:
                    String varosString = varos.getText().toString();

                    if(!isValidName(varosString)) {
                        varos.setError("Hiba!");
                        varos.invalidate();
                        whichPage = 0;
                    } else {
                        Geocoder gc = new Geocoder(this);

                        List<Address> list = null;
                        try {
                            list = gc.getFromLocationName(varosString, 1);

                            if (!list.isEmpty()) {
                                Address add = list.get(0);
                                lat = add.getLatitude();
                                lng = add.getLongitude();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        switchLayoutToFirstSettings(whichPage);
                        pageIndicatorSetter(whichPage);
                    }
                    break;
                case 2:
                    String phoneString = phone.getText().toString();

                    if(!isValidName(phoneString)) {
                        phone.setError("Hiba!");
                        phone.invalidate();
                        whichPage = 1;
                    } else {
                        switchLayoutToFirstSettings(whichPage);
                        pageIndicatorSetter(whichPage);
                    }
                    break;
                case 3:



                    //TODO: fb_profile_pic feltöltése ha facebook-al regisztrált
                    EstateUtil.updateReg(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            Log.d("RESULT", result.toString());
                            if ((boolean) result) {
                                Snackbar.make(view, "Hiba történt!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                if (uri != null) {
                                    uploadImages(SettingUtil.getToken(LoginActivity.this));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }

                        }
                    }, SettingUtil.getToken(this), String.valueOf(lat), String.valueOf(lng), phone.getText().toString());
                    break;
            }

        }

    }

    public void prewPageFirstSetting(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (whichPage > 0) {
            whichPage -= 1;
            switchLayoutToFirstSettings(whichPage);
            pageIndicatorSetter(whichPage);
        }

    }

    public void pageIndicatorSetter(int page) {
        ImageView layoneIndicator = (ImageView) findViewById(R.id.progress_1);
        ImageView laytwoIndicator = (ImageView) findViewById(R.id.progress_2);
        ImageView laythreendicator = (ImageView) findViewById(R.id.progress_3);

        Resources res = getResources();
        Drawable kekpotty = res.getDrawable(R.drawable.kekpotty);
        Drawable szurkepotty = res.getDrawable(R.drawable.szurkepotty);

        layoneIndicator.setImageDrawable(szurkepotty);
        laytwoIndicator.setImageDrawable(szurkepotty);
        laythreendicator.setImageDrawable(szurkepotty);

        switch (page) {
            case 0:
                layoneIndicator.setImageDrawable(kekpotty);
                break;
            case 1:
                laytwoIndicator.setImageDrawable(kekpotty);
                break;
            case 2:
                laythreendicator.setImageDrawable(kekpotty);
                break;
        }
    }

    Uri uri;

    public void uploadImages(String token) {
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.show();


        File imageFile = null;
        if(ImageUtil.getPath(this, uri) != null) {
            imageFile = new File(ImageUtil.getPath(this, uri));
        }

        try {
            ProfileImageUploadService service = new ProfileImageUploadService(imageFile, new SoapObjectResult() {
                @Override
                public void parseRerult(Object result) {
                    if(pd != null) {
                        pd.dismiss();
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            });
            service.execute(token);
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private int PICK_IMAGE_REQUEST = 2;
    private int TAKE_IMAGE_REQUEST = 1;

    public void pickImageFS(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void TakeImageFS(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        TAKE_IMAGE_REQUEST);

            }
        } else {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView image = (ImageView) findViewById(R.id.profile_photo_gallery);
                image.setImageBitmap(ScalingUtilities.createScaledBitmap(bitmap, 200, 200, ScalingUtilities.ScalingLogic.CROP));

                ImageView cam = (ImageView) findViewById(R.id.profile_photo_cam);
                cam.setImageDrawable(getResources().getDrawable(R.drawable.fenykepezo));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uri = data.getData();
            Bitmap bitmapCam = (Bitmap) data.getExtras().get("data");

            ImageView cam = (ImageView) findViewById(R.id.profile_photo_cam);
            cam.setImageBitmap(ScalingUtilities.createScaledBitmap(bitmapCam, 200, 200, ScalingUtilities.ScalingLogic.CROP));

            ImageView image = (ImageView) findViewById(R.id.profile_photo_gallery);
            image.setImageDrawable(getResources().getDrawable(R.drawable.feltoltes));

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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
                                            try {
                                                fb_profile_pic = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
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
                                                    switchLayoutTo(FIRSTSETTINGS);
                                                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    //finish();
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
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        switchLayoutTo(LOGIN);
    }

    public void sendRegistration(final View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                        Log.d("REGISTRATION: ", result.toString());
                        if ((boolean) result) {
                            //launchRingDialog(view);
                            //sendLoginAfterReg(view, email, passw);
                            ringProgressDialog.dismiss();
                            switchLayoutTo(FIRSTSETTINGS);
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //finish();
                        } else {
                            ringProgressDialog.dismiss();
                            Log.d("REGISTRATION: HIBAS ", "ADATOK");
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
                            Log.d("REGISTRATION_COMPANY: ", result.toString());
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
            this.finishAffinity();
        } else if (viewFlip.getDisplayedChild() == REGISTRATION) {
            switchLayoutTo(LOGIN);
            RelativeLayout layone= (RelativeLayout) findViewById(R.id.Layout_Login);
            layone.setVisibility(View.VISIBLE);
            RelativeLayout laytwo= (RelativeLayout) findViewById(R.id.Forgotten_Pass_Layout);
            laytwo.setVisibility(View.INVISIBLE);
            LinearLayout layFooter = (LinearLayout) findViewById(R.id.login_linear_footer);
            layFooter.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
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

package lar.com.lookaround;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.view.LayoutInflater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.restapi.SoapService;

public class LoginActivity extends AppCompatActivity {

    ViewFlipper viewFlip;
    private int mCurrentLayoutState;

    private static final int REGISTRATION = 0;
    private static final int LOGIN = 1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String url = "http://lookrnd.me/dev/api/do_login";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("email", "nagy.roland@nye.hu");
            postadatok.put("pass", "narol");
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("TESTADATOK", "Return: " + result);
                }
            }, postadatok);

            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View login, regist;
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        login = inflater.inflate(R.layout.content_login, null);
        regist = inflater.inflate(R.layout.content_registration, null);

        viewFlip = (ViewFlipper) findViewById(R.id.mainViewFlipper);
        viewFlip.addView(regist, REGISTRATION);
        viewFlip.addView(login, LOGIN);
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

    public void showAlert(View view) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("Rossz felhasználónév vagy jelszó!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText abc = (EditText)findViewById(R.id.keresztNev);
                        abc.getText().toString();
                        Log.d("DEBUG: ", abc.getText().toString());
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

    public void sendRegistration(View view) {
        if (!isCompany) {
            emailEditText = (EditText) findViewById(R.id.mail);
            passEditText = (EditText) findViewById(R.id.passw);
            firstnameEditText = (EditText) findViewById(R.id.keresztNev);
            lastnameEditText = (EditText) findViewById(R.id.vezetekNev);

            String firstName = firstnameEditText.getText().toString();
            String lastName = lastnameEditText.getText().toString();
            String passw = passEditText.getText().toString();
            String email = emailEditText.getText().toString();

            if (!isValidName(firstName)) {
                firstnameEditText.setError("Hibás név!");
                firstnameEditText.invalidate();
            }

            if (!isValidName(lastName)) {
                lastnameEditText.setError(("Hibás név!"));
                lastnameEditText.invalidate();
            }

            if (!isValidPassword(passw)) {
                passEditText.setError("Hibás jelszó!");
                passEditText.invalidate();
            }

            if (!isValidEmail(email)) {
                emailEditText.setError("Hibás Email");
                emailEditText.invalidate();
            }
        } else {
            emailCompanyEditText = (EditText) findViewById(R.id.mail2);
            passCompanyEditText = (EditText) findViewById(R.id.passw2);
            companyNameEditText = (EditText) findViewById(R.id.entry_company);

            String passwCompany = passCompanyEditText.getText().toString();
            String emailCompany = emailCompanyEditText.getText().toString();
            String companyName = companyNameEditText.getText().toString();

            if(!isValidName(companyName)) {
                companyNameEditText.setError("Hibás Cégnév!");
                companyNameEditText.invalidate();
            }

            if (!isValidPassword(passwCompany)) {
                passCompanyEditText.setError("Hibás jelszó!");
                passCompanyEditText.invalidate();
            }

            if (!isValidEmail(emailCompany)) {
                emailCompanyEditText.setError("Hibás Email");
                emailCompanyEditText.invalidate();
            }
        }
    }





    public void sendLogin(View view) {
        emailEditText = (EditText) findViewById(R.id.mailLogin);
        passEditText = (EditText) findViewById(R.id.passwLogin);
        boolean isAbleToJoin = true;

        String passw = passEditText.getText().toString();
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
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
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
        switchLayoutTo(REGISTRATION);
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
        if (pass != null && pass.length() >= 6) {
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

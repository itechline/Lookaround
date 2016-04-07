package lar.com.lookaround;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.LayoutInflater;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ViewFlipper viewFlip;
    private int mCurrentLayoutState;

    private static final int REGISTRATION = 0;
    private static final int LOGIN = 1;

    private EditText emailEditText;
    private EditText passEditText;
    private EditText firstnameEditText;
    private EditText lastnameEditText;

    //private RadioGroup radioGroup;
    //private RadioButton individual, estateagency;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //showAlert(view);
            }
        });



        View login, regist;
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        login = inflater.inflate(R.layout.content_login, null);
        regist = inflater.inflate(R.layout.content_registration, null);

        viewFlip = (ViewFlipper) findViewById(R.id.mainViewFlipper);
        viewFlip.addView(regist, REGISTRATION);
        viewFlip.addView(login, LOGIN);

        RelativeLayout layone = (RelativeLayout) findViewById(R.id.relative_individual);
        layone.setVisibility(View.VISIBLE);
        RelativeLayout laytwo = (RelativeLayout) findViewById(R.id.relative_agency);
        laytwo.setVisibility(View.INVISIBLE);
    }

    public void openBlankPage(View view) {
        startActivity(new Intent(MainActivity.this, BlankPageActivity.class));
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
        emailEditText = (EditText) findViewById(R.id.mail);
        passEditText = (EditText) findViewById(R.id.passw);
        firstnameEditText = (EditText) findViewById(R.id.keresztNev);
        lastnameEditText = (EditText) findViewById(R.id.vezetekNev);

        String firstName = firstnameEditText.getText().toString();
        String lastName = lastnameEditText.getText().toString();
        String passw = passEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if(!isValidFirstName(firstName)) {
            firstnameEditText.setError("Hibás név!");
            firstnameEditText.invalidate();
        }

        if(!isValidLastName(lastName)) {
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
    }



    public void sendLogin(View view) {
        emailEditText = (EditText) findViewById(R.id.mailLogin);
        passEditText = (EditText) findViewById(R.id.passwLogin);
        boolean isAbleToJoin = true;

        String passw = passEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (!isValidPassword(passw)) {
            passEditText.setError("Hibás jelszó!");
            isAbleToJoin = false;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Hibás Email");
            isAbleToJoin = false;
        }

        if(!isAbleToJoin) {
            showAlert(view);
        }
    }

    public void setIndividual(View view) {
        RelativeLayout layone = (RelativeLayout) findViewById(R.id.relative_individual);
        layone.setVisibility(View.VISIBLE);
        RelativeLayout laytwo = (RelativeLayout) findViewById(R.id.relative_agency);
        laytwo.setVisibility(View.INVISIBLE);
    }

    public void setEstateagency(View view) {
        //view = inflater.inflate(R.layout.content_registration, container, false);
        RelativeLayout layone= (RelativeLayout) findViewById(R.id.relative_individual);
        layone.setVisibility(View.INVISIBLE);
        RelativeLayout laytwo= (RelativeLayout) findViewById(R.id.relative_agency);
        laytwo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        // your code.
        switchLayoutTo(REGISTRATION);
    }

    private  boolean isValidFirstName(String name) {
        if(name.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidLastName(String name){
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

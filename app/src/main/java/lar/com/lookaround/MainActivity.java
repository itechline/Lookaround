package lar.com.lookaround;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ViewFlipper;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity {

    ViewFlipper viewFlip;
    private int mCurrentLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //showAlert(view);
            }
        });
    }

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
                        EditText abc = (EditText)findViewById(R.id.username);
                        abc.getText().toString();
                        Log.d("DEBUG: ", abc.getText().toString());
                       // dialog.dismiss();
                    }
                })
                /*.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })*/

                .setTitle("HIBA")
                .create();
        myAlert.show();
    }

    public void showLogin(View view) {
        //setContentView(R.layout.content_login);
        //viewFlip.addView();

    }

    /**
     * Switches the layout to the given constant ID as a parameter
     * @param switchTo (should be 0 or a positive number)
     */
    public void switchLayoutStateTo(int switchTo){
        while(mCurrentLayoutState != switchTo){
            if(mCurrentLayoutState > switchTo){
                mCurrentLayoutState--;
                //viewFlip.setInAnimation(inFromLeftAnimation());
                //viewFlip.setOutAnimation(outToRightAnimation());
                viewFlip.showPrevious();
            } else {
                mCurrentLayoutState++;
                //viewFlip.setInAnimation(inFromRightAnimation());
               // viewFlip.setOutAnimation(outToLeftAnimation());
                viewFlip.showNext();
            }

        };
    }


}

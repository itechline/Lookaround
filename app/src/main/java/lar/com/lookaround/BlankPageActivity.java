package lar.com.lookaround;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class BlankPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView felh_szoveg = (TextView)findViewById(R.id.felh_szoveg);

//in your OnCreate() method
        felh_szoveg.setText(Html.fromHtml("<h2>Általános Felhasználási Feltételek</h2>" +
                "<br><br><br>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec dictum sit amet urna vulputate sagittis. Nulla vulputate lacus ac velit congue tincidunt. Fusce viverra mi nec sodales convallis. Integer porta elit ipsum, eget consequat mi suscipit in. In varius velit et est suscipit commodo. Sed vitae malesuada nisl. Cras vestibulum consectetur tortor, quis rutrum urna iaculis gravida. Proin rhoncus lectus aliquet, luctus massa vel, fermentum ipsum. Proin vitae magna a justo viverra egestas in sed ex. Donec pretium elit arcu, et cursus ligula lacinia in. Sed fermentum facilisis magna, eget viverra nisi maximus sit amet." +
                "<br><br>Integer quis massa non mi elementum posuere. Sed vestibulum enim nec bibendum sodales. Aenean pretium eleifend orci, ut laoreet diam sodales ac. Maecenas eu suscipit ante. Pellentesque dignissim tincidunt dolor at gravida. Cras sit amet elementum urna, et gravida leo. Nunc eleifend vel mauris non semper. Mauris in urna sed elit aliquet lacinia at et nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean maximus risus leo, non consectetur nisi mollis non. Suspendisse fringilla ipsum ac tempor mollis." +
                "<br><br>Nulla vitae interdum velit, non lobortis leo. Proin faucibus faucibus lectus, eu elementum metus congue id. Nulla sit amet egestas augue. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus egestas purus ut odio commodo efficitur. Ut lobortis sapien eget dui rhoncus, a dictum orci convallis. In bibendum felis faucibus turpis mollis, a imperdiet justo ullamcorper. Maecenas sit amet dui nisi. Proin quis justo quis elit convallis congue eget et elit. Nullam risus diam, sagittis aliquet lacus vitae, faucibus molestie enim. Sed accumsan sapien et nunc porttitor commodo. Aliquam in nibh at mi placerat cursus. Proin eget volutpat nulla. Suspendisse ut porta lorem. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae"));
    }

}

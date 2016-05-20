package lar.com.lookaround;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class BlankPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView felh_szoveg = (TextView)findViewById(R.id.felh_szoveg);

        felh_szoveg.setText(Html.fromHtml("<h2>A Bonodom alkalmazás Szolgáltatás Általános Szerződési Feltételei</h2>" +
                "<br><br><br>A következőkben ismertetett Általános Szerződési Feltételek (a továbbiakban: „ÁSZF”) az ITECHLINE Kft. (5055, Jászladány Táncsics M 54/a.; a továbbiakban: „Szolgáltató”) által üzemeltetett, BONODOM applikációban elérhető ingatlan hirdetési szolgáltatás igénybevételéhez kapcsolódóan a Felhasználók és a Szolgáltató (lásd. lentebb) jogait és kötelezettségeit tartalmazza." +
                "<br><br><h3>1. Általános meghatározások</h3>\n" +
                "ÁSZF vagy a Szolgáltatás ÁSZF-je: a jelen dokumentum\n" +
                "Felhasználó: az alkalmazásban elérhető szolgáltatást igénybe vevő személy, aki az alkalmazásba regisztrált és oda feltölti az értékesíteni kívánt ingatlan adatait.\n" +
                "Alkalmazás: a Google Play áruházban elérhető alkalmazás\n" +
                "Szoftver: a Szolgáltatást üzemeltető szoftver.\n" +
                "Szolgáltató: az ITECHLINE Kft. (5055, Jászladány Táncsics M 54/a)\n" +
                "Szolgáltatás: a Szolgáltató által az alkalmazáson keresztül a Felhasználó részére nyújtott, és a jelen \n" +
                "ÁSZF-ben ismertetett szolgáltatás." +
                "<br><br><h3>2.  A Szolgáltató által nyújtott Szolgáltatás leírása</h3>\n" +
                "<h4>2.1</h4> A Szolgáltatás keretében a Szolgáltató lehetőséget biztosít a Felhasználó számára, hogy\n" +
                "a,  az alkalmazás felületére a Felhasználó által értékesíteni kívánt ingatlan adatait, fényképeit\n" +
                "feltöltse,\n" +
                "b,  az alkalmazásban megadja azokat a paramétereket, amelyeknek megfelelő ingatlant keres;\n" +
                "továbbá\n" +
                "c,  a Felhasználó igénye alapján a Felhasználó részére hirdetésfigyelő szolgáltatást nyújt. A\n" +
                "hirdetésfigyelési szolgáltatás keretében a Szolgáltató a Felhasználó által megadott e-mail\n" +
                "címre megküldi az alkalmazásba feltöltött mindazon ingatlanok linkjét, amelyek a Felhasználó\n" +
                "által az alkalmazásban megadott keresési feltételeknek megfelel.\n" +
                "<h4>2.2</h4> Felhasználó az alkalmazásba történő regisztrációjával a Szolgáltatás ÁSZF-jét kifejezetten elfogadja.\n" +
                "<br><br><h3>3. A Szolgáltató által nyújtott Szolgáltatás igénybevétele</h3>\n" +
                "<h4>3.1</h4> Felhasználó a Szolgáltatásba saját e-mail címével, Facebook ill. Google accountjával tud regisztrálni. Felhasználó tudomásul veszi, hogy regisztrációja elküldésével mind a Szolgáltatás ÁSZF-jét és Adatkezelési Szabályzatát elfogadja.\n" +
                "<h4>3.2</h4> Felhasználó ingyenesen legfeljebb 1 ingatlanra vonatkozó hirdetést tölthet fel egyszerre az alkalmazásba, és teheti elérhetővé az alkalmazás használói számára. Amennyiben kiderül, hogy egy Felhasználó – akár több felhasználó névvel vagy e-mail címmel – az itt megadottnál több ingatlan adatait is feltöltötte az alkalmazásba, ill. ott elérhetővé tette, úgy a Szolgáltatónak joga van Felhasználó valamennyi hirdetését törölni.\n" +
                "<h4>3.3</h4> Felhasználó az alkalmazásba feltöltheti az általa keresett ingatlan adatait. Ehhez kapcsolódóan a\n" +
                "Felhasználó az alkalmazásban hirdetésfigyelő szolgáltatás nyújtását rendelheti meg. \n" +
                "<h4>3.4</h4> A Felhasználó bármikor módosíthatja az általa az alkalmazásban illetve a regisztráció során megadott adatokat, a „Hirdetésem”, a személyes adatokat a „Személyes” menüpontban.\n" +
                "<h4>3.5</h4> Nem tölthetők fel hirdetéssel kapcsolatban az alkalmazásba olyan képek, amelyek:\n" +
                "a, nem a Felhasználó tulajdonát képezik, és felhasználásukra a jogosult felhatalmazást nem adott,\n" +
                "b, nem a meghirdetett ingatlant és annak közvetlen környezetét ábrázolják \n" +
                "c, nem megfelelő felbontásúak, minőségűek, torzítottak,\n" +
                "d, több fotó montázsából jött létre,\n" +
                "e, bármilyen a képre rászerkesztett feliratot, szöveget, linket, elérhetőségi adatot, figyelemfelkeltő grafikát, egyéb megjelölést tartalmaznak. \n" +
                "<h4>3.6</h4> A Felhasználó által feltöltött adatok, azoknak a Felhasználó által az alkalmazásban történt törléséig, de legfeljebb azok feltöltésétől számított 24 hónapig történik a tárolás.\n"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

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
                "<h4>3.6</h4> A Felhasználó által feltöltött adatok, azoknak a Felhasználó által az alkalmazásban történt törléséig, de legfeljebb azok feltöltésétől számított 24 hónapig történik a tárolás.\n" +
                "<h3>4. Felelősségi szabályok</h3>\n" +
                "<h4>4.1</h4> A Felhasználó hirdetéseinek ill. a Felhasználó által az alkalmazásba feltöltött adatok valóságtartalmáért és helytállóságáért kizárólag a Felhasználó felelős. A Szolgáltató nem felel harmadik személyeknek a Felhasználóval szemben érvényesíteni kívánt igényéért, ide értve a Felhasználó által feltöltött adatoktól való eltérést, illetve a feltöltött adatok valóságtartalmát is. A Felhasználó felelősségi és helytállási kötelezettségének a körébe esik, ha az alkalmazásban közzétett adatok és/vagy paraméterekhez képest később változások következnek be. Ilyen esetekben a felelősség kizárólag Felhasználót terheli. A beérkező esetleges reklamációkért (ideértve bármely bírságot, kártérítést és költséget) a felelősséget teljes egészében Felhasználó vállalja.\n" +
                "<h4>4.2</h4> Az adatszolgáltatás, a hibajavítás illetőleg az adatokban bekövetkező változásokkal\n" +
                "kapcsolatosan módosítások Felhasználó általi késedelmes teljesítése esetén az alkalmazásban\n" +
                "megjelenő hiányos vagy hibás adatok közzétételéért Szolgáltatót semmilyen felelősséget nem\n" +
                "vállal.\n" +
                "<h4>4.3</h4> A Szolgáltatót nem terheli semmilyen felelősség azért, hogy a jelen ÁSZF szerinti Szolgáltatása\n" +
                "révén létrejön-e tényleges szerződés Felhasználó és harmadik személyek között.\n" +
                "<h4>4.4</h4> A Szolgáltató nem felel a Felhasználó által harmadik személynek okozott károkért.\n" +
                "<h4>4.5</h4> A Felhasználó által az alkalmazásba feltöltött képek, videók, adatok (a továbbiakban: adatok)\n" +
                "Felhasználó tulajdonát képezik. Szolgáltató jogosult a Felhasználó által az alkalmazásba feltöltött adatokat marketing kommunikációi során felhasználni.\n" +
                "<h4>4.6</h4> Az alkalmazásba feltöltött adatok helyadat meghatározására a Szolgáltató külső partnert vesz igénybe. A Szolgáltató nem vállal felelősséget az így kapott adatok hibás vagy nem helytálló voltáért.\n" +
                "<h4>4.7</h4> Szolgáltató az alkalmazásra valamint a Szoftverre vonatkozó szerzői jogok jogosultja, és nincs\n" +
                "semmilyen harmadik személynek olyan joga, amely a Szolgáltatót vagy a Felhasználót ezek\n" +
                "megfelelő használatában korlátozhatná, vagy a használatot akadályozhatná.\n" +
                "<h4>4.8</h4> Felhasználó köteles tartózkodni minden olyan magatartástól, amely akár közvetlenül, akár\n" +
                "közvetve alkalmas arra, hogy az alkalmazásra illetve a Szoftverre, mint szerzői műre vonatkozó\n" +
                "szerzői jogokat sértsen. Szolgáltató a jelen pontban foglaltak megsértése esetén a Felhasználóval szemben kártérítési igénnyel léphet fel.\n" +
                "<h4>4.9</h4> A Felhasználó számára tilos bármilyen olyan rendszer vagy megoldás használata, amely az alkalmazásban nyújtott Szolgáltatások korlátozását vagy leállását célozza, azt lehetővé teszi, vagy\n" +
                "azt eredményezheti, vagy amely egyébként veszélyezteti az alkalmazás rendeltetésszerű működtetését. Szolgáltató a jelen pontban foglaltak megsértése esetén a Felhasználóval szemben kártérítési igénnyel léphet fel.\n" +
                "<h3>5. Vegyes rendelkezések</h3>\n" +
                "<h4>5.1</h4> A Szolgáltató nem felelős semmilyen kárért, amely az alkalmazásra való csatlakozás illetve a\n" +
                "Szolgáltatás használata miatt a Felhasználónál esetlegesen bekövetkezett. A Felhasználót\n" +
                "terheli a számítógépe, illetve az azon található adatok védelmének kötelezettsége.\n" +
                "<h4>5.2</h4> A Szolgáltató bármikor jogosult a jelen ÁSZF feltételeit egyoldalúan módosítani. Az esetleges\n" +
                "módosítás az alkalmazásban való megjelenéssel egyidejűleg lép hatályba.\n" +
                "<h4>5.3</h4> Minden kommunikáció nyelve magyar.\n" +
                "<h4>5.4</h4> A jelen ÁSZF-ben nem szabályozott kérdésekben a magyar jog, különösen a Polgári\n" +
                "Törvénykönyvről szóló 2013. évi V. törvény rendelkezései irányadóak.\n" +
                "<h4>5.5</h4> Az esetleges vitás kérdések rendezésénél a felek a hatályos jogszabályok előírásait tekintik irányadónak. Ha felek a vitát egyeztetéssel nem tudják rendezni, úgy jogvita esetére Felek a hatályos jogszabályok alapján hatáskörrel és illetékességgel rendelkező rendes bíróság eljárásának vetik alá magukat.\n" +
                "\n" +
                "<br><br>Jászladány, 2016.05.20"));

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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

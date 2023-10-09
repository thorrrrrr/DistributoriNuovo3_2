package com.gestione.distributori;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.model.RicavoMacchina;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

import java.util.Calendar;
import java.util.Date;

public class InsRicavoActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    /* access modifiers changed from: private */
    public String DataRicavo;
    private Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    private DatePickerDialog fromDatePickerDialog;
    private String idMacchina;
    private DatePickerDialog toDatePickerDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        setContentView(R.layout.activity_ins_ricavo);

        UI.InitToolbars(this, R.id.ins_ricavo_toolbar);

        String utenza = "";
        String password = "";
        if (this.bundle != null) {
            utenza = this.bundle.getString("utenza");
            password = this.bundle.getString("password");
            this.idMacchina = this.bundle.getString("idMacchina");
        }
        if (utenza.equals("") || password.equals("")) {
            Intent form_Login = new Intent(getApplicationContext(), LoginActivity.class);
            form_Login.putExtras(this.bundle);
            startActivity(form_Login);
            finish();
        }
        if (this.idMacchina.equals("")) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
        } else {
            Calendar c = Calendar.getInstance();
            int i = c.get(1);
            int i2 = c.get(2);
            int i3 = c.get(5);
            TextView textDataRic = (TextView) findViewById(R.id.etiDataArtInsRic);
            textDataRic.setText("Data Ric. " + DateFormat.format("dd-MM-yyyy", new Date()));
            textDataRic.setOnClickListener(this);
            this.DataRicavo = DateFormat.format("yyyy-MM-dd", new Date()).toString();
            Calendar newCalendar = Calendar.getInstance();
            final TextView textView = textDataRic;
            this.fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    textView.setText("Data Ric. " + DateFormat.format("dd-MM-yyyy", newDate.getTime()));
                    String unused = InsRicavoActivity.this.DataRicavo = DateFormat.format("yyyy-MM-dd", newDate.getTime()).toString();
                }
            }, newCalendar.get(1), newCalendar.get(2), newCalendar.get(5));
            final TextView textView2 = textDataRic;
            this.toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    textView2.setText("Data Ric. " + DateFormat.format("dd-MM-yyyy", newDate.getTime()));
                    String unused = InsRicavoActivity.this.DataRicavo = DateFormat.format("yyyy-MM-dd", newDate.getTime()).toString();
                }
            }, newCalendar.get(1), newCalendar.get(2), newCalendar.get(5));
        }
        ((Button) findViewById(R.id.ExitRicavo_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.SaveRicavo_button)).setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.Home_id) {
            Intent form_Home = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_Home.putExtras(this.bundle);
            startActivity(form_Home);
            finish();
            return true;
        }

        if (itemId == R.id.Exit_id) {
            finish();
            Process.killProcess(Process.myPid());
            super.onDestroy();
            return true;
        }

        if (itemId == R.id.Login_id) {
            SharedPreferences.Editor prefsEditor = getSharedPreferences("LoginPrefer", 0).edit();
            prefsEditor.putString("LOGIN_ID", "");
            prefsEditor.putString("LOGIN_PSWD", "");
            prefsEditor.putBoolean("REMEMBER", false);
            prefsEditor.commit();
            finish();
            Process.killProcess(Process.myPid());
            super.onDestroy();
            super.onStop();
            return true;
        }

        if (itemId == R.id.Utility_id) {
            Intent form_Utility = new Intent(getApplicationContext(), UtilityActivity.class);
            form_Utility.putExtras(this.bundle);
            startActivity(form_Utility);
            finish();
            return true;
        }
        return false;
    }

    @SuppressLint({"SimpleDateFormat"})
    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.etiDataArtInsRic) {
            this.fromDatePickerDialog.show();
            return;
        }

        if (itemId == R.id.ExitRicavo_button) {
            this.bundle.putString("idArticolo", "");
            Intent form_RicavoEsci = new Intent(getApplicationContext(), EleRicaviActivity.class);
            form_RicavoEsci.putExtras(this.bundle);
            startActivity(form_RicavoEsci);
            finish();
            return;
        }

        if (itemId == R.id.SaveRicavo_button) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            String ricavo = ((EditText) findViewById(R.id.edit_ricavoInsRicavo)).getText().toString();
            String battuteFreddo = ((EditText) findViewById(R.id.edit_quantitaBattuteFreddo)).getText().toString();
            String battuteCaldo = ((EditText) findViewById(R.id.edit_quantitaBattuteCaldo)).getText().toString();
            if (ricavo.equals("") || (ricavo.matches("0") && ricavo.length() == 1)) {
                dlgAlert.setMessage("Impostare il ricavo");
                dlgAlert.setTitle("Controllo ricavo");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (battuteFreddo.equals("")) {
                dlgAlert.setMessage("Impostare il n. delle battute freddo");
                dlgAlert.setTitle("Controllo battute");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (battuteCaldo.equals("")) {
                dlgAlert.setMessage("Impostare il n. delle battute caldo");
                dlgAlert.setTitle("Controllo battute");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else {
                RicavoMacchina ricavoMacchina = new RicavoMacchina();
                ricavoMacchina.setIdMacchina(Integer.parseInt(this.idMacchina));
                ricavoMacchina.setDataScaricoSoldi(this.DataRicavo.toString());
                ricavoMacchina.setSoldiScaricati(Float.parseFloat(ricavo));
                ricavoMacchina.setConsolidato("N");
                ricavoMacchina.setTrasmesso("N");
                ricavoMacchina.setBattuteFreddo(Integer.parseInt(battuteFreddo));
                ricavoMacchina.setBattuteCaldo(Integer.parseInt(battuteCaldo));
                this.dbHelper = new DbAdapter(this);
                if (this.dbHelper.createRicavo(ricavoMacchina) == 0) {
                    dlgAlert.setMessage("Impossibile salvare il ricavo in questo momento, riprovare in seguito");
                    dlgAlert.setTitle("Impostazione parametri");
                    dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                Intent form_RicavoSalva = new Intent(getApplicationContext(), EleRicaviActivity.class);
                form_RicavoSalva.putExtras(this.bundle);
                startActivity(form_RicavoSalva);
                finish();
                return;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.bundle.putString("idArticolo", "");
            Intent form_RicavoEsci = new Intent(getApplicationContext(), EleRicaviActivity.class);
            form_RicavoEsci.putExtras(this.bundle);
            startActivity(form_RicavoEsci);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

package com.gestione.distributori;

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

import com.gestione.distributori.model.ListiniClienti;
import com.gestione.distributori.model.RicaricaMacchina;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

import java.util.Calendar;
import java.util.Date;

public class InsRicaricaActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    /* access modifiers changed from: private */
    public String DataArticolo;
    private Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    private DatePickerDialog fromDatePickerDialog;
    private String idArticolo;
    private String idCliente;
    private String idMacchina;
    private DatePickerDialog toDatePickerDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        setContentView(R.layout.activity_ins_ricarica);

        UI.InitToolbars(this, R.id.ins_ricarica_toolbar);

        String utenza = "";
        String password = "";
        if (this.bundle != null) {
            utenza = this.bundle.getString("utenza");
            password = this.bundle.getString("password");
            this.idCliente = this.bundle.getString("idCliente");
            this.idMacchina = this.bundle.getString("idMacchina");
            this.idArticolo = this.bundle.getString("idArticolo");
        }
        if (utenza.equals("") || password.equals("")) {
            Intent form_Login = new Intent(getApplicationContext(), LoginActivity.class);
            form_Login.putExtras(this.bundle);
            startActivity(form_Login);
            finish();
        }
        if (this.idCliente.equals("") || this.idMacchina.equals("") || this.idArticolo.equals("")) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
        } else {
            this.dbHelper = new DbAdapter(this);
            new ListiniClienti();
            ListiniClienti listino = this.dbHelper.getRigaListino(this.idCliente, this.idMacchina, this.idArticolo);
            if (listino != null) {
                ((TextView) findViewById(R.id.idTipologiaArticolo)).setText(listino.getTipologia());
                ((TextView) findViewById(R.id.DescArtInsRic)).setText(listino.getNomeArticolo());
                ((TextView) findViewById(R.id.CostoArtInsRic)).setText(String.valueOf(listino.getCostoArticolo()));
                ((TextView) findViewById(R.id.PvendArtInsRic)).setText(String.valueOf(listino.getPrezzoVendita()));
                Calendar c = Calendar.getInstance();
                int i = c.get(1);
                int i2 = c.get(2);
                int i3 = c.get(5);
                TextView textDataRic = (TextView) findViewById(R.id.etiDataArtInsRic);
                textDataRic.setText("Data Ric. " + DateFormat.format("dd-MM-yyyy", new Date()));
                this.DataArticolo = DateFormat.format("yyyy-MM-dd", new Date()).toString();
                textDataRic.setOnClickListener(this);
                Calendar newCalendar = Calendar.getInstance();
                final TextView textView = textDataRic;
                this.fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        textView.setText("Data Ric. " + DateFormat.format("dd-MM-yyyy", newDate.getTime()));
                        String unused = InsRicaricaActivity.this.DataArticolo = DateFormat.format("yyyy-MM-dd", newDate.getTime()).toString();
                    }
                }, newCalendar.get(1), newCalendar.get(2), newCalendar.get(5));
                final TextView textView2 = textDataRic;
                this.toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        textView2.setText("Data Ric. " + DateFormat.format("dd-MM-yyyy", newDate.getTime()));
                        String unused = InsRicaricaActivity.this.DataArticolo = DateFormat.format("yyyy-MM-dd", newDate.getTime()).toString();
                    }
                }, newCalendar.get(1), newCalendar.get(2), newCalendar.get(5));
            }
        }
        ((Button) findViewById(R.id.ExitRicarica_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.SaveRic_button)).setOnClickListener(this);
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

    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.etiDataArtInsRic) {
            this.fromDatePickerDialog.show();
            return;
        }

        if (itemId == R.id.ExitRicarica_button) {
            this.bundle.putString("idArticolo", "");
            Intent form_RicaricaEsci = new Intent(getApplicationContext(), RicaricaActivity.class);
            form_RicaricaEsci.putExtras(this.bundle);
            startActivity(form_RicaricaEsci);
            finish();
            return;
        }

        if (itemId == R.id.SaveRic_button) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            EditText editQuantita = (EditText) findViewById(R.id.edit_quantitaArtInsRic);
            TextView textIdTipologiaArticolo = (TextView) findViewById(R.id.idTipologiaArticolo);
            TextView textDescrizioneArticolo = (TextView) findViewById(R.id.DescArtInsRic);
            String quantita = editQuantita.getText().toString();
            String costoArt = ((TextView) findViewById(R.id.CostoArtInsRic)).getText().toString();
            String pvendArt = ((TextView) findViewById(R.id.PvendArtInsRic)).getText().toString();
            if (quantita.equals("")) {
                dlgAlert.setMessage(R.string.impostareqnt);
                dlgAlert.setTitle(R.string.controlloqnt);
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
            String tipologiaArticolo = textIdTipologiaArticolo.getText().toString();
            if (!tipologiaArticolo.equals("K") && (quantita.indexOf(46) != -1 || quantita.indexOf(44) != -1)) {
                dlgAlert.setMessage("Per la tipologia di articolo scelta non si possono inserire valori frazionati");
                dlgAlert.setTitle(R.string.controlloqnt);
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (!quantita.matches("0") || quantita.length() != 1) {
                RicaricaMacchina ricaricaMacchina = new RicaricaMacchina();
                ricaricaMacchina.setIdMacchina(Integer.parseInt(this.idMacchina));
                ricaricaMacchina.setIdArticolo(Integer.parseInt(this.idArticolo));
                ricaricaMacchina.setNomeArticolo(textDescrizioneArticolo.getText().toString());
                if (tipologiaArticolo.equals("K")) {
                    ricaricaMacchina.setQtaConfezione(0);
                    ricaricaMacchina.setKG(Float.parseFloat(editQuantita.getText().toString()));
                } else {
                    ricaricaMacchina.setQtaConfezione(Integer.parseInt(editQuantita.getText().toString()));
                    ricaricaMacchina.setKG(0.0f);
                }
                ricaricaMacchina.setCostoAcquisto(Float.parseFloat(costoArt));
                ricaricaMacchina.setPrezzoVendita(Float.parseFloat(pvendArt));
                ricaricaMacchina.setDataRicarica(this.DataArticolo.toString());
                ricaricaMacchina.setConsolidato("N");
                ricaricaMacchina.setTrasmesso("N");
                this.dbHelper = new DbAdapter(this);
                if (this.dbHelper.createRicarica(ricaricaMacchina) == 0) {
                    dlgAlert.setMessage("Impossibile salvare la ricarica in questo momento, riprovare in seguito");
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
                this.bundle.putString("idArticolo", "");
                Intent form_RicaricaSalva = new Intent(getApplicationContext(), RicaricaActivity.class);
                form_RicaricaSalva.putExtras(this.bundle);
                startActivity(form_RicaricaSalva);
                finish();
                return;
            } else {
                dlgAlert.setMessage("Inserire un valore diverso da 0");
                dlgAlert.setTitle(R.string.controlloqnt);
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
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.bundle.putString("idArticolo", "");
            Intent form_RicaricaEsci = new Intent(getApplicationContext(), RicaricaActivity.class);
            form_RicaricaEsci.putExtras(this.bundle);
            startActivity(form_RicaricaEsci);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.gestione.distributori.model.InsManutenzione;
import com.gestione.distributori.model.TipiManutenzione;
import com.gestione.distributori.persistence.local.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsManutenzioneActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    /* access modifiers changed from: private */
    public String DataManutenzione;
    private Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    private DatePickerDialog fromDatePickerDialog;
    private Spinner spinnerTipiManutenzione;
    private DatePickerDialog toDatePickerDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        String utenza = "";
        String password = "";
        String mcliente = "";
        String mmodello = "";
        String midtipomacchina = "";
        String mtipomacchina = "";
        String midmacchina = "";
        String midtipomanutenzione = "";
        if (this.bundle != null) {
            utenza = this.bundle.getString("utenza");
            password = this.bundle.getString("password");
            mcliente = this.bundle.getString("mcliente");
            mmodello = this.bundle.getString("mmodello");
            midtipomacchina = this.bundle.getString("midtipomacchina");
            mtipomacchina = this.bundle.getString("mtipomacchina");
            midmacchina = this.bundle.getString("midmacchina");
            midtipomanutenzione = this.bundle.getString("midtipomanutenzione");
        }
        if (utenza.equals("") || password.equals("")) {
            Intent form_Login = new Intent(getApplicationContext(), LoginActivity.class);
            form_Login.putExtras(this.bundle);
            startActivity(form_Login);
            finish();
        }
        setContentView(R.layout.activity_ins_manutenzione);
        ((TextView) findViewById(R.id.etiCliente)).setText(mcliente);
        ((TextView) findViewById(R.id.DescMacchina)).setText(mmodello + " - " + mtipomacchina);
        ((TextView) findViewById(R.id.idmacchina)).setText(midmacchina);
        ((TextView) findViewById(R.id.idtipomacchina)).setText(midtipomacchina);
        ((TextView) findViewById(R.id.idtipomanutenzione)).setText(midtipomanutenzione);
        ((Button) findViewById(R.id.ExitIns_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.SaveInsMan_button)).setOnClickListener(this);
        this.spinnerTipiManutenzione = (Spinner) findViewById(R.id.spinnerTipiManutenzione);
        ArrayAdapter<SpinnerTipiManutenzione> categorieAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getTipiManutenzione(midtipomacchina.toString(), Integer.parseInt(midtipomanutenzione.toString())));
        categorieAdapter.setDropDownViewResource(R.layout.spinner_item);
        this.spinnerTipiManutenzione.setAdapter(categorieAdapter);
        this.spinnerTipiManutenzione.setOnItemSelectedListener(this);
        this.spinnerTipiManutenzione.setPrompt("Scegli il Tipo");
        TextView textDataInsMan = (TextView) findViewById(R.id.etiDataInsMan);
        textDataInsMan.setText(getText(R.string.dataManutenzione).toString() + " " + DateFormat.format("dd-MM-yyyy", new Date()));
        this.DataManutenzione = DateFormat.format("yyyy-MM-dd", new Date()).toString();
        textDataInsMan.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        final TextView textView = textDataInsMan;
        this.fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(InsManutenzioneActivity.this.getText(R.string.dataManutenzione).toString() + " " + DateFormat.format("dd-MM-yyyy", newDate.getTime()));
                String unused = InsManutenzioneActivity.this.DataManutenzione = DateFormat.format("yyyy-MM-dd", newDate.getTime()).toString();
            }
        }, newCalendar.get(1), newCalendar.get(2), newCalendar.get(5));
        final TextView textView2 = textDataInsMan;
        this.toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView2.setText(InsManutenzioneActivity.this.getText(R.string.dataManutenzione).toString() + " " + DateFormat.format("dd-MM-yyyy", newDate.getTime()));
                String unused = InsManutenzioneActivity.this.DataManutenzione = DateFormat.format("yyyy-MM-dd", newDate.getTime()).toString();
            }
        }, newCalendar.get(1), newCalendar.get(2), newCalendar.get(5));
    }

    public class SpinnerTipiManutenzione {
        private int TipiManutenzioneId;
        private String TipiManutenzioneValue;

        public SpinnerTipiManutenzione(int TipiManutenzioneId2, String TipiManutenzioneValue2) {
            this.TipiManutenzioneId = TipiManutenzioneId2;
            this.TipiManutenzioneValue = TipiManutenzioneValue2;
        }

        public int getId() {
            return this.TipiManutenzioneId;
        }

        public String getValue() {
            return this.TipiManutenzioneValue;
        }

        public String toString() {
            return this.TipiManutenzioneValue;
        }
    }

    public List<SpinnerTipiManutenzione> getTipiManutenzione(String idtipomacchina, int idtipomanutenzione) {
        List<SpinnerTipiManutenzione> Spinnertipimanutenzione = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (TipiManutenzione tipimanutenzione : this.dbHelper.getTipiManutenzione()) {
            new TipiManutenzione();
            if (tipimanutenzione.getIdTipoMacchina().equals(idtipomacchina) && tipimanutenzione.getIdTipoManutenzione() == idtipomanutenzione) {
                Spinnertipimanutenzione.add(new SpinnerTipiManutenzione(tipimanutenzione.getIdTipoManutenzione(), tipimanutenzione.getTipoManutenzioneDescrizione()));
            }
        }
        return Spinnertipimanutenzione;
    }

    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.etiDataInsMan) {
            this.fromDatePickerDialog.show();
            return;
        }

        if (itemId == R.id.SaveInsMan_button) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            TextView textidmacchina = (TextView) findViewById(R.id.idmacchina);
            TextView textNote = (TextView) findViewById(R.id.edit_note);
            TextView textidtipomanutenzione = (TextView) findViewById(R.id.idtipomanutenzione);
            String Descrizione = ((TextView) findViewById(R.id.edit_descrizione)).getText().toString();
            String Battute = ((TextView) findViewById(R.id.insnbattute)).getText().toString();
            if (Battute.equals("")) {
                dlgAlert.setMessage(R.string.impostareBattute);
                dlgAlert.setTitle("Contollo Inserimento");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (Descrizione.equals("")) {
                dlgAlert.setMessage(R.string.impostaredescrizione);
                dlgAlert.setTitle("Contollo Inserimento");
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
                InsManutenzione insmanutenzione = new InsManutenzione();
                insmanutenzione.setIDTipoManutenzione(Integer.parseInt(textidtipomanutenzione.getText().toString()));
                insmanutenzione.setIdMacchina(Integer.parseInt(textidmacchina.getText().toString()));
                insmanutenzione.setDataManutenzione(this.DataManutenzione.toString());
                insmanutenzione.setNBattute(Integer.parseInt(Battute));
                insmanutenzione.setDescrizione(Descrizione);
                insmanutenzione.setNote(textNote.getText().toString());
                insmanutenzione.setTrasmesso("N");
                this.dbHelper = new DbAdapter(this);
                if (this.dbHelper.createInsManutenzione(insmanutenzione) == 0) {
                    dlgAlert.setMessage("Impossibile salvare la Manutenzione in questo momento, riprovare in seguito");
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
                Intent form_Monitoraggio1 = new Intent(getApplicationContext(), MonitoraggioActivity.class);
                form_Monitoraggio1.putExtras(this.bundle);
                startActivity(form_Monitoraggio1);
                finish();
                return;
            }
        }

        if (itemId == R.id.ExitIns_button) {
            Intent form_Monitoraggio = new Intent(getApplicationContext(), MonitoraggioActivity.class);
            form_Monitoraggio.putExtras(this.bundle);
            startActivity(form_Monitoraggio);
            finish();
            return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
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
        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.getItemAtPosition(pos);
        parent.getId();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}

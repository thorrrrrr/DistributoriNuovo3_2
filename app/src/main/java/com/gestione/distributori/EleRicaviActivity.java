package com.gestione.distributori;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.adapter.RicaviAdapter;
import com.gestione.distributori.adapter.RicaviItem;
import com.gestione.distributori.model.RicavoMacchina;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SuppressLint({"SimpleDateFormat"})
public class EleRicaviActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private String DescCliente;
    private String DescMacchina;
    /* access modifiers changed from: private */
    public Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    private ListView elencoRicavi;
    private String idMacchina;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        String utenza = "";
        String password = "";
        if (this.bundle != null) {
            utenza = this.bundle.getString("utenza");
            password = this.bundle.getString("password");
            this.idMacchina = this.bundle.getString("idMacchina");
            this.DescMacchina = this.bundle.getString("DescMacchina");
            this.DescCliente = this.bundle.getString("DescCliente");
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
        }

        setContentView(R.layout.activity_ele_ricavi);

        UI.InitToolbars(this, R.id.ele_ricavi_toolbar);

        ((TextView) findViewById(R.id.formDescMacchinaRicavi_id)).setText(this.DescCliente + " - " + this.DescMacchina);
        RicaviAdapter ricaviAdapter = new RicaviAdapter(this, getElencoRicavi());
        this.elencoRicavi = (ListView) findViewById(R.id.elencoRicavi);
        this.elencoRicavi.setAdapter(ricaviAdapter);

        ((ImageButton) findViewById(R.id.ExitRicavi_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.SelTuttoRicavi_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.DeSelTuttoRicavi_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.Consolida_ric_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.Elimina_ric_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.InsRicavi_button)).setOnClickListener(this);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public ArrayList<RicaviItem> getElencoRicavi() {
        ArrayList<RicaviItem> ricaviList = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (RicavoMacchina ricavo : this.dbHelper.getRicaviByIdMacchina(this.idMacchina)) {
            new RicavoMacchina();
            float soldiScaricati = ricavo.getSoldiScaricati();
            String dataRicavo = "";
            try {
                dataRicavo = formatDate(ricavo.getDataScaricoSoldi());
            } catch (Exception e) {
            }
            ricaviList.add(new RicaviItem(dataRicavo, soldiScaricati, String.valueOf(ricavo.getIdRigaRicavo()), false));
        }
        return ricaviList;
    }

    private String formatDate(String dateString) throws ParseException {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(dateString));
        } catch (android.net.ParseException e) {
            return "";
        }
    }

    public void selectAll() {
        this.elencoRicavi = (ListView) findViewById(R.id.elencoRicavi);
        for (int i = 0; i < this.elencoRicavi.getChildCount(); i++) {
            ((CheckBox) ((RelativeLayout) this.elencoRicavi.getChildAt(i)).findViewById(R.id.chkSelConsRic)).setChecked(true);
        }
    }

    public void unSelectAll() {
        this.elencoRicavi = (ListView) findViewById(R.id.elencoRicavi);
        for (int i = 0; i < this.elencoRicavi.getChildCount(); i++) {
            ((CheckBox) ((RelativeLayout) this.elencoRicavi.getChildAt(i)).findViewById(R.id.chkSelConsRic)).setChecked(false);
        }
    }

    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.ExitRicavi_button) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
            return;
        }

        if (itemId == R.id.SelTuttoRicavi_button) {
            selectAll();
            return;
        }

        if (itemId == R.id.DeSelTuttoRicavi_button) {
            unSelectAll();
            return;
        }

        if (itemId == R.id.Consolida_ric_button) {
            AlertDialog.Builder builderCons = new AlertDialog.Builder(this);
            builderCons.setMessage("Sei sicuro di voler consolidare i Ricavi selezionati?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EleRicaviActivity.this.consolida();
                    Intent form_RicavoSalva = new Intent(EleRicaviActivity.this.getApplicationContext(), EleRicaviActivity.class);
                    form_RicavoSalva.putExtras(EleRicaviActivity.this.bundle);
                    EleRicaviActivity.this.startActivity(form_RicavoSalva);
                    EleRicaviActivity.this.finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builderCons.create().show();
            return;
        }

        if (itemId == R.id.Elimina_ric_button) {
            AlertDialog.Builder builderElimina = new AlertDialog.Builder(this);
            builderElimina.setMessage("Sei sicuro di voler cancellare i Ricavi selezionati?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EleRicaviActivity.this.elimina();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builderElimina.create().show();
            return;
        }

        if (itemId == R.id.InsRicavi_button) {
            Intent form_InsRicavo = new Intent(getApplicationContext(), InsRicavoActivity.class);
            form_InsRicavo.putExtras(this.bundle);
            startActivity(form_InsRicavo);
            finish();
            return;
        }
    }

    public void consolida() {
        this.elencoRicavi = (ListView) findViewById(R.id.elencoRicavi);
        this.dbHelper = new DbAdapter(getBaseContext());
        for (int i = 0; i < this.elencoRicavi.getChildCount(); i++) {
            CheckBox cb = (CheckBox) ((RelativeLayout) this.elencoRicavi.getChildAt(i)).findViewById(R.id.chkSelConsRic);
            RicaviItem ricavo = (RicaviItem) this.elencoRicavi.getItemAtPosition(i);
            if (ricavo != null) {
                String ricavo_id = ricavo.getIdRigaRicavo();
                if (cb.isChecked()) {
                    this.dbHelper.consolidaRicavo(ricavo_id);
                }
            }
        }
        this.elencoRicavi.setAdapter(new RicaviAdapter(this, getElencoRicavi()));
    }

    public void elimina() {
        this.elencoRicavi = (ListView) findViewById(R.id.elencoRicavi);
        this.dbHelper = new DbAdapter(getBaseContext());
        for (int i = 0; i < this.elencoRicavi.getChildCount(); i++) {
            CheckBox cb = (CheckBox) ((RelativeLayout) this.elencoRicavi.getChildAt(i)).findViewById(R.id.chkSelConsRic);
            RicaviItem ricavo = (RicaviItem) this.elencoRicavi.getItemAtPosition(i);
            if (ricavo != null) {
                String ricavo_id = ricavo.getIdRigaRicavo();
                if (cb.isChecked()) {
                    this.dbHelper.deleteRicavo(ricavo_id);
                }
            }
        }
        this.elencoRicavi.setAdapter(new RicaviAdapter(this, getElencoRicavi()));
    }
}

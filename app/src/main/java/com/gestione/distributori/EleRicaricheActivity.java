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

import com.gestione.distributori.adapter.RicaricheItem;
import com.gestione.distributori.model.RicaricaMacchina;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.adapter.RicaricheAdapter;
import com.gestione.distributori.utilities.UI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SuppressLint({"SimpleDateFormat"})
public class EleRicaricheActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private String DescCliente;
    private String DescMacchina;
    /* access modifiers changed from: private */
    public Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    private ListView elencoRicariche;
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
            Intent form_RicaricaAct = new Intent(getApplicationContext(), RicaricaActivity.class);
            form_RicaricaAct.putExtras(this.bundle);
            startActivity(form_RicaricaAct);
            finish();
        }
        setContentView(R.layout.activity_ele_ricariche);

        UI.InitToolbars(this, R.id.ele_ricariche_toolbar);

        ((TextView) findViewById(R.id.formDescMacchinaRic_id)).setText(this.DescCliente + " - " + this.DescMacchina);
        RicaricheAdapter ricaricheAdapter = new RicaricheAdapter(this, getElencoRicariche());
        this.elencoRicariche = (ListView) findViewById(R.id.elencoRicariche);
        this.elencoRicariche.setAdapter(ricaricheAdapter);
        ((ImageButton) findViewById(R.id.ExitRic_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.SelTuttoRicariche_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.DeSelTuttoRicariche_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.Consolida_button)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.EliminaRicariche_button)).setOnClickListener(this);
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
            Intent form_Ricarica = new Intent(getApplicationContext(), RicaricaActivity.class);
            form_Ricarica.putExtras(this.bundle);
            startActivity(form_Ricarica);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public ArrayList<RicaricheItem> getElencoRicariche() {
        ArrayList<RicaricheItem> ricaricheList = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (RicaricaMacchina ricarica : this.dbHelper.getRicaricheByIdMacchina(this.idMacchina)) {
            new RicaricaMacchina();
            String DescrQuantArt = "";
            if (ricarica.getKG() != 0.0f) {
                DescrQuantArt = ricarica.getNomeArticolo() + " - Ric: " + ricarica.getKG() + " Kg. ";
            } else if (ricarica.getQtaConfezione() != 0) {
                DescrQuantArt = ricarica.getNomeArticolo() + " - Ric: " + ricarica.getQtaConfezione() + " Un. ";
            }
            String dataRicarica = "";
            try {
                dataRicarica = formatDate(ricarica.getDataRicarica());
            } catch (Exception e) {
            }
            ricaricheList.add(new RicaricheItem(DescrQuantArt, dataRicarica, String.valueOf(ricarica.getIdRigaRicarica()), false));
        }
        return ricaricheList;
    }

    private String formatDate(String dateString) throws ParseException {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(dateString));
        } catch (android.net.ParseException e) {
            return "";
        }
    }

    public void selectAll() {
        this.elencoRicariche = (ListView) findViewById(R.id.elencoRicariche);
        for (int i = 0; i < this.elencoRicariche.getChildCount(); i++) {
            ((CheckBox) ((RelativeLayout) this.elencoRicariche.getChildAt(i)).findViewById(R.id.chkSelCons)).setChecked(true);
        }
    }

    public void unSelectAll() {
        this.elencoRicariche = (ListView) findViewById(R.id.elencoRicariche);
        for (int i = 0; i < this.elencoRicariche.getChildCount(); i++) {
            ((CheckBox) ((RelativeLayout) this.elencoRicariche.getChildAt(i)).findViewById(R.id.chkSelCons)).setChecked(false);
        }
    }

    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.ExitRic_button) {
            Intent form_Ricarica = new Intent(getApplicationContext(), RicaricaActivity.class);
            form_Ricarica.putExtras(this.bundle);
            startActivity(form_Ricarica);
            finish();
            return;
        }

        if (itemId == R.id.DeSelTuttoRicariche_button) {
            unSelectAll();
            return;
        }

        if (itemId == R.id.EliminaRicariche_button) {
            AlertDialog.Builder builderElimina = new AlertDialog.Builder(this);
            builderElimina.setMessage("Sei sicuro di voler eliminare le Ricariche selezionate?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EleRicaricheActivity.this.elimina();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builderElimina.create().show();
            return;
        }

        if (itemId == R.id.SelTuttoRicariche_button) {
            selectAll();
            return;
        }

        if (itemId == R.id.Consolida_button) {
            AlertDialog.Builder builderCons = new AlertDialog.Builder(this);
            builderCons.setMessage("Sei sicuro di voler consolidare le Ricariche selezionate?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EleRicaricheActivity.this.consolida();
                    Intent form_Ricarica = new Intent(EleRicaricheActivity.this.getApplicationContext(), RicaricaActivity.class);
                    form_Ricarica.putExtras(EleRicaricheActivity.this.bundle);
                    EleRicaricheActivity.this.startActivity(form_Ricarica);
                    EleRicaricheActivity.this.finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builderCons.create().show();
            return;
        }
    }

    public void consolida() {
        this.elencoRicariche = (ListView) findViewById(R.id.elencoRicariche);
        this.dbHelper = new DbAdapter(getBaseContext());
        for (int i = 0; i < this.elencoRicariche.getChildCount(); i++) {
            CheckBox cb = (CheckBox) ((RelativeLayout) this.elencoRicariche.getChildAt(i)).findViewById(R.id.chkSelCons);
            RicaricheItem ricarica = (RicaricheItem) this.elencoRicariche.getItemAtPosition(i);
            if (ricarica != null) {
                String ricarica_id = ricarica.getIdRigaRicarica();
                if (cb.isChecked()) {
                    this.dbHelper.consolidaRicarica(ricarica_id);
                }
            }
        }
        this.elencoRicariche.setAdapter(new RicaricheAdapter(this, getElencoRicariche()));
    }

    public void elimina() {
        this.elencoRicariche = (ListView) findViewById(R.id.elencoRicariche);
        this.dbHelper = new DbAdapter(getBaseContext());
        for (int i = 0; i < this.elencoRicariche.getChildCount(); i++) {
            CheckBox cb = (CheckBox) ((RelativeLayout) this.elencoRicariche.getChildAt(i)).findViewById(R.id.chkSelCons);
            RicaricheItem ricarica = (RicaricheItem) this.elencoRicariche.getItemAtPosition(i);
            if (ricarica != null) {
                String ricarica_id = ricarica.getIdRigaRicarica();
                if (cb.isChecked()) {
                    this.dbHelper.deleteRicarica(ricarica_id);
                }
            }
        }
        this.elencoRicariche.setAdapter(new RicaricheAdapter(this, getElencoRicariche()));
    }
}

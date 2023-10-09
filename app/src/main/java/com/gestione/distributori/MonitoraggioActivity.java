package com.gestione.distributori;

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
import android.widget.ListView;

import com.gestione.distributori.adapter.MonitoraggioAdapter;
import com.gestione.distributori.adapter.MonitoraggioItem;
import com.gestione.distributori.model.MonitoraggioDistributori;
import com.gestione.distributori.persistence.local.DbAdapter;

import java.util.ArrayList;

public class MonitoraggioActivity extends Activity implements View.OnClickListener {
    String DescCliente;
    String DescMacchina;
    /* access modifiers changed from: private */
    public Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    int idCliente;
    String idClienteStr;
    int idMacchina;
    String idMacchinaStr;
    private ListView listviewmonitoraggio;
    int posCliente;
    String posClientestr;
    int posMacchina;
    String posMacchinastr;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        String utenza = "";
        String password = "";
        Intent intent = getIntent();
        if (this.bundle != null) {
            utenza = this.bundle.getString("utenza");
            password = this.bundle.getString("password");
            if (intent.hasExtra("idCliente")) {
                this.idClienteStr = this.bundle.getString("idCliente");
                this.idCliente = Integer.parseInt(this.idClienteStr);
            }
            if (intent.hasExtra("idMacchina")) {
                this.idMacchinaStr = this.bundle.getString("idMacchina");
                this.idMacchina = Integer.parseInt(this.idMacchinaStr);
            }
            if (intent.hasExtra("posCliente")) {
                this.posClientestr = this.bundle.getString("posCliente");
            } else {
                this.posClientestr = "";
            }
            if (intent.hasExtra("posMacchina")) {
                this.posMacchinastr = this.bundle.getString("posMacchina");
            } else {
                this.posMacchinastr = "";
            }
        }
        if (utenza.equals("") || password.equals("")) {
            Intent form_Login = new Intent(getApplicationContext(), LoginActivity.class);
            form_Login.putExtras(this.bundle);
            startActivity(form_Login);
            finish();
        }
        setContentView(R.layout.activity_monitoraggio);
        this.listviewmonitoraggio = (ListView) findViewById(R.id.ListMonitoraggio);
        MonitoraggioAdapter monitoraggioAdapter = new MonitoraggioAdapter(this, getMonitoraggioDistributori(), this.bundle);
        if (monitoraggioAdapter.getCount() == 0) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Nessuna Manutenzione da Eseguire");
            dlgAlert.setTitle("Manutenzione");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent form_MenuPrinc = new Intent(MonitoraggioActivity.this.getApplicationContext(), MenuPrincipaleActivity.class);
                    form_MenuPrinc.putExtras(MonitoraggioActivity.this.bundle);
                    MonitoraggioActivity.this.startActivity(form_MenuPrinc);
                    MonitoraggioActivity.this.finish();
                    dialog.dismiss();
                }
            });
        }
        this.listviewmonitoraggio.setAdapter(monitoraggioAdapter);
        this.listviewmonitoraggio.setItemsCanFocus(true);
    }

    public ArrayList<MonitoraggioItem> getMonitoraggioDistributori() {
        ArrayList<MonitoraggioItem> monitoraggioList = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (MonitoraggioDistributori monitoraggio : this.dbHelper.getMonitoraggioDistributori()) {
            new MonitoraggioDistributori();
            monitoraggioList.add(new MonitoraggioItem(monitoraggio.getRagioneSociale(), monitoraggio.getIdmacchina(), monitoraggio.getModello(), monitoraggio.getIdTipoMacchina(), monitoraggio.getTipoMacchina(), monitoraggio.getIdTipomanutenzione(), monitoraggio.getDescrizione(), monitoraggio.getBattuteAttuali(), monitoraggio.getBattuteManutenzione(), monitoraggio.getScadenzaManutenzione()));
        }
        return monitoraggioList;
    }

    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.Exit_button) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principale, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.Exit_button) {
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

        if (itemId == R.id.Ricariche_id) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            this.idClienteStr = this.bundle.getString("idCliente");
            this.idCliente = Integer.parseInt(this.idClienteStr);
            this.idMacchinaStr = this.bundle.getString("idMacchina");
            this.idMacchina = Integer.parseInt(this.idMacchinaStr);
            if (this.idCliente == 0) {
                dlgAlert.setMessage("Scegliere un cliente");
                dlgAlert.setTitle("Accesso alle Ricariche");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return false;
            } else if (this.idMacchina == 0) {
                dlgAlert.setMessage("Scegliere un distributore");
                dlgAlert.setTitle("Accesso alle Ricariche");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return false;
            } else {
                Intent form_Ricariche = new Intent(getApplicationContext(), RicaricaActivity.class);
                form_Ricariche.putExtras(this.bundle);
                startActivity(form_Ricariche);
                finish();
                return true;
            }
        }

        if (itemId == R.id.Ricavi_id) {
            this.idClienteStr = this.bundle.getString("idCliente");
            this.idCliente = Integer.parseInt(this.idClienteStr);
            this.idMacchinaStr = this.bundle.getString("idMacchina");
            this.idMacchina = Integer.parseInt(this.idMacchinaStr);
            AlertDialog.Builder dlgAlert2 = new AlertDialog.Builder(this);
            if (this.idCliente == 0) {
                dlgAlert2.setMessage("Scegliere un cliente");
                dlgAlert2.setTitle("Accesso ai Ricavi");
                dlgAlert2.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert2.setCancelable(true);
                dlgAlert2.create().show();
                dlgAlert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return false;
            } else if (this.idMacchina == 0) {
                dlgAlert2.setMessage("Scegliere un distributore");
                dlgAlert2.setTitle("Accesso ai Ricavi");
                dlgAlert2.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert2.setCancelable(true);
                dlgAlert2.create().show();
                dlgAlert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return false;
            } else {
                Intent form_Ricavi = new Intent(getApplicationContext(), EleRicaviActivity.class);
                form_Ricavi.putExtras(this.bundle);
                startActivity(form_Ricavi);
                finish();
                return true;
            }
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
}

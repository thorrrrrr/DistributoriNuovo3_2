package com.gestione.distributori;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.model.InsManutenzione;
import com.gestione.distributori.model.RicaricaMacchina;
import com.gestione.distributori.model.RicavoMacchina;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.persistence.remote.DatabaseMySql;
import com.gestione.distributori.utilities.UI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class UtilityActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    public String Messaggio;
    public Runnable ShowMessage = new Runnable() {
        public void run() {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(UtilityActivity.this);
            dlgAlert.setMessage(UtilityActivity.this.Messaggio);
            dlgAlert.setTitle("Allineamento Listini");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create();
            dlgAlert.show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    };
    private Bundle bundle = new Bundle();
    private DatabaseMySql databaseMySql;
    public DbAdapter dbHelper;
    public ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            String utenza = this.bundle.getString("utenza");
            String password = this.bundle.getString("password");
            if (utenza.equals("") || password.equals("")) {
                Intent form_Login = new Intent(getApplicationContext(), LoginActivity.class);
                form_Login.putExtras(this.bundle);
                startActivity(form_Login);
                finish();
            }
        }
        setContentView(R.layout.activity_utility);

        UI.InitToolbars(this, R.id.utility_toolbar);

        ((Button) findViewById(R.id.Settings_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Listini_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Trasmetti_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Reset_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.MenuPrinc_button)).setOnClickListener(this);
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

        if (itemId == R.id.Settings_button) {
            Intent form_Setting = new Intent(getApplicationContext(), SettingsActivity.class);
            form_Setting.putExtras(this.bundle);
            startActivity(form_Setting);
            finish();
            return;
        }

        if (itemId == R.id.Listini_button) {
            ScaricaListini();
            return;
        }

        if (itemId == R.id.Trasmetti_button) {
            try {
                TrasmettiDati();
                return;
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
                return;
            }
        }

        if (itemId == R.id.Reset_button) {
            AlertDialog.Builder builderCons = new AlertDialog.Builder(this);
            builderCons.setMessage("Sei sicuro di voler resettare la Base Dati locale?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    UtilityActivity.this.ResetBaseDati();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builderCons.create().show();
            return;
        }

        if (itemId == R.id.MenuPrinc_button) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
            return;
        }
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

    public void ScaricaListini() {
        this.dialog = new ProgressDialog(this);
        this.dialog.setMessage("Attendere prego ...");
        this.dialog.setIndeterminate(true);
        this.dialog.setCancelable(false);
        this.dialog.onStart();
        this.dialog.show();
        new Thread(new Runnable() {
            public void run() {
                if (!UtilityActivity.this.isNetworkAvailable()) {
                    UtilityActivity.this.dialog.cancel();
                    UtilityActivity.this.dialog.dismiss();
                    String unused = UtilityActivity.this.Messaggio = "Rete Internet non presente, Scarico Listini non effettuabile";
                    UtilityActivity.this.runOnUiThread(UtilityActivity.this.ShowMessage);
                    return;
                }
                DbAdapter unused2 = UtilityActivity.this.dbHelper = new DbAdapter(UtilityActivity.this.getBaseContext());
                String indirizzoIp = UtilityActivity.this.dbHelper.getIndirizzoIp();
                String porta = UtilityActivity.this.dbHelper.getPorta();
                String dbName = UtilityActivity.this.dbHelper.getNomeBaseDati();
                if (UtilityActivity.this.dbHelper.verificaConnMySql(indirizzoIp, porta, UtilityActivity.this.dbHelper.getUtenteMySql(), UtilityActivity.this.dbHelper.getPasswordMySql(), dbName) != "OK") {
                    UtilityActivity.this.dialog.cancel();
                    UtilityActivity.this.dialog.dismiss();
                    String unused3 = UtilityActivity.this.Messaggio = "Scarico Listini non riuscito Errore Rete";
                    UtilityActivity.this.runOnUiThread(UtilityActivity.this.ShowMessage);
                } else if (UtilityActivity.this.dbHelper.allineaBaseDati() == "OK") {
                    UtilityActivity.this.dialog.cancel();
                    UtilityActivity.this.dialog.dismiss();
                    String unused4 = UtilityActivity.this.Messaggio = "Scarico Listini avvenuto con successo";
                    UtilityActivity.this.runOnUiThread(UtilityActivity.this.ShowMessage);
                } else {
                    UtilityActivity.this.dialog.cancel();
                    UtilityActivity.this.dialog.dismiss();
                    String unused5 = UtilityActivity.this.Messaggio = "Scarico Listini non riuscito";
                    UtilityActivity.this.runOnUiThread(UtilityActivity.this.ShowMessage);
                }
            }
        }).start();
    }

    public void TrasmettiDati() throws SQLException, ParseException {
        boolean reteAttiva = isNetworkAvailable();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        if (!reteAttiva) {
            dlgAlert.setMessage("Rete Internet non presente, Trasmissione Dati non effettuabile");
            dlgAlert.setTitle("Trasmissione Dati");
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
        this.dbHelper = new DbAdapter(getBaseContext());
        this.databaseMySql = new DatabaseMySql(getBaseContext());
        this.dbHelper.deleteRicaricheTrasmesse();
        this.dbHelper.deleteRicaviTrasmessi();
        this.dbHelper.deleteManutenzioneTrasmessi();
        List<RicaricaMacchina> RicaricheMacchina = this.dbHelper.getRicaricheConsolidate();
        List<RicavoMacchina> RicaviMacchina = this.dbHelper.getRicaviConsolidati();
        List<InsManutenzione> InsManutenzione = this.dbHelper.getInsManutenzioneConsolidati();
        if (RicaricheMacchina.isEmpty() && RicaviMacchina.isEmpty() && InsManutenzione.isEmpty()) {
            dlgAlert.setMessage("Nessun Dato da Trasmettere");
            dlgAlert.setTitle("Trasmissione Dati");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (this.databaseMySql.InsertManutenzione(InsManutenzione) != "OK") {
            dlgAlert.setMessage("Trasmissione Dati non riuscita");
            dlgAlert.setTitle("Trasmissione Dati");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (this.databaseMySql.InsertRicaricaDistributore(RicaricheMacchina) != "OK") {
            dlgAlert.setMessage("Trasmissione Dati non riuscita");
            dlgAlert.setTitle("Trasmissione Dati");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (this.databaseMySql.InsertRicavoDistributore(RicaviMacchina) != "OK") {
            dlgAlert.setMessage("Trasmissione Dati non riuscita");
            dlgAlert.setTitle("Trasmissione Dati");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            dlgAlert.setMessage("Trasmissione Dati avvenuta con successo");
            dlgAlert.setTitle("Trasmissione Dati");
            dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    UtilityActivity.this.dbHelper.deleteRicaricheTrasmesse();
                    UtilityActivity.this.dbHelper.deleteRicaviTrasmessi();
                    UtilityActivity.this.dbHelper.deleteManutenzioneTrasmessi();
                    dialog.dismiss();
                }
            });
        }
    }

    public void ResetBaseDati() {
        boolean reteAttiva = isNetworkAvailable();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        if (!reteAttiva) {
            dlgAlert.setMessage("Rete Internet non presente, Reset Base Dati Locale non effettuabile");
            dlgAlert.setTitle("Reset Base Dati");
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
        this.dbHelper = new DbAdapter(getBaseContext());
        if (this.dbHelper.resetBaseDati() == "OK") {
            dlgAlert.setMessage("Reset Base Dati Locale avvenuto con successo");
            dlgAlert.setTitle("Reset Base Dati");
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
        dlgAlert.setMessage("Reset Base Dati Locale non riuscito");
        dlgAlert.setTitle("Allineamento Listini");
        dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

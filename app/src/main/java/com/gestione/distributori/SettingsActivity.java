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
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.model.Settings;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

import java.util.ArrayList;

public class SettingsActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    public String Messaggio = "";
    public Runnable ShowMessage = new Runnable() {
        public void run() {
            if (SettingsActivity.this.allineamentoDati.equals("OK")) {
                SettingsActivity.this.dialog.cancel();
                SettingsActivity.this.dialog.dismiss();
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SettingsActivity.this);
                dlgAlert.setMessage(SettingsActivity.this.Messaggio);
                dlgAlert.setTitle("Allineamento Base Dati Remota");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Bundle bundleStatoApp = new Bundle();
                        bundleStatoApp.putString("StatoApp", "Inizializzato");
                        Intent form_intent = new Intent(SettingsActivity.this.getApplicationContext(), LoginActivity.class);
                        form_intent.putExtra("StatoApp", bundleStatoApp);
                        SettingsActivity.this.startActivity(form_intent);
                        SettingsActivity.this.finish();
                    }
                });
                return;
            }
            SettingsActivity.this.dialog.cancel();
            SettingsActivity.this.dialog.dismiss();
            AlertDialog.Builder dlgAlert2 = new AlertDialog.Builder(SettingsActivity.this);
            dlgAlert2.setMessage("Impossibile scaricare i dati dalla Base Dati Remota, premere il pulsante Esci e riprovare in seguito");
            dlgAlert2.setTitle("Allineamento Base Dati");
            dlgAlert2.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
            dlgAlert2.setCancelable(true);
            dlgAlert2.create().show();
            dlgAlert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    };
    public String allineamentoDati = "";
    private Bundle bundle = new Bundle();
    public DbAdapter dbHelper;
    public ProgressDialog dialog;
    public Context mContext;
    private String statoApp = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        UI.InitToolbars(this, R.id.settings_toolbar);

        this.bundle = getIntent().getExtras();
        this.statoApp = this.bundle.getString("StatoApp");
        this.dbHelper = new DbAdapter(this);

        String indirizzoIp = getResources().getString(R.string.db_indirizzoIp);
        String porta = getResources().getString(R.string.db_porta);
        String nomeBaseDati = getResources().getString(R.string.db_nomeBaseDati);
        String utenteMySql = getResources().getString(R.string.db_utenteMySql);
        String passwordMySql = getResources().getString(R.string.db_passwordMySql);

        ((EditText) findViewById(R.id.edit_indirizzoIp)).setText(indirizzoIp);
        ((EditText) findViewById(R.id.edit_porta)).setText(porta);
        ((EditText) findViewById(R.id.edit_nomeBaseDati)).setText(nomeBaseDati);
        ((EditText) findViewById(R.id.edit_utenteMySql)).setText(utenteMySql);
        ((EditText) findViewById(R.id.edit_passwordMySql)).setText(passwordMySql);

        new ArrayList();
        for (Settings setting : this.dbHelper.getAllSettings()) {
            new Settings();
            if (setting.getDescrizione().equals("Indirizzo IP")) {
                EditText editIndirizzoIp = (EditText) findViewById(R.id.edit_indirizzoIp);
                if (!setting.getValore().isEmpty()) {
                    editIndirizzoIp.setText(setting.getValore());
                }
            }
            if (setting.getDescrizione().equals("Porta")) {
                EditText editPorta = (EditText) findViewById(R.id.edit_porta);
                if (!setting.getValore().isEmpty()) {
                    editPorta.setText(setting.getValore());
                }
            }
            if (setting.getDescrizione().equals("NomeBaseDati")) {
                EditText editNomeBaseDati = (EditText) findViewById(R.id.edit_nomeBaseDati);
                if (!setting.getValore().isEmpty()) {
                    editNomeBaseDati.setText(setting.getValore());
                }
            }
            if (setting.getDescrizione().equals("UtenteMySQL")) {
                EditText editUtenteMySql = (EditText) findViewById(R.id.edit_utenteMySql);
                if (!setting.getValore().isEmpty()) {
                    editUtenteMySql.setText(setting.getValore());
                }
            }
            if (setting.getDescrizione().equals("PasswordMySQL")) {
                EditText editPasswordMySql = (EditText) findViewById(R.id.edit_passwordMySql);
                if (!setting.getValore().isEmpty()) {
                    editPasswordMySql.setText(setting.getValore());
                }
            }
        }
        if (this.statoApp.equals("Inizializzazione")) {
            Button btnInizializza = (Button) findViewById(R.id.Inizializza_button);
            btnInizializza.setText(R.string.Inizializza_button_label);
            btnInizializza.setOnClickListener(this);
            this.Messaggio = "Allineamento con Base Dati Remota riuscito, premi il pulsante esci e riavvia l'applicazione";
        } else {
            Button btnInizializza2 = (Button) findViewById(R.id.Inizializza_button);
            btnInizializza2.setText(R.string.Save_button_label);
            btnInizializza2.setOnClickListener(this);
            this.Messaggio = "Allineamento con Base Dati Remota riuscito";
        }
        ((Button) findViewById(R.id.Exit_button)).setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.Exit_id) {
            finish();
            Process.killProcess(Process.myPid());
            super.onDestroy();
            return true;
        }

        return false;
    }

    public void onClick(View v) {
        int itemId = v.getId();

        if (itemId == R.id.Exit_button) {
            if (this.statoApp.equals("Inizializzazione")) {
                finish();
                Process.killProcess(Process.myPid());
                super.onDestroy();
                return;
            }
            Intent form_menuPrincipale = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_menuPrincipale.putExtras(this.bundle);
            startActivity(form_menuPrincipale);
            finish();
            return;
        }

        if (itemId == R.id.Inizializza_button) {
            String indirizzoIp = ((EditText) findViewById(R.id.edit_indirizzoIp)).getText().toString();
            String porta = ((EditText) findViewById(R.id.edit_porta)).getText().toString();
            String nomeBaseDati = ((EditText) findViewById(R.id.edit_nomeBaseDati)).getText().toString();
            String utente = ((EditText) findViewById(R.id.edit_utenteMySql)).getText().toString();
            String password = ((EditText) findViewById(R.id.edit_passwordMySql)).getText().toString();
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            if (indirizzoIp.equals("")) {
                dlgAlert.setMessage("Impostare l'Indirizzo IP");
                dlgAlert.setTitle("Controllo Parametri");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (porta.equals("")) {
                dlgAlert.setMessage("Impostare la porta di collegamento");
                dlgAlert.setTitle("Controllo Parametri");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (nomeBaseDati.equals("")) {
                dlgAlert.setMessage("Impostare il nome della Base Dati MySql");
                dlgAlert.setTitle("Controllo Parametri");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (utente.equals("")) {
                dlgAlert.setMessage("Impostare l'utente MySQL");
                dlgAlert.setTitle("Controllo Parametri");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (password.equals("")) {
                dlgAlert.setMessage("Impostare la password MySQL");
                dlgAlert.setTitle("Controllo Parametri");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (!isNetworkAvailable()) {
                dlgAlert.setMessage("Rete Internet non presente, inizializzazione non effettuabile");
                dlgAlert.setTitle("Connessione Remota");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (!this.dbHelper.verificaConnMySql(indirizzoIp, porta, utente, password, nomeBaseDati).equals("OK")) {
                dlgAlert.setMessage("I Parametri di connessione risultano errati, inizializzazione non effettuabile");
                dlgAlert.setTitle("Connessione Remota");
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
                if (!this.dbHelper.verificaSettings().equals("OK")) {
                    this.dbHelper.inizializzaSettings();
                }
                if (this.dbHelper.updateSettings("Indirizzo IP", indirizzoIp) == 0) {
                    dlgAlert.setMessage("Impossibile salvare i parametri di connessione in questo momento, riprovare in seguito");
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
                } else if (this.dbHelper.updateSettings("Porta", porta) == 0) {
                    dlgAlert.setMessage("Impossibile salvare i parametri di connessione in questo momento, riprovare in seguito");
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
                } else if (this.dbHelper.updateSettings("NomeBaseDati", nomeBaseDati) == 0) {
                    dlgAlert.setMessage("Impossibile salvare i parametri di connessione in questo momento, riprovare in seguito");
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
                } else if (this.dbHelper.updateSettings("UtenteMySQL", utente) == 0) {
                    dlgAlert.setMessage("Impossibile salvare i parametri di connessione in questo momento, riprovare in seguito");
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
                } else if (this.dbHelper.updateSettings("PasswordMySQL", password) == 0) {
                    dlgAlert.setMessage("Impossibile salvare i parametri di connessione in questo momento, riprovare in seguito");
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
                } else {
                    this.dialog = new ProgressDialog(this);
                    this.dialog.setMessage("Attendere prego ...");
                    this.dialog.setIndeterminate(true);
                    this.dialog.setCancelable(false);
                    this.dialog.onStart();
                    this.dialog.show();
                    new Thread(new Runnable() {
                        public void run() {
                            String unused = SettingsActivity.this.allineamentoDati = SettingsActivity.this.dbHelper.allineaBaseDati();
                            SettingsActivity.this.runOnUiThread(SettingsActivity.this.ShowMessage);
                        }
                    }).start();
                    return;
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (this.statoApp.equals("Inizializzazione")) {
                finish();
                Process.killProcess(Process.myPid());
                super.onDestroy();
            } else {
                Intent form_menuPrincipale = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
                form_menuPrincipale.putExtras(this.bundle);
                startActivity(form_menuPrincipale);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

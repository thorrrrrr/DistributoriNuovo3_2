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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.model.Utente;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

public class LoginActivity extends Activity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    public String Messaggio;
    public Runnable ShowMessage = new Runnable() {
        public void run() {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
            dlgAlert.setMessage(LoginActivity.this.Messaggio);
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
    public DbAdapter dbHelper;
    public ProgressDialog dialog;
    String intervallo = "";

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            UI.InitToolbars(this, R.id.login_toolbar);

            SharedPreferences myPrefs = getSharedPreferences("LoginPrefer", 0);
            boolean isRemember = myPrefs.getBoolean("REMEMBER", false);
            this.dbHelper = new DbAdapter(this);
            if (isRemember) {
                String id = myPrefs.getString("LOGIN_ID", "");
                String pswd = myPrefs.getString("LOGIN_PSWD", "");
                ((EditText) findViewById(R.id.edit_utenza)).setText(id);
                ((EditText) findViewById(R.id.edit_Password)).setText(pswd);
                ((CheckBox) findViewById(R.id.chkpwd)).setChecked(isRemember);
                Utente utente = this.dbHelper.loginApplicazione(id, pswd);
                Bundle bundleMenu = new Bundle();
                if (!(utente == null || utente.getIdUtente() == 0)) {
                    if (!utente.getAbilitato().equals("S")) {
                        bundleMenu.putString("utenza", "");
                        bundleMenu.putString("password", "");
                        bundleMenu.putString("StatoApp", "Inizializzato");
                    } else {
                        bundleMenu.putString("utenza", utente.getUtenza());
                        bundleMenu.putString("password", pswd.toString());
                        bundleMenu.putString("StatoApp", "Inizializzato");
                        Intent form_menuPrincipale = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
                        form_menuPrincipale.putExtras(bundleMenu);
                        startActivity(form_menuPrincipale);
                        finish();
                    }
                }
            }
            String inizializzazione = "";
            Bundle bundleStatoApp = new Bundle();
            this.bundle = getIntent().getExtras();
            if (this.bundle != null) {
                inizializzazione = this.bundle.getString("StatoApp");
            }
            if (inizializzazione == "") {
                this.dbHelper = new DbAdapter(this);
                String inizializzazione2 = this.dbHelper.controllaEsistenzaBaseDati();
                bundleStatoApp.putString("StatoApp", inizializzazione2);
                if (inizializzazione2 == "Inizializzazione") {
                    Intent form_intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    form_intent.putExtras(bundleStatoApp);
                    startActivity(form_intent);
                    finish();
                } else {
                    ((Button) findViewById(R.id.login_button)).setOnClickListener(this);
                }
                ((Button) findViewById(R.id.Exit_button)).setOnClickListener(this);
                return;
            }
            ((Button) findViewById(R.id.login_button)).setOnClickListener(this);
            ((Button) findViewById(R.id.Exit_button)).setOnClickListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog(ex.getMessage());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

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
        return false;
    }

    public void onClick(View v) {
        try {
            int itemId = v.getId();

            if (itemId == R.id.Exit_button) {
                finish();
                Process.killProcess(Process.myPid());
                super.onDestroy();
                super.onStop();
                return;
            }

            if (itemId == R.id.login_button) {
                String utenza = ((EditText) findViewById(R.id.edit_utenza)).getText().toString();
                String password = ((EditText) findViewById(R.id.edit_Password)).getText().toString();
                SharedPreferences.Editor prefsEditor = getSharedPreferences("LoginPrefer", 0).edit();
                prefsEditor.putString("LOGIN_ID", utenza);
                prefsEditor.putString("LOGIN_PSWD", password);
                prefsEditor.putBoolean("REMEMBER", ((CheckBox) findViewById(R.id.chkpwd)).isChecked());
                prefsEditor.commit();
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                if (utenza.equals("")) {
                    dlgAlert.setMessage("Impostare l'Utenza");
                    dlgAlert.setTitle("Login Applicazione");
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
                    dlgAlert.setMessage("Impostare la Password");
                    dlgAlert.setTitle("Login Applicazione");
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
                    Utente utente = this.dbHelper.loginApplicazione(utenza, password);
                    if (utente == null) {
                        dlgAlert.setMessage("Utenza o Password errate provvedere a reinserirle");
                        dlgAlert.setTitle("Login Applicazione");
                        dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        return;
                    } else if (utente.getIdUtente() == 0) {
                        dlgAlert.setMessage("Utenza o Password errate provvedere a reinserirle");
                        dlgAlert.setTitle("Login Applicazione");
                        dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        return;
                    } else if (!utente.getAbilitato().equals("S")) {
                        AlertDialog.Builder builderCons = new AlertDialog.Builder(this);
                        builderCons.setTitle("Login Applicazione");
                        builderCons.setMessage("Utente non Abilitato Rieseguire Allineamento?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LoginActivity.this.AllineaBaseDati();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builderCons.create().show();
                        return;
                    } else {
                        Bundle bundleMenu = new Bundle();
                        bundleMenu.putString("utenza", utente.getUtenza());
                        bundleMenu.putString("password", password.toString());
                        bundleMenu.putString("StatoApp", "Inizializzato");
                        Intent form_menuPrincipale = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
                        form_menuPrincipale.putExtras(bundleMenu);
                        startActivity(form_menuPrincipale);
                        finish();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog(ex.getMessage());
        }
//        ex.printStackTrace();
//        ErrorDialog(ex.getMessage());
    }

    private void ErrorDialog(String Description) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("You get Error...");
        alertDialog.setMessage(Description);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    /* access modifiers changed from: private */
    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void AllineaBaseDati() {
        this.dialog = new ProgressDialog(this);
        this.dialog.setMessage("Attendere prego ...");
        this.dialog.setIndeterminate(true);
        this.dialog.setCancelable(false);
        this.dialog.onStart();
        this.dialog.show();
        new Thread(new Runnable() {
            public void run() {
                if (!LoginActivity.this.isNetworkAvailable()) {
                    LoginActivity.this.dialog.cancel();
                    LoginActivity.this.dialog.dismiss();
                    String unused = LoginActivity.this.Messaggio = "Rete Internet non presente, Allineamento non effettuabile";
                    LoginActivity.this.runOnUiThread(LoginActivity.this.ShowMessage);
                    return;
                }
                DbAdapter unused2 = LoginActivity.this.dbHelper = new DbAdapter(LoginActivity.this.getBaseContext());
                String indirizzoIp = LoginActivity.this.dbHelper.getIndirizzoIp();
                String porta = LoginActivity.this.dbHelper.getPorta();
                String dbName = LoginActivity.this.dbHelper.getNomeBaseDati();
                if (LoginActivity.this.dbHelper.verificaConnMySql(indirizzoIp, porta, LoginActivity.this.dbHelper.getUtenteMySql(), LoginActivity.this.dbHelper.getPasswordMySql(), dbName) != "OK") {
                    LoginActivity.this.dialog.cancel();
                    LoginActivity.this.dialog.dismiss();
                    String unused3 = LoginActivity.this.Messaggio = "Allineamento non riuscito Errore Rete";
                    LoginActivity.this.runOnUiThread(LoginActivity.this.ShowMessage);
                } else if (LoginActivity.this.dbHelper.allineaBaseDati() == "OK") {
                    LoginActivity.this.dialog.cancel();
                    LoginActivity.this.dialog.dismiss();
                    String unused4 = LoginActivity.this.Messaggio = "Allineamento avvenuto con successo";
                    LoginActivity.this.runOnUiThread(LoginActivity.this.ShowMessage);
                } else {
                    LoginActivity.this.dialog.cancel();
                    LoginActivity.this.dialog.dismiss();
                    String unused5 = LoginActivity.this.Messaggio = "Allineamento non riuscito";
                    LoginActivity.this.runOnUiThread(LoginActivity.this.ShowMessage);
                }
            }
        }).start();
    }
}

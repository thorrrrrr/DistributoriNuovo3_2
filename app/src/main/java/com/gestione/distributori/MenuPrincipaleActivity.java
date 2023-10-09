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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.model.Cliente;
import com.gestione.distributori.model.Macchina;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipaleActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {
    String DescCliente;
    String DescMacchina;
    private Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    int idCliente;
    String idClienteStr;
    int idMacchina;
    String idMacchinaStr;
    int posCliente;
    String posClientestr;
    int posMacchina;
    String posMacchinastr;
    private Spinner spinnerClienti;
    private Spinner spinnerDistributori;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBaseTheme);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.bundle = getIntent().getExtras();
        String utenza = "";
        String password = "";
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
        setContentView(R.layout.activity_menu_principale);

        UI.InitToolbars(this, R.id.mainmenu_toolbar);

        this.spinnerClienti = (Spinner) findViewById(R.id.spinnerCliente);
        this.spinnerDistributori = (Spinner) findViewById(R.id.spinnerDistributore);
        ArrayAdapter<SpinnerClienti> clientiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getClienti());
        clientiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerClienti.setAdapter(clientiAdapter);
        this.spinnerClienti.setOnItemSelectedListener(this);
        this.spinnerClienti.setPrompt("Scegli il Cliente");
        this.spinnerDistributori.setOnItemSelectedListener(this);
        if (!this.posClientestr.equals("")) {
            this.spinnerClienti.setSelection(Integer.parseInt(this.posClientestr), false);
            ArrayAdapter<SpinnerDistributori> distributoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getDistributori(this.idClienteStr));
            distributoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinnerDistributori.setAdapter(distributoriAdapter);
            this.spinnerDistributori.setSelection(Integer.parseInt(this.posMacchinastr), false);
        }
        ((Button) findViewById(R.id.Ricariche_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Ricavi_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Utility_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Exit_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.Monitoraggio_button)).setOnClickListener(this);
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
        return false;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.getItemAtPosition(pos);
        int itemId = parent.getId();

        if (itemId == R.id.spinnerCliente) {
            this.idCliente = ((SpinnerClienti) this.spinnerClienti.getSelectedItem()).getId();
            this.posCliente = this.spinnerClienti.getSelectedItemPosition();
            this.bundle.putString("idCliente", String.valueOf(this.idCliente));
            this.bundle.putString("posCliente", String.valueOf(this.posCliente));
            this.DescCliente = this.spinnerClienti.getSelectedItem().toString();
            this.bundle.putString("DescCliente", this.DescCliente);
            if (this.idCliente != 0) {
                ArrayAdapter<SpinnerDistributori> distributoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getDistributori(Integer.toString(this.idCliente)));
                distributoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.spinnerDistributori.setAdapter(distributoriAdapter);
                return;
            }
            return;
        }

        if (itemId == R.id.spinnerDistributore) {
            this.idMacchina = ((SpinnerDistributori) this.spinnerDistributori.getSelectedItem()).getId();
            this.posMacchina = this.spinnerDistributori.getSelectedItemPosition();
            this.bundle.putString("idMacchina", String.valueOf(this.idMacchina));
            this.bundle.putString("posMacchina", String.valueOf(this.posMacchina));
            this.DescMacchina = this.spinnerDistributori.getSelectedItem().toString();
            this.bundle.putString("DescMacchina", this.DescMacchina);
            return;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public class SpinnerClienti {
        private int clienteId;
        private String clienteValue;

        public SpinnerClienti(int clienteId2, String clienteValue2) {
            this.clienteId = clienteId2;
            this.clienteValue = clienteValue2;
        }

        public int getId() {
            return this.clienteId;
        }

        public String getValue() {
            return this.clienteValue;
        }

        public String toString() {
            return this.clienteValue;
        }
    }

    public class SpinnerDistributori {
        private int distributoreId;
        private String distributoreValue;

        public SpinnerDistributori(int distributoreId2, String distributoreValue2) {
            this.distributoreId = distributoreId2;
            this.distributoreValue = distributoreValue2;
        }

        public int getId() {
            return this.distributoreId;
        }

        public String getValue() {
            return this.distributoreValue;
        }

        public String toString() {
            return this.distributoreValue;
        }
    }

    public List<SpinnerClienti> getClienti() {
        List<SpinnerClienti> clientiSpinner = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (Cliente cliente : this.dbHelper.getListaClienti()) {
            new Cliente();
            clientiSpinner.add(new SpinnerClienti(cliente.getIdCliente(), cliente.getRagioneSociale()));
        }
        return clientiSpinner;
    }

    public List<SpinnerDistributori> getDistributori(String idCliente2) {
        List<SpinnerDistributori> distributoriSpinner = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (Macchina macchina : this.dbHelper.getListaMacchineCliente(idCliente2)) {
            new Macchina();
            distributoriSpinner.add(new SpinnerDistributori(macchina.getIdMacchina(), macchina.getMarca() + " - " + macchina.getModello() + " - " + macchina.getCodice()));
        }
        return distributoriSpinner;
    }

    public void onClick(View v) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        int itemId = v.getId();

        if (itemId == R.id.Exit_button) {
            finish();
            Process.killProcess(Process.myPid());
            super.onDestroy();
            return;
        }

        if (itemId == R.id.Utility_button) {
            Intent form_Utility = new Intent(getApplicationContext(), UtilityActivity.class);
            form_Utility.putExtras(this.bundle);
            startActivity(form_Utility);
            finish();
            return;
        }

        if (itemId == R.id.Ricavi_button) {
            this.idClienteStr = this.bundle.getString("idCliente");
            this.idCliente = Integer.parseInt(this.idClienteStr);
            this.idMacchinaStr = this.bundle.getString("idMacchina");
            this.idMacchina = Integer.parseInt(this.idMacchinaStr);
            if (this.idCliente == 0) {
                dlgAlert.setMessage("Scegliere un cliente");
                dlgAlert.setTitle("Accesso ai Ricavi");
                dlgAlert.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            } else if (this.idMacchina == 0) {
                dlgAlert.setMessage("Scegliere un distributore");
                dlgAlert.setTitle("Accesso ai Ricavi");
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
                Intent form_Ricavi = new Intent(getApplicationContext(), EleRicaviActivity.class);
                form_Ricavi.putExtras(this.bundle);
                startActivity(form_Ricavi);
                finish();
                return;
            }
        }

        if (itemId == R.id.Monitoraggio_button) {
            Intent form_Monitoraggio = new Intent(getApplicationContext(), MonitoraggioActivity.class);
            form_Monitoraggio.putExtras(this.bundle);
            startActivity(form_Monitoraggio);
            finish();
            return;
        }

        if (itemId == R.id.Ricariche_button) {
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
                return;
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
                return;
            } else {
                Intent form_Ricariche = new Intent(getApplicationContext(), RicaricaActivity.class);
                form_Ricariche.putExtras(this.bundle);
                startActivity(form_Ricariche);
                finish();
                return;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Process.killProcess(Process.myPid());
            super.onDestroy();
        }
        return super.onKeyDown(keyCode, event);
    }
}

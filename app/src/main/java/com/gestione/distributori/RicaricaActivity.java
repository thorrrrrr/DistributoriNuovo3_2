package com.gestione.distributori;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.gestione.distributori.adapter.ListinoAdapter;
import com.gestione.distributori.adapter.ListinoItem;
import com.gestione.distributori.model.Categorie;
import com.gestione.distributori.model.ListiniClienti;
import com.gestione.distributori.persistence.local.DbAdapter;
import com.gestione.distributori.utilities.UI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RicaricaActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private String DescCategoria;
    private String DescCliente;
    private String DescMacchina;
    /* access modifiers changed from: private */
    public Bundle bundle = new Bundle();
    private DbAdapter dbHelper;
    /* access modifiers changed from: private */
    public String idArticolo;
    int idCategoria;
    private String idCliente;
    private String idMacchina;
    /* access modifiers changed from: private */
    public ListView listinoArticoli;
    int posCategoria;
    private Spinner spinnerCategorie;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getExtras();
        String utenza = "";
        String password = "";
        if (this.bundle != null) {
            utenza = this.bundle.getString("utenza");
            password = this.bundle.getString("password");
            this.idCliente = this.bundle.getString("idCliente");
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
        if (this.idCliente.equals("") || this.idMacchina.equals("")) {
            Intent form_MenuPrinc = new Intent(getApplicationContext(), MenuPrincipaleActivity.class);
            form_MenuPrinc.putExtras(this.bundle);
            startActivity(form_MenuPrinc);
            finish();
        }
        setContentView(R.layout.activity_ricarica);

        UI.InitToolbars(this, R.id.ricarica_toolbar);

        ((TextView) findViewById(R.id.formDescMacchinaListino_id)).setText(this.DescCliente + " - " + this.DescMacchina);
        ListinoAdapter listinoAdapter = new ListinoAdapter(getApplicationContext(), getArticoliListino());
        this.listinoArticoli = (ListView) findViewById(R.id.ListinoClienti);
        this.listinoArticoli.setAdapter(listinoAdapter);
        this.listinoArticoli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String unused = RicaricaActivity.this.idArticolo = ((ListinoItem) RicaricaActivity.this.listinoArticoli.getItemAtPosition(position)).getIdArticolo();
                RicaricaActivity.this.bundle.putString("idArticolo", String.valueOf(RicaricaActivity.this.idArticolo));
                if (!RicaricaActivity.this.idArticolo.equals("")) {
                    Intent form_InsRicarica = new Intent(RicaricaActivity.this.getApplicationContext(), InsRicaricaActivity.class);
                    form_InsRicarica.putExtras(RicaricaActivity.this.bundle);
                    RicaricaActivity.this.startActivity(form_InsRicarica);
                    RicaricaActivity.this.finish();
                }
            }
        });
        ((Button) findViewById(R.id.Exit_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.EleRic_button)).setOnClickListener(this);
        ((CheckBox) findViewById(R.id.Tutto_Listino)).setOnClickListener(this);
        this.spinnerCategorie = (Spinner) findViewById(R.id.spinnerCategoria);
        ArrayAdapter<SpinnerCategoria> categorieAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getCategorie());
        categorieAdapter.setDropDownViewResource(R.layout.spinner_item);
        this.spinnerCategorie.setAdapter(categorieAdapter);
        this.spinnerCategorie.setOnItemSelectedListener(this);
        this.spinnerCategorie.setPrompt("Scegli la Categoria");
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

    public ArrayList<ListinoItem> getArticoliListino() {
        ArrayList<ListinoItem> articoliList = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (ListiniClienti listinoCli : this.dbHelper.getListinoByIdClienteIdMacchina(this.idCliente, this.idMacchina, ((CheckBox) findViewById(R.id.Tutto_Listino)).isChecked())) {
            new ListiniClienti();
            if (this.idCategoria == -1) {
                articoliList.add(new ListinoItem(listinoCli.getNomeArticolo(), "Prezzo Vendita : " + listinoCli.getPrezzoVendita(), String.valueOf(listinoCli.getIdArticolo())));
            } else if (listinoCli.getidCategoria() == this.idCategoria) {
                articoliList.add(new ListinoItem(listinoCli.getNomeArticolo(), "Prezzo Vendita : " + listinoCli.getPrezzoVendita(), String.valueOf(listinoCli.getIdArticolo())));
            }
        }
        Collections.sort(articoliList, new Comparator<ListinoItem>() {
            public int compare(ListinoItem listinoItem, ListinoItem t1) {
                return listinoItem.getDescrizioneArt().toString().compareTo(t1.getDescrizioneArt().toString());
            }
        });
        return articoliList;
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

        if (itemId == R.id.EleRic_button) {
            Intent form_EleRic = new Intent(getApplicationContext(), EleRicaricheActivity.class);
            form_EleRic.putExtras(this.bundle);
            startActivity(form_EleRic);
            finish();
            return;
        }

        if (itemId == R.id.Tutto_Listino) {
            ListinoAdapter listinoAdapter = new ListinoAdapter(getApplicationContext(), getArticoliListino());
            this.listinoArticoli = (ListView) findViewById(R.id.ListinoClienti);
            this.listinoArticoli.setAdapter(listinoAdapter);
            this.listinoArticoli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String unused = RicaricaActivity.this.idArticolo = ((ListinoItem) RicaricaActivity.this.listinoArticoli.getItemAtPosition(position)).getIdArticolo();
                    RicaricaActivity.this.bundle.putString("idArticolo", String.valueOf(RicaricaActivity.this.idArticolo));
                    if (!RicaricaActivity.this.idArticolo.equals("")) {
                        Intent form_InsRicarica = new Intent(RicaricaActivity.this.getApplicationContext(), InsRicaricaActivity.class);
                        form_InsRicarica.putExtras(RicaricaActivity.this.bundle);
                        RicaricaActivity.this.startActivity(form_InsRicarica);
                        RicaricaActivity.this.finish();
                    }
                }
            });
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

    public class SpinnerCategoria {
        private int CategoriaId;
        private String CategoriaValue;

        public SpinnerCategoria(int CategoriaId2, String CategoriaValue2) {
            this.CategoriaId = CategoriaId2;
            this.CategoriaValue = CategoriaValue2;
        }

        public int getId() {
            return this.CategoriaId;
        }

        public String getValue() {
            return this.CategoriaValue;
        }

        public String toString() {
            return this.CategoriaValue;
        }
    }

    public List<SpinnerCategoria> getCategorie() {
        List<SpinnerCategoria> Spinnercategoria = new ArrayList<>();
        this.dbHelper = new DbAdapter(this);
        new ArrayList();
        for (Categorie categoria : this.dbHelper.getListaCategorie()) {
            new Categorie();
            if (categoria.getFlagVisibile().equals("S")) {
                Spinnercategoria.add(new SpinnerCategoria(categoria.getIdCategorie(), categoria.getDescCategorie()));
            }
        }
        return Spinnercategoria;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.getItemAtPosition(pos);

        int itemId = parent.getId();

        if (itemId == R.id.spinnerCategoria) {
            this.idCategoria = ((SpinnerCategoria) this.spinnerCategorie.getSelectedItem()).getId();
            this.posCategoria = this.spinnerCategorie.getSelectedItemPosition();
            this.bundle.putString("idCategoria", String.valueOf(this.idCategoria));
            this.bundle.putString("posCategoria", String.valueOf(this.posCategoria));
            this.DescCategoria = this.spinnerCategorie.getSelectedItem().toString();
            this.bundle.putString("DescCategoria", this.DescCategoria);
            if (this.idCategoria != 0) {
                ListinoAdapter listinoAdapter = new ListinoAdapter(getApplicationContext(), getArticoliListino());
                this.listinoArticoli = (ListView) findViewById(R.id.ListinoClienti);
                this.listinoArticoli.setAdapter(listinoAdapter);
                this.listinoArticoli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String unused = RicaricaActivity.this.idArticolo = ((ListinoItem) RicaricaActivity.this.listinoArticoli.getItemAtPosition(position)).getIdArticolo();
                        RicaricaActivity.this.bundle.putString("idArticolo", String.valueOf(RicaricaActivity.this.idArticolo));
                        if (!RicaricaActivity.this.idArticolo.equals("")) {
                            Intent form_InsRicarica = new Intent(RicaricaActivity.this.getApplicationContext(), InsRicaricaActivity.class);
                            form_InsRicarica.putExtras(RicaricaActivity.this.bundle);
                            RicaricaActivity.this.startActivity(form_InsRicarica);
                            RicaricaActivity.this.finish();
                        }
                    }
                });
                return;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}

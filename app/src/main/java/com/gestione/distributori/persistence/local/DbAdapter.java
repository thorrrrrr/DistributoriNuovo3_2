package com.gestione.distributori.persistence.local;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.gestione.distributori.model.Categorie;
import com.gestione.distributori.model.Cliente;
import com.gestione.distributori.model.InsManutenzione;
import com.gestione.distributori.model.InstallazioneMacchina;
import com.gestione.distributori.model.ListiniClienti;
import com.gestione.distributori.model.Macchina;
import com.gestione.distributori.model.Magazzino;
import com.gestione.distributori.model.Manutenzione;
import com.gestione.distributori.model.MonitoraggioDistributori;
import com.gestione.distributori.model.RicaricaMacchina;
import com.gestione.distributori.model.RicavoMacchina;
import com.gestione.distributori.model.TipiManutenzione;
import com.gestione.distributori.model.Utente;
import com.gestione.distributori.model.Settings;
import com.gestione.distributori.persistence.remote.DatabaseMySql;
import com.mysql.jdbc.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressLint("Range")
public class DbAdapter {
    private static final String DATABASE_NAME = "distributori.sqlite";
    private static final String KEY_ABILITATO = "abilitato";
    private static final String KEY_BATTUTECALDO = "nbattuteCaldo";
    private static final String KEY_BATTUTEFREDDO = "nbattute";
    private static final String KEY_CODPAUSAMATIC = "codPausamatic";
    private static final String KEY_CONSOLIDATO = "consolidato";
    private static final String KEY_COSTOACQUISTO = "costoAcquisto";
    private static final String KEY_COSTOARTICOLO = "costoArticolo";
    private static final String KEY_DATARICARICA = "dataRicarica";
    private static final String KEY_DATASCARICOSOLDI = "dataScaricoSoldi";
    private static final String KEY_DESCCATEGORIA = "desccategoria";
    private static final String KEY_DESCRIZIONE = "descrizione";
    private static final String KEY_DESCRIZIONEPOMANUTENZIONE = "stipomanutenzionedescrizione";
    private static final String KEY_FLAGVISIBILE = "flagvisibile";
    private static final String KEY_IDARTICOLO = "idArticolo";
    private static final String KEY_IDCATEGORIA = "idCategoria";
    private static final String KEY_IDCATEGORIE = "idcategoria";
    private static final String KEY_IDCLIENTE = "idCliente";
    private static final String KEY_IDMACCHINA = "idMacchina";
    private static final String KEY_IDMAGAZZINO = "idarticolo";
    private static final String KEY_IDMONITORAGGIO = "idmonitoraggio";
    private static final String KEY_IDRIGARICARICA = "idRigaRicarica";
    private static final String KEY_IDRIGARICAVO = "idRigaRicavo";
    private static final String KEY_IDSETTINGS = "idSettings";
    private static final String KEY_IDTIPOMACCHINA = "idtipomacchina";
    private static final String KEY_IDTIPOMANUTENZIONE = "idtipomanutenzione";
    private static final String KEY_IDUTENTE = "idUtente";
    private static final String KEY_KG = "KG";
    private static final String KEY_KGRESIDUA = "kgresidua";
    private static final String KEY_MARCA = "marca";
    private static final String KEY_MDATAMANUTENZIONE = "datamanutenzione";
    private static final String KEY_MDATASCADENZAMAN = "datascadenzaman";
    private static final String KEY_MDESCRIZIONE = "descrizione";
    private static final String KEY_MIDMACCHINA = "idmacchina";
    private static final String KEY_MIDMANUTENZIONE = "idmanutenzione";
    private static final String KEY_MIDTIPOMANUTENZIONE = "idtipomanutenzione";
    private static final String KEY_MNBATTUTE = "nbattute";
    private static final String KEY_MNBATTUTEATTUALI = "battuteattuali";
    private static final String KEY_MNBATTUTEMANUTENZIONE = "battutemanutenzione";
    private static final String KEY_MNDESCRIZIONE = "descrizione";
    private static final String KEY_MNIDMACCHINA = "idmacchina";
    private static final String KEY_MNIDTIPOMACCHINA = "idtipomacchina";
    private static final String KEY_MNIDTIPOMANUTENZIONE = "idtipomanutenzione";
    private static final String KEY_MNMODELLO = "modello";
    private static final String KEY_MNOTE = "note";
    private static final String KEY_MNRAGIONESOCIALE = "ragionesociale";
    private static final String KEY_MNSCADENZAMANUTENZIONE = "scadenzamanutenzione";
    private static final String KEY_MNTIPOMACCHINA = "tipomacchina";
    private static final String KEY_MODELLO = "modello";
    private static final String KEY_NBATTUTE = "nbattute";
    private static final String KEY_NGGPREAVVISO = "nggpreavviso";
    private static final String KEY_NGGSCADENZA = "nggscadenza";
    private static final String KEY_NOMEARTICOLO = "nomeArticolo";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PREZZOVENDITA = "prezzoVendita";
    private static final String KEY_QTACONFEZIONE = "qtaCnfezione";
    private static final String KEY_QTACONFEZIONIRESIDUA = "qtaconfezioneresidua";
    private static final String KEY_RAGIONESOCIALE = "ragioneSociale";
    private static final String KEY_SOLDISCARICATI = "soldiScaricati";
    private static final String KEY_TIPOLOGIA = "tipologiaArticolo";
    private static final String KEY_TIPOPROFILO = "tipoProfilo";
    private static final String KEY_TRASMESSO = "trasmesso";
    private static final String KEY_UTENTE = "utente";
    private static final String KEY_VALORE = "valore";
    private static final String LOG_TAG = DbAdapter.class.getSimpleName();
    private static final String TABLE_CATEGORIE = "categorie";
    private static final String TABLE_CLIENTI = "clienti";
    private static final String TABLE_INSMANUTENZIONE = "insmanutenzione";
    private static final String TABLE_INSTALLAZIONEMACCHINA = "installazionemacchina";
    private static final String TABLE_LISTINICLIENTI = "listiniclienti";
    private static final String TABLE_MACCHINA = "macchina";
    private static final String TABLE_MAGAZZINO = "magazzino";
    private static final String TABLE_MANUTENZIONE = "manutenzione";
    private static final String TABLE_MONITORAGGIO_MANUTENZIONE = "monitoraggiomanutenzione";
    private static final String TABLE_RICARICAMACCHINA = "ricaricamacchina";
    private static final String TABLE_RICAVOMACCHINA = "ricavomacchina";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_TIPI_MANUTENZIONE = "tbl_tipimanutenzione";
    private static final String TABLE_UTENTI = "utenti";
    private Context context;
    private SQLiteDatabase database;
    private DatabaseMySql databaseMySql;
    private SQLiteDatabase databaseRead;
    private DatabaseHelper dbHelper;

    public DbAdapter(Context context2) {
        this.context = context2;
    }

    public long createRicarica(RicaricaMacchina ricarica) {
        long ricarica_id = 0;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.database = this.dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_IDMACCHINA, Integer.valueOf(ricarica.getIdMacchina()));
            values.put(KEY_IDARTICOLO, Integer.valueOf(ricarica.getIdArticolo()));
            values.put(KEY_NOMEARTICOLO, ricarica.getNomeArticolo());
            values.put(KEY_QTACONFEZIONE, Integer.valueOf(ricarica.getQtaConfezione()));
            values.put(KEY_KG, Float.valueOf(ricarica.getKG()));
            values.put(KEY_DATARICARICA, ricarica.getDataRicarica());
            values.put(KEY_COSTOACQUISTO, Float.valueOf(ricarica.getCostoAcquisto()));
            values.put(KEY_PREZZOVENDITA, Float.valueOf(ricarica.getPrezzoVendita()));
            values.put(KEY_CONSOLIDATO, ricarica.getCondolidato());
            values.put(KEY_TRASMESSO, ricarica.getTrasmesso());
            ricarica_id = this.database.insert(TABLE_RICARICAMACCHINA, (String) null, values);
        } catch (SQLiteException e) {
        } catch (Exception e2) {
        } finally {
            this.dbHelper.close();
        }
        return ricarica_id;
    }

    public RicaricaMacchina getRicaricaMacchina(String ricarica_id) {
        String selectQuery = "SELECT  * FROM ricaricamacchina WHERE idRigaRicarica = " + ricarica_id;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                RicaricaMacchina ricarica = new RicaricaMacchina();
                ricarica.setIdRigaRicarica(c.getInt(c.getColumnIndex(KEY_IDRIGARICARICA)));
                ricarica.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                ricarica.setIdArticolo(c.getInt(c.getColumnIndex(KEY_IDARTICOLO)));
                if (c.isAfterLast()) {
                    ricarica.setNomeArticolo("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO))) {
                    ricarica.setNomeArticolo("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO)).equals("")) {
                    ricarica.setNomeArticolo("");
                } else {
                    ricarica.setNomeArticolo(c.getString(c.getColumnIndex(KEY_NOMEARTICOLO)));
                }
                ricarica.setQtaConfezione(c.getInt(c.getColumnIndex(KEY_QTACONFEZIONE)));
                ricarica.setKG(c.getFloat(c.getColumnIndex(KEY_KG)));
                if (c.isAfterLast()) {
                    ricarica.setDataRicarica("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_DATARICARICA))) {
                    ricarica.setDataRicarica("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_DATARICARICA)).equals("")) {
                    ricarica.setDataRicarica("");
                } else {
                    ricarica.setDataRicarica(c.getString(c.getColumnIndex(KEY_DATARICARICA)));
                }
                ricarica.setCostoAcquisto(c.getFloat(c.getColumnIndex(KEY_COSTOACQUISTO)));
                ricarica.setPrezzoVendita(c.getFloat(c.getColumnIndex(KEY_PREZZOVENDITA)));
                if (c.isAfterLast()) {
                    ricarica.setConsolidato("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CONSOLIDATO))) {
                    ricarica.setConsolidato("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_CONSOLIDATO)).equals("")) {
                    ricarica.setConsolidato("");
                } else {
                    ricarica.setConsolidato(c.getString(c.getColumnIndex(KEY_CONSOLIDATO)));
                }
                if (c.isAfterLast()) {
                    ricarica.setTrasmesso("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TRASMESSO))) {
                    ricarica.setTrasmesso("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_TRASMESSO)).equals("")) {
                    ricarica.setTrasmesso("");
                } else {
                    ricarica.setTrasmesso(c.getString(c.getColumnIndex(KEY_TRASMESSO)));
                }
                this.dbHelper.close();
                return ricarica;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<RicaricaMacchina> getRicaricheByIdMacchina(String idMacchina) {
        List<RicaricaMacchina> ricariche = new ArrayList<>();
        String selectQuery = "SELECT * FROM ricaricamacchina WHERE idMacchina = " + idMacchina + " AND " + KEY_CONSOLIDATO + " = 'N' " + "ORDER BY " + KEY_DATARICARICA + " DESC ";
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        RicaricaMacchina ricarica = new RicaricaMacchina();
                        ricarica.setIdRigaRicarica(c.getInt(c.getColumnIndex(KEY_IDRIGARICARICA)));
                        ricarica.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                        ricarica.setIdArticolo(c.getInt(c.getColumnIndex(KEY_IDARTICOLO)));
                        if (c.isAfterLast()) {
                            ricarica.setNomeArticolo("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO))) {
                            ricarica.setNomeArticolo("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO)).equals("")) {
                            ricarica.setNomeArticolo("");
                        } else {
                            ricarica.setNomeArticolo(c.getString(c.getColumnIndex(KEY_NOMEARTICOLO)));
                        }
                        ricarica.setQtaConfezione(c.getInt(c.getColumnIndex(KEY_QTACONFEZIONE)));
                        ricarica.setKG(c.getFloat(c.getColumnIndex(KEY_KG)));
                        if (c.isAfterLast()) {
                            ricarica.setDataRicarica("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_DATARICARICA))) {
                            ricarica.setDataRicarica("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_DATARICARICA)).equals("")) {
                            ricarica.setDataRicarica("");
                        } else {
                            ricarica.setDataRicarica(c.getString(c.getColumnIndex(KEY_DATARICARICA)));
                        }
                        ricarica.setCostoAcquisto(c.getFloat(c.getColumnIndex(KEY_COSTOACQUISTO)));
                        ricarica.setPrezzoVendita(c.getFloat(c.getColumnIndex(KEY_PREZZOVENDITA)));
                        if (c.isAfterLast()) {
                            ricarica.setConsolidato("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CONSOLIDATO))) {
                            ricarica.setConsolidato("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_CONSOLIDATO)).equals("")) {
                            ricarica.setConsolidato("");
                        } else {
                            ricarica.setConsolidato(c.getString(c.getColumnIndex(KEY_CONSOLIDATO)));
                        }
                        if (c.isAfterLast()) {
                            ricarica.setTrasmesso("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TRASMESSO))) {
                            ricarica.setTrasmesso("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_TRASMESSO)).equals("")) {
                            ricarica.setTrasmesso("");
                        } else {
                            ricarica.setTrasmesso(c.getString(c.getColumnIndex(KEY_TRASMESSO)));
                        }
                        ricariche.add(ricarica);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return ricariche;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<RicaricaMacchina> getRicaricheConsolidate() {
        List<RicaricaMacchina> ricariche = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT  * FROM ricaricamacchina WHERE consolidato = 'S'", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        RicaricaMacchina ricarica = new RicaricaMacchina();
                        ricarica.setIdRigaRicarica(c.getInt(c.getColumnIndex(KEY_IDRIGARICARICA)));
                        ricarica.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                        ricarica.setIdArticolo(c.getInt(c.getColumnIndex(KEY_IDARTICOLO)));
                        if (c.isAfterLast()) {
                            ricarica.setNomeArticolo("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO))) {
                            ricarica.setNomeArticolo("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO)).equals("")) {
                            ricarica.setNomeArticolo("");
                        } else {
                            ricarica.setNomeArticolo(c.getString(c.getColumnIndex(KEY_NOMEARTICOLO)));
                        }
                        ricarica.setQtaConfezione(c.getInt(c.getColumnIndex(KEY_QTACONFEZIONE)));
                        ricarica.setKG(c.getFloat(c.getColumnIndex(KEY_KG)));
                        if (c.isAfterLast()) {
                            ricarica.setDataRicarica("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_DATARICARICA))) {
                            ricarica.setDataRicarica("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_DATARICARICA)).equals("")) {
                            ricarica.setDataRicarica("");
                        } else {
                            ricarica.setDataRicarica(c.getString(c.getColumnIndex(KEY_DATARICARICA)));
                        }
                        ricarica.setCostoAcquisto(c.getFloat(c.getColumnIndex(KEY_COSTOACQUISTO)));
                        ricarica.setPrezzoVendita(c.getFloat(c.getColumnIndex(KEY_PREZZOVENDITA)));
                        if (c.isAfterLast()) {
                            ricarica.setConsolidato("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CONSOLIDATO))) {
                            ricarica.setConsolidato("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_CONSOLIDATO)).equals("")) {
                            ricarica.setConsolidato("");
                        } else {
                            ricarica.setConsolidato(c.getString(c.getColumnIndex(KEY_CONSOLIDATO)));
                        }
                        if (c.isAfterLast()) {
                            ricarica.setTrasmesso("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TRASMESSO))) {
                            ricarica.setTrasmesso("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_TRASMESSO)).equals("")) {
                            ricarica.setTrasmesso("");
                        } else {
                            ricarica.setTrasmesso(c.getString(c.getColumnIndex(KEY_TRASMESSO)));
                        }
                        ricariche.add(ricarica);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return ricariche;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public int consolidaRicarica(String ricarica_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONSOLIDATO, "S");
        return this.database.update(TABLE_RICARICAMACCHINA, values, "idRigaRicarica=" + ricarica_id, (String[]) null);
    }

    public int ricaricaTrasmessa(String ricarica_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TRASMESSO, "S");
        int id = this.database.update(TABLE_RICARICAMACCHINA, values, "idRigaRicarica=" + ricarica_id, (String[]) null);
        this.dbHelper.close();
        return id;
    }

    public int deleteRicarica(String ricarica_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_RICARICAMACCHINA, "idRigaRicarica = ?", new String[]{String.valueOf(ricarica_id)});
    }

    public int deleteRicaricheTrasmesse() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_RICARICAMACCHINA, "trasmesso=?", new String[]{"S"});
    }

    public int deleteAllRicariche() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_RICARICAMACCHINA, "", (String[]) null);
    }

//    public int deleteAllMonitoraggioManutenzione() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        return this.database.delete(TABLE_MONITORAGGIO_MANUTENZIONE, "", (String[]) null);
//    }
//    public int deleteAllTipiManutenzione() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        return this.database.delete(TABLE_TIPI_MANUTENZIONE, "", (String[]) null);
//    }
//    public int deleteAllManutenzione() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        return this.database.delete(TABLE_MANUTENZIONE, "", (String[]) null);
//    }

    public long createRicavo(RicavoMacchina ricavo) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDMACCHINA, Integer.valueOf(ricavo.getIdMacchina()));
        values.put(KEY_DATASCARICOSOLDI, ricavo.getDataScaricoSoldi());
        values.put(KEY_SOLDISCARICATI, Float.valueOf(ricavo.getSoldiScaricati()));
        values.put(KEY_CONSOLIDATO, ricavo.getCondolidato());
        values.put(KEY_TRASMESSO, ricavo.getTrasmesso());
        values.put("nbattute", Integer.valueOf(ricavo.getBattuteFreddo()));
        values.put(KEY_BATTUTECALDO, Integer.valueOf(ricavo.getBattuteCaldo()));
        return this.database.insert(TABLE_RICAVOMACCHINA, (String) null, values);
    }

    public long createMonitoraggioManutenzione(MonitoraggioDistributori monitoraggiodistributori) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MNRAGIONESOCIALE, monitoraggiodistributori.getRagioneSociale());
        values.put("modello", monitoraggiodistributori.getModello());
        values.put("idmacchina", monitoraggiodistributori.getIdmacchina());
        values.put("idtipomacchina", monitoraggiodistributori.getIdTipoMacchina());
        values.put(KEY_MNTIPOMACCHINA, monitoraggiodistributori.getTipoMacchina());
        values.put("idtipomanutenzione", monitoraggiodistributori.getIdTipomanutenzione());
        values.put("descrizione", monitoraggiodistributori.getDescrizione());
        values.put(KEY_MNBATTUTEATTUALI, monitoraggiodistributori.getBattuteAttuali());
        values.put(KEY_MNBATTUTEMANUTENZIONE, monitoraggiodistributori.getBattuteManutenzione());
        values.put(KEY_MNSCADENZAMANUTENZIONE, monitoraggiodistributori.getScadenzaManutenzione());
        return this.database.insert(TABLE_MONITORAGGIO_MANUTENZIONE, (String) null, values);
    }

    public long createManutenzione(Manutenzione manutenzione) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idmacchina", Integer.valueOf(manutenzione.getIdMacchina()));
        values.put(KEY_MDATAMANUTENZIONE, manutenzione.getDataManutenzione());
        values.put("descrizione", manutenzione.getDescrizione());
        values.put(KEY_MDATASCADENZAMAN, manutenzione.getDatascadenzaMan());
        values.put("nbattute", Integer.valueOf(manutenzione.getNBattute()));
        values.put(KEY_MNOTE, manutenzione.getNote());
        values.put("idtipomanutenzione", Integer.valueOf(manutenzione.getIDTipoManutenzione()));
        return this.database.insert(TABLE_MANUTENZIONE, (String) null, values);
    }

    public long createInsManutenzione(InsManutenzione insmanutenzione) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idmacchina", Integer.valueOf(insmanutenzione.getIdMacchina()));
        values.put(KEY_MDATAMANUTENZIONE, insmanutenzione.getDataManutenzione());
        values.put("descrizione", insmanutenzione.getDescrizione());
        values.put(KEY_MDATASCADENZAMAN, insmanutenzione.getDatascadenzaMan());
        values.put("nbattute", Integer.valueOf(insmanutenzione.getNBattute()));
        values.put(KEY_MNOTE, insmanutenzione.getNote());
        values.put("idtipomanutenzione", Integer.valueOf(insmanutenzione.getIDTipoManutenzione()));
        values.put(KEY_TRASMESSO, insmanutenzione.getTrasmesso());
        return this.database.insert(TABLE_INSMANUTENZIONE, (String) null, values);
    }

    public long createTipoManutenzione(TipiManutenzione tipiManutenzione) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idtipomanutenzione", Integer.valueOf(tipiManutenzione.getIdTipoManutenzione()));
        values.put(KEY_DESCRIZIONEPOMANUTENZIONE, tipiManutenzione.getTipoManutenzioneDescrizione());
        values.put("nbattute", tipiManutenzione.getNBattute());
        values.put(KEY_NGGSCADENZA, tipiManutenzione.getNGgScadenza());
        values.put(KEY_NGGPREAVVISO, tipiManutenzione.getNGgPreavviso());
        values.put("idtipomacchina", tipiManutenzione.getIdTipoMacchina());
        return this.database.insert(TABLE_TIPI_MANUTENZIONE, (String) null, values);
    }

    @SuppressLint("Range")
    public RicavoMacchina getRicavoMacchina(String ricavo_id) {
        String selectQuery = "SELECT  * FROM ricavomacchina WHERE idRigaRicavo = " + ricavo_id;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                RicavoMacchina ricavo = new RicavoMacchina();
                ricavo.setIdRigaRicavo(c.getInt(c.getColumnIndex(KEY_IDRIGARICAVO)));
                ricavo.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                if (c.isAfterLast()) {
                    ricavo.setDataScaricoSoldi("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_DATASCARICOSOLDI))) {
                    ricavo.setDataScaricoSoldi("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_DATASCARICOSOLDI)).equals("")) {
                    ricavo.setDataScaricoSoldi("");
                } else {
                    ricavo.setDataScaricoSoldi(c.getString(c.getColumnIndex(KEY_DATASCARICOSOLDI)));
                }
                ricavo.setSoldiScaricati(c.getFloat(c.getColumnIndex(KEY_SOLDISCARICATI)));
                if (c.isAfterLast()) {
                    ricavo.setConsolidato("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CONSOLIDATO))) {
                    ricavo.setConsolidato("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_CONSOLIDATO)).equals("")) {
                    ricavo.setConsolidato("");
                } else {
                    ricavo.setConsolidato(c.getString(c.getColumnIndex(KEY_CONSOLIDATO)));
                }
                if (c.isAfterLast()) {
                    ricavo.setTrasmesso("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TRASMESSO))) {
                    ricavo.setTrasmesso("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_TRASMESSO)).equals("")) {
                    ricavo.setTrasmesso("");
                } else {
                    ricavo.setTrasmesso(c.getString(c.getColumnIndex(KEY_TRASMESSO)));
                }
                ricavo.setBattuteCaldo(c.getInt(c.getColumnIndex(KEY_BATTUTECALDO)));
                ricavo.setBattuteFreddo(c.getInt(c.getColumnIndex("nbattute")));
                this.dbHelper.close();
                return ricavo;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<RicavoMacchina> getRicaviByIdMacchina(String idMacchina) {
        List<RicavoMacchina> ricavi = new ArrayList<>();
        String selectQuery = "SELECT  * FROM ricavomacchina WHERE idMacchina = " + idMacchina + " AND " + KEY_CONSOLIDATO + " = 'N' " + " ORDER BY " + KEY_DATASCARICOSOLDI + " DESC ";
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        RicavoMacchina ricavo = new RicavoMacchina();
                        ricavo.setIdRigaRicavo(c.getInt(c.getColumnIndex(KEY_IDRIGARICAVO)));
                        ricavo.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                        if (c.isAfterLast()) {
                            ricavo.setDataScaricoSoldi("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_DATASCARICOSOLDI))) {
                            ricavo.setDataScaricoSoldi("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_DATASCARICOSOLDI)).equals("")) {
                            ricavo.setDataScaricoSoldi("");
                        } else {
                            ricavo.setDataScaricoSoldi(c.getString(c.getColumnIndex(KEY_DATASCARICOSOLDI)));
                        }
                        ricavo.setSoldiScaricati(c.getFloat(c.getColumnIndex(KEY_SOLDISCARICATI)));
                        if (c.isAfterLast()) {
                            ricavo.setConsolidato("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CONSOLIDATO))) {
                            ricavo.setConsolidato("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_CONSOLIDATO)).equals("")) {
                            ricavo.setConsolidato("");
                        } else {
                            ricavo.setConsolidato(c.getString(c.getColumnIndex(KEY_CONSOLIDATO)));
                        }
                        if (c.isAfterLast()) {
                            ricavo.setTrasmesso("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TRASMESSO))) {
                            ricavo.setTrasmesso("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_TRASMESSO)).equals("")) {
                            ricavo.setTrasmesso("");
                        } else {
                            ricavo.setTrasmesso(c.getString(c.getColumnIndex(KEY_TRASMESSO)));
                        }
                        ricavo.setBattuteCaldo(c.getInt(c.getColumnIndex(KEY_BATTUTECALDO)));
                        ricavo.setBattuteFreddo(c.getInt(c.getColumnIndex("nbattute")));
                        ricavi.add(ricavo);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return ricavi;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<MonitoraggioDistributori> getMonitoraggioDistributori() {
        List<MonitoraggioDistributori> listmonitoraggio = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT  * FROM monitoraggiomanutenzione", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        MonitoraggioDistributori monitoraggio = new MonitoraggioDistributori();
                        monitoraggio.setRagioneSociale(c.getString(c.getColumnIndex(KEY_MNRAGIONESOCIALE)));
                        monitoraggio.setModello(c.getString(c.getColumnIndex("modello")));
                        monitoraggio.setIdMacchina(c.getString(c.getColumnIndex("idmacchina")));
                        monitoraggio.setIdTipoMacchina(c.getString(c.getColumnIndex("idtipomacchina")));
                        monitoraggio.setTipoMacchina(c.getString(c.getColumnIndex(KEY_MNTIPOMACCHINA)));
                        monitoraggio.setIdTipomanutenzione(c.getString(c.getColumnIndex("idtipomanutenzione")));
                        monitoraggio.setDescrizione(c.getString(c.getColumnIndex("descrizione")));
                        monitoraggio.setBattuteAttuali(c.getString(c.getColumnIndex(KEY_MNBATTUTEATTUALI)));
                        monitoraggio.setBattuteManutenzione(c.getString(c.getColumnIndex(KEY_MNBATTUTEMANUTENZIONE)));
                        monitoraggio.setScadenzaManutenzione(c.getString(c.getColumnIndex(KEY_MNSCADENZAMANUTENZIONE)));
                        listmonitoraggio.add(monitoraggio);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return listmonitoraggio;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<TipiManutenzione> getTipiManutenzione() {
        List<TipiManutenzione> listtipimanutenzione = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT  * FROM tbl_tipimanutenzione", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        TipiManutenzione tipiManutenzione = new TipiManutenzione();
                        tipiManutenzione.setIdTipoManutenzione(c.getInt(c.getColumnIndex("idtipomanutenzione")));
                        tipiManutenzione.setTipoManutenzioneDescrizione(c.getString(c.getColumnIndex(KEY_DESCRIZIONEPOMANUTENZIONE)));
                        tipiManutenzione.setNBattute(c.getString(c.getColumnIndex("nbattute")));
                        tipiManutenzione.setNGgScadenza(c.getString(c.getColumnIndex(KEY_NGGSCADENZA)));
                        tipiManutenzione.setNGgPreavviso(c.getString(c.getColumnIndex(KEY_NGGPREAVVISO)));
                        tipiManutenzione.setIdTipoMacchina(c.getString(c.getColumnIndex("idtipomacchina")));
                        listtipimanutenzione.add(tipiManutenzione);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return listtipimanutenzione;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<InsManutenzione> getInsManutenzioneConsolidati() {
        List<InsManutenzione> insManutenziones = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT  * FROM insmanutenzione WHERE trasmesso = 'N'", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        InsManutenzione insmanutenzione = new InsManutenzione();
                        insmanutenzione.setIdManutenzione(c.getInt(c.getColumnIndex(KEY_MIDMANUTENZIONE)));
                        insmanutenzione.setIdMacchina(c.getInt(c.getColumnIndex("idmacchina")));
                        insmanutenzione.setDataManutenzione(c.getString(c.getColumnIndex(KEY_MDATAMANUTENZIONE)));
                        insmanutenzione.setDatascadenzaMan(c.getString(c.getColumnIndex(KEY_MDATASCADENZAMAN)));
                        insmanutenzione.setNBattute(c.getInt(c.getColumnIndex("nbattute")));
                        insmanutenzione.setDescrizione(c.getString(c.getColumnIndex("descrizione")));
                        insmanutenzione.setNote(c.getString(c.getColumnIndex(KEY_MNOTE)));
                        insmanutenzione.setIDTipoManutenzione(c.getInt(c.getColumnIndex("idtipomanutenzione")));
                        insManutenziones.add(insmanutenzione);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return insManutenziones;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public int consolidaRicavo(String ricavo_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONSOLIDATO, "S");
        return this.database.update(TABLE_RICAVOMACCHINA, values, "idRigaRicavo=" + ricavo_id, (String[]) null);
    }

    public List<RicavoMacchina> getRicaviConsolidati() {
        List<RicavoMacchina> ricavi = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT  * FROM ricavomacchina WHERE consolidato = 'S'", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        RicavoMacchina ricavo = new RicavoMacchina();
                        ricavo.setIdRigaRicavo(c.getInt(c.getColumnIndex(KEY_IDRIGARICAVO)));
                        ricavo.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                        if (c.isAfterLast()) {
                            ricavo.setDataScaricoSoldi("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_DATASCARICOSOLDI))) {
                            ricavo.setDataScaricoSoldi("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_DATASCARICOSOLDI)).equals("")) {
                            ricavo.setDataScaricoSoldi("");
                        } else {
                            ricavo.setDataScaricoSoldi(c.getString(c.getColumnIndex(KEY_DATASCARICOSOLDI)));
                        }
                        ricavo.setSoldiScaricati(c.getFloat(c.getColumnIndex(KEY_SOLDISCARICATI)));
                        if (c.isAfterLast()) {
                            ricavo.setConsolidato("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CONSOLIDATO))) {
                            ricavo.setConsolidato("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_CONSOLIDATO)).equals("")) {
                            ricavo.setConsolidato("");
                        } else {
                            ricavo.setConsolidato(c.getString(c.getColumnIndex(KEY_CONSOLIDATO)));
                        }
                        if (c.isAfterLast()) {
                            ricavo.setTrasmesso("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TRASMESSO))) {
                            ricavo.setTrasmesso("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_TRASMESSO)).equals("")) {
                            ricavo.setTrasmesso("");
                        } else {
                            ricavo.setTrasmesso(c.getString(c.getColumnIndex(KEY_TRASMESSO)));
                        }
                        ricavo.setBattuteCaldo(c.getInt(c.getColumnIndex(KEY_BATTUTECALDO)));
                        ricavo.setBattuteFreddo(c.getInt(c.getColumnIndex("nbattute")));
                        ricavi.add(ricavo);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return ricavi;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public int manutenzioneTrasmesso(String insmanutenzione_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TRASMESSO, "S");
        return this.database.update(TABLE_INSMANUTENZIONE, values, "idmanutenzione=" + insmanutenzione_id, (String[]) null);
    }

    public int deleteInsManutenzione(String insmanutenzione_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_INSMANUTENZIONE, "idmanutenzione = ?", new String[]{String.valueOf(insmanutenzione_id)});
    }

    public int deleteManutenzioneTrasmessi() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_INSMANUTENZIONE, "trasmesso=?", new String[]{"S"});
    }

    public int deleteAllInsManutenzione() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_INSMANUTENZIONE, "", (String[]) null);
    }

    public int ricavoTrasmesso(String ricavo_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TRASMESSO, "S");
        return this.database.update(TABLE_RICAVOMACCHINA, values, "idRigaRicavo=" + ricavo_id, (String[]) null);
    }

    public int deleteRicavo(String ricavo_id) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_RICAVOMACCHINA, "idRigaRicavo = ?", new String[]{String.valueOf(ricavo_id)});
    }

    public int deleteRicaviTrasmessi() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_RICAVOMACCHINA, "trasmesso=?", new String[]{"S"});
    }

    public int deleteAllRicavi() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_RICAVOMACCHINA, "", (String[]) null);
    }

    public long createRigaListino(ListiniClienti listino) {
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.database = this.dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_IDCLIENTE, Integer.valueOf(listino.getIdCliente()));
            values.put(KEY_IDMACCHINA, Integer.valueOf(listino.getIdMacchina()));
            values.put(KEY_IDARTICOLO, Integer.valueOf(listino.getIdArticolo()));
            values.put(KEY_NOMEARTICOLO, listino.getNomeArticolo());
            values.put(KEY_COSTOARTICOLO, Float.valueOf(listino.getCostoArticolo()));
            values.put(KEY_PREZZOVENDITA, Float.valueOf(listino.getPrezzoVendita()));
            values.put(KEY_TIPOLOGIA, listino.getTipologia());
            long rigaListino_id = this.database.insert(TABLE_LISTINICLIENTI, (String) null, values);
            this.database.setTransactionSuccessful();
            if (this.database != null) {
                this.database.endTransaction();
                if (this.database.isOpen()) {
                    this.database.close();
                }
            }
            return rigaListino_id;
        } catch (Throwable th) {
            if (this.database != null) {
                this.database.endTransaction();
                if (this.database.isOpen()) {
                    this.database.close();
                }
            }
            throw th;
        }
    }

    public ListiniClienti getRigaListino(String idCliente, String idMacchina, String idArticolo) {
        String selectQuery = "SELECT  * FROM listiniclienti WHERE idCliente = " + idCliente + " AND " + KEY_IDMACCHINA + " = " + idMacchina + " AND " + KEY_IDARTICOLO + " = " + idArticolo;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                ListiniClienti rigaListino = new ListiniClienti();
                rigaListino.setIdCliente(c.getInt(c.getColumnIndex(KEY_IDCLIENTE)));
                rigaListino.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                rigaListino.setIdArticolo(c.getInt(c.getColumnIndex(KEY_IDARTICOLO)));
                if (c.isAfterLast()) {
                    rigaListino.setNomeArticolo("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO))) {
                    rigaListino.setNomeArticolo("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO)).equals("")) {
                    rigaListino.setNomeArticolo("");
                } else {
                    rigaListino.setNomeArticolo(c.getString(c.getColumnIndex(KEY_NOMEARTICOLO)));
                }
                rigaListino.setCostoArticolo(c.getFloat(c.getColumnIndex(KEY_COSTOARTICOLO)));
                rigaListino.setPrezzoVendita(c.getFloat(c.getColumnIndex(KEY_PREZZOVENDITA)));
                if (c.isAfterLast()) {
                    rigaListino.setTipologia("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TIPOLOGIA))) {
                    rigaListino.setTipologia("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_TIPOLOGIA)).equals("")) {
                    rigaListino.setTipologia("");
                } else {
                    rigaListino.setTipologia(c.getString(c.getColumnIndex(KEY_TIPOLOGIA)));
                }
                rigaListino.setIdCategoria(c.getInt(c.getColumnIndex(KEY_IDCATEGORIA)));
                this.dbHelper.close();
                return rigaListino;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<ListiniClienti> getListinoByIdClienteIdMacchina(String idCliente, String idMacchina, boolean TuttoListino) {
        String selectQuery;
        List<ListiniClienti> listino = new ArrayList<>();
        if (TuttoListino) {
            selectQuery = "SELECT  * FROM listiniclienti WHERE idCliente = " + idCliente + " AND " + KEY_IDMACCHINA + " = " + idMacchina + " ORDER BY " + KEY_IDARTICOLO;
        } else {
            selectQuery = "SELECT  DISTINCT listiniclienti.* FROM listiniclienti inner join magazzino on listiniclienti.idArticolo=magazzino.idarticolo inner join categorie on listiniclienti.idCategoria=categorie.idcategoria WHERE idCliente = " + idCliente + " AND " + KEY_IDMACCHINA + " = " + idMacchina + " AND  (((" + TABLE_MAGAZZINO + "." + KEY_QTACONFEZIONIRESIDUA + ">0 or " + TABLE_MAGAZZINO + "." + KEY_KGRESIDUA + ">0 ) AND " + TABLE_LISTINICLIENTI + ".PREZZOVENDITA >0 )or " + TABLE_CATEGORIE + "." + KEY_DESCCATEGORIA + "='Accessori')" + " ORDER BY " + TABLE_LISTINICLIENTI + "." + KEY_IDARTICOLO;
        }
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        ListiniClienti rigaListino = new ListiniClienti();
                        rigaListino.setIdCliente(c.getInt(c.getColumnIndex(KEY_IDCLIENTE)));
                        rigaListino.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                        rigaListino.setIdArticolo(c.getInt(c.getColumnIndex(KEY_IDARTICOLO)));
                        if (c.isAfterLast()) {
                            rigaListino.setNomeArticolo("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO))) {
                            rigaListino.setNomeArticolo("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_NOMEARTICOLO)).equals("")) {
                            rigaListino.setNomeArticolo("");
                        } else {
                            rigaListino.setNomeArticolo(c.getString(c.getColumnIndex(KEY_NOMEARTICOLO)));
                        }
                        rigaListino.setCostoArticolo(c.getFloat(c.getColumnIndex(KEY_COSTOARTICOLO)));
                        rigaListino.setPrezzoVendita(c.getFloat(c.getColumnIndex(KEY_PREZZOVENDITA)));
                        if (c.isAfterLast()) {
                            rigaListino.setTipologia("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TIPOLOGIA))) {
                            rigaListino.setTipologia("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_TIPOLOGIA)).equals("")) {
                            rigaListino.setTipologia("");
                        } else {
                            rigaListino.setTipologia(c.getString(c.getColumnIndex(KEY_TIPOLOGIA)));
                        }
                        rigaListino.setIdCategoria(c.getInt(c.getColumnIndex(KEY_IDCATEGORIA)));
                        listino.add(rigaListino);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return listino;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public int deleteListino(String idCliente, String idMacchina) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_LISTINICLIENTI, "idCliente=? AND idMacchina=?", new String[]{idCliente, idMacchina});
    }

//    public void deleteAllListini() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM listiniclienti");
//    }

    public long createCliente(Cliente cliente) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDCLIENTE, Integer.valueOf(cliente.getIdCliente()));
        values.put(KEY_RAGIONESOCIALE, cliente.getRagioneSociale());
        long cliente_id = this.database.insert(TABLE_CLIENTI, (String) null, values);
        this.database.close();
        this.dbHelper.close();
        return cliente_id;
    }

    public Cliente getCliente(String idCliente) {
        String selectQuery = "SELECT  * FROM clienti WHERE idCliente = " + idCliente;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                Cliente cliente = new Cliente();
                cliente.setIdCliente(c.getInt(c.getColumnIndex(KEY_IDCLIENTE)));
                if (c.isAfterLast()) {
                    cliente.setRagioneSociale("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_RAGIONESOCIALE))) {
                    cliente.setRagioneSociale("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_RAGIONESOCIALE)).equals("")) {
                    cliente.setRagioneSociale("");
                } else {
                    cliente.setRagioneSociale(c.getString(c.getColumnIndex(KEY_RAGIONESOCIALE)));
                }
                this.dbHelper.close();
                return cliente;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<Cliente> getListaClienti() {
        List<Cliente> clienti = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT  * FROM clienti ORDER BY ragioneSociale", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Cliente cliente = new Cliente();
                        cliente.setIdCliente(c.getInt(c.getColumnIndex(KEY_IDCLIENTE)));
                        if (c.isAfterLast()) {
                            cliente.setRagioneSociale("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_RAGIONESOCIALE))) {
                            cliente.setRagioneSociale("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_RAGIONESOCIALE)).equals("")) {
                            cliente.setRagioneSociale("");
                        } else {
                            cliente.setRagioneSociale(c.getString(c.getColumnIndex(KEY_RAGIONESOCIALE)));
                        }
                        clienti.add(cliente);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return clienti;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public int deleteClienti(String idCliente) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_LISTINICLIENTI, "idCliente=?", new String[]{idCliente});
    }

//    public void deleteAllClienti() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM clienti");
//    }
//    public void deleteAllCategorie() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM categorie");
//    }

    public long createMacchina(Macchina macchina) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDMACCHINA, Integer.valueOf(macchina.getIdMacchina()));
        values.put(KEY_MARCA, macchina.getMarca());
        values.put("modello", macchina.getModello());
        values.put(KEY_CODPAUSAMATIC, macchina.getCodice());
        long macchina_id = this.database.insert(TABLE_MACCHINA, (String) null, values);
        this.database.close();
        this.dbHelper.close();
        return macchina_id;
    }

    public Macchina getMacchina(String idMacchina) {
        String selectQuery = "SELECT  * FROM macchina WHERE idMacchina = " + idMacchina;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                Macchina macchina = new Macchina();
                macchina.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                if (c.isAfterLast()) {
                    macchina.setMarca("");
                } else if (c.isNull(c.getColumnIndexOrThrow("modello"))) {
                    macchina.setMarca("");
                } else if (c.getString(c.getColumnIndexOrThrow("modello")).equals("")) {
                    macchina.setMarca("");
                } else {
                    macchina.setMarca(c.getString(c.getColumnIndex("modello")));
                }
                if (c.isAfterLast()) {
                    macchina.setModello("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_MARCA))) {
                    macchina.setModello("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_MARCA)).equals("")) {
                    macchina.setModello("");
                } else {
                    macchina.setModello(c.getString(c.getColumnIndex(KEY_MARCA)));
                }
                if (c.isAfterLast()) {
                    macchina.setCodice("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CODPAUSAMATIC))) {
                    macchina.setCodice("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_CODPAUSAMATIC)).equals("")) {
                    macchina.setCodice("");
                } else {
                    macchina.setCodice(c.getString(c.getColumnIndex(KEY_CODPAUSAMATIC)));
                }
                this.dbHelper.close();
                return macchina;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<Macchina> getListaMacchineCliente(String idCliente) {
        List<Macchina> macchine = new ArrayList<>();
        String selectQuery = "SELECT a.* FROM macchina a INNER JOIN installazionemacchina b ON a.idMacchina = b.idMacchina WHERE b.idCliente= " + idCliente + " " + "ORDER BY " + KEY_MARCA;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Macchina macchina = new Macchina();
                        macchina.setIdMacchina(c.getInt(c.getColumnIndex(KEY_IDMACCHINA)));
                        if (c.isAfterLast()) {
                            macchina.setMarca("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_MARCA))) {
                            macchina.setMarca("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_MARCA)).equals("")) {
                            macchina.setMarca("");
                        } else {
                            macchina.setMarca(c.getString(c.getColumnIndex(KEY_MARCA)));
                        }
                        if (c.isAfterLast()) {
                            macchina.setModello("");
                        } else if (c.isNull(c.getColumnIndexOrThrow("modello"))) {
                            macchina.setModello("");
                        } else if (c.getString(c.getColumnIndexOrThrow("modello")).equals("")) {
                            macchina.setModello("");
                        } else {
                            macchina.setModello(c.getString(c.getColumnIndex("modello")));
                        }
                        if (c.isAfterLast()) {
                            macchina.setCodice("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_CODPAUSAMATIC))) {
                            macchina.setCodice("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_CODPAUSAMATIC)).equals("")) {
                            macchina.setCodice("");
                        } else {
                            macchina.setCodice(c.getString(c.getColumnIndex(KEY_CODPAUSAMATIC)));
                        }
                        macchine.add(macchina);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return macchine;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public List<Categorie> getListaCategorie() {
        List<Categorie> categorie = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT * FROM categorie ORDER BY idcategoria", (String[]) null);
            if (c != null) {
                Categorie categoriaTot = new Categorie();
                categoriaTot.setIdCategorie(-1);
                categoriaTot.setDescCategorie("Tutte");
                categoriaTot.setFlagVisibile("S");
                categorie.add(categoriaTot);
                if (c.moveToFirst()) {
                    do {
                        Categorie categoria = new Categorie();
                        categoria.setIdCategorie(c.getInt(c.getColumnIndex(KEY_IDCATEGORIE)));
                        categoria.setDescCategorie(c.getString(c.getColumnIndex(KEY_DESCCATEGORIA)));
                        categoria.setFlagVisibile(c.getString(c.getColumnIndex(KEY_FLAGVISIBILE)));
                        categorie.add(categoria);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return categorie;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public int deleteMacchina(String idMacchina) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this.database.delete(TABLE_MACCHINA, "idMacchina=?", new String[]{idMacchina});
    }

//    public void deleteAllMacchine() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM macchina");
//    }

    public long createInstallazioneMacchina(InstallazioneMacchina installazioneMacchina) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDMACCHINA, Integer.valueOf(installazioneMacchina.getIdMacchina()));
        values.put(KEY_IDCLIENTE, Integer.valueOf(installazioneMacchina.getidCliente()));
        long installazioneMacchina_id = this.database.insert(TABLE_INSTALLAZIONEMACCHINA, (String) null, values);
        this.database.close();
        this.dbHelper.close();
        return installazioneMacchina_id;
    }

//    public void deleteAllInstallazioneMacchine() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM installazionemacchina");
//    }

    public long createUtente(Utente utente) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDUTENTE, Integer.valueOf(utente.getIdUtente()));
        values.put(KEY_UTENTE, utente.getUtenza());
        values.put(KEY_PASSWORD, utente.getPassword());
        values.put(KEY_TIPOPROFILO, utente.getTipoProfilo());
        values.put(KEY_ABILITATO, utente.getAbilitato());
        long utente_id = this.database.insert(TABLE_UTENTI, (String) null, values);
        this.database.close();
        this.dbHelper.close();
        return utente_id;
    }

    public Utente getUtente(String utenza, String password) {
        String selectQuery = "SELECT  * FROM utenti WHERE utente = " + utenza + " AND " + KEY_PASSWORD + " = " + password;
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                Utente utente = new Utente();
                utente.setIdUtente(c.getInt(c.getColumnIndex(KEY_IDUTENTE)));
                if (c.isAfterLast()) {
                    utente.setUtenza("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_UTENTE))) {
                    utente.setUtenza("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_UTENTE)).equals("")) {
                    utente.setUtenza("");
                } else {
                    utente.setUtenza(c.getString(c.getColumnIndex(KEY_UTENTE)));
                }
                if (c.isAfterLast()) {
                    utente.setPassword("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_PASSWORD))) {
                    utente.setPassword("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_PASSWORD)).equals("")) {
                    utente.setPassword("");
                } else {
                    utente.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                }
                if (c.isAfterLast()) {
                    utente.setTipoProfilo("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TIPOPROFILO))) {
                    utente.setTipoProfilo("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_TIPOPROFILO)).equals("")) {
                    utente.setTipoProfilo("");
                } else {
                    utente.setTipoProfilo(c.getString(c.getColumnIndex(KEY_TIPOPROFILO)));
                }
                if (c.isAfterLast()) {
                    utente.setAbilitato("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_ABILITATO))) {
                    utente.setAbilitato("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_ABILITATO)).equals("")) {
                    utente.setAbilitato("");
                } else {
                    utente.setAbilitato(c.getString(c.getColumnIndex(KEY_ABILITATO)));
                }
                this.dbHelper.close();
                return utente;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

//    public void deleteAllUtenti() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM utenti");
//    }
//    public void deleteAllMagazzino() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM magazzino");
//    }

    public long createCategoria(Categorie categoria) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDCATEGORIE, Integer.valueOf(categoria.getIdCategorie()));
        values.put(KEY_DESCCATEGORIA, categoria.getDescCategorie());
        values.put(KEY_FLAGVISIBILE, categoria.getFlagVisibile());
        long categoria_id = this.database.insert(TABLE_CATEGORIE, (String) null, values);
        this.database.close();
        this.dbHelper.close();
        return categoria_id;
    }

    public long createSettings(Settings setting) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descrizione", setting.getDescrizione());
        values.put(KEY_VALORE, setting.getValore());
        return this.database.insert(TABLE_SETTINGS, (String) null, values);
    }

    public int updateSettings(String descrizione, String valore) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALORE, valore);
        return this.database.update(TABLE_SETTINGS, values, "descrizione='" + descrizione + "'", (String[]) null);
    }

    public List<Settings> getAllSettings() {
        List<Settings> settings = new ArrayList<>();
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery("SELECT idSettings, descrizione, valore FROM settings", (String[]) null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Settings setting = new Settings();
                        setting.setIdSettings(c.getInt(c.getColumnIndex(KEY_IDSETTINGS)));
                        setting.setDescrizione(c.getString(c.getColumnIndex("descrizione")));
                        if (c.isAfterLast()) {
                            setting.setValore("");
                        } else if (c.isNull(c.getColumnIndexOrThrow(KEY_VALORE))) {
                            setting.setValore("");
                        } else if (c.getString(c.getColumnIndexOrThrow(KEY_VALORE)).equals("")) {
                            setting.setValore("");
                        } else {
                            setting.setValore(c.getString(c.getColumnIndex(KEY_VALORE)));
                        }
                        settings.add(setting);
                    } while (c.moveToNext());
                }
                this.dbHelper.close();
                return settings;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public Settings getSettings(String descrizione) {
        String selectQuery = "SELECT idSettings, descrizione, valore FROM settings WHERE descrizione = '" + descrizione + "'";
        try {
            this.dbHelper = new DatabaseHelper(this.context);
            this.databaseRead = this.dbHelper.getReadableDatabase();
            Cursor c = this.databaseRead.rawQuery(selectQuery, (String[]) null);
            if (c != null) {
                c.moveToFirst();
                Settings setting = new Settings();
                setting.setIdSettings(c.getInt(c.getColumnIndex(KEY_IDSETTINGS)));
                setting.setDescrizione(c.getString(c.getColumnIndex("descrizione")));
                if (c.isAfterLast()) {
                    setting.setValore("");
                } else if (c.isNull(c.getColumnIndexOrThrow(KEY_VALORE))) {
                    setting.setValore("");
                } else if (c.getString(c.getColumnIndexOrThrow(KEY_VALORE)).equals("")) {
                    setting.setValore("");
                } else {
                    setting.setValore(c.getString(c.getColumnIndex(KEY_VALORE)));
                }
                this.dbHelper.close();
                return setting;
            }
            this.dbHelper.close();
            return null;
        } catch (SQLiteException e) {
            this.dbHelper.close();
            return null;
        } catch (Exception e2) {
            this.dbHelper.close();
            return null;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

//    public void deleteAllSettings() {
//        this.dbHelper = new DatabaseHelper(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        this.database.execSQL("DELETE FROM settings");
//    }

    public Utente loginApplicazione(String utenza, String password) {
        Utente utente = null;
        this.dbHelper = new DatabaseHelper(this.context);
        this.databaseRead = this.dbHelper.getReadableDatabase();
        Cursor c = this.databaseRead.rawQuery("SELECT idUtente, utente, password, tipoProfilo, abilitato FROM utenti WHERE utente = '" + utenza + "' AND " + KEY_PASSWORD + " = '" + password + "'", (String[]) null);
        if (c != null) {
            utente = new Utente();
            c.moveToFirst();
            if (c.isAfterLast()) {
                utente.setIdUtente(0);
            } else if (c.isNull(c.getColumnIndexOrThrow(KEY_IDUTENTE))) {
                utente.setIdUtente(0);
            } else if (c.getInt(c.getColumnIndexOrThrow(KEY_IDUTENTE)) == 0) {
                utente.setIdUtente(0);
            } else {
                utente.setIdUtente(c.getInt(c.getColumnIndex(KEY_IDUTENTE)));
            }
            if (c.isAfterLast()) {
                utente.setUtenza("");
            } else if (c.isNull(c.getColumnIndexOrThrow(KEY_UTENTE))) {
                utente.setUtenza("");
            } else if (c.getString(c.getColumnIndexOrThrow(KEY_UTENTE)).equals("")) {
                utente.setUtenza("");
            } else {
                utente.setUtenza(c.getString(c.getColumnIndex(KEY_UTENTE)));
            }
            if (c.isAfterLast()) {
                utente.setPassword("");
            } else if (c.isNull(c.getColumnIndexOrThrow(KEY_PASSWORD))) {
                utente.setPassword("");
            } else if (c.getString(c.getColumnIndexOrThrow(KEY_PASSWORD)).equals("")) {
                utente.setPassword("");
            } else {
                utente.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
            }
            if (c.isAfterLast()) {
                utente.setTipoProfilo("");
            } else if (c.isNull(c.getColumnIndexOrThrow(KEY_TIPOPROFILO))) {
                utente.setTipoProfilo("");
            } else if (c.getString(c.getColumnIndexOrThrow(KEY_TIPOPROFILO)).equals("")) {
                utente.setTipoProfilo("");
            } else {
                utente.setTipoProfilo(c.getString(c.getColumnIndex(KEY_TIPOPROFILO)));
            }
            if (c.isAfterLast()) {
                utente.setAbilitato("");
            } else if (c.isNull(c.getColumnIndexOrThrow(KEY_ABILITATO))) {
                utente.setAbilitato("");
            } else if (c.getString(c.getColumnIndexOrThrow(KEY_ABILITATO)).equals("")) {
                utente.setAbilitato("");
            } else {
                utente.setAbilitato(c.getString(c.getColumnIndex(KEY_ABILITATO)));
            }
        }
        return utente;
    }

    // Galaxy Nexus: /data/user/0/com.gestione.distributori/databases/distributori.sqlite
    public String controllaEsistenzaBaseDati() {
        File db = this.context.getDatabasePath(DATABASE_NAME);
        db.toString();
        if (!db.exists()) {
            this.dbHelper = new DatabaseHelper(this.context);
            this.database = this.dbHelper.getWritableDatabase();
            inizializzaSettings();
            return "Inizializzazione";
        }
        String statoApp = verificaSettings();
        if (statoApp != "OK") {
            return "Inizializzazione";
        }
        if (0 != 0) {
            if (verificaConnMySql(getIndirizzoIp(), getPorta(), getUtenteMySql(), getPasswordMySql(), getNomeBaseDati()) == "OK") {
                if (allineaBaseDati() != "OK") {
                    return "NoAllin";
                }
            } else if (statoApp == "Errore Connessione") {
                return "NoConn";
            }
        }
        return "Inizializzato";
    }

    /* JADX INFO: finally extract failed */
    public long inizializzaSettings() {
        try {
            Settings setting = new Settings();
            setting.setDescrizione("Indirizzo IP");
            setting.setValore("");
            long setting_id = createSettings(setting);
            if (setting_id != 0) {
                Settings setting2 = new Settings();
                setting2.setDescrizione("Porta");
                setting2.setValore("");
                setting_id = createSettings(setting2);
            }
            if (setting_id != 0) {
                Settings setting3 = new Settings();
                setting3.setDescrizione("NomeBaseDati");
                setting3.setValore("");
                setting_id = createSettings(setting3);
            }
            if (setting_id != 0) {
                Settings setting4 = new Settings();
                setting4.setDescrizione("UtenteMySQL");
                setting4.setValore("");
                setting_id = createSettings(setting4);
            }
            if (setting_id != 0) {
                Settings setting5 = new Settings();
                setting5.setDescrizione("PasswordMySQL");
                setting5.setValore("");
                setting_id = createSettings(setting5);
            }
            this.dbHelper.close();
            return setting_id;
        } catch (Exception e) {
            this.dbHelper.close();
            return 0;
        } catch (Throwable th) {
            this.dbHelper.close();
            throw th;
        }
    }

    public String verificaSettings() {
        Settings setting;
        Settings setting2;
        Settings setting3;
        Settings setting4;
        new Settings();
        Settings setting5 = getSettings("Indirizzo IP");
        if (setting5 == null || setting5.getValore() == "" || (setting = getSettings("Porta")) == null || setting.getValore() == "" || (setting2 = getSettings("NomeBaseDati")) == null || setting2.getValore() == "" || (setting3 = getSettings("UtenteMySQL")) == null || setting3.getValore() == "" || (setting4 = getSettings("PasswordMySQL")) == null || setting4.getValore() == "") {
            return "Inizializzazione";
        }
        return "OK";
    }

    public String getIndirizzoIp() {
        new Settings();
        Settings setting = getSettings("Indirizzo IP");
        if (setting != null) {
            return setting.getValore();
        }
        return "";
    }

    public String getPorta() {
        new Settings();
        Settings setting = getSettings("Porta");
        if (setting != null) {
            return setting.getValore();
        }
        return "";
    }

    public String getNomeBaseDati() {
        new Settings();
        Settings setting = getSettings("NomeBaseDati");
        if (setting != null) {
            return setting.getValore();
        }
        return "";
    }

    public String getUtenteMySql() {
        new Settings();
        Settings setting = getSettings("UtenteMySQL");
        if (setting != null) {
            return setting.getValore();
        }
        return "";
    }

    public String getPasswordMySql() {
        new Settings();
        Settings setting = getSettings("PasswordMySQL");
        if (setting != null) {
            return setting.getValore();
        }
        return "";
    }

    public String allineaBaseDati() {
        try {
            // MISURA TEMPO ESECUZIONE =======================================================================
            long startTime = System.currentTimeMillis();
            // MISURA TEMPO ESECUZIONE =======================================================================

            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
            databaseMySql = new DatabaseMySql(context);

            allineaListini();
            allineaClienti();
            allineaMacchine();
            allineaInstallazioniMacchine();
            allineaUtenti();
            allineaMagazzini();
            allineaCategorie();
            allineaMonitoraggi();
            allineaTipiManutenzione();
            allineaManutenzioni();

            database.close();
            dbHelper.close();

            // MISURA TEMPO ESECUZIONE =======================================================================
            Log.d("TEMPO TOTALE: ", String.valueOf((System.currentTimeMillis() - startTime) / 1000));
            // MISURA TEMPO ESECUZIONE =======================================================================
            return "OK";
        } catch (Exception e) {
            return "Error";
        }
    }

    private void allineaManutenzioni() {
        List<Manutenzione> manutenzioni = databaseMySql.getManutenzione();

        database.delete(TABLE_MANUTENZIONE, "", (String[]) null);

        if (manutenzioni != null) {
            for (var manutenzione : manutenzioni) {
                ContentValues values = new ContentValues();

                values.put("idmacchina", manutenzione.getIdMacchina());
                values.put(KEY_MDATAMANUTENZIONE, manutenzione.getDataManutenzione());
                values.put("descrizione", manutenzione.getDescrizione());
                values.put(KEY_MDATASCADENZAMAN, manutenzione.getDatascadenzaMan());
                values.put("nbattute", manutenzione.getNBattute());
                values.put(KEY_MNOTE, manutenzione.getNote());
                values.put("idtipomanutenzione", manutenzione.getIDTipoManutenzione());

                if (database.insert(TABLE_MANUTENZIONE, (String) null, values) == 0) {
                    break;
                }
            }
        }
    }

    private void allineaTipiManutenzione() {
        List<TipiManutenzione> tipiManutenzione = databaseMySql.getTipiManutenzione();

        database.delete(TABLE_TIPI_MANUTENZIONE, "", (String[]) null);

        if (tipiManutenzione != null) {
            for (var tipomanutenzione : tipiManutenzione) {
                ContentValues values = new ContentValues();

                values.put("idtipomanutenzione", tipomanutenzione.getIdTipoManutenzione());
                values.put(KEY_DESCRIZIONEPOMANUTENZIONE, tipomanutenzione.getTipoManutenzioneDescrizione());
                values.put("nbattute", tipomanutenzione.getNBattute());
                values.put(KEY_NGGSCADENZA, tipomanutenzione.getNGgScadenza());
                values.put(KEY_NGGPREAVVISO, tipomanutenzione.getNGgPreavviso());
                values.put("idtipomacchina", tipomanutenzione.getIdTipoMacchina());

                if (database.insert(TABLE_TIPI_MANUTENZIONE, (String) null, values) == 0) {
                    break;
                }
            }
        }
    }

    private void allineaMonitoraggi() {
        List<MonitoraggioDistributori> monitoraggi = databaseMySql.getMonitoraggioDistributori();

        database.delete(TABLE_MONITORAGGIO_MANUTENZIONE, "", (String[]) null);

        if (monitoraggi != null) {
            for (MonitoraggioDistributori monitoraggio : monitoraggi) {
                ContentValues values = new ContentValues();

                values.put(KEY_MNRAGIONESOCIALE, monitoraggio.getRagioneSociale());
                values.put("modello", monitoraggio.getModello());
                values.put("idmacchina", monitoraggio.getIdmacchina());
                values.put("idtipomacchina", monitoraggio.getIdTipoMacchina());
                values.put(KEY_MNTIPOMACCHINA, monitoraggio.getTipoMacchina());
                values.put("idtipomanutenzione", monitoraggio.getIdTipomanutenzione());
                values.put("descrizione", monitoraggio.getDescrizione());
                values.put(KEY_MNBATTUTEATTUALI, monitoraggio.getBattuteAttuali());
                values.put(KEY_MNBATTUTEMANUTENZIONE, monitoraggio.getBattuteManutenzione());
                values.put(KEY_MNSCADENZAMANUTENZIONE, monitoraggio.getScadenzaManutenzione());

                if (database.insert(TABLE_MONITORAGGIO_MANUTENZIONE, (String) null, values) == 0) {
                    break;
                }
            }
        }
    }

    private void allineaCategorie() {
        var categorie = databaseMySql.getCategorie();

        database.execSQL("DELETE FROM categorie");

        for (Categorie categoria : categorie) {
            ContentValues values = new ContentValues();

            values.put(KEY_IDCATEGORIE, categoria.getIdCategorie());
            values.put(KEY_DESCCATEGORIA, categoria.getDescCategorie());
            values.put(KEY_FLAGVISIBILE, categoria.getFlagVisibile());

            long categoria_id = database.insert(TABLE_CATEGORIE, (String) null, values);

            if (categoria_id == 0) {
                break;
            }
        }
    }

    private void allineaMagazzini() {
        List<Magazzino> magazzini = databaseMySql.getMagazzino();

        database.execSQL("DELETE FROM magazzino");

        SQLiteStatement statement = database.compileStatement("INSERT INTO magazzino(idarticolo,kgresidua,qtaconfezioneresidua ) VALUES (?, ?, ?)");

        database.beginTransaction();
        for (Magazzino magazzino : magazzini) {
            statement.clearBindings();
            statement.bindLong(1, (long) magazzino.getIdArticolo());
            statement.bindDouble(2, (double) magazzino.getKgResidua());
            statement.bindLong(3, (long) magazzino.getQtaConfezioniResidua());
            long magazzino_id = statement.executeInsert();
            if (magazzino_id == 0) {
                break;
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void allineaUtenti() {
        var utenti = databaseMySql.getUtenti();

        database.execSQL("DELETE FROM utenti");

        long utente_id = 0;
        for (Utente utente : utenti) {
            //utente_id = createUtente(utente);
            ContentValues values = new ContentValues();

            values.put(KEY_IDUTENTE, utente.getIdUtente());
            values.put(KEY_UTENTE, utente.getUtenza());
            values.put(KEY_PASSWORD, utente.getPassword());
            values.put(KEY_TIPOPROFILO, utente.getTipoProfilo());
            values.put(KEY_ABILITATO, utente.getAbilitato());

            utente_id = database.insert(TABLE_UTENTI, (String) null, values);

            if (utente_id == 0) {
                break;
            }
        }
    }

    private void allineaInstallazioniMacchine() {
        List<InstallazioneMacchina> installazioneMacchine = databaseMySql.getInstallazioniMacchine();

        database.execSQL("DELETE FROM installazionemacchina");

        SQLiteStatement statement = database.compileStatement("INSERT INTO installazionemacchina(idMacchina,idCliente ) VALUES (?, ?)");

        database.beginTransaction();
        for (InstallazioneMacchina installazioneMacchina : installazioneMacchine) {
            statement.clearBindings();
            statement.bindLong(1, (long) installazioneMacchina.getIdMacchina());
            statement.bindLong(2, (long) installazioneMacchina.getidCliente());
            if (statement.executeInsert() == 0) {
                break;
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void allineaMacchine() {
        List<Macchina> macchine = databaseMySql.getMacchine();

        database.execSQL("DELETE FROM macchina");

        SQLiteStatement statement = database.compileStatement("INSERT INTO macchina(idMacchina,marca,modello,codPausamatic ) VALUES (?, ?, ?, ?)");

        database.beginTransaction();
        for (Macchina macchina : macchine) {
            statement.clearBindings();
            statement.bindLong(1, (long) macchina.getIdMacchina());
            statement.bindString(2, macchina.getMarca());
            statement.bindString(3, macchina.getModello());
            statement.bindString(4, macchina.getCodice());
            if (statement.executeInsert() == 0) {
                break;
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void allineaClienti() {
        List<Cliente> clienti = databaseMySql.getClienti();

        database.execSQL("DELETE FROM clienti");

        SQLiteStatement statement = database.compileStatement("INSERT INTO clienti(idCliente,ragioneSociale ) VALUES (?, ?)");

        database.beginTransaction();
        for (Cliente cliente : clienti) {
            statement.clearBindings();
            statement.bindLong(1, (long) cliente.getIdCliente());
            statement.bindString(2, cliente.getRagioneSociale());
            if (statement.executeInsert() == 0) {
                break;
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void allineaListini() {
        List<ListiniClienti> listino = databaseMySql.getListini();

        database.execSQL("DELETE FROM listiniclienti");

        SQLiteStatement statement = database.compileStatement("INSERT INTO listiniclienti(idCliente,idMacchina,idArticolo,nomeArticolo,costoArticolo,prezzoVendita,tipologiaArticolo,idCategoria ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        database.beginTransaction();
        for (ListiniClienti listinoCliente : listino) {
            statement.clearBindings();
            statement.bindLong(1, (long) listinoCliente.getIdCliente());
            statement.bindLong(2, (long) listinoCliente.getIdMacchina());
            statement.bindLong(3, (long) listinoCliente.getIdArticolo());
            statement.bindString(4, listinoCliente.getNomeArticolo());
            statement.bindDouble(5, (double) listinoCliente.getCostoArticolo());
            statement.bindDouble(6, (double) listinoCliente.getPrezzoVendita());
            statement.bindString(7, listinoCliente.getTipologia());
            statement.bindLong(8, (long) listinoCliente.getidCategoria());
            if (statement.executeInsert() == 0) {
                break;
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public String verificaConnMySql(String indirizzoIp, String porta, String utente, String password, String dbName) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            Properties prop = new Properties();
            prop.setProperty("connectTimeout", "10000");
            prop.setProperty("socketTimeout", "10000");
            Connection conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + indirizzoIp + ":" + porta + "/") + dbName + "?user=" + utente + "&password=" + password, prop);
            if (conn == null) {
                return "ErrConn";
            }
            conn.close();
            return "OK";
        } catch (SQLException e) {
            return "ErrConn";
        } catch (ClassNotFoundException e2) {
            return "ErrConn";
        } catch (InstantiationException e3) {
            return "ErrConn";
        } catch (IllegalAccessException e4) {
            return "OK";
        }
    }

    public String backupSDCard() {
        String dbPath = this.context.getDatabasePath(DATABASE_NAME).toString();
        try {
            File backupDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "DISTRIBUTORI" + "/" + "BACKUP_FOLDER");
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs();
            }
            String state = Environment.getExternalStorageState();
            if ("mounted".equals(state)) {
                File currentDB = new File(dbPath);
                File backupDB = new File(backupDirectory, "BackupDistributori");
                FileInputStream fis = new FileInputStream(currentDB);
                OutputStream output = new FileOutputStream(backupDB);
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = fis.read(buffer);
                    if (length > 0) {
                        output.write(buffer, 0, length);
                    } else {
                        output.flush();
                        output.close();
                        fis.close();
                        return "OK";
                    }
                }
            } else if ("mounted_ro".equals(state)) {
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void TrasmettiDati() throws SQLException, ParseException {
        if (isNetworkAvailable()) {
            this.databaseMySql = new DatabaseMySql(this.context);
            List<RicaricaMacchina> RicaricheMacchina = getRicaricheConsolidate();
            List<RicavoMacchina> RicaviMacchina = getRicaviConsolidati();
            String trasmettiDatiRicariche = this.databaseMySql.InsertRicaricaDistributore(RicaricheMacchina);
            String trasmettiDatiRicavi = this.databaseMySql.InsertRicavoDistributore(RicaviMacchina);
            if (trasmettiDatiRicariche.equals("OK")) {
                deleteRicaricheTrasmesse();
            }
            if (trasmettiDatiRicavi.equals("OK")) {
                deleteRicaviTrasmessi();
            }
        }
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        String str = activeNetworkInfo.getTypeName().toString();
        if (activeNetworkInfo.getType() == 1) {
            return true;
        }
        return false;
    }

    public String resetBaseDati() {
        try {
            deleteAllRicariche();
            deleteAllRicavi();
            deleteAllInsManutenzione();
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void ErrorDialog(String Description) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setTitle("You get Error...");
        alertDialog.setMessage(Description);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}


/*
    public String allineaBaseDati() {
        try {
            // MISURA TEMPO ESECUZIONE =======================================================================
            long startTime = System.currentTimeMillis();
            // MISURA TEMPO ESECUZIONE =======================================================================

            allineaListini();
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllClienti();
            List<Cliente> clienti = this.databaseMySql.getClienti();
            SQLiteStatement statement2 = this.database.compileStatement("INSERT INTO clienti(idCliente,ragioneSociale ) VALUES (?, ?)");
            this.database.beginTransaction();
            for (Cliente cliente : clienti) {
                statement2.clearBindings();
                statement2.bindLong(1, (long) cliente.getIdCliente());
                statement2.bindString(2, cliente.getRagioneSociale());
                if (statement2.executeInsert() == 0) {
                    break;
                }
            }
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllMacchine();
            List<Macchina> macchine = this.databaseMySql.getMacchine();
            SQLiteStatement statement3 = this.database.compileStatement("INSERT INTO macchina(idMacchina,marca,modello,codPausamatic ) VALUES (?, ?, ?, ?)");
            this.database.beginTransaction();
            for (Macchina macchina : macchine) {
                statement3.clearBindings();
                statement3.bindLong(1, (long) macchina.getIdMacchina());
                statement3.bindString(2, macchina.getMarca());
                statement3.bindString(3, macchina.getModello());
                statement3.bindString(4, macchina.getCodice());
                if (statement3.executeInsert() == 0) {
                    break;
                }
            }
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllInstallazioneMacchine();
            List<InstallazioneMacchina> installazioneMacchine = this.databaseMySql.getInstallazioniMacchine();
            SQLiteStatement statement4 = this.database.compileStatement("INSERT INTO installazionemacchina(idMacchina,idCliente ) VALUES (?, ?)");
            this.database.beginTransaction();
            for (InstallazioneMacchina installazioneMacchina : installazioneMacchine) {
                statement4.clearBindings();
                statement4.bindLong(1, (long) installazioneMacchina.getIdMacchina());
                statement4.bindLong(2, (long) installazioneMacchina.getidCliente());
                if (statement4.executeInsert() == 0) {
                    break;
                }
            }
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllUtenti();
            long utente_id = 0;
            for (Utente utente : this.databaseMySql.getUtenti()) {
                utente_id = createUtente(utente);
                if (utente_id == 0) {
                    break;
                }
            }
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllMagazzino();
            List<Magazzino> magazzinos = this.databaseMySql.getMagazzino();
            SQLiteStatement statement5 = this.database.compileStatement("INSERT INTO magazzino(idarticolo,kgresidua,qtaconfezioneresidua ) VALUES (?, ?, ?)");
            this.database.beginTransaction();
            for (Magazzino magazzino : magazzinos) {
                statement5.clearBindings();
                statement5.bindLong(1, (long) magazzino.getIdArticolo());
                statement5.bindDouble(2, (double) magazzino.getKgResidua());
                statement5.bindLong(3, (long) magazzino.getQtaConfezioniResidua());
                long magazzino_id = statement5.executeInsert();
                if (utente_id == 0) {
                    break;
                }
            }
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllCategorie();
            for (Categorie categoria : this.databaseMySql.getCategorie()) {
                if (createCategoria(categoria) == 0) {
                    break;
                }
            }
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllMonitoraggioManutenzione();
            List<MonitoraggioDistributori> monitoraggiodistributoris = this.databaseMySql.getMonitoraggioDistributori();
            if (monitoraggiodistributoris != null) {
                for (MonitoraggioDistributori monitoraggiodistributori : monitoraggiodistributoris) {
                    if (createMonitoraggioManutenzione(monitoraggiodistributori) == 0) {
                        break;
                    }
                }
            }
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllTipiManutenzione();
            List<TipiManutenzione> tipiManutenzioneList = this.databaseMySql.getTipiManutenzione();
            if (tipiManutenzioneList != null) {
                for (TipiManutenzione tipimanutenzione : tipiManutenzioneList) {
                    if (createTipoManutenzione(tipimanutenzione) == 0) {
                        break;
                    }
                }
            }
            this.databaseMySql = new DatabaseMySql(this.context);
            deleteAllManutenzione();
            List<Manutenzione> ManutenzioneList = this.databaseMySql.getManutenzione();
            if (ManutenzioneList != null) {
                for (Manutenzione manutenzione : ManutenzioneList) {
                    if (createManutenzione(manutenzione) == 0) {
                        break;
                    }
                }
            }

            // MISURA TEMPO ESECUZIONE =======================================================================
            long stopTime = System.currentTimeMillis();
            Log.d("TEMPO TOTALE: ", String.valueOf((stopTime - startTime) / 1000));
            // MISURA TEMPO ESECUZIONE =======================================================================

            return "OK";
        } catch (Exception e) {
            return "Error";
        }
    }
*/
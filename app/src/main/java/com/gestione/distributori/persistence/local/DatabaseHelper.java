package com.gestione.distributori.persistence.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_CATEGORIE = "CREATE TABLE categorie(idcategoria INTEGER PRIMARY KEY AUTOINCREMENT,desccategoria STRING,flagvisibile STRING)";
    private static final String CREATE_TABLE_CLIENTI = "CREATE TABLE clienti(idCliente INTEGER,ragioneSociale STRING)";
    private static final String CREATE_TABLE_INSMANUTENZIONE = "CREATE TABLE insmanutenzione(idmanutenzione INTEGER PRIMARY KEY AUTOINCREMENT,idmacchina INTEGER,datamanutenzione STRING,descrizione STRING,datascadenzaman STRING,nbattute INTEGER,note STRING,idtipomanutenzione INTEGER,trasmesso STRING)";
    private static final String CREATE_TABLE_INSTALLAZIONEMACCHINA = "CREATE TABLE installazionemacchina(idMacchina INTEGER,idCliente INTEGER)";
    private static final String CREATE_TABLE_LISTINICLIENTI = "CREATE TABLE listiniclienti(idCliente INTEGER,idMacchina INTEGER,idArticolo INTEGER,nomeArticolo STRING, costoArticolo FLOAT, prezzoVendita FLOAT, tipologiaArticolo STRING,idCategoria INTEGER)";
    private static final String CREATE_TABLE_MACCHINA = "CREATE TABLE macchina(idMacchina INTEGER,marca STRING,modello STRING,codPausamatic STRING)";
    private static final String CREATE_TABLE_MAGAZZINO = "CREATE TABLE magazzino(idarticolo INTEGER PRIMARY KEY AUTOINCREMENT,kgresidua float(9,2),qtaconfezioneresidua INTEGER)";
    private static final String CREATE_TABLE_MANUTENZIONE = "CREATE TABLE manutenzione(idmanutenzione INTEGER PRIMARY KEY AUTOINCREMENT,idmacchina INTEGER,datamanutenzione STRING,descrizione STRING,datascadenzaman STRING,nbattute INTEGER,note STRING,idtipomanutenzione INTEGER)";
    private static final String CREATE_TABLE_MONITORAGGIO_MANUTENZIONE = "CREATE TABLE monitoraggiomanutenzione(idmonitoraggio INTEGER PRIMARY KEY AUTOINCREMENT,stipomanutenzionedescrizione STRING,ragionesociale STRING,modello STRING,idmacchina STRING,idtipomacchina STRING,tipomacchina STRING,idtipomanutenzione STRING,descrizione STRING,battuteattuali STRING,battutemanutenzione STRING,scadenzamanutenzione STRING)";
    private static final String CREATE_TABLE_RICARICAMACCHINA = "CREATE TABLE ricaricamacchina(idRigaRicarica INTEGER PRIMARY KEY AUTOINCREMENT,idMacchina INTEGER,idArticolo INTEGER,nomeArticolo STRING,qtaCnfezione INTEGER, KG FLOAT,dataRicarica DATETIME, costoAcquisto FLOAT,prezzoVendita FLOAT,consolidato STRING,trasmesso STRING)";
    private static final String CREATE_TABLE_RICAVOMACCHINA = "CREATE TABLE ricavomacchina(idRigaRicavo INTEGER PRIMARY KEY AUTOINCREMENT,idMacchina INTEGER,dataScaricoSoldi DATETIME, soldiScaricati FLOAT, consolidato STRING,trasmesso STRING, nbattute INTEGER,nbattuteCaldo INTEGER)";
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE settings(idSettings INTEGER PRIMARY KEY AUTOINCREMENT,descrizione STRING,valore STRING)";
    private static final String CREATE_TABLE_TIPI_MANUTENZIONE = "CREATE TABLE tbl_tipimanutenzione(idtipomanutenzione INTEGER PRIMARY KEY AUTOINCREMENT,stipomanutenzionedescrizione STRING,nbattute INTEGER,nggscadenza INTEGER,nggpreavviso INTEGER,idtipomacchina STRING)";
    private static final String CREATE_TABLE_UTENTI = "CREATE TABLE utenti(idUtente INTEGER PRIMARY KEY AUTOINCREMENT,utente STRING,password STRING,tipoProfilo STRING,abilitato STRING)";
    private static final String DATABASE_NAME = "distributori";
    private static final int DATABASE_VERSION = 2;
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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_RICARICAMACCHINA);
            db.execSQL(CREATE_TABLE_RICAVOMACCHINA);
            db.execSQL(CREATE_TABLE_LISTINICLIENTI);
            db.execSQL(CREATE_TABLE_CLIENTI);
            db.execSQL(CREATE_TABLE_MACCHINA);
            db.execSQL(CREATE_TABLE_INSTALLAZIONEMACCHINA);
            db.execSQL(CREATE_TABLE_UTENTI);
            db.execSQL(CREATE_TABLE_SETTINGS);
            db.execSQL(CREATE_TABLE_MAGAZZINO);
            db.execSQL(CREATE_TABLE_CATEGORIE);
            db.execSQL(CREATE_TABLE_MONITORAGGIO_MANUTENZIONE);
            db.execSQL(CREATE_TABLE_TIPI_MANUTENZIONE);
            db.execSQL(CREATE_TABLE_MANUTENZIONE);
            db.execSQL(CREATE_TABLE_INSMANUTENZIONE);
        } catch (Exception e) {
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS ricaricamacchina");
            db.execSQL("DROP TABLE IF EXISTS ricavomacchina");
            db.execSQL("DROP TABLE IF EXISTS listiniclienti");
            db.execSQL("DROP TABLE IF EXISTS clienti");
            db.execSQL("DROP TABLE IF EXISTS macchina");
            db.execSQL("DROP TABLE IF EXISTS installazionemacchina");
            db.execSQL("DROP TABLE IF EXISTS utenti");
            db.execSQL("DROP TABLE IF EXISTS settings");
            db.execSQL("DROP TABLE IF EXISTS magazzino");
            db.execSQL("DROP TABLE IF EXISTS categorie");
            db.execSQL("DROP TABLE IF EXISTS monitoraggiomanutenzione");
            db.execSQL("DROP TABLE IF EXISTS tbl_tipimanutenzione");
            db.execSQL("DROP TABLE IF EXISTS manutenzione");
            db.execSQL("DROP TABLE IF EXISTS insmanutenzione");
            onCreate(db);
        } catch (Exception e) {
        }
    }
}

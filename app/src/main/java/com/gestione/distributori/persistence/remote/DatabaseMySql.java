package com.gestione.distributori.persistence.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;

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
import com.gestione.distributori.persistence.local.DbAdapter;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
//import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@SuppressLint({"SimpleDateFormat"})
public class DatabaseMySql {
    public static Connection connPrinc = null;
    public static String dbName = "";
    public static String driver = "com.mysql.jdbc.Driver";
    private Connection conn;
    private Context context;
    private DbAdapter dbAdapter;
    private String indirizzoIP;
    private String passwordMySql;
    private String porta;
    private String utenteMySql;

    public DatabaseMySql(Context context2) {
        this.context = context2;
    }

    public List<ListiniClienti> getListini() {
        List<ListiniClienti> listino = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT LISTINICLIENTI.IDCLIENTE, LISTINICLIENTI.IDMACCHINA, LISTINICLIENTI.IDARTICOLO, ARTICOLO.NOMEARTICOLO, LISTINICLIENTI.COSTOARTICOLO, LISTINICLIENTI.PREZZOVENDITA, CASE WHEN QTACONFEZIONIRESIDUA IS NULL THEN CASE WHEN KGRESIDUA IS NULL THEN '' ELSE 'K' END ELSE 'Q' END AS TIPOLOGIA, ARTICOLO.IDCATEGORIA FROM LISTINICLIENTI INNER JOIN ARTICOLO ON ARTICOLO.IDARTICOLO = LISTINICLIENTI.IDARTICOLO LEFT JOIN MAGAZZINO ON MAGAZZINO.IDARTICOLO  = LISTINICLIENTI.IDARTICOLO ORDER BY IDCLIENTE, IDMACCHINA");
            while (rs.next()) {
                ListiniClienti rigaListino = new ListiniClienti();
                rigaListino.setIdCliente(rs.getInt("IDCLIENTE"));
                rigaListino.setIdMacchina(rs.getInt("IDMACCHINA"));
                rigaListino.setIdArticolo(rs.getInt("IDARTICOLO"));
                rigaListino.setNomeArticolo(rs.getString("NOMEARTICOLO"));
                rigaListino.setCostoArticolo(rs.getFloat("COSTOARTICOLO"));
                rigaListino.setPrezzoVendita(rs.getFloat("PREZZOVENDITA"));
                rigaListino.setTipologia(rs.getString("TIPOLOGIA"));
                rigaListino.setIdCategoria(rs.getInt("IDCATEGORIA"));
                listino.add(rigaListino);
            }
            stmt.close();
            this.conn.close();
            return listino;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return listino;
        }
    }

    public List<Cliente> getClienti() {
        List<Cliente> clienti = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT distinct  cliente.IDCLIENTE,cliente.RAGIONESOCIALE FROM cliente inner join installazionemacchina on cliente.IDCLIENTE = installazionemacchina.IDCLIENTE inner join listiniclienti  on cliente.IDCLIENTE = listiniclienti.IDCLIENTE and installazionemacchina.IDMACCHINA = listiniclienti.IDMACCHINA where (installazionemacchina.DATADISINSTALLAZIONE is null) order by cliente.RAGIONESOCIALE");
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("IDCLIENTE"));
                cliente.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
                clienti.add(cliente);
            }
            stmt.close();
            this.conn.close();
            return clienti;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return clienti;
        }
    }

    public List<Macchina> getMacchine() {
        List<Macchina> macchine = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT IDMACCHINA, MARCA, MODELLO, CODPAUSAMATIC FROM MACCHINA ORDER BY MARCA, MODELLO, CODPAUSAMATIC");
            while (rs.next()) {
                Macchina macchina = new Macchina();
                macchina.setIdMacchina(rs.getInt("IDMACCHINA"));
                macchina.setMarca(rs.getString("MARCA"));
                macchina.setModello(rs.getString("MODELLO"));
                macchina.setCodice(rs.getString("CODPAUSAMATIC"));
                macchine.add(macchina);
            }
            stmt.close();
            this.conn.close();
            return macchine;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return macchine;
        }
    }

    public List<InstallazioneMacchina> getInstallazioniMacchine() {
        List<InstallazioneMacchina> installazioneMacchine = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT IDMACCHINA, IDCLIENTE FROM INSTALLAZIONEMACCHINA WHERE DATADISINSTALLAZIONE IS NULL");
            while (rs.next()) {
                InstallazioneMacchina installazioneMacchina = new InstallazioneMacchina();
                installazioneMacchina.setIdMacchina(rs.getInt("IDMACCHINA"));
                installazioneMacchina.setIdCliente(rs.getInt("IDCLIENTE"));
                installazioneMacchine.add(installazioneMacchina);
            }
            stmt.close();
            this.conn.close();
            return installazioneMacchine;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return installazioneMacchine;
        }
    }

    public List<Utente> getUtenti() {
        List<Utente> utenti = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT IDUTENTE, UTENTE, UTENTI.IDPROFILO, PASSWORD, FUNZIONALITA AS TIPOPROFILO, ABILITATO FROM UTENTI LEFT JOIN PROFILI_FUNZIONALITA ON PROFILI_FUNZIONALITA.IDPROFILO = UTENTI.IDPROFILO LEFT JOIN FUNZIONALITA ON FUNZIONALITA.IDFUNZIONALITA = PROFILI_FUNZIONALITA.IDFUNZIONALITA WHERE FUNZIONALITA = 'AppAndroid'");
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setIdUtente(rs.getInt("IDUTENTE"));
                utente.setUtenza(rs.getString("UTENTE"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setTipoProfilo(rs.getString("TIPOPROFILO"));
                utente.setAbilitato(rs.getString("ABILITATO"));
                utenti.add(utente);
            }
            stmt.close();
            this.conn.close();
            return utenti;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return utenti;
        }
    }

    public List<Magazzino> getMagazzino() {
        List<Magazzino> magazzinos = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT IDARTICOLO,KGRESIDUA,QTACONFEZIONIRESIDUA FROM MAGAZZINO ORDER BY IDARTICOLO");
            while (rs.next()) {
                Magazzino magazzino = new Magazzino();
                magazzino.setIdArticolo(rs.getInt("IDARTICOLO"));
                magazzino.setKgResidua(rs.getFloat("KGRESIDUA"));
                magazzino.setQtaConfezioniResidua(rs.getInt("QTACONFEZIONIRESIDUA"));
                magazzinos.add(magazzino);
            }
            stmt.close();
            this.conn.close();
            return magazzinos;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return magazzinos;
        }
    }

    public List<MonitoraggioDistributori> getMonitoraggioDistributori() {
        List<MonitoraggioDistributori> monitoraggioDistributoris = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            stmt.execute("call sp_MonitoraggioDistributoriApp()");
            ResultSet rs = (ResultSet) stmt.executeQuery("select * from temp_Monitoraggio_Distributori");
            while (rs.next()) {
                MonitoraggioDistributori monitoraggiodistributore = new MonitoraggioDistributori();
                monitoraggiodistributore.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
                monitoraggiodistributore.setModello(rs.getString("MODELLO"));
                monitoraggiodistributore.setIdMacchina(rs.getString("IDMACCHINA"));
                monitoraggiodistributore.setIdTipoMacchina(rs.getString("IDTIPOMACCHINA"));
                monitoraggiodistributore.setTipoMacchina(rs.getString("NOMETIPOMACCHINA"));
                monitoraggiodistributore.setIdTipomanutenzione(rs.getString("IDTipoManutenzione"));
                monitoraggiodistributore.setDescrizione(rs.getString("sTipoManutenzioneDescrizione"));
                monitoraggiodistributore.setBattuteAttuali(rs.getString("BattuteAttualiMacchina"));
                monitoraggiodistributore.setBattuteManutenzione(rs.getString("MaxBatMan"));
                monitoraggiodistributore.setScadenzaManutenzione(rs.getString("DataPrevisioneMan"));
                monitoraggioDistributoris.add(monitoraggiodistributore);
            }
            stmt.close();
            this.conn.close();
            return monitoraggioDistributoris;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return monitoraggioDistributoris;
        }
    }

    public List<Categorie> getCategorie() {
        List<Categorie> Categories = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT *  FROM CATEGORIE ORDER BY IDCATEGORIA");
            while (rs.next()) {
                Categorie categoria = new Categorie();
                categoria.setIdCategorie(rs.getInt("IDCATEGORIA"));
                categoria.setDescCategorie(rs.getString("DESCCATEGORIA"));
                categoria.setFlagVisibile(rs.getString("FLAGVISIBILE"));
                Categories.add(categoria);
            }
            stmt.close();
            this.conn.close();
            return Categories;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return Categories;
        }
    }

    public List<TipiManutenzione> getTipiManutenzione() {
        List<TipiManutenzione> tipiManutenzioneList = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT tbl_tipimanutenzione.IDTipoManutenzione,sTipoManutenzioneDescrizione,nBattute,nGgScadenza,nGgPreavviso,IDTipoMacchina FROM tbl_macchina_tipimanutenzione inner join tbl_tipimanutenzione ON tbl_tipimanutenzione.IDTipoManutenzione = tbl_macchina_tipimanutenzione.IDTipoManutenzione; ");
            while (rs.next()) {
                TipiManutenzione tipimanutenzione = new TipiManutenzione();
                tipimanutenzione.setIdTipoManutenzione(rs.getInt("idtipomanutenzione"));
                tipimanutenzione.setTipoManutenzioneDescrizione(rs.getString("stipomanutenzionedescrizione"));
                tipimanutenzione.setNBattute(rs.getString("nbattute"));
                tipimanutenzione.setNGgScadenza(rs.getString("nggscadenza"));
                tipimanutenzione.setNGgPreavviso(rs.getString("nggpreavviso"));
                tipimanutenzione.setIdTipoMacchina(rs.getString("idtipomacchina"));
                tipiManutenzioneList.add(tipimanutenzione);
            }
            stmt.close();
            this.conn.close();
            return tipiManutenzioneList;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return tipiManutenzioneList;
        }
    }

    public List<Manutenzione> getManutenzione() {
        List<Manutenzione> ManutenzioneList = new ArrayList<>();
        try {
            this.dbAdapter = new DbAdapter(this.context);
            this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
            this.porta = this.dbAdapter.getPorta();
            dbName = this.dbAdapter.getNomeBaseDati();
            this.utenteMySql = this.dbAdapter.getUtenteMySql();
            this.passwordMySql = this.dbAdapter.getPasswordMySql();
            if (this.indirizzoIP == "" || this.porta == "" || dbName == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                return null;
            }
            Class.forName(driver).newInstance();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName, this.utenteMySql, this.passwordMySql);
            Statement stmt = (Statement) this.conn.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("SELECT * FROM MANUTENZIONE");
            while (rs.next()) {
                Manutenzione manutenzione = new Manutenzione();
                manutenzione.setDataManutenzione(rs.getString("datamanutenzione"));
                manutenzione.setDatascadenzaMan(rs.getString("datascadenzaman"));
                manutenzione.setNBattute(rs.getInt("nbattute"));
                manutenzione.setIdMacchina(rs.getInt("idmacchina"));
                manutenzione.setNote(rs.getString("note"));
                manutenzione.setDescrizione(rs.getString("descrizione"));
                manutenzione.setIDTipoManutenzione(rs.getInt("idtipomanutenzione"));
                ManutenzioneList.add(manutenzione);
            }
            stmt.close();
            this.conn.close();
            return ManutenzioneList;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (IllegalAccessException e4) {
            return ManutenzioneList;
        }
    }

    public String InsertRicaricaDistributore(List<RicaricaMacchina> ricariche) throws SQLException, ParseException {
        String str;
        this.dbAdapter = new DbAdapter(this.context);
        this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
        this.porta = this.dbAdapter.getPorta();
        dbName = this.dbAdapter.getNomeBaseDati();
        this.utenteMySql = this.dbAdapter.getUtenteMySql();
        this.passwordMySql = this.dbAdapter.getPasswordMySql();
        try {
            if (this.indirizzoIP == "" || this.porta == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                this.conn = null;
            } else {
                Class.forName(driver).newInstance();
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                Properties prop = new Properties();
                prop.setProperty("connectTimeout", "10000");
                prop.setProperty("socketTimeout", "10000");
                this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName + "?user=" + this.utenteMySql + "&password=" + this.passwordMySql, prop);
            }
            if (this.conn != null) {
                for (int i = 0; i < ricariche.size(); i++) {
                    RicaricaMacchina ricarica = ricariche.get(i);
                    this.conn.setAutoCommit(false);
                    PreparedStatement pst = (PreparedStatement) this.conn.prepareStatement("INSERT INTO RICARICAMACCHINA (IDMACCHINA, IDARTICOLO, QTACONFEZIONE, KG, DATARICARICA, COSTOACQUISTO, PREZZOVENDITA) " + "VALUES (?, ?, ?, ?, ?, ?, ?)");
                    pst.setInt(1, ricarica.getIdMacchina());
                    pst.setInt(2, ricarica.getIdArticolo());
                    if (ricarica.getQtaConfezione() != 0) {
                        pst.setFloat(3, (float) ricarica.getQtaConfezione());
                    } else {
                        pst.setNull(3, 6);
                    }
                    if (ricarica.getKG() != 0.0f) {
                        pst.setFloat(4, ricarica.getKG());
                    } else {
                        pst.setNull(4, 6);
                    }
                    Date date = new Date();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(ricarica.getDataRicarica());
                        System.out.println(date);
                    } catch (android.net.ParseException e) {
                        e.printStackTrace();
                    }
                    pst.setDate(5, new java.sql.Date(date.getTime()));
                    if (ricarica.getCostoAcquisto() != 0.0f) {
                        pst.setFloat(6, ricarica.getCostoAcquisto());
                    } else {
                        pst.setNull(6, 6);
                    }
                    if (ricarica.getPrezzoVendita() != 0.0f) {
                        pst.setFloat(7, ricarica.getCostoAcquisto());
                    } else {
                        pst.setNull(7, 6);
                    }
                    pst.execute();
                    PreparedStatement pst2 = (PreparedStatement) this.conn.prepareStatement(("UPDATE MAGAZZINO SET QTACONFEZIONIRESIDUA = (QTACONFEZIONIRESIDUA - ?), " + "KGRESIDUA = (KGRESIDUA - ?) ") + "WHERE IDARTICOLO = ?");
                    pst2.setFloat(1, (float) ricarica.getQtaConfezione());
                    pst2.setFloat(2, ricarica.getKG());
                    pst2.setInt(3, ricarica.getIdArticolo());
                    pst2.execute();
                    this.conn.commit();
                    this.dbAdapter.ricaricaTrasmessa(String.valueOf(ricarica.getIdRigaRicarica()));
                }
                str = "OK";
                if (this.conn != null && !this.conn.isClosed()) {
                    this.conn.close();
                }
            } else {
                str = "Errore";
                if (this.conn != null && !this.conn.isClosed()) {
                    this.conn.close();
                }
            }
        } catch (SQLException e2) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (ClassNotFoundException e3) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (InstantiationException e4) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (IllegalAccessException e5) {
            str = "OK";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (Throwable th) {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
            throw th;
        }
        return str;
    }

    public String InsertRicavoDistributore(List<RicavoMacchina> ricavi) throws SQLException, ParseException {
        String str;
        this.dbAdapter = new DbAdapter(this.context);
        this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
        this.porta = this.dbAdapter.getPorta();
        dbName = this.dbAdapter.getNomeBaseDati();
        this.utenteMySql = this.dbAdapter.getUtenteMySql();
        this.passwordMySql = this.dbAdapter.getPasswordMySql();
        try {
            if (this.indirizzoIP == "" || this.porta == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                this.conn = null;
            } else {
                Class.forName(driver).newInstance();
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                Properties prop = new Properties();
                prop.setProperty("connectTimeout", "10000");
                prop.setProperty("socketTimeout", "10000");
                this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName + "?user=" + this.utenteMySql + "&password=" + this.passwordMySql, prop);
            }
            if (this.conn != null) {
                for (int i = 0; i < ricavi.size(); i++) {
                    RicavoMacchina ricavo = ricavi.get(i);
                    this.conn.setAutoCommit(false);
                    PreparedStatement pst = (PreparedStatement) this.conn.prepareStatement("INSERT INTO RICAVOMACCHINA (IDMACCHINA, DATASCARICOSOLDI, SOLDISCARICATI, NBATTUTECALDO,NBATTUTE ) " + "VALUES (?, ?, ?, ?, ?)");
                    pst.setInt(1, ricavo.getIdMacchina());
                    Date date = new Date();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(ricavo.getDataScaricoSoldi());
                        System.out.println(date);
                    } catch (android.net.ParseException e) {
                        e.printStackTrace();
                    }
                    pst.setDate(2, new java.sql.Date(date.getTime()));
                    if (ricavo.getSoldiScaricati() != 0.0f) {
                        pst.setFloat(3, ricavo.getSoldiScaricati());
                    } else {
                        pst.setNull(3, 6);
                    }
                    if (ricavo.getBattuteCaldo() != 0) {
                        pst.setInt(4, ricavo.getBattuteCaldo());
                    } else {
                        pst.setNull(4, 4);
                    }
                    if (ricavo.getBattuteFreddo() != 0) {
                        pst.setInt(5, ricavo.getBattuteFreddo());
                    } else {
                        pst.setNull(5, 4);
                    }
                    pst.execute();
                    this.conn.commit();
                    this.dbAdapter.ricavoTrasmesso(String.valueOf(ricavo.getIdRigaRicavo()));
                }
                str = "OK";
                if (this.conn != null && !this.conn.isClosed()) {
                    this.conn.close();
                }
            } else {
                str = "Errore";
                if (this.conn != null && !this.conn.isClosed()) {
                    this.conn.close();
                }
            }
        } catch (SQLException e2) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (ClassNotFoundException e3) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (InstantiationException e4) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (IllegalAccessException e5) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "OK";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (Throwable th) {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
            throw th;
        }
        return str;
    }

    public String InsertManutenzione(List<InsManutenzione> insManutenziones) throws SQLException, ParseException {
        String str;
        this.dbAdapter = new DbAdapter(this.context);
        this.indirizzoIP = this.dbAdapter.getIndirizzoIp();
        this.porta = this.dbAdapter.getPorta();
        dbName = this.dbAdapter.getNomeBaseDati();
        this.utenteMySql = this.dbAdapter.getUtenteMySql();
        this.passwordMySql = this.dbAdapter.getPasswordMySql();
        try {
            if (this.indirizzoIP == "" || this.porta == "" || this.utenteMySql == "" || this.passwordMySql == "") {
                this.conn = null;
            } else {
                Class.forName(driver).newInstance();
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                Properties prop = new Properties();
                prop.setProperty("connectTimeout", "10000");
                prop.setProperty("socketTimeout", "10000");
                this.conn = (Connection) DriverManager.getConnection(("jdbc:mysql://" + this.indirizzoIP + ":" + this.porta + "/") + dbName + "?user=" + this.utenteMySql + "&password=" + this.passwordMySql, prop);
            }
            if (this.conn != null) {
                for (int i = 0; i < insManutenziones.size(); i++) {
                    InsManutenzione insmanutenzione = insManutenziones.get(i);
                    this.conn.setAutoCommit(false);
                    PreparedStatement pst = (PreparedStatement) this.conn.prepareStatement("INSERT INTO MANUTENZIONE (IDMACCHINA,DATAMANUTENZIONE,DESCRIZIONE,nbattute,note,IDTipoManutenzione) " + "VALUES (?, ?, ?, ?, ?, ?)");
                    pst.setInt(1, insmanutenzione.getIdMacchina());
                    Date date = new Date();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(insmanutenzione.getDataManutenzione());
                        System.out.println(date);
                    } catch (android.net.ParseException e) {
                        e.printStackTrace();
                    }
                    pst.setDate(2, new java.sql.Date(date.getTime()));
                    if (insmanutenzione.getDescrizione() != null) {
                        pst.setString(3, insmanutenzione.getDescrizione());
                    } else {
                        pst.setNull(3, 12);
                    }
                    if (insmanutenzione.getNBattute() != 0) {
                        pst.setInt(4, insmanutenzione.getNBattute());
                    } else {
                        pst.setNull(4, 4);
                    }
                    if (insmanutenzione.getNote() != null) {
                        pst.setString(5, insmanutenzione.getNote());
                    } else {
                        pst.setNull(5, 12);
                    }
                    if (insmanutenzione.getIDTipoManutenzione() != 0) {
                        pst.setInt(6, insmanutenzione.getIDTipoManutenzione());
                    } else {
                        pst.setNull(6, 4);
                    }
                    pst.execute();
                    this.conn.commit();
                    this.dbAdapter.manutenzioneTrasmesso(String.valueOf(insmanutenzione.getIdManutenzione()));
                }
                str = "OK";
                if (this.conn != null && !this.conn.isClosed()) {
                    this.conn.close();
                }
            } else {
                str = "Errore";
                if (this.conn != null && !this.conn.isClosed()) {
                    this.conn.close();
                }
            }
        } catch (SQLException e2) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (ClassNotFoundException e3) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (InstantiationException e4) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "Errore";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (IllegalAccessException e5) {
            if (this.conn != null) {
                this.conn.rollback();
            }
            str = "OK";
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (Throwable th) {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
            throw th;
        }
        return str;
    }

    public String FormattaData(String data) {
        try {
            return data.substring(6, 4) + "-" + data.substring(3, 2) + "-" + data.substring(0, 2);
        } catch (Exception e) {
            return "";
        }
    }
}

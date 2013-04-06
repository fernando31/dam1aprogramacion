package CuentaHogar;

import com.toedter.calendar.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Clase CuentaHogar (Parte grafica y control de datos del programa principal)
 * @author Fernando J. Gonzalez Lopez
 * @version 0.1
 */
public class CuentaHogar extends javax.swing.JFrame {
    
    
/*-----------------------------VARIABLES---------------------------------*/   
    
    private String bbdd = "CUENTAHOGAR";
    private String login = "fernando";
    private String password = "fernando";
    private ConexionBBDD conectionBBDD = new ConexionBBDD(bbdd, login, password);    
    private ElementosDinamicos ed = new ElementosDinamicos(bbdd, login, password);
    private Sentencias sent = new Sentencias(bbdd, login, password);
    private String tipoMov;
    private int id;
    private static final String B2PG = "btn2pG";
    private static final String B2PI = "btn2pI";
    private static final String B2PT = "btn2pT";
    private static final String B2PTM = "btn2pTM";
    private static final String B2PFTM = "btn2pFiltroTM";


    public String getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(String TipoMov) {
        this.tipoMov = TipoMov;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

/*------------------------------CONSTRUCTOR ENTORNO GRAFICO---------------------------------*/ 
    
    /**
     * Creates new form CuentaHogar
     */
    public CuentaHogar() {
        
        this.dimensionarVentanas();
        
        try {
            this.conectarBBDD();
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
 
    private void dimensionarVentanas(){
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(d.width / 2 - (d.width / 2) / 2, d.height / 2 - ((d.height / 2)) / 2);
        initComponents();

        int anchoVC = 655;
        int altoVC = 345;
        vCtas.setSize(anchoVC, altoVC);
        vCtas.setLocation(d.width / 2 - anchoVC / 2, d.height / 2 - altoVC / 2);

        int anchoVG = 600;
        int altoVG = 430;
        vMov.setSize(anchoVG, altoVG);
        vMov.setLocation(d.width / 2 - anchoVG / 2, d.height / 2 - altoVG / 2);

        int anchoVE = 545;
        int altoVE = 160;
        vEditar.setSize(anchoVE, altoVE);
        vEditar.setLocation(d.width / 2 - anchoVE / 2, d.height / 2 - altoVE / 2);
    }
 
    private void conectarBBDD() throws Exception{

        if(this.comprobarConexion()){
            barraMenu.setEnabled(true);
            ed.estadoComponente(barraMenu, true);
            barraHerramientas.setEnabled(true);
            btn2pTM.setVisible(false);
            btn2pTM.setFocusable(false);
            ed.estadoComponente(barraHerramientas, true);
            
            btn2pFiltroTM.setEnabled(true);
            pFiltro.setEnabled(true);
            pFiltro.setVisible(false);
            
            btnGraficaTM.setEnabled(true);
            btnImprimirTM.setEnabled(true);
            btn2pG.setSelected(true);
            this.gestionarBtns2p(B2PG);
            
            sent.cargarListaMov(this.getTipoMov(), sent.getSinFiltro(), tMov, true);
            sent.cargarTablaCuentas(tablaCuentasVC, despCtas, true);
            sent.cargarTablaTipoMov(sent.getG(), tTipoMovVM, despMov, true);
            this.controlarDobleClickTabla(tMov);
            
            ed.añadirDesplegable(tablaCuentasVC, despTrasOrigen, 0);
            ed.añadirDesplegable(tablaCuentasVC, despTrasDest, 0);
            
        } else {
            conectionBBDD.desconectar();
        }
    }    
    
    
    private boolean comprobarConexion() throws Exception{
        try {
            Connection link = conectionBBDD.conectar();
            return true;      
        } catch (Exception ex) {       
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al conectar con la BBDD", ex);
            return false;
        }
        
    }
    
    
/*------------------------------GESTION DE BOTONES DEL ENTORNO GRAFICO---------------------------------*/ 
 
    
    private void gestionarBtns2p(String boton) throws Exception {

        if (!vEditar.isVisible()) {
            switch (boton) {
                case "btn2pG":
                    this.gestionarBtn2pG();
                    break;
                case "btn2pI":
                    this.gestionarBtn2pI();
                    break;
                case "btn2pT":
                    this.gestionarBtn2pT();
                    break;
                case "btn2pTM":
                    this.gestionarBtn2pTM();
                    break;
                case "btn2pFiltroTM":
                    this.gestionarBtn2pFiltroTM();
                    break;
                default:
                    Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, "gestionarBtns2p(String boton);");
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ventana de Edición abierta");
        }
    }
    
    private void estadoBtn2p(boolean estado){
        btn2pG.setEnabled(estado);
        btn2pI.setEnabled(estado);
        btn2pT.setEnabled(estado);
        btn2pTM.setEnabled(estado);
        btn2pFiltroTM.setEnabled(estado);
    }
    
    private void activarBtn2p(JToggleButton btn2p){

        btn2pG.setSelected(false);
        btn2pI.setSelected(false);
        btn2pT.setSelected(false);
        btn2pTM.setSelected(false);
        btn2pFiltroTM.setSelected(false);
        
        btn2p.setSelected(true);
   
    }
    
    private void activarBtn2pG() throws Exception {
        ed.estadoComponente(pMov, true);
        pBajoMov.setBackground(Color.RED);
        pMov.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gastos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        etTipoMov.setText("Gasto");

        smG.setEnabled(false);
        smI.setEnabled(true);

        smAddTG.setEnabled(true);
        smAddTI.setEnabled(false);

        pTM.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de Gastos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        sent.cargarListaMov(this.getTipoMov(), sent.getSinFiltro(), tMov, true);
        
       /*
        despCtas.setBackground(Color.RED);
        despMov.setBackground(Color.RED);
        btnTipoMov.setBackground(Color.RED);
       */
        
        this.mostrarVentTipoMov(sent.getG());
    }
    
    private void activarBtn2pI() throws Exception {
        ed.estadoComponente(pMov, true);
        pBajoMov.setBackground(Color.GREEN);
        pMov.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ingresos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        etTipoMov.setText("Ingreso");

        smI.setEnabled(false);
        smG.setEnabled(true);

        smAddTI.setEnabled(true);
        smAddTG.setEnabled(false);

        pTM.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de Ingresos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        sent.cargarListaMov(this.getTipoMov(), sent.getSinFiltro(), tMov, true);

       /* 
        despCtas.setBackground(Color.GREEN);
        despMov.setBackground(Color.GREEN);
        btnTipoMov.setBackground(Color.GREEN);
      */
        
        this.mostrarVentTipoMov(sent.getI());
    }
    
    private void activarBtn2pT() throws Exception {
        ed.estadoComponente(pTras, true);
        pBajoTras.setBackground(Color.GRAY);

        smT.setEnabled(false);

        smAddCuenta.setEnabled(false);
        smAddTG.setEnabled(false);
        smAddTI.setEnabled(false);

        pTM.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de Traspasos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        sent.cargarListaMov(this.getTipoMov(), sent.getSinFiltro(), tMov, true);
    }
    
    
    private void activarBtn2pTM() throws Exception {

        pTM.setEnabled(true);
        btnEditarTM.setEnabled(true);
        btnEliminarTM.setEnabled(true);
        btnGraficaTM.setEnabled(false);
        btnImprimirTM.setEnabled(false);
        btn2pFiltroTM.setEnabled(false);
        pFiltro.setEnabled(false);
        ed.estadoComponente(pFiltro, false);

        tMov.setComponentPopupMenu(jPpMenuTablas);

        if (this.getTipoMov().equals(sent.getG())) {
            pBajoTM.setBackground(Color.RED);
        } else if (this.getTipoMov().equals(sent.getI())) {
            pBajoTM.setBackground(Color.GREEN);
        } else if (this.getTipoMov().equals(sent.getT())) {
            pBajoTM.setBackground(Color.GRAY);
        }

    }
    
    private void activarbtn2pFiltroTM() throws Exception {
        ed.estadoComponente(pFiltro, true);
        pFiltro.setVisible(true);
        btn2pFiltroTM.setEnabled(false);
        btnGraficaTM.setEnabled(false);
        btnImprimirTM.setEnabled(false);
        smFiltro.setEnabled(false);

        if (this.getTipoMov().equals(sent.getG())) {
            pBajoTM.setBackground(Color.RED);
        } else if (this.getTipoMov().equals(sent.getI())) {
            pBajoTM.setBackground(Color.GREEN);
        } else if (this.getTipoMov().equals(sent.getT())) {
            pBajoTM.setBackground(Color.GRAY);
        }
    }
    
    private void desactivarBtn2pG_I() throws Exception {
        ed.estadoComponente(pMov, false);
        pBajoMov.setBackground(Color.LIGHT_GRAY);

        smG.setEnabled(true);
        smI.setEnabled(true);
    }
    
    private void desctivarBtn2pT() throws Exception {
        ed.estadoComponente(pTras, false);
        pBajoTras.setBackground(Color.LIGHT_GRAY);

        smT.setEnabled(true);
        smAddCuenta.setEnabled(true);
    }
    
    private void desactivarBtn2pTM() throws Exception {
        pTM.setEnabled(false);
        btnEditarTM.setEnabled(false);
        btnEliminarTM.setEnabled(false);
        btnGraficaTM.setEnabled(true);
        btnImprimirTM.setEnabled(true);
        btn2pFiltroTM.setEnabled(true);
        pFiltro.setEnabled(true);
        ed.estadoComponente(pFiltro, true);

        tMov.setComponentPopupMenu(null);

        pBajoTM.setBackground(Color.LIGHT_GRAY);
    }
    
    private void desactivarbtn2pFiltroTM() throws Exception {
        ed.estadoComponente(pFiltro, false);
        pFiltro.setVisible(false);
        smFiltro.setEnabled(true);
        this.cargarEtFiltroTM();
    }
    
    private void gestionarBtn2pG() throws Exception {
        this.setTipoMov(sent.getG());
        this.activarBtn2p(btn2pG);
        this.activarBtn2pG();
        this.desctivarBtn2pT();
        this.desactivarBtn2pTM();
        this.desactivarbtn2pFiltroTM();
    }
    
    private void gestionarBtn2pI() throws Exception {
        this.setTipoMov(sent.getI());
        this.activarBtn2p(btn2pI);
        this.activarBtn2pI();
        this.desctivarBtn2pT();
        this.desactivarBtn2pTM();
        this.desactivarbtn2pFiltroTM();
    }
    
    private void gestionarBtn2pT() throws Exception {
        this.setTipoMov(sent.getT());
        this.activarBtn2p(btn2pT);
        this.activarBtn2pT();
        this.desactivarBtn2pG_I();
        this.desactivarBtn2pTM();
        this.desactivarbtn2pFiltroTM();
    }
    
    private void gestionarBtn2pTM() throws Exception {
        this.activarBtn2p(btn2pTM);
        this.activarBtn2pTM();
        this.desctivarBtn2pT();
        this.desactivarBtn2pG_I();
        this.desactivarbtn2pFiltroTM();
    }
    
    public void gestionarBtn2pFiltroTM() throws Exception {

        this.activarBtn2p(btn2pFiltroTM);
        this.activarbtn2pFiltroTM();
        this.desactivarBtn2pG_I();
        this.desctivarBtn2pT();
    }

    
/*------------------------------METODOS ESPECIFICOS DEL ENTORNO GRAFICO---------------------------------*/            
        

    private void crearMov(JComboBox comboBox1, JComboBox comboBox2, JDateChooser date, JTextField textField) throws Exception{
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                    
        String cuenta = (String) comboBox1.getSelectedItem();
        String movimiento = (String) comboBox2.getSelectedItem();
        String fecha = formatoFecha.format((Date) date.getDate());
        String importe = (String) textField.getText();       
        
        String[] valores = {cuenta, movimiento, fecha, importe};
        String tipo = this.getTipoMov();
        int id = sent.comprobarMovID(tipo, valores);
        
        sent.insertarMov(tipo, id , valores, tMov, true);
        sent.cargarListaMov(tipo, sent.getSinFiltro(), tMov, true);
        
        date.setDate(null);
        textField.setText("");
    }

    private void mostrarVentTipoMov(String tipo) throws Exception {

        sent.cargarTablaTipoMov(tipo, tTipoMovVM, despMov, true);

        if (tipo.equals(sent.getG())) {
            vMov.setTitle(sent.getG().toUpperCase());
            pBajoTMovVM.setBackground(Color.RED);
            pTMovVM.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gastos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            etTMovVM.setText("Nombre Gasto");

        } else if (tipo.equals(sent.getI())) {
            vMov.setTitle(sent.getI().toUpperCase());
            pBajoTMovVM.setBackground(Color.GREEN);
            pTMovVM.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ingresos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            etTMovVM.setText("Nombre Ingreso");
        }
    }
         
    public void cargarTablaEditarMov(String tipo, JTable tabla) throws Exception{

        if (tabla.getSelectedRowCount() > 0) {

            vEditar.setVisible(true);
            
            /*
            vEditar.setUndecorated(true);
            vEditar.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            */
            
            vEditar.setResizable(false);
            vEditar.setFocusableWindowState(false);
            
            tMov.setEnabled(false);
            btnEliminarTM.setEnabled(false);
            btnEditarTM.setEnabled(false);
            tMov.setComponentPopupMenu(null);
            this.estadoBtn2p(false);
                        
            String[] valores = ed.recuperarValoresFila(tabla);

            if (valores != null) {

                int id = sent.comprobarMovID(tipo, valores);
                this.setId(id);
                
                sent.cargarListaMov(tipo, "AND M.ID_MOVIMIENTO = " + id + " ", tablaEditar, false);

                TableColumn cuenta = tablaEditar.getColumnModel().getColumn(0);
                JComboBox cuentas = new JComboBox();
                cuentas.removeAllItems();
                for (int i = 0; i < despCtas.getItemCount(); i++) {
                    cuentas.addItem(despCtas.getItemAt(i));
                }
                cuenta.setCellEditor(new DefaultCellEditor(cuentas));
               
                TableColumn movimiento = tablaEditar.getColumnModel().getColumn(1);
                JComboBox movimientos = new JComboBox();
                movimientos.removeAllItems();              
                if (tipo.equals(sent.getG()) || tipo.equals(sent.getI())) {
                    for (int i = 0; i < despMov.getItemCount(); i++) {
                        movimientos.addItem(despMov.getItemAt(i));
                    }
                    movimiento.setCellEditor(new DefaultCellEditor(movimientos));
                } else if (tipo.equals(sent.getT())) {
                    for (int i = 0; i < despCtas.getItemCount(); i++) {
                        movimientos.addItem(despCtas.getItemAt(i));
                        movimiento.setCellEditor(new DefaultCellEditor(movimientos));

                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ninguna fila seleccionada");
        }
    }
 
    private void controlarDobleClickTabla(final JTable tabla) {
        //Controlamos el doble click en la tabla
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        cargarTablaEditarMov(getTipoMov(), tabla);
                    } catch (Exception ex) {
                        Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
                        ed.mostrarError("Fallo al gestionar Doble Click", ex);
                    }
                }
            }
        });
    }
    

/*------------------------------FILTROS---------------------------------*/      

    
    private void cargarEtFiltroTM(){
         DefaultTableModel dtm = (DefaultTableModel) tMov.getModel();   
         etFiltroCol0.setText(dtm.getColumnName(0));
         txtFiltroCol0.setText("");
         etFiltroCol1.setText(dtm.getColumnName(1));
         txtFiltroCol1.setText("");
         etFiltroCol2.setText(dtm.getColumnName(2));
         txtFiltroCol2.setText("");
         etFiltroCol3.setText(dtm.getColumnName(3));
         txtFiltroCol3.setText("");
    }
    
    private void cargarFiltroTM(){
        String[] campos = {txtFiltroCol0.getText(), txtFiltroCol1.getText(), txtFiltroCol2.getText(), txtFiltroCol3.getText()};
        this.cargarFiltro(tMov, campos);
    }
    
    private void cargarFiltro(JTable tabla, String[] campos) {
        
        DefaultTableModel dtm = (DefaultTableModel) tabla.getModel();
        TableRowSorter<TableModel> trs = new TableRowSorter<TableModel>(dtm);
        tabla.setRowSorter(trs);
                
        ArrayList lista = new ArrayList();
        for (int  i = 0; i < campos.length; i++){
            lista.add(RowFilter.regexFilter("(?i).*" + campos[i] + ".*", i));
        }

        RowFilter filtroAnd = RowFilter.andFilter(lista);
        
        trs.setRowFilter(filtroAnd);
    }
    
    
    
    
/*------------------------------ELEMENTOS Y LISENERS---------------------------------*/     
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        vCtas = new javax.swing.JFrame();
        panelGeneralVC = new javax.swing.JPanel();
        panelBajoCuentasVC = new javax.swing.JPanel();
        panelCuentasVC = new javax.swing.JPanel();
        btnEliminarVC = new javax.swing.JButton();
        btnEditarVC = new javax.swing.JButton();
        btnCrearVC = new javax.swing.JButton();
        txtCuentaVC = new javax.swing.JTextField();
        etiqCuentaVC = new javax.swing.JLabel();
        panleBajoTablaCuentasVC = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCuentasVC = new javax.swing.JTable();
        btnSalirVC = new javax.swing.JButton();
        txtNumCuentaVC1 = new javax.swing.JTextField();
        checkNumCuentaVC = new javax.swing.JCheckBox();
        etiqCapitalInicialVC = new javax.swing.JLabel();
        txtCapitalInicialVC = new javax.swing.JTextField();
        etiqEurVC = new javax.swing.JLabel();
        txtNumCuentaVC2 = new javax.swing.JTextField();
        txtNumCuentaVC3 = new javax.swing.JTextField();
        txtNumCuentaVC4 = new javax.swing.JTextField();
        vMov = new javax.swing.JFrame();
        pGralVM = new javax.swing.JPanel();
        pBajoTMovVM = new javax.swing.JPanel();
        pTMovVM = new javax.swing.JPanel();
        btnEliminarVM = new javax.swing.JButton();
        btnEditarVM = new javax.swing.JButton();
        btnCrearVM = new javax.swing.JButton();
        txtTMovVM = new javax.swing.JTextField();
        etTMovVM = new javax.swing.JLabel();
        pBajoTTMovVM = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tTipoMovVM = new javax.swing.JTable();
        btnSalirVM = new javax.swing.JButton();
        jPpMenuTablas = new javax.swing.JPopupMenu();
        subMenuEmEditar = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        subMenuEmEliminar = new javax.swing.JMenuItem();
        vEditar = new javax.swing.JFrame();
        panelBajoEditarVE = new javax.swing.JPanel();
        panelEditarVE = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaEditar = new javax.swing.JTable();
        btnCancelarVE = new javax.swing.JButton();
        btnAceptarVE = new javax.swing.JButton();
        pGral = new javax.swing.JPanel();
        pBajoMov = new javax.swing.JPanel();
        pMov = new javax.swing.JPanel();
        despMov = new javax.swing.JComboBox();
        etTipoMov = new javax.swing.JLabel();
        btnTipoMov = new javax.swing.JButton();
        etImpMov = new javax.swing.JLabel();
        etFechaMov = new javax.swing.JLabel();
        txtImpMov = new javax.swing.JTextField();
        etEur = new javax.swing.JLabel();
        txtFechaMov = new com.toedter.calendar.JDateChooser();
        btnAceptarMov = new javax.swing.JButton();
        btnCtas = new javax.swing.JButton();
        etCtas = new javax.swing.JLabel();
        despCtas = new javax.swing.JComboBox();
        pBajoTM = new javax.swing.JPanel();
        pTM = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tMov = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        btnEliminarTM = new javax.swing.JButton();
        btnGraficaTM = new javax.swing.JButton();
        btnEditarTM = new javax.swing.JButton();
        pFiltro = new javax.swing.JPanel();
        txtFiltroCol0 = new javax.swing.JTextField();
        txtFiltroCol1 = new javax.swing.JTextField();
        txtFiltroCol2 = new javax.swing.JTextField();
        txtFiltroCol3 = new javax.swing.JTextField();
        etFiltroCol0 = new javax.swing.JLabel();
        etFiltroCol1 = new javax.swing.JLabel();
        etFiltroCol2 = new javax.swing.JLabel();
        etFiltroCol3 = new javax.swing.JLabel();
        despFiltroCol3 = new javax.swing.JComboBox();
        despFiltroCol2 = new javax.swing.JComboBox();
        btn2pFiltroTM = new javax.swing.JToggleButton();
        btnImprimirTM = new javax.swing.JButton();
        barraHerramientas = new javax.swing.JToolBar();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btn2pG = new javax.swing.JToggleButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btn2pT = new javax.swing.JToggleButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        btn2pI = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn2pTM = new javax.swing.JToggleButton();
        pBajoTras = new javax.swing.JPanel();
        pTras = new javax.swing.JPanel();
        despTrasOrigen = new javax.swing.JComboBox();
        etTrasOrigen = new javax.swing.JLabel();
        etImpTras = new javax.swing.JLabel();
        etFechaTras = new javax.swing.JLabel();
        txtImpTras = new javax.swing.JTextField();
        etEur3 = new javax.swing.JLabel();
        txtFechaTras = new com.toedter.calendar.JDateChooser();
        btnAceptarTras = new javax.swing.JButton();
        despTrasDest = new javax.swing.JComboBox();
        etTrasDest = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        mArchivo = new javax.swing.JMenu();
        smAbrirArchivo = new javax.swing.JMenuItem();
        smGuardarArchivo = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        smImprimir = new javax.swing.JMenu();
        smImprmirMovimientos = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        smImprmirGastos = new javax.swing.JMenuItem();
        smImprmirIngresos = new javax.swing.JMenuItem();
        smImprmirTraspasos = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        smG = new javax.swing.JMenuItem();
        smT = new javax.swing.JMenuItem();
        smI = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        smSalir = new javax.swing.JMenuItem();
        mEdicion = new javax.swing.JMenu();
        smAddCuenta = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        smAddTG = new javax.swing.JMenuItem();
        smAddTI = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        smFiltro = new javax.swing.JMenuItem();
        mAyuda = new javax.swing.JMenu();
        subMenuAcercaDe = new javax.swing.JMenuItem();

        vCtas.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        vCtas.setTitle("CUENTAS");
        vCtas.setResizable(false);

        panelBajoCuentasVC.setBackground(new java.awt.Color(128, 128, 128));

        panelCuentasVC.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuentas"));

        btnEliminarVC.setText("Eliminar");
        btnEliminarVC.setEnabled(false);

        btnEditarVC.setText("Editar");
        btnEditarVC.setEnabled(false);

        btnCrearVC.setText("Crear");

        etiqCuentaVC.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etiqCuentaVC.setText("Nombre Cuenta");

        panleBajoTablaCuentasVC.setBackground(new java.awt.Color(192, 192, 192));

        tablaCuentasVC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaCuentasVC.setEnabled(false);
        tablaCuentasVC.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaCuentasVC);

        javax.swing.GroupLayout panleBajoTablaCuentasVCLayout = new javax.swing.GroupLayout(panleBajoTablaCuentasVC);
        panleBajoTablaCuentasVC.setLayout(panleBajoTablaCuentasVCLayout);
        panleBajoTablaCuentasVCLayout.setHorizontalGroup(
            panleBajoTablaCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panleBajoTablaCuentasVCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        panleBajoTablaCuentasVCLayout.setVerticalGroup(
            panleBajoTablaCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panleBajoTablaCuentasVCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnSalirVC.setText("Salir");
        btnSalirVC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirVCActionPerformed(evt);
            }
        });

        txtNumCuentaVC1.setToolTipText("");
        txtNumCuentaVC1.setEnabled(false);

        checkNumCuentaVC.setText("Numero Cuenta (Opcional)");

        etiqCapitalInicialVC.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etiqCapitalInicialVC.setText("Capital Inicial");

        etiqEurVC.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etiqEurVC.setText("Eur");

        txtNumCuentaVC2.setEnabled(false);

        txtNumCuentaVC3.setEnabled(false);

        txtNumCuentaVC4.setEnabled(false);

        javax.swing.GroupLayout panelCuentasVCLayout = new javax.swing.GroupLayout(panelCuentasVC);
        panelCuentasVC.setLayout(panelCuentasVCLayout);
        panelCuentasVCLayout.setHorizontalGroup(
            panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panleBajoTablaCuentasVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCuentasVCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCuentasVCLayout.createSequentialGroup()
                        .addComponent(btnEditarVC, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarVC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalirVC, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCuentasVCLayout.createSequentialGroup()
                        .addGroup(panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCuentasVCLayout.createSequentialGroup()
                                .addComponent(etiqCuentaVC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCuentaVC, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etiqCapitalInicialVC))
                            .addGroup(panelCuentasVCLayout.createSequentialGroup()
                                .addComponent(checkNumCuentaVC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumCuentaVC1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumCuentaVC2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addGroup(panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCuentasVCLayout.createSequentialGroup()
                                .addComponent(txtCapitalInicialVC, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etiqEurVC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(btnCrearVC, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCuentasVCLayout.createSequentialGroup()
                                .addComponent(txtNumCuentaVC3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumCuentaVC4)))))
                .addContainerGap())
        );
        panelCuentasVCLayout.setVerticalGroup(
            panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCuentasVCLayout.createSequentialGroup()
                .addGroup(panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCuentaVC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etiqCuentaVC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCrearVC)
                    .addComponent(etiqCapitalInicialVC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCapitalInicialVC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etiqEurVC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkNumCuentaVC)
                    .addComponent(txtNumCuentaVC1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumCuentaVC2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumCuentaVC3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumCuentaVC4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panleBajoTablaCuentasVC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btnEliminarVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalirVC)
                    .addComponent(btnEditarVC)))
        );

        javax.swing.GroupLayout panelBajoCuentasVCLayout = new javax.swing.GroupLayout(panelBajoCuentasVC);
        panelBajoCuentasVC.setLayout(panelBajoCuentasVCLayout);
        panelBajoCuentasVCLayout.setHorizontalGroup(
            panelBajoCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBajoCuentasVCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCuentasVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBajoCuentasVCLayout.setVerticalGroup(
            panelBajoCuentasVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBajoCuentasVCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCuentasVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelGeneralVCLayout = new javax.swing.GroupLayout(panelGeneralVC);
        panelGeneralVC.setLayout(panelGeneralVCLayout);
        panelGeneralVCLayout.setHorizontalGroup(
            panelGeneralVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGeneralVCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBajoCuentasVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelGeneralVCLayout.setVerticalGroup(
            panelGeneralVCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralVCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBajoCuentasVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout vCtasLayout = new javax.swing.GroupLayout(vCtas.getContentPane());
        vCtas.getContentPane().setLayout(vCtasLayout);
        vCtasLayout.setHorizontalGroup(
            vCtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGeneralVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        vCtasLayout.setVerticalGroup(
            vCtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGeneralVC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        vMov.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        vMov.setTitle("GASTOS");

        pBajoTMovVM.setBackground(new java.awt.Color(255, 0, 0));

        pTMovVM.setBorder(javax.swing.BorderFactory.createTitledBorder("Gastos"));

        btnEliminarVM.setText("Eliminar");
        btnEliminarVM.setEnabled(false);

        btnEditarVM.setText("Editar");
        btnEditarVM.setEnabled(false);

        btnCrearVM.setText("Crear");

        etTMovVM.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etTMovVM.setText("Nombre Gasto");

        pBajoTTMovVM.setBackground(new java.awt.Color(192, 192, 192));

        tTipoMovVM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tTipoMovVM.setEnabled(false);
        tTipoMovVM.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tTipoMovVM);

        javax.swing.GroupLayout pBajoTTMovVMLayout = new javax.swing.GroupLayout(pBajoTTMovVM);
        pBajoTTMovVM.setLayout(pBajoTTMovVMLayout);
        pBajoTTMovVMLayout.setHorizontalGroup(
            pBajoTTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTTMovVMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );
        pBajoTTMovVMLayout.setVerticalGroup(
            pBajoTTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pBajoTTMovVMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnSalirVM.setText("Salir");
        btnSalirVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirVMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pTMovVMLayout = new javax.swing.GroupLayout(pTMovVM);
        pTMovVM.setLayout(pTMovVMLayout);
        pTMovVMLayout.setHorizontalGroup(
            pTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pBajoTTMovVM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pTMovVMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTMovVMLayout.createSequentialGroup()
                        .addComponent(btnEditarVM, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarVM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalirVM, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTMovVMLayout.createSequentialGroup()
                        .addComponent(etTMovVM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTMovVM)
                        .addGap(18, 18, 18)
                        .addComponent(btnCrearVM, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pTMovVMLayout.setVerticalGroup(
            pTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTMovVMLayout.createSequentialGroup()
                .addGroup(pTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCrearVM)
                    .addComponent(txtTMovVM, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etTMovVM, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pBajoTTMovVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalirVM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEliminarVM, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEditarVM))))
        );

        javax.swing.GroupLayout pBajoTMovVMLayout = new javax.swing.GroupLayout(pBajoTMovVM);
        pBajoTMovVM.setLayout(pBajoTMovVMLayout);
        pBajoTMovVMLayout.setHorizontalGroup(
            pBajoTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTMovVMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTMovVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pBajoTMovVMLayout.setVerticalGroup(
            pBajoTMovVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTMovVMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTMovVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pGralVMLayout = new javax.swing.GroupLayout(pGralVM);
        pGralVM.setLayout(pGralVMLayout);
        pGralVMLayout.setHorizontalGroup(
            pGralVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGralVMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pBajoTMovVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pGralVMLayout.setVerticalGroup(
            pGralVMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGralVMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pBajoTMovVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout vMovLayout = new javax.swing.GroupLayout(vMov.getContentPane());
        vMov.getContentPane().setLayout(vMovLayout);
        vMovLayout.setHorizontalGroup(
            vMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGralVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        vMovLayout.setVerticalGroup(
            vMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGralVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subMenuEmEditar.setText("Editar");
        subMenuEmEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuEmEditarActionPerformed(evt);
            }
        });
        jPpMenuTablas.add(subMenuEmEditar);
        jPpMenuTablas.add(jSeparator9);

        subMenuEmEliminar.setText("Eliminar");
        subMenuEmEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuEmEliminarActionPerformed(evt);
            }
        });
        jPpMenuTablas.add(subMenuEmEliminar);

        vEditar.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        vEditar.setTitle("EDITAR");
        vEditar.setResizable(false);

        panelBajoEditarVE.setBackground(new java.awt.Color(128, 128, 128));

        panelEditarVE.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tablaEditar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaEditar.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tablaEditar);

        btnCancelarVE.setText("Cancelar");
        btnCancelarVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVEActionPerformed(evt);
            }
        });

        btnAceptarVE.setText("Aceptar");
        btnAceptarVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarVEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEditarVELayout = new javax.swing.GroupLayout(panelEditarVE);
        panelEditarVE.setLayout(panelEditarVELayout);
        panelEditarVELayout.setHorizontalGroup(
            panelEditarVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditarVELayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAceptarVE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelarVE)
                .addContainerGap())
        );
        panelEditarVELayout.setVerticalGroup(
            panelEditarVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditarVELayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEditarVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarVE, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptarVE, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout panelBajoEditarVELayout = new javax.swing.GroupLayout(panelBajoEditarVE);
        panelBajoEditarVE.setLayout(panelBajoEditarVELayout);
        panelBajoEditarVELayout.setHorizontalGroup(
            panelBajoEditarVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBajoEditarVELayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEditarVE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBajoEditarVELayout.setVerticalGroup(
            panelBajoEditarVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBajoEditarVELayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEditarVE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout vEditarLayout = new javax.swing.GroupLayout(vEditar.getContentPane());
        vEditar.getContentPane().setLayout(vEditarLayout);
        vEditarLayout.setHorizontalGroup(
            vEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vEditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBajoEditarVE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        vEditarLayout.setVerticalGroup(
            vEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vEditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBajoEditarVE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CuentaHogar");
        setResizable(false);

        pBajoMov.setBackground(new java.awt.Color(192, 192, 192));
        pBajoMov.setEnabled(false);

        pMov.setBorder(javax.swing.BorderFactory.createTitledBorder("Gastos"));
        pMov.setEnabled(false);

        despMov.setToolTipText("Seleccionar Gastos");
        despMov.setEnabled(false);

        etTipoMov.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etTipoMov.setText("Gasto");
        etTipoMov.setEnabled(false);

        btnTipoMov.setText("...");
        btnTipoMov.setToolTipText("Añadir Tipo Gasto...");
        btnTipoMov.setEnabled(false);
        btnTipoMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoMovActionPerformed(evt);
            }
        });

        etImpMov.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etImpMov.setText("Importe");
        etImpMov.setEnabled(false);

        etFechaMov.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etFechaMov.setText("Fecha");
        etFechaMov.setEnabled(false);

        txtImpMov.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtImpMov.setToolTipText("nnnn,nn");
        txtImpMov.setEnabled(false);

        etEur.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etEur.setText("Eur");
        etEur.setEnabled(false);

        txtFechaMov.setToolTipText("yyyy-MM-dd");
        txtFechaMov.setDateFormatString("yyyy-MM-dd");
        txtFechaMov.setEnabled(false);

        btnAceptarMov.setText("Aceptar");
        btnAceptarMov.setToolTipText("Insertar Datos en Tabla");
        btnAceptarMov.setEnabled(false);
        btnAceptarMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarMovActionPerformed(evt);
            }
        });

        btnCtas.setText("...");
        btnCtas.setToolTipText("Añadir Cuenta...");
        btnCtas.setEnabled(false);
        btnCtas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCtasActionPerformed(evt);
            }
        });

        etCtas.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etCtas.setText("Cuenta");
        etCtas.setEnabled(false);

        despCtas.setToolTipText("Seleccionar Cuenta");
        despCtas.setEnabled(false);

        javax.swing.GroupLayout pMovLayout = new javax.swing.GroupLayout(pMov);
        pMov.setLayout(pMovLayout);
        pMovLayout.setHorizontalGroup(
            pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMovLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pMovLayout.createSequentialGroup()
                        .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etFechaMov)
                            .addComponent(etImpMov))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pMovLayout.createSequentialGroup()
                                .addComponent(txtFechaMov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAceptarMov)
                                .addContainerGap())
                            .addGroup(pMovLayout.createSequentialGroup()
                                .addComponent(txtImpMov)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etEur, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))))
                    .addGroup(pMovLayout.createSequentialGroup()
                        .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etCtas)
                            .addComponent(etTipoMov))
                        .addGap(17, 17, 17)
                        .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(despMov, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(despCtas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCtas)
                            .addComponent(btnTipoMov))
                        .addContainerGap())))
        );
        pMovLayout.setVerticalGroup(
            pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMovLayout.createSequentialGroup()
                .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(despCtas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etCtas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCtas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(despMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etTipoMov, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTipoMov))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtImpMov, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etImpMov, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etEur, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAceptarMov, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFechaMov, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(etFechaMov, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pBajoMovLayout = new javax.swing.GroupLayout(pBajoMov);
        pBajoMov.setLayout(pBajoMovLayout);
        pBajoMovLayout.setHorizontalGroup(
            pBajoMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoMovLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pMov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pBajoMovLayout.setVerticalGroup(
            pBajoMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoMovLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pBajoTM.setBackground(new java.awt.Color(192, 192, 192));
        pBajoTM.setEnabled(false);

        pTM.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de Gastos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        pTM.setEnabled(false);

        tMov.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tMov.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tMov.getTableHeader().setReorderingAllowed(false);
        tMov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tMovMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tMov);

        btnEliminarTM.setText("Eliminar");
        btnEliminarTM.setEnabled(false);
        btnEliminarTM.setFocusable(false);
        btnEliminarTM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarTM.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminarTM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTMActionPerformed(evt);
            }
        });

        btnGraficaTM.setText("Grafica");
        btnGraficaTM.setEnabled(false);
        btnGraficaTM.setFocusable(false);
        btnGraficaTM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGraficaTM.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGraficaTM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficaTMActionPerformed(evt);
            }
        });

        btnEditarTM.setText("Editar");
        btnEditarTM.setEnabled(false);
        btnEditarTM.setFocusable(false);
        btnEditarTM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditarTM.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditarTM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarTMActionPerformed(evt);
            }
        });

        pFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        pFiltro.setEnabled(false);

        txtFiltroCol0.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFiltroCol0.setToolTipText("");
        txtFiltroCol0.setEnabled(false);
        txtFiltroCol0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroCol0KeyReleased(evt);
            }
        });

        txtFiltroCol1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFiltroCol1.setEnabled(false);
        txtFiltroCol1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroCol1KeyReleased(evt);
            }
        });

        txtFiltroCol2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFiltroCol2.setEnabled(false);
        txtFiltroCol2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroCol2KeyReleased(evt);
            }
        });

        txtFiltroCol3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFiltroCol3.setEnabled(false);
        txtFiltroCol3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroCol3KeyReleased(evt);
            }
        });

        etFiltroCol0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etFiltroCol0.setText("etFiltroCol0");
        etFiltroCol0.setEnabled(false);

        etFiltroCol1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etFiltroCol1.setText("etFiltroCol1");
        etFiltroCol1.setEnabled(false);

        etFiltroCol2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etFiltroCol2.setText("etFiltroCol2");
        etFiltroCol2.setEnabled(false);

        etFiltroCol3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etFiltroCol3.setText("etFiltroCol3");
        etFiltroCol3.setEnabled(false);

        despFiltroCol3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", ">=", "<=" }));
        despFiltroCol3.setEnabled(false);

        despFiltroCol2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", ">=", "<=" }));
        despFiltroCol2.setEnabled(false);

        javax.swing.GroupLayout pFiltroLayout = new javax.swing.GroupLayout(pFiltro);
        pFiltro.setLayout(pFiltroLayout);
        pFiltroLayout.setHorizontalGroup(
            pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pFiltroLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(etFiltroCol0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFiltroCol0, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(etFiltroCol1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFiltroCol1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFiltroCol2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pFiltroLayout.createSequentialGroup()
                        .addComponent(etFiltroCol2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(despFiltroCol2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFiltroCol3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pFiltroLayout.createSequentialGroup()
                        .addComponent(etFiltroCol3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(despFiltroCol3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        pFiltroLayout.setVerticalGroup(
            pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pFiltroLayout.createSequentialGroup()
                .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(etFiltroCol0)
                        .addComponent(etFiltroCol1)
                        .addComponent(etFiltroCol2))
                    .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(etFiltroCol3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(despFiltroCol3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(despFiltroCol2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFiltroCol0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiltroCol1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiltroCol2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiltroCol3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn2pFiltroTM.setText("Buscar");
        btn2pFiltroTM.setEnabled(false);
        btn2pFiltroTM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2pFiltroTMActionPerformed(evt);
            }
        });

        btnImprimirTM.setText("Imprimir");
        btnImprimirTM.setEnabled(false);
        btnImprimirTM.setFocusable(false);
        btnImprimirTM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimirTM.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimirTM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirTMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pTMLayout = new javax.swing.GroupLayout(pTM);
        pTM.setLayout(pTMLayout);
        pTMLayout.setHorizontalGroup(
            pTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pFiltro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEliminarTM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditarTM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn2pFiltroTM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimirTM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGraficaTM)
                .addContainerGap())
        );
        pTMLayout.setVerticalGroup(
            pTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTMLayout.createSequentialGroup()
                .addGroup(pTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGraficaTM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarTM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarTM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2pFiltroTM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImprimirTM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pBajoTMLayout = new javax.swing.GroupLayout(pBajoTM);
        pBajoTM.setLayout(pBajoTMLayout);
        pBajoTMLayout.setHorizontalGroup(
            pBajoTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pBajoTMLayout.setVerticalGroup(
            pBajoTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        barraHerramientas.setRollover(true);
        barraHerramientas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        barraHerramientas.setEnabled(false);

        jSeparator3.setEnabled(false);
        barraHerramientas.add(jSeparator3);

        btn2pG.setText("Gastos");
        btn2pG.setEnabled(false);
        btn2pG.setFocusable(false);
        btn2pG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2pG.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn2pG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2pGActionPerformed(evt);
            }
        });
        barraHerramientas.add(btn2pG);

        jSeparator6.setEnabled(false);
        barraHerramientas.add(jSeparator6);

        btn2pT.setText("Traspasos");
        btn2pT.setEnabled(false);
        btn2pT.setFocusable(false);
        btn2pT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2pT.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn2pT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2pTActionPerformed(evt);
            }
        });
        barraHerramientas.add(btn2pT);

        jSeparator13.setEnabled(false);
        barraHerramientas.add(jSeparator13);

        btn2pI.setText("Ingresos");
        btn2pI.setEnabled(false);
        btn2pI.setFocusable(false);
        btn2pI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2pI.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn2pI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2pIActionPerformed(evt);
            }
        });
        barraHerramientas.add(btn2pI);

        jSeparator2.setEnabled(false);
        barraHerramientas.add(jSeparator2);

        btn2pTM.setEnabled(false);
        btn2pTM.setFocusable(false);
        btn2pTM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2pTM.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraHerramientas.add(btn2pTM);

        pBajoTras.setBackground(new java.awt.Color(192, 192, 192));
        pBajoTras.setEnabled(false);

        pTras.setBorder(javax.swing.BorderFactory.createTitledBorder("Traspasos"));
        pTras.setToolTipText("");
        pTras.setEnabled(false);

        despTrasOrigen.setBackground(new java.awt.Color(255, 0, 0));
        despTrasOrigen.setToolTipText("Seleccionar Gastos");
        despTrasOrigen.setEnabled(false);

        etTrasOrigen.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etTrasOrigen.setText("Cuenta origen");
        etTrasOrigen.setEnabled(false);

        etImpTras.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etImpTras.setText("Importe");
        etImpTras.setEnabled(false);

        etFechaTras.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etFechaTras.setText("Fecha");
        etFechaTras.setEnabled(false);

        txtImpTras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtImpTras.setToolTipText("nnnn,nn");
        txtImpTras.setEnabled(false);

        etEur3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etEur3.setText("Eur");
        etEur3.setEnabled(false);

        txtFechaTras.setToolTipText("yyyy-MM-dd");
        txtFechaTras.setDateFormatString("yyyy-MM-dd");
        txtFechaTras.setEnabled(false);

        btnAceptarTras.setText("Aceptar");
        btnAceptarTras.setToolTipText("Insertar Datos en Tabla");
        btnAceptarTras.setEnabled(false);
        btnAceptarTras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarTrasActionPerformed(evt);
            }
        });

        despTrasDest.setBackground(new java.awt.Color(0, 255, 0));
        despTrasDest.setToolTipText("Seleccionar Gastos");
        despTrasDest.setEnabled(false);

        etTrasDest.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        etTrasDest.setText("Cuenta destino");
        etTrasDest.setEnabled(false);

        javax.swing.GroupLayout pTrasLayout = new javax.swing.GroupLayout(pTras);
        pTras.setLayout(pTrasLayout);
        pTrasLayout.setHorizontalGroup(
            pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pTrasLayout.createSequentialGroup()
                        .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etTrasDest)
                            .addComponent(etTrasOrigen))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(despTrasOrigen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(despTrasDest, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pTrasLayout.createSequentialGroup()
                        .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etFechaTras)
                            .addComponent(etImpTras))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pTrasLayout.createSequentialGroup()
                                .addComponent(txtImpTras, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etEur3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 14, Short.MAX_VALUE))
                            .addGroup(pTrasLayout.createSequentialGroup()
                                .addComponent(txtFechaTras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAceptarTras)))))
                .addContainerGap())
        );
        pTrasLayout.setVerticalGroup(
            pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTrasLayout.createSequentialGroup()
                .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(despTrasOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etTrasOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(despTrasDest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etTrasDest, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtImpTras, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etImpTras, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etEur3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAceptarTras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFechaTras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(etFechaTras, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pBajoTrasLayout = new javax.swing.GroupLayout(pBajoTras);
        pBajoTras.setLayout(pBajoTrasLayout);
        pBajoTrasLayout.setHorizontalGroup(
            pBajoTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pBajoTrasLayout.setVerticalGroup(
            pBajoTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBajoTrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pGralLayout = new javax.swing.GroupLayout(pGral);
        pGral.setLayout(pGralLayout);
        pGralLayout.setHorizontalGroup(
            pGralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraHerramientas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1041, Short.MAX_VALUE)
            .addGroup(pGralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pGralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pBajoMov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pBajoTras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pBajoTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pGralLayout.setVerticalGroup(
            pGralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGralLayout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pGralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pGralLayout.createSequentialGroup()
                        .addComponent(pBajoMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pBajoTras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pBajoTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        barraMenu.setEnabled(false);

        mArchivo.setText("Archivo");
        mArchivo.setEnabled(false);

        smAbrirArchivo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        smAbrirArchivo.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoAbrir.png")); // NOI18N
        smAbrirArchivo.setText("Abrir Archivo");
        mArchivo.add(smAbrirArchivo);

        smGuardarArchivo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        smGuardarArchivo.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoGuardar.png")); // NOI18N
        smGuardarArchivo.setText("Guardar Archivo");
        mArchivo.add(smGuardarArchivo);
        mArchivo.add(jSeparator8);

        smImprimir.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoImpresora.png")); // NOI18N
        smImprimir.setText("Imprimir");

        smImprmirMovimientos.setText("Movimientos por Cuenta");
        smImprmirMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smImprmirMovimientosActionPerformed(evt);
            }
        });
        smImprimir.add(smImprmirMovimientos);
        smImprimir.add(jSeparator16);

        smImprmirGastos.setText("Resumen Gastos");
        smImprmirGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smImprmirGastosActionPerformed(evt);
            }
        });
        smImprimir.add(smImprmirGastos);

        smImprmirIngresos.setText("Resumen Ingresos");
        smImprmirIngresos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smImprmirIngresosActionPerformed(evt);
            }
        });
        smImprimir.add(smImprmirIngresos);

        smImprmirTraspasos.setText("Resumen Traspasos");
        smImprmirTraspasos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smImprmirTraspasosActionPerformed(evt);
            }
        });
        smImprimir.add(smImprmirTraspasos);

        mArchivo.add(smImprimir);
        mArchivo.add(jSeparator5);

        smG.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoGastos.png")); // NOI18N
        smG.setText("Gastos");
        smG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smGActionPerformed(evt);
            }
        });
        mArchivo.add(smG);

        smT.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoTraspasos.png")); // NOI18N
        smT.setText("Traspasos");
        smT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smTActionPerformed(evt);
            }
        });
        mArchivo.add(smT);

        smI.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoIngresos.png")); // NOI18N
        smI.setText("Ingresos");
        smI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smIActionPerformed(evt);
            }
        });
        mArchivo.add(smI);
        mArchivo.add(jSeparator7);

        smSalir.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoSalir.png")); // NOI18N
        smSalir.setText("Salir");
        smSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smSalirActionPerformed(evt);
            }
        });
        mArchivo.add(smSalir);

        barraMenu.add(mArchivo);

        mEdicion.setText("Edicion");
        mEdicion.setEnabled(false);

        smAddCuenta.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoCrearCuenta.png")); // NOI18N
        smAddCuenta.setText("Añadir Cuenta...");
        smAddCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smAddCuentaActionPerformed(evt);
            }
        });
        mEdicion.add(smAddCuenta);
        mEdicion.add(jSeparator1);

        smAddTG.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoCrearGasto.png")); // NOI18N
        smAddTG.setText("Añadir Tipo Gasto...");
        smAddTG.setToolTipText("");
        smAddTG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smAddTGActionPerformed(evt);
            }
        });
        mEdicion.add(smAddTG);

        smAddTI.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoCrearIngreso.png")); // NOI18N
        smAddTI.setText("Añadir Tipo Ingreso...");
        smAddTI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smAddTIActionPerformed(evt);
            }
        });
        mEdicion.add(smAddTI);
        mEdicion.add(jSeparator11);

        smFiltro.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoBuscar.png")); // NOI18N
        smFiltro.setText("Buscar");
        smFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smFiltroActionPerformed(evt);
            }
        });
        mEdicion.add(smFiltro);

        barraMenu.add(mEdicion);

        mAyuda.setText("Ayuda");
        mAyuda.setEnabled(false);

        subMenuAcercaDe.setIcon(new javax.swing.ImageIcon("/home/fernando/NetBeansProjects/CuentaHogar_6/iconos/iconoInfo.png")); // NOI18N
        subMenuAcercaDe.setText("Acerca de...");
        mAyuda.add(subMenuAcercaDe);

        barraMenu.add(mAyuda);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pGral, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCtasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCtasActionPerformed
        try {
            sent.cargarTablaCuentas(tablaCuentasVC, despCtas, true);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo cargar Tabla Cuentas", ex);
        }
        vCtas.setVisible(true);
    }//GEN-LAST:event_btnCtasActionPerformed

    private void btnSalirVCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirVCActionPerformed
        vCtas.dispose();
    }//GEN-LAST:event_btnSalirVCActionPerformed

    private void btnTipoMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoMovActionPerformed
        vMov.setVisible(true);
    }//GEN-LAST:event_btnTipoMovActionPerformed

    private void btnSalirVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirVMActionPerformed
        vMov.dispose();
    }//GEN-LAST:event_btnSalirVMActionPerformed

    private void btnAceptarMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarMovActionPerformed
        try {
            this.crearMov(despCtas, despMov, txtFechaMov, txtImpMov);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al crear Movimiento", ex);
        }
    }//GEN-LAST:event_btnAceptarMovActionPerformed

    private void btn2pGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2pGActionPerformed
        try {
            this.gestionarBtns2p(B2PG);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
        
    }//GEN-LAST:event_btn2pGActionPerformed

    private void btn2pIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2pIActionPerformed
        try {
            this.gestionarBtns2p(B2PI);
        } catch (Exception ex) {    
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
        
    }//GEN-LAST:event_btn2pIActionPerformed

    private void btnEliminarTMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTMActionPerformed
        try {
            String tipo = this.getTipoMov();
            String[] valores = ed.recuperarValoresFila(tMov);
            int id = sent.comprobarMovID(tipo, valores);
            sent.eliminarMov(tipo, id, tMov, true);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al eliminar Movimiento", ex);
        }
    }//GEN-LAST:event_btnEliminarTMActionPerformed

    private void subMenuEmEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuEmEliminarActionPerformed
        try {
            String tipo = this.getTipoMov();
            String[] valores = ed.recuperarValoresFila(tMov);
            int id = sent.comprobarMovID(tipo, valores);
            sent.eliminarMov(tipo, id, tMov, true);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al eliminar Movimiento", ex);
        }
    }//GEN-LAST:event_subMenuEmEliminarActionPerformed

    private void smAddCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smAddCuentaActionPerformed
        vCtas.setVisible(true);
    }//GEN-LAST:event_smAddCuentaActionPerformed

    private void smAddTGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smAddTGActionPerformed
        try {
            this.gestionarBtns2p(B2PG);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
        vMov.setVisible(true);
    }//GEN-LAST:event_smAddTGActionPerformed

    private void smAddTIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smAddTIActionPerformed
        try {
            this.gestionarBtns2p(B2PI);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
        vMov.setVisible(true);
    }//GEN-LAST:event_smAddTIActionPerformed

    private void btnEditarTMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarTMActionPerformed
        try {     
            this.cargarTablaEditarMov(this.getTipoMov(), tMov);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al cargar Tabla de Editar Movimiento", ex);
        }
    }//GEN-LAST:event_btnEditarTMActionPerformed

    private void subMenuEmEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuEmEditarActionPerformed
        try {
            this.cargarTablaEditarMov(this.getTipoMov(), tMov);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al cargar Tabla de Editar Movimiento", ex);
        }
    }//GEN-LAST:event_subMenuEmEditarActionPerformed

    private void btnCancelarVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVEActionPerformed
        tMov.setEnabled(true);
        btnEliminarTM.setEnabled(true);
        btnEditarTM.setEnabled(true);
        tMov.setComponentPopupMenu(jPpMenuTablas);
        vEditar.dispose();
        this.estadoBtn2p(true);
    }//GEN-LAST:event_btnCancelarVEActionPerformed

    private void btnAceptarVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarVEActionPerformed
        try {
            
            tMov.setEnabled(true);
            btnEliminarTM.setEnabled(true);
            btnEditarTM.setEnabled(true);
            tMov.setComponentPopupMenu(jPpMenuTablas);
            this.estadoBtn2p(true);
            
            sent.editarMov(this.getTipoMov(), this.getId(), tMov, tablaEditar, true);
            
            vEditar.dispose();
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al Editar Movimiento", ex);
        }
    }//GEN-LAST:event_btnAceptarVEActionPerformed

    private void tMovMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tMovMousePressed
        try {
            this.gestionarBtns2p(B2PTM);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_tMovMousePressed

    private void btn2pTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2pTActionPerformed
        try {
            this.gestionarBtns2p(B2PT);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_btn2pTActionPerformed

    private void smSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_smSalirActionPerformed

    private void smIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smIActionPerformed
        try {
            this.gestionarBtns2p(B2PI);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_smIActionPerformed

    private void smGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smGActionPerformed
        try {
            this.gestionarBtns2p(B2PG);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_smGActionPerformed

    private void smTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smTActionPerformed
        try {
            this.gestionarBtns2p(B2PT);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_smTActionPerformed

    private void btnAceptarTrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarTrasActionPerformed
        try {
            this.crearMov(despTrasOrigen, despTrasDest, txtFechaTras, txtImpTras);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al crear Movimiento", ex);
        }
    }//GEN-LAST:event_btnAceptarTrasActionPerformed

    private void btn2pFiltroTMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2pFiltroTMActionPerformed
        try {
            this.gestionarBtns2p(B2PFTM);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_btn2pFiltroTMActionPerformed

    private void txtFiltroCol0KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCol0KeyReleased
        this.cargarFiltroTM();
    }//GEN-LAST:event_txtFiltroCol0KeyReleased

    private void txtFiltroCol1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCol1KeyReleased
        this.cargarFiltroTM();
    }//GEN-LAST:event_txtFiltroCol1KeyReleased

    private void txtFiltroCol2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCol2KeyReleased
        this.cargarFiltroTM();
    }//GEN-LAST:event_txtFiltroCol2KeyReleased

    private void txtFiltroCol3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCol3KeyReleased
        this.cargarFiltroTM();
    }//GEN-LAST:event_txtFiltroCol3KeyReleased

    private void smImprmirGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smImprmirGastosActionPerformed
        try {
            ed.generarReport("Informes/Gastos.jrxml");
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al realizar Informe", ex);
        }
    }//GEN-LAST:event_smImprmirGastosActionPerformed

    private void smFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smFiltroActionPerformed

        btn2pFiltroTM.isSelected();
        try {
            this.gestionarBtns2p(B2PFTM);
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al gestionar Bontones", ex);
        }
    }//GEN-LAST:event_smFiltroActionPerformed

    private void smImprmirMovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smImprmirMovimientosActionPerformed
        try {
            ed.generarReport("Informes/Movimientos.jrxml");
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al realizar Informe", ex);
        }
    }//GEN-LAST:event_smImprmirMovimientosActionPerformed

    private void smImprmirIngresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smImprmirIngresosActionPerformed
        try {
            ed.generarReport("Informes/Ingresos.jrxml");
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al realizar Informe", ex);
        }
    }//GEN-LAST:event_smImprmirIngresosActionPerformed

    private void smImprmirTraspasosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smImprmirTraspasosActionPerformed
        try {
            ed.generarReport("Informes/Traspasos.jrxml");
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al realizar Informe", ex);
        }
    }//GEN-LAST:event_smImprmirTraspasosActionPerformed

    private void btnImprimirTMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirTMActionPerformed
        try {
            ed.imprimirTabla(pTM, tMov, "Informes/Imprimir.jrxml");
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al realizar Informe", ex);
        }
    }//GEN-LAST:event_btnImprimirTMActionPerformed

    private void btnGraficaTMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficaTMActionPerformed
        try {
            ed.generarReport("Informes/GraficaG.jrxml");
        } catch (Exception ex) {
            Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
            ed.mostrarError("Fallo al realizar Informe", ex);
        }
    }//GEN-LAST:event_btnGraficaTMActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CuentaHogar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CuentaHogar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CuentaHogar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CuentaHogar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CuentaHogar().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(CuentaHogar.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "FALLO GRAVE!!\n Imposible arrancar CuentaHogar", "ERROR", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JToggleButton btn2pFiltroTM;
    private javax.swing.JToggleButton btn2pG;
    private javax.swing.JToggleButton btn2pI;
    private javax.swing.JToggleButton btn2pT;
    private javax.swing.JToggleButton btn2pTM;
    private javax.swing.JButton btnAceptarMov;
    private javax.swing.JButton btnAceptarTras;
    private javax.swing.JButton btnAceptarVE;
    private javax.swing.JButton btnCancelarVE;
    private javax.swing.JButton btnCrearVC;
    private javax.swing.JButton btnCrearVM;
    private javax.swing.JButton btnCtas;
    private javax.swing.JButton btnEditarTM;
    private javax.swing.JButton btnEditarVC;
    private javax.swing.JButton btnEditarVM;
    private javax.swing.JButton btnEliminarTM;
    private javax.swing.JButton btnEliminarVC;
    private javax.swing.JButton btnEliminarVM;
    private javax.swing.JButton btnGraficaTM;
    private javax.swing.JButton btnImprimirTM;
    private javax.swing.JButton btnSalirVC;
    private javax.swing.JButton btnSalirVM;
    private javax.swing.JButton btnTipoMov;
    private javax.swing.JCheckBox checkNumCuentaVC;
    private javax.swing.JComboBox despCtas;
    private javax.swing.JComboBox despFiltroCol2;
    private javax.swing.JComboBox despFiltroCol3;
    private javax.swing.JComboBox despMov;
    private javax.swing.JComboBox despTrasDest;
    private javax.swing.JComboBox despTrasOrigen;
    private javax.swing.JLabel etCtas;
    private javax.swing.JLabel etEur;
    private javax.swing.JLabel etEur3;
    private javax.swing.JLabel etFechaMov;
    private javax.swing.JLabel etFechaTras;
    private javax.swing.JLabel etFiltroCol0;
    private javax.swing.JLabel etFiltroCol1;
    private javax.swing.JLabel etFiltroCol2;
    private javax.swing.JLabel etFiltroCol3;
    private javax.swing.JLabel etImpMov;
    private javax.swing.JLabel etImpTras;
    private javax.swing.JLabel etTMovVM;
    private javax.swing.JLabel etTipoMov;
    private javax.swing.JLabel etTrasDest;
    private javax.swing.JLabel etTrasOrigen;
    private javax.swing.JLabel etiqCapitalInicialVC;
    private javax.swing.JLabel etiqCuentaVC;
    private javax.swing.JLabel etiqEurVC;
    private javax.swing.JPopupMenu jPpMenuTablas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JMenu mArchivo;
    private javax.swing.JMenu mAyuda;
    private javax.swing.JMenu mEdicion;
    private javax.swing.JPanel pBajoMov;
    private javax.swing.JPanel pBajoTM;
    private javax.swing.JPanel pBajoTMovVM;
    private javax.swing.JPanel pBajoTTMovVM;
    private javax.swing.JPanel pBajoTras;
    private javax.swing.JPanel pFiltro;
    private javax.swing.JPanel pGral;
    private javax.swing.JPanel pGralVM;
    private javax.swing.JPanel pMov;
    private javax.swing.JPanel pTM;
    private javax.swing.JPanel pTMovVM;
    private javax.swing.JPanel pTras;
    private javax.swing.JPanel panelBajoCuentasVC;
    private javax.swing.JPanel panelBajoEditarVE;
    private javax.swing.JPanel panelCuentasVC;
    private javax.swing.JPanel panelEditarVE;
    private javax.swing.JPanel panelGeneralVC;
    private javax.swing.JPanel panleBajoTablaCuentasVC;
    private javax.swing.JMenuItem smAbrirArchivo;
    private javax.swing.JMenuItem smAddCuenta;
    private javax.swing.JMenuItem smAddTG;
    private javax.swing.JMenuItem smAddTI;
    private javax.swing.JMenuItem smFiltro;
    private javax.swing.JMenuItem smG;
    private javax.swing.JMenuItem smGuardarArchivo;
    private javax.swing.JMenuItem smI;
    private javax.swing.JMenu smImprimir;
    private javax.swing.JMenuItem smImprmirGastos;
    private javax.swing.JMenuItem smImprmirIngresos;
    private javax.swing.JMenuItem smImprmirMovimientos;
    private javax.swing.JMenuItem smImprmirTraspasos;
    private javax.swing.JMenuItem smSalir;
    private javax.swing.JMenuItem smT;
    private javax.swing.JMenuItem subMenuAcercaDe;
    private javax.swing.JMenuItem subMenuEmEditar;
    private javax.swing.JMenuItem subMenuEmEliminar;
    private javax.swing.JTable tMov;
    private javax.swing.JTable tTipoMovVM;
    private javax.swing.JTable tablaCuentasVC;
    private javax.swing.JTable tablaEditar;
    private javax.swing.JTextField txtCapitalInicialVC;
    private javax.swing.JTextField txtCuentaVC;
    private com.toedter.calendar.JDateChooser txtFechaMov;
    private com.toedter.calendar.JDateChooser txtFechaTras;
    private javax.swing.JTextField txtFiltroCol0;
    private javax.swing.JTextField txtFiltroCol1;
    private javax.swing.JTextField txtFiltroCol2;
    private javax.swing.JTextField txtFiltroCol3;
    private javax.swing.JTextField txtImpMov;
    private javax.swing.JTextField txtImpTras;
    private javax.swing.JTextField txtNumCuentaVC1;
    private javax.swing.JTextField txtNumCuentaVC2;
    private javax.swing.JTextField txtNumCuentaVC3;
    private javax.swing.JTextField txtNumCuentaVC4;
    private javax.swing.JTextField txtTMovVM;
    private javax.swing.JFrame vCtas;
    private javax.swing.JFrame vEditar;
    private javax.swing.JFrame vMov;
    // End of variables declaration//GEN-END:variables
}

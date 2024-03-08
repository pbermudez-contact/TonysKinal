package org.pablobermudez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.pablobermudez.bean.Empresa;
import org.pablobermudez.bean.Presupuesto;
import org.pablobermudez.db.Conexion;
import org.pablobermudez.main.Principal;


public class PresupuestoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;    
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha; 
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
       // fecha.getStylesheets().add("/org/pablobermudez/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        
        
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
    }

    
    
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                          resultado.getDate("fechaSolicitud"),
                                          resultado.getDouble("cantidadPresupuesto"),
                                          resultado.getInt("codigoEmpresa")));
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
       public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/pablobermudez/image/save.png"));
                imgEliminar.setImage(new Image("/org/pablobermudez/image/cancel.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiar();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/pablobermudez/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/pablobermudez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }
       
     public void seleccionarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)(tblPresupuestos.getSelectionModel().getSelectedItem())).getCodigoEmpresa()));
    }      
     
     public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
 
 
    
    public void guardar() {
        Presupuesto registro = new Presupuesto();
        if(fecha.getSelectedDate() == null){
            JOptionPane.showMessageDialog(null, "Falta La Fecha");
        }else if (txtCantidadPresupuesto.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Falta La Cantidad");
        }else if (cmbCodigoEmpresa.getSelectionModel() == null){
            JOptionPane.showMessageDialog(null, "Falta Empresa");
        }else{
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCodigoEmpresa((((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            try {
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(2, registro.getCantidadPresupuesto());
                procedimiento.setInt(3, registro.getCodigoEmpresa());
                procedimiento.execute();
                listaPresupuesto.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
                }
            }
        }    
     
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/pablobermudez/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/pablobermudez/image/Eliminar.png"));
                tipoDeOperacion = PresupuestoController.operaciones.NINGUNO;
                break;
            default:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminar el registro?","Eliminar Presupuesto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            limpiar();
                        }catch(SQLException e){
                            JOptionPane.showMessageDialog(null, "No Puede Eliminar Este Dato");
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiar();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
           
                }
       
        }
        
        
    }   
    
    
  public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/pablobermudez/image/save.png"));
                    imgReporte.setImage(new Image("/org/pablobermudez/image/cancel.png"));
                    activarControles();
                    tipoDeOperacion = PresupuestoController.operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar Un Elemento");
                }
                 break;
            case ACTUALIZAR:
                 actualizar();
                 limpiar();
                 desactivarControles();
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);
                 btnEditar.setText("Editar");
                 btnReporte.setText("Reporte");
                 imgEditar.setImage(new Image("/org/pablobermudez/image/Editar.png"));
                 imgReporte.setImage(new Image("/org/pablobermudez/image/reporte.png"));
                 tipoDeOperacion = PresupuestoController.operaciones.NINGUNO;
                 limpiar();
                 cargarDatos();
                 break;
        }
    }  
  
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EditarPresupuesto(?,?,?,?)");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setFechaSolicitud(fecha.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date (registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiar();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/pablobermudez/image/Editar.png"));
                imgReporte.setImage(new Image("/org/pablobermudez/image/reporte.png"));
                tipoDeOperacion = PresupuestoController.operaciones.NINGUNO;
                cargarDatos();
                break;
            }
        }
    
    public void desactivarControles(){
    txtCodigoPresupuesto.setEditable(false);
    txtCantidadPresupuesto.setEditable(false);
    cmbCodigoEmpresa.setDisable(true);
    fecha.setDisable(true);
        
    }
    
    public void activarControles(){
    txtCodigoPresupuesto.setDisable(false);
    txtCantidadPresupuesto.setEditable(true);
    cmbCodigoEmpresa.setDisable(false);
    fecha.setDisable(false);
    }
    
    public void limpiar(){
    txtCodigoPresupuesto.clear();
    txtCantidadPresupuesto.clear();
    fecha.selectedDateProperty().set(null);
    cmbCodigoEmpresa.setValue(null);  
    }
 
     
     
     
     
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
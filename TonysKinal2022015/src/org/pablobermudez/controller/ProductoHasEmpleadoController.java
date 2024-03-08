package org.pablobermudez.controller;


import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
import org.pablobermudez.bean.Empleado;
import org.pablobermudez.bean.Servicio;
import org.pablobermudez.bean.ServicioHasEmpleado;
import org.pablobermudez.db.Conexion;
import org.pablobermudez.main.Principal;

public class ProductoHasEmpleadoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServicioHasEmpleado> listaServicioHasEmpleado;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigo;
    @FXML private TextField txtLugar;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private JFXTimePicker jfxHora;
    @FXML private GridPane grpFecha;
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colLugar;
    @FXML private Button btnNuevo;
    @FXML private ImageView imgNuevo;

 
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFecha.add(fecha, 0, 9);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());    
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServicioHasEmpleado());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoEmpleado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Date>("fechaEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, String>("horaEvento"));
        colLugar.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, String>("lugarEvento"));
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/pablobermudez/image/save.png"));
                tblServiciosHasEmpleados.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/pablobermudez/image/save.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    public void guardar(){
        String text = txtCodigo.getText();
        String text2 = txtLugar.getText();
        text.replaceAll(" ", "");
        text2.replaceAll(" ", "");
        if(text.length() == 0 || text2.length() == 0 || cmbCodigoServicio.getValue() == null || cmbCodigoEmpleado.getValue() == null
                    || fecha.getSelectedDate() == null || jfxHora.getValue() == null){
            JOptionPane.showMessageDialog(null, "Faltan datos");
        
        }else{
        
        ServicioHasEmpleado registro = new ServicioHasEmpleado();
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigo.getText()));
        registro.setLugarEvento(txtLugar.getText());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setHoraEvento(String.valueOf(jfxHora.getValue()));
        registro.setFechaEvento(fecha.getSelectedDate());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Empleado(?, ?, ?, ?, ?, ?);");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServicioHasEmpleado.add(registro);
            
        
        }catch(Exception e){
            e.printStackTrace();
        }
        }
    }
    
    
    
   
    
   
   public void seleccionarElemento(){
       if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
            txtCodigo.setText(String.valueOf(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            txtLugar.setText(String.valueOf(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServicioHasEmpleado)(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem())).getCodigoEmpleado()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioHasEmpleado)(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem())).getCodigoServicio()));
            jfxHora.setValue(LocalTime.parse(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
            fecha.selectedDateProperty().set(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        }
    }
    
    
    public ObservableList<ServicioHasEmpleado> getServicioHasEmpleado(){
        ArrayList<ServicioHasEmpleado> lista = new ArrayList<ServicioHasEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Empleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServicioHasEmpleado(resultado.getInt("Servicios_codigoServicio"),
                    resultado.getInt("codigoServicio"),
                    resultado.getInt("codigoEmpleado"),
                    resultado.getDate("fechaEvento"),
                    resultado.getString("horaEvento"),
                    resultado.getString("lugarEvento")));
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicioHasEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                    resultado.getDate("fechaServicio"),
                    resultado.getString("tipoServicio"),
                    resultado.getString("horaServicio"),
                    resultado.getString("lugarServicio"),
                    resultado.getString("telefonoContacto"),
                    resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                    registro.getDate("fechaServicio"),
                    registro.getString("tipoServicio"),
                    registro.getString("horaServicio"),
                    registro.getString("lugarServicio"),
                    registro.getString("telefonoContacto"),
                    registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
   public ObservableList<Empleado> getEmpleado(){
        ArrayList <Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento =  Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado (resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleado (registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    
    
    public void limpiarControles(){
        txtCodigo.setText("");
        txtLugar.setText("");
        cmbCodigoServicio.setValue(null);
        cmbCodigoEmpleado.setValue(null);
        jfxHora.setValue(null);
        fecha.setSelectedDate(null);   
    }
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtLugar.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        jfxHora.setDisable(true);
        fecha.setDisable(true);
    }
    public void activarControles(){
        txtCodigo.setEditable(true);
        txtLugar.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        jfxHora.setDisable(false);
        fecha.setDisable(false);
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}


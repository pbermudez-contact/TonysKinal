package org.pablobermudez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.pablobermudez.bean.TipoEmpleado;
import org.pablobermudez.db.Conexion;
import org.pablobermudez.main.Principal;


public class TipoEmpleadoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodTipoEmpleado;
    @FXML private TextField txtdescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colTipoEmpleado;
    @FXML private TableColumn descripcion;
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
     }
    
    public void cargarDatos(){
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        descripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
        
        
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleado");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/pablobermudez/image/save.png"));
                imgEliminar.setImage(new Image("/org/pablobermudez/image/cancel.png"));
                tipoDeOperacion = TipoEmpleadoController.operaciones.GUARDAR;
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
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                cargarDatos();
                break;
                        
               
                       
        }
        
    }
    
    public void seleccionarElemento(){
        txtCodTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtdescripcion.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion()));
        
        
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
            tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
            break;
        default:
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Estas Seguro de Eliminar el resgitro?","Eliminar TipoEmpleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                            procedimiento.setInt(1, ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                            limpiar();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un Elemento");
                }
    }
}
    
    
public void guardar(){
    TipoEmpleado registro = new TipoEmpleado();
    registro.setDescripcion(txtdescripcion.getText());
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado (?)");
        procedimiento.setString(1, registro.getDescripcion());
        procedimiento.execute();
        listaTipoEmpleado.add(registro);
    } catch(Exception e){
        e.printStackTrace();
    }
}
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/pablobermudez/image/save.png"));
                    imgReporte.setImage(new Image("/org/pablobermudez/image/cancel.png"));
                    activarControles();
                    tipoDeOperacion = TipoEmpleadoController.operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un objeto");
                    
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
                    tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
 
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditaTipoEm(?,?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtdescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            
            
            
        }catch(Exception e){
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
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                cargarDatos();
                break;
            }
        }
        
        
        public void desactivarControles(){
        txtCodTipoEmpleado.setEditable(false);
        txtdescripcion.setEditable(false);
 
        
    }
    
        public void activarControles(){
        txtCodTipoEmpleado.setEditable(false);
        txtdescripcion.setEditable(true);
 
        
    }
 
        public void limpiar(){
        txtCodTipoEmpleado.clear();
        txtdescripcion.clear();

        
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
    


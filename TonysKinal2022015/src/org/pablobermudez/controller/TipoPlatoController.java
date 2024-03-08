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
import org.pablobermudez.bean.TipoPlato;
import org.pablobermudez.db.Conexion;
import org.pablobermudez.main.Principal;


public class TipoPlatoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodTipoPlato;
    @FXML private TableColumn colDescripcion;
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
        tblTipoPlato.setItems(getTipoEmpleado());
        colCodTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
        
        
    }
    
    public ObservableList<TipoPlato> getTipoEmpleado(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlato");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoPlato = FXCollections.observableArrayList(lista);
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
                tipoDeOperacion = TipoPlatoController.operaciones.GUARDAR;
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
                tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                cargarDatos();
                break;
                        
               
                       
        }
        
    }
    
    public void seleccionarElemento(){
        txtCodTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcion.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo()));
        
        
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
            tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
            break;
        default:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Estas Seguro de Eliminar el resgitro?","Eliminar TipoEmpleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                            procedimiento.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
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
    TipoPlato registro = new TipoPlato();
    registro.setDescripcionTipo(txtDescripcion.getText());
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato (?)");
        procedimiento.setString(1, registro.getDescripcionTipo());
        procedimiento.execute();
        listaTipoPlato.add(registro);
    } catch(Exception e){
        e.printStackTrace();
    }
}
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() !=null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/pablobermudez/image/save.png"));
                    imgReporte.setImage(new Image("/org/pablobermudez/image/cancel.png"));
                    activarControles();
                    tipoDeOperacion = TipoPlatoController.operaciones.ACTUALIZAR;
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
                    tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
 
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
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
                tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                cargarDatos();
                break;
            }
        }
        public void desactivarControles(){
        txtCodTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
 
        
    }
    
        public void activarControles(){
        txtCodTipoPlato.setEditable(false);
        txtDescripcion.setEditable(true);
 
        
    }
 
        public void limpiar(){
        txtCodTipoPlato.clear();
        txtDescripcion.clear();

        
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

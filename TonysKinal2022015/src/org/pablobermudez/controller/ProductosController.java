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
import org.pablobermudez.bean.Producto;
import org.pablobermudez.db.Conexion;
import org.pablobermudez.main.Principal;


public class ProductosController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtCodProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtProducto;
    @FXML private TableView tblProductos;
    @FXML private TableColumn codProducto;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colProducto;
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
        tblProductos.setItems(getProducto());
        codProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
        
    }
    
public ObservableList<Producto> getProducto(){
    ArrayList<Producto> lista = new ArrayList<Producto>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
        lista.add(new Producto (resultado.getInt("codigoProducto"),
                                resultado.getString("nombreProducto"),
                                resultado.getInt("cantidad")));
            
      }
    } catch(Exception e){
        e.printStackTrace();
    }
    return listaProducto = FXCollections.observableArrayList(lista);
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
                tipoDeOperacion = ProductosController.operaciones.GUARDAR;
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
                tipoDeOperacion = ProductosController.operaciones.NINGUNO;
                cargarDatos();
                break;
                        
               
                       
        }
        
    }
    
    public void seleccionarElemento(){
        txtCodProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));                                                                           ;
        
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
            tipoDeOperacion = ProductosController.operaciones.NINGUNO;
            break;
        default:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Estas Seguro de Eliminar el resgitro?","Eliminar Producto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
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
    Producto registro = new Producto();
    //registro.setCodigoProducto(Integer.paraseInt(txtCodigoProducto.getText());
    registro.setNombreProducto(txtProducto.getText());
    registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto (?,?,)");
        procedimiento.setString(1, registro.getNombreProducto());
        procedimiento.setInt(2, registro.getCantidad());
        procedimiento.execute();
        listaProducto.add(registro);
    } catch(Exception e){
        e.printStackTrace();
    }
}
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/pablobermudez/image/save.png"));
                    imgReporte.setImage(new Image("/org/pablobermudez/image/cancel.png"));
                    activarControles();
                    tipoDeOperacion = ProductosController.operaciones.ACTUALIZAR;
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
                    tipoDeOperacion = ProductosController.operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?,?,?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
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
                tipoDeOperacion = ProductosController.operaciones.NINGUNO;
                cargarDatos();
                break;
            }
        }       
        
        
        
        public void desactivarControles(){
        txtCodProducto.setEditable(false);
        txtCantidad.setEditable(false);
        txtProducto.setEditable(false);
        
    }
    
        public void activarControles(){
        txtCodProducto.setEditable(false);
        txtCantidad.setEditable(true);
        txtProducto.setEditable(true);
        
    }
 
        public void limpiar(){
        txtCodProducto.clear();
        txtCantidad.clear();
        txtProducto.clear();
        
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

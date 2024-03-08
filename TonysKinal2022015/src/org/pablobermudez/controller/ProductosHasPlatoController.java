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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.pablobermudez.bean.Plato;
import org.pablobermudez.bean.Producto;
import org.pablobermudez.bean.ProductoHasPlatos;
import org.pablobermudez.db.Conexion;
import org.pablobermudez.main.Principal;

public class ProductosHasPlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR, ELIMINAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ProductoHasPlatos> listaProductoPlato;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtCodigo;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblHasProductoPlato;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private Button btnNuevo;
    @FXML private ImageView imgNuevo;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        
    }
        public void cargarDatos(){
        tblHasProductoPlato.setItems(getProductoHasPlato());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ProductoHasPlatos, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoHasPlatos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlatos, Integer>("codigoProducto"));
    }
        
            public void seleccionarElemento(){
        if(tblHasProductoPlato.getSelectionModel().getSelectedItem() != null){
        txtCodigo.setText(String.valueOf(((ProductoHasPlatos)tblHasProductoPlato.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        }else{
        }
    }
        
        
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/pablobermudez/image/save.png"));
                tblHasProductoPlato.getSelectionModel().clearSelection();
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
                tblHasProductoPlato.getSelectionModel().clearSelection();
                break;
        }
    }
    
   
        public ObservableList<ProductoHasPlatos>getProductoHasPlato(){
        ArrayList<ProductoHasPlatos> lista = new ArrayList<ProductoHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ProductoHasPlatos(resultado.getInt("Productos_codigoProducto"),
                resultado.getInt("codigoPlato"),
                resultado.getInt("codigoProducto")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductoPlato = FXCollections.observableArrayList(lista);
    }
        
        
      public void guardar(){
          String text = txtCodigo.getText();
          text.replaceAll(" ", "");
          if(text.length() == 0 || cmbCodigoPlato.getValue() == null || cmbCodigoProducto.getValue() == null){
              JOptionPane.showMessageDialog(null, "Faltan Datos");
          }else{
        ProductoHasPlatos registro = new ProductoHasPlatos();
            registro.setProductos_codigoProducto(Integer.parseInt(txtCodigo.getText()));
            registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
            registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductos_has_Platos(?,?,?)");
                procedimiento.setInt(1, registro.getProductos_codigoProducto());
                procedimiento.setInt(2, registro.getCodigoPlato());
                procedimiento.setInt(3, registro.getCodigoProducto());
                procedimiento.execute();
                listaProductoPlato.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
          }
    }
        

            public ObservableList<Plato>getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                resultado.getInt("cantidadPlato"),
                resultado.getString("nombrePlato"),
                resultado.getString("descripcionPlato"),
                resultado.getDouble("precioPlato"),
                resultado.getInt("codigoTipoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
            
    public ObservableList<Producto>getProducto(){
        ArrayList <Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                            resultado.getString("nombreProducto"),
                            resultado.getInt("cantidad")));
            
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableList(lista);
    }
    
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigo.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigo.setText("");
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
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

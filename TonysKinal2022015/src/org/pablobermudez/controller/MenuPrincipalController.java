package org.pablobermudez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.pablobermudez.main.Principal;

public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaInicio(){
        escenarioPrincipal.ventanaInicio();
    }
    public void ventanaMenuComida(){
        escenarioPrincipal.ventanaMenuComida();
    }
    public void ventanaHasProductoEmpleado(){
        escenarioPrincipal.ventanaHasProductoEmpleado();
    }  
    public void ventanaHasServicio(){
        escenarioPrincipal.ventanaHasServicio();
    }  
    
}

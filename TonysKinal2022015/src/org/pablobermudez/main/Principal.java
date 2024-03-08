/*Documentacion 
Pablo Ernesto Bermudez Tobar
IN5AM
Carnet: 2022015
Fecha de Creación: 28/3/2023
Fecha de Modificación:11/04/2023,06/06/2023

 */
package org.pablobermudez.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.pablobermudez.controller.EmpresasController;
import org.pablobermudez.controller.InicioController;
import org.pablobermudez.controller.LoginController;
import org.pablobermudez.controller.MenuComidaController;
import org.pablobermudez.controller.MenuPrincipalController;
import org.pablobermudez.controller.PresupuestoController;
import org.pablobermudez.controller.ProductoHasEmpleadoController;
import org.pablobermudez.controller.ProductosController;
import org.pablobermudez.controller.ProductosHasPlatoController;
import org.pablobermudez.controller.ProgramadorController;
import org.pablobermudez.controller.TipoEmpleadoController;
import org.pablobermudez.controller.TipoPlatoController;
import org.pablobermudez.controller.UsuarioController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/pablobermudez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/pablobermudez/image/Pollito.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/pablobermudez/view/MenuPrincipalView.fxml"));
        // Scene escena = new Scene(root); 
        // escenarioPrincipal.setScene(escena);
        ventanaInicio();
        escenarioPrincipal.show();
    }
   
   
  public void ventanaHasServicio(){
        try{
             ProductosHasPlatoController hasServicio  = (ProductosHasPlatoController)cambiarEscena("ProductoHasPlatoView",1086,528);
            hasServicio.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
            
           }
}    
    
  public void ventanaHasProductoEmpleado(){
        try{
             ProductoHasEmpleadoController hasproducto  = (ProductoHasEmpleadoController)cambiarEscena("ProductoHasEmpleadoView",1215,528);
            hasproducto.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
            
            }
    }     
    
  public void ventanaMenuComida(){
        try{
             MenuComidaController comida = (MenuComidaController)cambiarEscena("MenuComidaView.fxml",615,851);
            comida.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
            
            }
    }   
    
 public void ventanaLogin(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml",605,574);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
            
            }
    }
 public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml",487,602);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
            
            }
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",929,579);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
            
            }
    }
    
    public void ventanaProgramador(){
        
        
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",736,408);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public void ventanaEmpresa(){
         
            try{
            EmpresasController empresa = (EmpresasController)cambiarEscena("EmpresaView.fxml",994,547);
            empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }    
        
    }
        
         public void ventanaProductos(){
            try{
            ProductosController producto = (ProductosController)cambiarEscena("ProductosView.fxml",978,547);
            producto.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
                
        }    
         
         public void ventanaTipoPlato(){
             
             try{
             TipoPlatoController plato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",887,547);
             plato.setEscenarioPrincipal(this);
             }catch(Exception e){
                 e.printStackTrace();
             }
         
         }
         
         public void ventanaTipoEmpleado(){
             try{
             TipoEmpleadoController empleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",912,547);
             empleado.setEscenarioPrincipal(this);
             }catch(Exception e){
                 e.printStackTrace();
             }
         
          
             
         }
        
         public void ventanaPresupuesto(){
             try{
                 PresupuestoController presupuestoController = (PresupuestoController)cambiarEscena("PresupuestosView.fxml",994,547);
                 presupuestoController.setEscenarioPrincipal(this);
             }catch (Exception e) {
                 e.printStackTrace();
             }
             
             }    
    public void ventanaInicio(){
         try{
         InicioController inicio = (InicioController)cambiarEscena("Inicio.fxml",706,437);   
         inicio.setEscenarioPrincipal(this);
         }catch (Exception e) {
             e.printStackTrace();
         }
         
         }

    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene(cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    }
    
}

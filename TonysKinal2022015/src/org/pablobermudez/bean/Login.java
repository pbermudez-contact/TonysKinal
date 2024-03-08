package org.pablobermudez.bean;

public class Login {
    private String UsuarioMaster;
    private String PasswordLogin;
    
    public Login(){
    
    }

    public Login(String UsuarioMaster, String PasswordLogin) {
        this.UsuarioMaster = UsuarioMaster;
        this.PasswordLogin = PasswordLogin;
    }

    public String getUsuarioMaster() {
        return UsuarioMaster;
    }

    public void setUsuarioMaster(String UsuarioMaster) {
        this.UsuarioMaster = UsuarioMaster;
    }

    public String getPasswordLogin() {
        return PasswordLogin;
    }

    public void setPasswordLogin(String PasswordLogin) {
        this.PasswordLogin = PasswordLogin;
    }

  

 
  

    
}

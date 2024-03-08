package org.pablobermudez.bean;


public class TipoEmpleado {
    private int codigoTipoEmpleado;
    private String descripcion;
    
    public TipoEmpleado(){

    }
    
    public TipoEmpleado(int codigoTipoEmpleado, String cantidad) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcion = cantidad;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String cantidad) {
        this.descripcion = cantidad;
    }
    
    
}

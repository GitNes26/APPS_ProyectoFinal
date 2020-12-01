package com.nestor.proyectofinal;

public class Permiso {
    private String permiso;
    private boolean acceso;
    private String peermisoReal;

    public Permiso(String permiso, boolean acceso, String peermisoReal) {
        this.permiso = permiso;
        this.acceso = acceso;
        this.peermisoReal = peermisoReal;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public boolean getAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }

    public String getPeermisoReal() {
        return peermisoReal;
    }

    public void setPeermisoReal(String peermisoReal) {
        this.peermisoReal = peermisoReal;
    }
}

package com.nestor.proyectofinal;

public class Permiso {
    private String permiso;
    private boolean acceso;

    public Permiso(String permiso, boolean acceso) {
        this.permiso = permiso;
        this.acceso = acceso;
    }

    public Permiso(boolean acceso) {
        this.acceso = acceso;
    }

    public Permiso() {
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

public enum Prioridad {
    ALTA(3),
    MEDIA(2),
    BAJA(1);

    private final int valorNumerico;

    private Prioridad(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
}
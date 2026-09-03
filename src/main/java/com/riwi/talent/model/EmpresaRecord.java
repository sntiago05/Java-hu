package com.riwi.talent.model;
/**
 * Un Record permite representar datos con menos código.
 * Java genera automáticamente el constructor, los accesores,
 * equals(), hashCode() y toString().
 * Es superficialmente inmutable porque sus atributos no pueden
 * cambiar después de crear el objeto.
 */
public record EmpresaRecord(
        String nombre,
        String nit,
        int anioFundacion) {
}


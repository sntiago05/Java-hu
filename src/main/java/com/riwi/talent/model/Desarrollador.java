package com.riwi.talent.model;

/**
 * TASK 3 (HU4): especialización de Empleado. Al declararse "final" cierra
 * definitivamente esta rama del árbol sellado (Persona -> Empleado ->
 * Desarrollador), cumpliendo con el requisito de sealed classes de que
 * toda subclase directa debe ser final, sealed o non-sealed.
 */
public final class Desarrollador extends Empleado implements Promocionable {

    private String lenguajePrincipal;

    public Desarrollador(
            byte nivelAcceso,
            short anioIngreso,
            int idEmpleado,
            long numeroDocumento,
            float puntajeTest,
            double salarioBase,
            char tipoContrato,
            boolean esActivo,
            String nombre,
            int edad,
            int idSede,
            double bonoMensual,
            String lenguajePrincipal) {
        super(nivelAcceso, anioIngreso, idEmpleado, numeroDocumento, puntajeTest, salarioBase,
                tipoContrato, esActivo, nombre, edad, idSede, bonoMensual);
        this.lenguajePrincipal = lenguajePrincipal;
    }

    public String getLenguajePrincipal() {
        return lenguajePrincipal;
    }

    public void setLenguajePrincipal(String lenguajePrincipal) {
        this.lenguajePrincipal = lenguajePrincipal;
    }

    // TASK 4: implementación propia del contrato Promocionable.
    // Un desarrollador recibe un bono equivalente al 8% de su salario base.
    @Override
    public double calcularBonoAscenso() {
        return salarioBase * 0.08;
    }
}

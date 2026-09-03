package com.riwi.talent.model;

/**
 * TASK 3 (HU4): segunda especialización de Empleado, también "final"
 * por ser hoja de la jerarquía sellada.
 */
public final class Gerente extends Empleado implements Promocionable {

    private double presupuestoMensual;

    public Gerente(
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
            double presupuestoMensual) {
        super(nivelAcceso, anioIngreso, idEmpleado, numeroDocumento, puntajeTest, salarioBase,
                tipoContrato, esActivo, nombre, edad, idSede, bonoMensual);
        this.presupuestoMensual = presupuestoMensual;
    }

    public double getPresupuestoMensual() {
        return presupuestoMensual;
    }

    public void setPresupuestoMensual(double presupuestoMensual) {
        this.presupuestoMensual = presupuestoMensual;
    }

    // TASK 4: implementación propia del contrato Promocionable.
    // Un gerente recibe un bono equivalente al 3% de su presupuesto mensual
    // administrado, distinto de la fórmula de Desarrollador.
    @Override
    public double calcularBonoAscenso() {
        return presupuestoMensual * 0.03;
    }
}

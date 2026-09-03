package modelo;

/**
 * TASK 1 (HU4): segunda rama permitida por la sealed class Persona.
 * Representa a un consultor externo que no pertenece a la nómina interna
 * (por eso no hereda de Empleado), pero sigue siendo una Persona dentro
 * del dominio de negocio.
 *
 * Al ser "final", esta clase cierra por completo su propia rama de la
 * jerarquía: nadie puede extenderla. Es el nivel máximo de protección
 * que ofrecen las sealed classes.
 */
public final class ConsultorExterno extends Persona {

    private String empresaConsultora;
    private double tarifaPorHora;

    public ConsultorExterno(String nombre, int edad, String empresaConsultora, double tarifaPorHora) {
        super(nombre, edad);
        this.empresaConsultora = empresaConsultora;
        this.tarifaPorHora = tarifaPorHora;
    }

    public String getEmpresaConsultora() {
        return empresaConsultora;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    @Override
    public String toString() {
        return "ConsultorExterno{nombre='" + getNombre() + "', empresa='" + empresaConsultora +
                "', tarifaPorHora=" + tarifaPorHora + "}";
    }
}

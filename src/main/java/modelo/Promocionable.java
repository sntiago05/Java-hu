package modelo;

/**
 * =========================================================
 * TASK 4 (HU4): Abstracción y evolución de interfaces.
 * =========================================================
 *
 * Contrato de comportamiento para cualquier tipo que pueda ser evaluado
 * para un ascenso y recibir un bono asociado.
 */
public interface Promocionable {

    // Método abstracto: cada implementación decide su propia fórmula
    // de bono de ascenso.
    double calcularBonoAscenso();

    /**
     * EVOLUCIÓN DE INTERFACES (Java 8+): métodos default.
     * ---------------------------------------------------------
     * Antes de Java 8, agregar un nuevo método a una interfaz ya publicada
     * era una operación que ROMPÍA la compatibilidad binaria: todas las
     * clases que ya implementaban esa interfaz dejaban de compilar hasta
     * que implementaran el método nuevo. Esto hacía casi imposible hacer
     * evolucionar interfaces ampliamente usadas (como pasó históricamente
     * con las interfaces del Collections Framework).
     *
     * Java 8 introdujo los métodos "default": una implementación por
     * defecto vive dentro de la propia interfaz, así que las clases que
     * ya la implementaban (Desarrollador, Gerente, o cualquier otra que
     * se agregue en el futuro) siguen compilando sin cambios y heredan
     * este comportamiento automáticamente, pudiendo sobreescribirlo solo
     * si lo necesitan.
     *
     * Aquí se usa para centralizar el log de la operación de ascenso sin
     * obligar a cada clase a repetir esta lógica.
     */
    default void registrarLogAscenso(String nombrePersona) {
        double bono = calcularBonoAscenso();
        System.out.printf("[LOG ASCENSO] %s fue evaluado. Bono calculado: %.2f%n", nombrePersona, bono);
    }
}

package modelo;

/**
 * =========================================================
 * TASK 2 (HU4): Modelado inmutable con records.
 * =========================================================
 *
 * ---------------------------------------------------------
 * SINTAXIS LEGACY (Java 8/11) — POJO tradicional:
 * ---------------------------------------------------------
 * Para lograr el mismo resultado (un objeto que agrupa idEmpleado,
 * promedio y feedback) en Java 8/11 habría que escribir manualmente
 * TODO este código repetitivo:
 *
 *     public final class DesempeñoReportLegacy {
 *         private final int idEmpleado;
 *         private final double promedio;
 *         private final String feedback;
 *
 *         public DesempeñoReportLegacy(int idEmpleado, double promedio, String feedback) {
 *             this.idEmpleado = idEmpleado;
 *             this.promedio = promedio;
 *             this.feedback = feedback;
 *         }
 *
 *         public int getIdEmpleado() { return idEmpleado; }
 *         public double getPromedio() { return promedio; }
 *         public String getFeedback() { return feedback; }
 *
 *         @Override
 *         public boolean equals(Object o) {
 *             // comparar campo por campo manualmente...
 *         }
 *
 *         @Override
 *         public int hashCode() {
 *             // combinar hashes manualmente...
 *         }
 *
 *         @Override
 *         public String toString() {
 *             return "DesempeñoReportLegacy{idEmpleado=" + idEmpleado +
 *                     ", promedio=" + promedio + ", feedback='" + feedback + "'}";
 *         }
 *     }
 *
 * Además, nada impide que un desarrollador agregue un setter y rompa la
 * inmutabilidad que un reporte de desempeño de fin de mes debería
 * garantizar (un reporte ya emitido no debería poder alterarse).
 *
 * ---------------------------------------------------------
 * SINTAXIS MODERNA (Java 17/21) — record:
 * ---------------------------------------------------------
 * Una sola línea reemplaza TODO el bloque anterior. El compilador genera
 * automáticamente: constructor canónico, accesores (idEmpleado(),
 * promedio(), feedback()), equals(), hashCode() y toString().
 * Los campos de un record son implícitamente "private final", por lo que
 * la inmutabilidad queda garantizada por el lenguaje, no por convención
 * del equipo de desarrollo.
 */
public record DesempeñoReport(int idEmpleado, double promedio, String feedback) {
}

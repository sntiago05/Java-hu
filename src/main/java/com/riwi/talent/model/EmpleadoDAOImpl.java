package com.riwi.talent.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TASK 2 (HU5): implementación física de EmpleadoDAO usando JDBC puro.
 *
 * SEGURIDAD: todas las consultas usan PreparedStatement con parámetros
 * ("?") en lugar de concatenar Strings con los valores recibidos del
 * usuario. Esto evita ataques de inyección SQL, porque el driver JDBC
 * envía los parámetros por separado del texto de la sentencia: el motor
 * de base de datos nunca interpreta el valor de un parámetro como parte
 * del propio SQL, sin importar qué caracteres especiales contenga
 * (comillas, punto y coma, comentarios "--", etc.).
 *
 * Ejemplo de lo que NUNCA se debe hacer (vulnerable a inyección):
 *     String sql = "SELECT * FROM empleados WHERE nombre = '" + nombre + "'";
 *     // si nombre = "x' OR '1'='1", la condición siempre sería verdadera.
 *
 * En este DAO, en cambio, "nombre" siempre viaja como parámetro:
 *     ps.setString(1, nombre);
 */
public class EmpleadoDAOImpl implements EmpleadoDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO empleados (nivel_acceso, anio_ingreso, numero_documento, puntaje_test, " +
            "salario_base, tipo_contrato, es_activo, nombre, edad, id_sede, bono_mensual) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_LISTAR = "SELECT * FROM empleados ORDER BY id_empleado";

    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM empleados WHERE id_empleado = ?";

    private static final String SQL_ACTUALIZAR =
            "UPDATE empleados SET nivel_acceso = ?, anio_ingreso = ?, numero_documento = ?, " +
            "puntaje_test = ?, salario_base = ?, tipo_contrato = ?, es_activo = ?, nombre = ?, " +
            "edad = ?, id_sede = ?, bono_mensual = ? WHERE id_empleado = ?";

    private static final String SQL_ELIMINAR = "DELETE FROM empleados WHERE id_empleado = ?";

    // TASK 4 (HU5): consulta SELECT compleja con JOIN y AVG() para
    // calcular el promedio de las calificaciones trimestrales de cada
    // empleado directamente en la base de datos, y construir el
    // feedback con un CASE SQL. El resultado se mapea uno a uno al
    // record DesempeñoReport.
    private static final String SQL_REPORTE_DESEMPENO =
            "SELECT e.id_empleado AS id_empleado, " +
            "       COALESCE(AVG(c.calificacion), 0) AS promedio, " +
            "       CASE WHEN COALESCE(AVG(c.calificacion), 0) >= 4.0 " +
            "            THEN 'Desempeño sobresaliente, listo para evaluación de ascenso.' " +
            "            ELSE 'Desempeño dentro de lo esperado, con oportunidades de mejora.' " +
            "       END AS feedback " +
            "FROM empleados e " +
            "LEFT JOIN calificaciones c ON c.id_empleado = e.id_empleado " +
            "GROUP BY e.id_empleado " +
            "ORDER BY e.id_empleado";

    // =====================================================================
    // TASK 1 (HU5) recordatorio: cada método siguiente usa
    // try-with-resources para Connection + PreparedStatement (+ ResultSet
    // cuando aplica). Nunca se cierra nada manualmente en un "finally":
    // el compilador se encarga de cerrar TODOS los recursos declarados en
    // el try, en orden inverso a su apertura, incluso si ocurre una
    // excepción. Ver ConexionBD.java para el contraste detallado contra
    // el cierre manual de Java 8 hacia atrás.
    // =====================================================================

    @Override
    public boolean insertar(Empleado empleado) {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {

            mapearParametrosComunes(ps, empleado);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar el empleado: " + e.getMessage());
            return false;
        }
        // Connection y PreparedStatement se cierran automáticamente aquí,
        // aunque executeUpdate() lance una excepción.
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los empleados: " + e.getMessage());
        }
        // Connection, PreparedStatement y ResultSet se cierran los tres,
        // en orden inverso, sin necesidad de un bloque finally.
        return empleados;
    }

    @Override
    public Optional<Empleado> buscarPorId(int idEmpleado) {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, idEmpleado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEmpleado(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el empleado: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean actualizar(Empleado empleado) {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            int siguienteIndice = mapearParametrosComunes(ps, empleado);
            ps.setInt(siguienteIndice, empleado.getIdEmpleado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idEmpleado) {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, idEmpleado);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<DesempeñoReport> generarReporteDesempeno() {
        List<DesempeñoReport> reportes = new ArrayList<>();

        // TASK 4 (HU5): ANÁLISIS MODERNO.
        // Frente al POJO tradicional de Java 8 (que exigiría escribir a
        // mano un ReporteDesempenoLegacy con constructor, getters,
        // equals/hashCode/toString, y además exponer setters que
        // permitirían mutar un reporte ya emitido), aquí el ResultSet se
        // mapea directamente a un record inmutable: DesempeñoReport.
        // Esto reduce drásticamente el código de mapeo (una sola línea
        // por fila, dentro del bucle while) y garantiza, por el propio
        // lenguaje, que el reporte devuelto no podrá modificarse una vez
        // construido, algo especialmente importante en una capa que
        // expone datos de solo lectura para consolidar reportes.
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_REPORTE_DESEMPENO);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DesempeñoReport reporte = new DesempeñoReport(
                        rs.getInt("id_empleado"),
                        rs.getDouble("promedio"),
                        rs.getString("feedback")
                );
                reportes.add(reporte);
            }

        } catch (SQLException e) {
            System.out.println("Error al generar el reporte de desempeño: " + e.getMessage());
        }
        return reportes;
    }

    /**
     * Método de apoyo privado: asigna los parámetros comunes al INSERT y
     * al UPDATE (todos los campos de Empleado menos el id, que en el
     * UPDATE se asigna aparte en la cláusula WHERE).
     *
     * @return el índice del siguiente parámetro libre (útil para que
     *         actualizar() sepa en qué posición debe fijar el WHERE id).
     */
    private int mapearParametrosComunes(PreparedStatement ps, Empleado empleado) throws SQLException {
        int i = 1;
        ps.setByte(i++, empleado.getNivelAcceso());
        ps.setShort(i++, empleado.getAnioIngreso());
        ps.setLong(i++, empleado.getNumeroDocumento());
        ps.setFloat(i++, empleado.getPuntajeTest());
        ps.setDouble(i++, empleado.getSalarioBase());
        ps.setString(i++, String.valueOf(empleado.getTipoContrato()));
        ps.setBoolean(i++, empleado.isEsActivo());
        ps.setString(i++, empleado.getNombre());
        ps.setInt(i++, empleado.getEdad());
        ps.setInt(i++, empleado.getIdSede());
        ps.setDouble(i++, empleado.getBonoMensual());
        return i;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getByte("nivel_acceso"),
                rs.getShort("anio_ingreso"),
                rs.getInt("id_empleado"),
                rs.getLong("numero_documento"),
                rs.getFloat("puntaje_test"),
                rs.getDouble("salario_base"),
                rs.getString("tipo_contrato").charAt(0),
                rs.getBoolean("es_activo"),
                rs.getString("nombre"),
                rs.getInt("edad"),
                rs.getInt("id_sede"),
                rs.getDouble("bono_mensual")
        );
    }
}

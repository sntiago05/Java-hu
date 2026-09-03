package com.riwi.talent.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * =========================================================
 * TASK 1 (HU5): Gestión de conexiones y recursos JDBC.
 * =========================================================
 *
 * Clase de utilidad responsable exclusivamente de abrir una conexión
 * hacia la base de datos relacional. Se centraliza aquí para que toda la
 * capa DAO dependa de un único punto de configuración (URL, usuario,
 * clave), en vez de repetir estos datos en cada consulta.
 *
 * Para que esta clase funcione en tiempo de ejecución, el proyecto debe
 * incluir en el pom.xml el driver JDBC correspondiente al motor elegido
 * (por defecto se configura para MySQL con mysql-connector-j) y la base
 * de datos "talent_hub_db" debe existir con la tabla "empleados" (ver
 * src/main/resources/schema.sql).
 *
 * ---------------------------------------------------------
 * SINTAXIS LEGACY (Java 8 hacia atrás) — cierre manual con "finally":
 * ---------------------------------------------------------
 * Antes de Java 7 (que introdujo try-with-resources) no existía una forma
 * declarativa de cerrar recursos. El patrón obligatorio era abrir los
 * recursos fuera del try y cerrarlos manualmente, uno por uno, dentro de
 * un bloque finally, verificando siempre null para evitar una segunda
 * NullPointerException durante el propio cierre:
 *
 *     Connection con = null;
 *     PreparedStatement ps = null;
 *     ResultSet rs = null;
 *     try {
 *         con = DriverManager.getConnection(URL, USUARIO, CLAVE);
 *         ps = con.prepareStatement("SELECT * FROM empleados");
 *         rs = ps.executeQuery();
 *         // ... procesar rs ...
 *     } catch (SQLException e) {
 *         e.printStackTrace();
 *     } finally {
 *         // Cierre manual, en orden inverso a la apertura, y cada uno
 *         // envuelto en su propio try/catch porque close() también
 *         // puede lanzar SQLException.
 *         if (rs != null) {
 *             try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
 *         }
 *         if (ps != null) {
 *             try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
 *         }
 *         if (con != null) {
 *             try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
 *         }
 *     }
 *
 * El problema de este estilo es que es sumamente propenso a errores:
 * si un desarrollador olvida cerrar UNO solo de los recursos (por
 * ejemplo, si el equipo agrega un nuevo ResultSet y olvida su bloque de
 * cierre), la conexión física con la base de datos queda abierta. Si
 * esto ocurre repetidamente (por ejemplo, en un bucle que atiende
 * peticiones), el pool de conexiones del servidor de base de datos se
 * agota progresivamente: esto es una fuga de recursos (Resource/Memory
 * Leak) que normalmente solo se detecta en producción, cuando el sistema
 * empieza a rechazar nuevas conexiones.
 *
 * ---------------------------------------------------------
 * SINTAXIS MODERNA (Java 17/21) — try-with-resources:
 * ---------------------------------------------------------
 * Desde Java 7, cualquier clase que implemente la interfaz
 * AutoCloseable (Connection, PreparedStatement y ResultSet lo hacen)
 * puede declararse dentro del paréntesis del try. Al finalizar el
 * bloque —ya sea normalmente o por una excepción— la JVM garantiza que
 * se invoque close() sobre CADA recurso declarado, en orden inverso a
 * su apertura, de forma automática y sin necesidad de escribir ningún
 * finally.
 *
 * ¿Cómo previene esto las fugas de memoria (Memory Leaks)?
 *  1) El cierre es responsabilidad del compilador/JVM, no de la
 *     disciplina del desarrollador: es imposible "olvidar" cerrar un
 *     recurso declarado en el try-with-resources.
 *  2) Si ocurre una excepción tanto al ejecutar la consulta como al
 *     cerrar un recurso, Java conserva la excepción original como
 *     "causa principal" y agrega la de cierre como "suprimida"
 *     (Throwable.getSuppressed()), sin perder información de diagnóstico
 *     como sí ocurría fácilmente en el estilo legacy anidado.
 *  3) Al reducir el código boilerplate de cierre, el código resultante
 *     es más corto, más legible y, por lo tanto, más fácil de auditar:
 *     es mucho más sencillo verificar que "todo recurso abierto está en
 *     la lista del try" que rastrear cierres dispersos en un finally
 *     con múltiples try/catch anidados.
 *
 * Todos los métodos de EmpleadoDAOImpl utilizan try-with-resources para
 * Connection, PreparedStatement y, cuando aplica, ResultSet.
 */
public final class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/talent_hub_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "root";

    private ConexionBD() {
        // Clase de utilidad: no debe instanciarse.
    }

    /**
     * Abre y devuelve una nueva conexión JDBC. Se retorna la conexión
     * (no se cierra aquí) para que sea el propio DAO quien la declare
     * dentro de su try-with-resources y controle su ciclo de vida
     * completo junto con el PreparedStatement y el ResultSet que dependan
     * de ella.
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}

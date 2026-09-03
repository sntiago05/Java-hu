package com.riwi.talent.model;

import java.util.List;
import java.util.Optional;

/**
 * TASK 2 (HU5): contrato de acceso a datos para la entidad Empleado.
 * Definir el CRUD como interfaz permite que la capa Controller dependa
 * de una abstracción (EmpleadoDAO) y no de una implementación concreta
 * de JDBC, facilitando en el futuro cambiar el motor de persistencia
 * (por ejemplo, pasar de MySQL a PostgreSQL, o a un mock para pruebas)
 * sin modificar ni el Controller ni la Vista.
 */
public interface EmpleadoDAO {

    /**
     * Inserta un nuevo empleado en la base de datos.
     *
     * @return true si la operación afectó al menos una fila.
     */
    boolean insertar(Empleado empleado);

    /**
     * Lista todos los empleados almacenados en la base de datos.
     */
    List<Empleado> listarTodos();

    /**
     * Busca un empleado puntual por su identificador.
     */
    Optional<Empleado> buscarPorId(int idEmpleado);

    /**
     * Actualiza los datos de un empleado ya existente.
     *
     * @return true si la operación afectó al menos una fila.
     */
    boolean actualizar(Empleado empleado);

    /**
     * Elimina un empleado por su identificador.
     *
     * @return true si la operación afectó al menos una fila.
     */
    boolean eliminar(int idEmpleado);

    /**
     * TASK 4 (HU5): ejecuta una consulta SELECT compleja (con JOIN y
     * funciones de agregación sobre las calificaciones trimestrales) y
     * mapea cada fila del ResultSet directamente a un record
     * DesempeñoReport, inmutable.
     */
    List<DesempeñoReport> generarReporteDesempeno();
}

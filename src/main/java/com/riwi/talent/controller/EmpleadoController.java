package com.riwi.talent.controller;

import com.riwi.talent.model.DesempeñoReport;
import com.riwi.talent.model.Empleado;
import com.riwi.talent.model.EmpleadoDAO;

import java.util.List;
import java.util.Optional;

/**
 * TASK 3 (HU5): Controlador — capa de mediación del patrón MVC.
 *
 * El Controller NUNCA usa Scanner ni imprime menús: solo recibe datos ya
 * validados/parseados desde la Vista, coordina la llamada al DAO
 * (Modelo) y devuelve resultados simples (boolean, List, Optional) para
 * que sea la Vista quien decida cómo mostrarlos. Esto mantiene una
 * separación de responsabilidades clara: la Vista sabe "cómo preguntar y
 * cómo mostrar", el Controller sabe "qué hacer con la petición", y el
 * Modelo (DAO) sabe "cómo persistir".
 */
public class EmpleadoController {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoController(EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    public boolean registrarEmpleado(Empleado empleado) {
        return empleadoDAO.insertar(empleado);
    }

    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoDAO.listarTodos();
    }

    public Optional<Empleado> obtenerEmpleadoPorId(int idEmpleado) {
        return empleadoDAO.buscarPorId(idEmpleado);
    }

    public boolean actualizarEmpleado(Empleado empleado) {
        return empleadoDAO.actualizar(empleado);
    }

    public boolean eliminarEmpleado(int idEmpleado) {
        return empleadoDAO.eliminar(idEmpleado);
    }

    public List<DesempeñoReport> obtenerReporteDesempeno() {
        return empleadoDAO.generarReporteDesempeno();
    }
}

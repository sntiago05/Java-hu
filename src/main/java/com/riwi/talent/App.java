package com.riwi.talent;

import com.riwi.talent.controller.EmpleadoController;
import com.riwi.talent.model.EmpleadoDAO;
import com.riwi.talent.model.EmpleadoDAOImpl;
import com.riwi.talent.view.EmpleadoView;

/**
 * TASK 3 (HU5): punto de entrada de la aplicación. Su única
 * responsabilidad es el "ensamblado" (composition root) de las tres
 * capas del patrón MVC:
 *   Modelo (EmpleadoDAOImpl) -> Controller (EmpleadoController) -> Vista (EmpleadoView)
 * Ni el Controller ni el DAO conocen a la Vista; es la Vista quien
 * conoce al Controller, y el Controller quien conoce al DAO a través de
 * su interfaz (EmpleadoDAO), no de la implementación concreta.
 */
public class App {

    public static void main(String[] args) {
        EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
        EmpleadoController controller = new EmpleadoController(empleadoDAO);
        EmpleadoView view = new EmpleadoView(controller);

        view.iniciar();
    }
}

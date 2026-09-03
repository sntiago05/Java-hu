package com.riwi.talent.view;

import com.riwi.talent.controller.EmpleadoController;
import com.riwi.talent.model.DesempeñoReport;
import com.riwi.talent.model.Empleado;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * TASK 3 (HU5): Vista — única capa autorizada a interactuar con el
 * usuario. Aquí, y solo aquí, se declara y se usa el Scanner. Ningún
 * método del Controller ni del DAO recibe un Scanner como parámetro: la
 * Vista lee los datos crudos, los convierte a los tipos correctos y
 * construye los objetos Empleado antes de entregárselos al Controller.
 */
public class EmpleadoView {

    private final Scanner sc = new Scanner(System.in);
    private final EmpleadoController controller;

    public EmpleadoView(EmpleadoController controller) {
        this.controller = controller;
    }

    public void iniciar() {
        System.out.println("""
                |=====================================|
                |     CORPORATE TALENT HUB (HU5)      |
                |   Persistencia con JDBC + MVC       |
                |=====================================|
                """);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ", 1, 6);

            switch (opcion) {
                case 1 -> registrarEmpleado();
                case 2 -> listarEmpleados();
                case 3 -> actualizarEmpleado();
                case 4 -> eliminarEmpleado();
                case 5 -> mostrarReporteDesempeno();
                case 6 -> System.out.println("Cerrando el sistema...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 6);

        sc.close();
    }

    private void mostrarMenu() {
        System.out.println("""

                ===== MENÚ PRINCIPAL (CRUD Empleados - JDBC) =====
                1. Registrar nuevo empleado
                2. Listar empleados
                3. Actualizar empleado
                4. Eliminar empleado
                5. Reporte de desempeño (Record + SELECT complejo)
                6. Salir
                ===================================================
                """);
    }

    private void registrarEmpleado() {
        System.out.println("\n--- Registro de nuevo empleado ---");
        Empleado nuevo = leerDatosEmpleado(0);

        boolean exito = controller.registrarEmpleado(nuevo);
        if (exito) {
            System.out.println("Empleado registrado con éxito en la base de datos.");
        } else {
            System.out.println("No se pudo registrar el empleado.");
        }
    }

    private void listarEmpleados() {
        List<Empleado> empleados = controller.obtenerTodosLosEmpleados();

        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en la base de datos.");
            return;
        }

        System.out.println("\n=== Empleados registrados ===");
        for (Empleado emp : empleados) {
            System.out.println(emp);
        }
    }

    private void actualizarEmpleado() {
        var id = leerEntero("Ingrese el ID del empleado a actualizar: ", 1, Integer.MAX_VALUE);
        Optional<Empleado> existente = controller.obtenerEmpleadoPorId(id);

        if (existente.isEmpty()) {
            System.out.println("No existe un empleado con ese ID.");
            return;
        }

        System.out.println("Empleado actual: " + existente.get());
        System.out.println("Ingrese los nuevos datos:");
        Empleado actualizado = leerDatosEmpleado(id);

        boolean exito = controller.actualizarEmpleado(actualizado);
        System.out.println(exito ? "Empleado actualizado con éxito." : "No se pudo actualizar el empleado.");
    }

    private void eliminarEmpleado() {
        var id = leerEntero("Ingrese el ID del empleado a eliminar: ", 1, Integer.MAX_VALUE);
        boolean exito = controller.eliminarEmpleado(id);
        System.out.println(exito ? "Empleado eliminado con éxito." : "No existe un empleado con ese ID.");
    }

    // =========================================================
    // TASK 4 (HU5): presentación del reporte final consolidado usando
    // un Text Block para dar formato legible a cada registro. Los datos
    // provienen del record DesempeñoReport, inmutable, que solo puede
    // leerse (idEmpleado(), promedio(), feedback()); la Vista nunca
    // podría alterar accidentalmente un reporte ya generado.
    // =========================================================
    private void mostrarReporteDesempeno() {
        List<DesempeñoReport> reportes = controller.obtenerReporteDesempeno();

        if (reportes.isEmpty()) {
            System.out.println("No hay datos suficientes para generar el reporte.");
            return;
        }

        System.out.println("\n=== REPORTE FINAL CONSOLIDADO DE DESEMPEÑO ===");
        for (DesempeñoReport reporte : reportes) {
            String bloque = """
                    -------------------------------------
                    ID Empleado : %d
                    Promedio    : %.2f
                    Feedback    : %s
                    -------------------------------------
                    """.formatted(reporte.idEmpleado(), reporte.promedio(), reporte.feedback());
            System.out.print(bloque);
        }
    }

    private Empleado leerDatosEmpleado(int idExistente) {
        var nombre = leerTexto("Nombre completo: ");
        var edad = leerEntero("Edad (18-65): ", 18, 65);
        var idSede = leerEntero("ID de sede (1-10): ", 1, 10);
        var nivelAcceso = (byte) leerEntero("Nivel de acceso (1-5): ", 1, 5);
        var anioIngreso = (short) leerEntero("Año de ingreso (2000-2026): ", 2000, 2026);
        var numeroDocumento = leerLong("Número de documento: ");
        var puntajeTest = (float) leerDecimal("Puntaje test de ingreso (0-100): ", 0, 100);
        var salarioBase = leerDecimal("Salario base: ", 0, 50_000_000);
        var tipoContrato = leerCaracterContrato();
        var esActivo = true;
        var bonoMensual = leerDecimal("Bono mensual: ", 0, 5_000_000);

        return new Empleado(nivelAcceso, anioIngreso, idExistente, numeroDocumento, puntajeTest,
                salarioBase, tipoContrato, esActivo, nombre, edad, idSede, bonoMensual);
    }

    private char leerCaracterContrato() {
        while (true) {
            System.out.print("Tipo de contrato (I = Indefinido, T = Término fijo, P = Prestación de servicios): ");
            try {
                var entrada = Character.toUpperCase(sc.next().charAt(0));
                sc.nextLine();
                if (entrada == 'I' || entrada == 'T' || entrada == 'P') {
                    return entrada;
                }
                System.out.println("Opción inválida. Debe ser I, T o P.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.");
                sc.nextLine();
            }
        }
    }

    private int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            try {
                var entrada = sc.nextInt();
                sc.nextLine();
                if (entrada < minimo || entrada > maximo) {
                    System.out.println("Valor fuera de rango. Debe estar entre " + minimo + " y " + maximo + ".");
                    continue;
                }
                return entrada;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar un número entero.");
                sc.nextLine();
            }
        }
    }

    private long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                var entrada = sc.nextLong();
                sc.nextLine();
                if (entrada <= 0) {
                    System.out.println("El número de documento debe ser positivo.");
                    continue;
                }
                return entrada;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar solo números.");
                sc.nextLine();
            }
        }
    }

    private double leerDecimal(String mensaje, double minimo, double maximo) {
        while (true) {
            System.out.print(mensaje);
            try {
                var entrada = sc.nextDouble();
                sc.nextLine();
                if (entrada < minimo || entrada > maximo) {
                    System.out.println("Valor fuera de rango. Debe estar entre " + minimo + " y " + maximo + ".");
                    continue;
                }
                return entrada;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar un número.");
                sc.nextLine();
            }
        }
    }

    private String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("El campo no puede estar vacío.");
            }
        } while (texto.isEmpty());
        return texto;
    }
}

import modelo.Empleado;
import modelo.EmpresaRecord;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {

    // Capacidad máxima de empleados y matriz de calificaciones trimestrales.
    // La matriz es double[][]: cada fila corresponde a un empleado (mismo
    // índice que la lista "empleados") y cada columna a un trimestre (0,1,2).
    private static final int MAX_EMPLEADOS = 10;
    private static final List<Empleado> empleados = new ArrayList<>();
    private static final double[][] calificaciones = new double[MAX_EMPLEADOS][3];

    public static void main(String[] args) {

        System.out.println("""
                |=====================================|
                |     CORPORATE TALENT HUB            |
                |   Gestión del talento humano        |
                |=====================================|
                """);

        // --- Demostración base de la Semana 1 (OOP, records, NPE útil) ---
        Empleado empleadoDemo = crearEmpleado();
        EmpresaRecord empresa = new EmpresaRecord(
                "CodeUp Solutions",
                "900123456-7",
                2015
        );

        System.out.println(empleadoDemo);
        System.out.println("Empresa: " + empresa.nombre());
        System.out.println("Salario final: " + empleadoDemo.calcularSalarioFinal());
        System.out.println("¿Tiene bono extra?: " + empleadoDemo.tieneBonoExtra());
        System.out.println("¿Es elegible?: " + empleadoDemo.validarElegibilidad());

        compararObjetos();
        probarNullPointer(empleadoDemo);

        // --- Semana 2: sistema dinámico con menú interactivo ---
        iniciarSistema();
    }

    private static Empleado crearEmpleado() {
        return new Empleado(
                Byte.MAX_VALUE,
                Short.MAX_VALUE,
                102,
                1_023_456_789L,
                92.5f,
                3_000_000,
                'I',
                true,
                "Laura Gómez",
                27,
                2,
                500_000
        );
    }

    // == compara si dos variables apuntan al mismo objeto en memoria.
    // No compara el contenido de los objetos.
    private static void compararObjetos() {

        Empleado empleado1 = crearEmpleado();
        Empleado empleado2 = crearEmpleado();
        Empleado copia = empleado1;

        System.out.println("empleado1 == empleado2: " + (empleado1 == empleado2));
        System.out.println("empleado1 == copia: " + (empleado1 == copia));
    }

    // En Java 8 normalmente la excepción solo indica que ocurrió una
    // NullPointerException y muestra el stack trace.
    // En Java 17/21 el mensaje explica qué referencia era null,
    // facilitando encontrar la causa del problema.
    private static void probarNullPointer(Empleado empleado) {
        empleado.setNombre(null);
        try {
            System.out.println(empleado.getNombre().toUpperCase());
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // =========================================================
    // TASK 1 y TASK 2: menú principal con switch legacy (Java 8)
    // dentro de un bucle do-while que mantiene el sistema activo.
    // =========================================================
    private static void iniciarSistema() {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Seleccione una opción: ", 1, 4);

            // ---------------------------------------------------------
            // SWITCH LEGACY (Java 8): estructura case / break tradicional.
            // Riesgo del "fall-through": si se olvida un break, la ejecución
            // no se detiene en el case elegido y sigue cayendo en los
            // siguientes, ejecutando código que no correspondía a la opción
            // seleccionada por el usuario. Aquí cada case cierra con break
            // explícito para evitarlo.
            // ---------------------------------------------------------
            switch (opcion) {
                case 1:
                    registrarEmpleado(sc);
                    break;
                case 2:
                    verReporteDesempeno();
                    break;
                case 3:
                    consultarCategoriaSalarial(sc);
                    break;
                case 4:
                    System.out.println("Cerrando el sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (opcion != 4);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("""

                ===== MENÚ PRINCIPAL =====
                1. Registrar nuevo empleado
                2. Ver reporte de desempeño
                3. Consultar categoría salarial
                4. Salir
                ===========================
                """);
    }

    // =========================================================
    // TASK 2: captura dinámica de datos con var (Java 11+) y
    // validación de rangos con if / else.
    // =========================================================
    private static void registrarEmpleado(Scanner sc) {
        if (empleados.size() >= MAX_EMPLEADOS) {
            System.out.println("Se alcanzó el máximo de empleados registrados (" + MAX_EMPLEADOS + ").");
            return;
        }

        System.out.println("\n--- Registro de nuevo empleado ---");

        // Evolución (Java 11+): con "var" el compilador infiere el tipo a
        // partir del valor asignado (aquí sigue siendo String, int, byte...).
        // En Java 8 estas variables se habrían declarado de forma explícita,
        // por ejemplo: String nombre = leerTexto(...); int edad = leerEntero(...);
        // El comportamiento y el tipo en tiempo de compilación son idénticos;
        // solo cambia la forma de escribirlo.
        var nombre = leerTexto(sc, "Nombre completo: ");
        var edad = leerEntero(sc, "Edad (18-65): ", 18, 65);
        var idSede = leerEntero(sc, "ID de sede (1-10): ", 1, 10);
        var nivelAcceso = (byte) leerEntero(sc, "Nivel de acceso (1-5): ", 1, 5);
        var anioIngreso = (short) leerEntero(sc, "Año de ingreso (2000-2026): ", 2000, 2026);
        var idEmpleado = empleados.size() + 1;
        var numeroDocumento = leerLong(sc, "Número de documento: ");
        var puntajeTest = (float) leerDecimal(sc, "Puntaje test de ingreso (0-100): ", 0, 100);
        var salarioBase = leerDecimal(sc, "Salario base: ", 0, 50_000_000);
        var tipoContrato = leerCaracterContrato(sc);
        var esActivo = true;
        var bonoMensual = leerDecimal(sc, "Bono mensual: ", 0, 5_000_000);

        Empleado nuevo = new Empleado(nivelAcceso, anioIngreso, idEmpleado, numeroDocumento,
                puntajeTest, salarioBase, tipoContrato, esActivo, nombre, edad, idSede, bonoMensual);
        empleados.add(nuevo);

        // TASK 3: se registran las 3 calificaciones trimestrales del
        // empleado en la fila correspondiente de la matriz double[][].
        int fila = empleados.size() - 1;
        System.out.println("Ingrese las calificaciones de los 3 trimestres (escala 0.0 a 5.0):");
        for (int trimestre = 0; trimestre < 3; trimestre++) {
            calificaciones[fila][trimestre] =
                    leerDecimal(sc, "Calificación trimestre " + (trimestre + 1) + ": ", 0.0, 5.0);
        }

        System.out.println("Empleado registrado con éxito. ID asignado: " + idEmpleado);
    }

    private static char leerCaracterContrato(Scanner sc) {
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

    // =========================================================
    // TASK 3: matrices de desempeño y casting.
    // Recorre la matriz double[][] con bucles for anidados, calcula el
    // promedio de cada empleado y aplica un casting explícito para el
    // "Puntaje Simplificado".
    // =========================================================
    private static void verReporteDesempeno() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        System.out.println("\n=== Reporte de desempeño ===");

        for (int i = 0; i < empleados.size(); i++) {
            Empleado emp = empleados.get(i);

            double sumaCalificaciones = 0;
            for (int trimestre = 0; trimestre < 3; trimestre++) {
                sumaCalificaciones += calificaciones[i][trimestre];
            }
            double promedio = sumaCalificaciones / 3;

            // Casting explícito de double a int: se trunca la parte decimal
            // (no redondea). Ejemplo: 4.87 se convierte en 4. El "Puntaje
            // Simplificado" pierde precisión frente al promedio real y solo
            // sirve como referencia rápida, no como dato exacto.
            int puntajeSimplificado = (int) promedio;

            // TASK 4: operador ternario para decidir el estado de promoción.
            String estadoPromocion = promedio >= 4.0 ? "Promovido" : "No promovido";

            String categoriaSalarial = obtenerCategoriaSalarial(emp.calcularSalarioFinal());

            System.out.printf(
                    "- %s | Promedio: %.2f | Puntaje simplificado: %d | Estado: %s | %s%n",
                    emp.getNombre(), promedio, puntajeSimplificado, estadoPromocion, categoriaSalarial
            );
        }
    }

    private static void consultarCategoriaSalarial(Scanner sc) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        for (Empleado emp : empleados) {
            System.out.println(emp.getIdEmpleado() + " - " + emp.getNombre());
        }
        var id = leerEntero(sc, "Ingrese el ID del empleado: ", 1, empleados.size());
        Empleado seleccionado = empleados.get(id - 1);
        System.out.println(seleccionado.getNombre() + " -> "
                + obtenerCategoriaSalarial(seleccionado.calcularSalarioFinal()));
    }

    // =========================================================
    // TASK 1: Switch Expression moderna (Java 17/21), sintaxis "->".
    // A diferencia del switch clásico de Java 8 (case: break;), aquí no
    // existe riesgo de "fall-through": cada rama se evalúa de forma aislada,
    // no se necesita break y el switch se usa como expresión que devuelve
    // un valor directamente.
    // =========================================================
    private static String obtenerCategoriaSalarial(double salarioFinal) {
        int nivel = (int) (salarioFinal / 1_000_000);
        return switch (nivel) {
            case 0, 1 -> "Categoría Junior";
            case 2, 3 -> "Categoría Semi-Senior";
            case 4, 5 -> "Categoría Senior";
            default -> "Categoría Ejecutivo";
        };
    }

    // =========================================================
    // TASK 4: manejo de excepciones. Cada método de captura envuelve la
    // lectura del Scanner en un try-catch que atrapa InputMismatchException,
    // de forma que una entrada inválida no rompe el programa.
    //
    // ANÁLISIS LTS: en Java 8, una InputMismatchException típicamente solo
    // reporta el tipo de excepción y la línea del stack trace, sin más
    // contexto. Java 17/21 no cambia el mensaje de esta excepción puntual,
    // pero sí mejoró de forma general el diagnóstico de errores en tiempo
    // de ejecución: por ejemplo, las Helpful NullPointerExceptions (desde
    // Java 14+) indican exactamente qué referencia era null dentro de la
    // expresión, y tanto el compilador como la JVM entregan mensajes más
    // claros y específicos que en Java 8, lo que agiliza la depuración.
    // =========================================================
    private static int leerEntero(Scanner sc, String mensaje, int minimo, int maximo) {
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

    private static long leerLong(Scanner sc, String mensaje) {
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

    private static double leerDecimal(Scanner sc, String mensaje, double minimo, double maximo) {
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

    private static String leerTexto(Scanner sc, String mensaje) {
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

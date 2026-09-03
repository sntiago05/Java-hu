import modelo.ConsultorExterno;
import modelo.Desarrollador;
import modelo.DesempeñoReport;
import modelo.Empleado;
import modelo.EmpresaRecord;
import modelo.Gerente;
import modelo.Persona;
import modelo.Promocionable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    // Capacidad máxima de empleados y matriz de calificaciones trimestrales.
    // La matriz es double[][]: cada fila corresponde a un empleado (mismo
    // índice que la lista "empleados") y cada columna a un trimestre (0,1,2).
    private static final int MAX_EMPLEADOS = 10;
    private static final List<Empleado> empleados = new ArrayList<>();
    private static final double[][] calificaciones = new double[MAX_EMPLEADOS][3];

    // =========================================================
    // TASK 1 (Semana 3): HashMap<String, Empleado> para búsqueda instantánea
    // por ID. La clave es el idEmpleado convertido a String. Mantenerlo
    // sincronizado con "empleados" evita recorrer toda la lista (O(n)) cada
    // vez que se necesita encontrar un empleado puntual: con el HashMap la
    // búsqueda es O(1) en el caso promedio.
    // =========================================================
    private static final Map<String, Empleado> empleadosPorId = new HashMap<>();

    // =========================================================
    // HU4 (TASK 1): lista de Personas que no son Empleado (ConsultorExterno),
    // demostrando que la jerarquía sellada de Persona admite más de una
    // rama sin perder el control de qué tipos existen.
    // =========================================================
    private static final List<ConsultorExterno> consultores = new ArrayList<>();

    // =========================================================
    // HU4 (TASK 2): historial de reportes de desempeño inmutables emitidos
    // al final de cada mes, usando el record DesempeñoReport.
    // =========================================================
    private static final List<DesempeñoReport> historialReportes = new ArrayList<>();

    // =========================================================
    // TASK 2 (Semana 3): catálogos inmutables con Factory Methods (Java 9/11).
    // ¿Por qué List.of()/Map.of() son más seguros que un ArrayList/HashMap
    // tradicional?
    // - Cualquier intento de modificarlos (.add(), .put(), .remove()...)
    //   lanza UnsupportedOperationException en tiempo de ejecución, así que
    //   un cambio accidental se detecta de inmediato en vez de corromper
    //   datos que otras partes del programa asumen fijos.
    // - No aceptan elementos null, evitando NullPointerException ocultas.
    // La contraparte es justamente esa: son inmutables. Si el catálogo
    // necesita crecer o encogerse en tiempo de ejecución, List.of()/Map.of()
    // no sirven y hay que volver a ArrayList/HashMap.
    // =========================================================
    private static final List<String> TECNOLOGIAS = List.of("Java", "Spring Boot", "SQL", "Git", "Docker");
    private static final Map<String, String> SEDES = Map.of(
            "1", "Medellín",
            "2", "Bogotá",
            "3", "Cali",
            "4", "Barranquilla"
    );

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

        // --- HU4: demostración de la jerarquía sellada, records,
        // pattern matching y la interfaz Promocionable ---
        poblarJerarquiaDemo();

        // --- Semana 2: sistema dinámico con menú interactivo ---
        iniciarSistema();
    }

    // =========================================================
    // TASK 1 (HU4): fábrica de ejemplo para poblar la jerarquía sellada
    // con un Desarrollador, un Gerente y un ConsultorExterno, y así poder
    // demostrar el polimorfismo y el pattern matching sobre Persona.
    // =========================================================
    private static void poblarJerarquiaDemo() {
        Desarrollador dev = new Desarrollador(
                (byte) 3, (short) 2023, empleados.size() + 1, 1_111_111_111L,
                90f, 4_000_000, 'I', true, "Camilo Ríos", 29, 1, 300_000, "Java"
        );
        Gerente ger = new Gerente(
                (byte) 5, (short) 2019, empleados.size() + 2, 2_222_222_222L,
                95f, 9_000_000, 'I', true, "Marcela Prada", 41, 2, 800_000, 15_000_000
        );

        empleados.add(dev);
        empleados.add(ger);
        empleadosPorId.put(String.valueOf(dev.getIdEmpleado()), dev);
        empleadosPorId.put(String.valueOf(ger.getIdEmpleado()), ger);

        consultores.add(new ConsultorExterno("Andrea Salas", 35, "Deloitte", 180_000));

        System.out.println("\n=== TASK 3 (HU4): validación de rol Legacy vs Moderno ===");
        for (Empleado emp : List.of(dev, ger)) {
            validarRolLegacy(emp);
            validarRolModerno(emp);
        }

        System.out.println("\n=== TASK 4 (HU4): interfaz Promocionable (Polimorfismo + método default) ===");
        evaluarAscenso(dev);
        evaluarAscenso(ger);
    }

    // =========================================================
    // TASK 3 (HU4) - SINTAXIS LEGACY (Java 8/11):
    // instanceof seguido de un casting manual OBLIGATORIO. Si se olvida
    // el cast, el código ni compila; y si el cast es incorrecto, se lanza
    // ClassCastException en tiempo de ejecución. Además el tipo se
    // comprueba y se castea por separado, repitiendo la referencia "p".
    // =========================================================
    private static void validarRolLegacy(Persona p) {
        if (p instanceof Desarrollador) {
            String lenguaje = ((Desarrollador) p).getLenguajePrincipal();
            System.out.println("[Legacy] " + p.getNombre() + " es Desarrollador -> Lenguaje: " + lenguaje);
        } else if (p instanceof Gerente) {
            double presupuesto = ((Gerente) p).getPresupuestoMensual();
            System.out.println("[Legacy] " + p.getNombre() + " es Gerente -> Presupuesto: " + presupuesto);
        }
    }

    // =========================================================
    // TASK 3 (HU4) - SINTAXIS MODERNA (Java 17/21):
    // Pattern Matching for instanceof. La comprobación de tipo y la
    // creación de la variable ya casteada ocurren en una sola expresión
    // ("p instanceof Desarrollador des"), eliminando el casting manual y
    // el riesgo de un cast incorrecto: si el patrón no coincide, "des"
    // simplemente no existe en esa rama, y el compilador lo garantiza.
    // Combinado con el "sealed" de Persona/Empleado, el compilador incluso
    // puede advertir si un switch de patrones deja algún tipo sin cubrir.
    // =========================================================
    private static void validarRolModerno(Persona p) {
        if (p instanceof Desarrollador des) {
            System.out.println("[Java 21] " + p.getNombre() + " es Desarrollador -> Lenguaje: " + des.getLenguajePrincipal());
        } else if (p instanceof Gerente ger) {
            System.out.println("[Java 21] " + p.getNombre() + " es Gerente -> Presupuesto: " + ger.getPresupuestoMensual());
        }
    }

    // =========================================================
    // TASK 4 (HU4): Polimorfismo mediante la interfaz Promocionable.
    // El método reciba cualquier objeto que implemente Promocionable
    // (Desarrollador o Gerente) y llama al mismo método default sin
    // necesidad de saber de qué subclase concreta se trata: cada una
    // calcula su propio bono mediante calcularBonoAscenso().
    // =========================================================
    private static void evaluarAscenso(Promocionable promocionable) {
        String nombre = (promocionable instanceof Empleado emp) ? emp.getNombre() : "Persona";
        promocionable.registrarLogAscenso(nombre);
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
            opcion = leerEntero(sc, "Seleccione una opción: ", 1, 10);

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
                    eliminarEmpleado(sc);
                    break;
                case 5:
                    mostrarCatalogosInmutables();
                    break;
                case 6:
                    compararPrimeroUltimo();
                    break;
                case 7:
                    filtrarPorPuntajeMinimo(sc);
                    break;
                case 8:
                    reporteFinal();
                    break;
                case 9:
                    emitirReportesDesempenoFinMes();
                    break;
                case 10:
                    System.out.println("Cerrando el sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (opcion != 10);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("""

                ===== MENÚ PRINCIPAL =====
                1. Registrar nuevo empleado
                2. Ver reporte de desempeño
                3. Consultar categoría salarial
                4. Eliminar empleado (ArrayList + HashMap)
                5. Ver catálogos inmutables (List.of / Map.of)
                6. Primero/último empleado (Legacy vs Java 21)
                7. Filtrar por puntaje mínimo (removeIf)
                8. Reporte final (total y promedio de salarios)
                9. Emitir reportes de desempeño de fin de mes (Record HU4)
                10. Salir
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

        // TASK 1: se agrega tanto a la lista (orden de registro) como al
        // HashMap (acceso instantáneo por ID). Ambas colecciones deben
        // mantenerse sincronizadas manualmente porque son estructuras
        // independientes que solo comparten las mismas referencias a Empleado.
        empleados.add(nuevo);
        empleadosPorId.put(String.valueOf(idEmpleado), nuevo);

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
    // TASK 1 (Semana 3): eliminar de ArrayList y HashMap a la vez.
    // =========================================================
    private static void eliminarEmpleado(Scanner sc) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        System.out.println("\n--- Empleados registrados ---");
        for (var empleado : empleados) {
            System.out.println(empleado.getIdEmpleado() + " - " + empleado.getNombre());
        }

        var idTexto = leerTexto(sc, "Ingrese el ID del empleado a eliminar: ");
        Empleado empleado = empleadosPorId.get(idTexto);

        if (empleado == null) {
            System.out.println("No existe un empleado con ese ID.");
            return;
        }

        empleados.remove(empleado);
        empleadosPorId.remove(idTexto);
        System.out.println("Empleado eliminado: " + empleado.getNombre());
    }

    // =========================================================
    // TASK 2 (Semana 3): mostrar los catálogos inmutables y demostrar que
    // intentar modificarlos lanza UnsupportedOperationException.
    // =========================================================
    private static void mostrarCatalogosInmutables() {
        System.out.println("\n--- Catálogo de Tecnologías (List.of) ---");
        System.out.println(TECNOLOGIAS);

        System.out.println("\n--- Catálogo de Sedes (Map.of) ---");
        SEDES.forEach((id, nombreSede) -> System.out.println(id + " -> " + nombreSede));

        try {
            TECNOLOGIAS.add("Kotlin");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nNo se pudo agregar 'Kotlin': la lista es inmutable (UnsupportedOperationException).");
        }
    }

    // =========================================================
    // TASK 3 (Semana 3): Sequenced Collections de Java 21.
    // Legacy (8/11): get(0) y get(size()-1) exigen calcular el índice
    // manualmente; un error de cálculo (por ejemplo olvidar el "-1")
    // produce un IndexOutOfBoundsException en tiempo de ejecución.
    // Java 21: getFirst()/getLast() expresan la intención directamente,
    // sin aritmética de índices, y reversed() entrega la colección en
    // orden inverso sin escribir ningún algoritmo de ordenamiento.
    // =========================================================
    private static void compararPrimeroUltimo() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        Empleado primeroLegacy = empleados.get(0);
        Empleado ultimoLegacy = empleados.get(empleados.size() - 1);
        System.out.println("\n[Legacy 8/11] Primero: " + primeroLegacy.getNombre()
                + " | Último: " + ultimoLegacy.getNombre());

        Empleado primeroModerno = empleados.getFirst();
        Empleado ultimoModerno = empleados.getLast();
        System.out.println("[Java 21]     Primero: " + primeroModerno.getNombre()
                + " | Último: " + ultimoModerno.getNombre());

        System.out.println("\nLista en orden inverso (Java 21, reversed()):");
        for (var empleado : empleados.reversed()) {
            System.out.println("- " + empleado.getNombre());
        }
    }

    // =========================================================
    // TASK 4 (Semana 3): removeIf para filtrado avanzado.
    // En Java 8, quitar elementos mientras se recorre la lista con un
    // for-each normal lanza ConcurrentModificationException; había que usar
    // un Iterator explícito con iterator.remove(). removeIf resuelve esto
    // en una sola línea, de forma segura y declarativa.
    // =========================================================
    private static void filtrarPorPuntajeMinimo(Scanner sc) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        var minimo = (float) leerDecimal(sc, "Puntaje mínimo requerido (0-100): ", 0, 100);
        var totalAntes = empleados.size();

        empleados.removeIf(empleado -> empleado.getPuntajeTest() < minimo);
        empleadosPorId.values().removeIf(empleado -> empleado.getPuntajeTest() < minimo);

        var eliminados = totalAntes - empleados.size();
        System.out.println(eliminados + " empleado(s) eliminado(s) por no alcanzar el puntaje mínimo.");
    }

    // =========================================================
    // TASK 2 (HU4): emite un DesempeñoReport (record) inmutable por cada
    // empleado registrado, integrando el modelado inmutable al flujo real
    // de la aplicación (cierre de mes). El promedio se toma de la misma
    // matriz "calificaciones" que ya usa verReporteDesempeno().
    // Al ser un record, una vez creado el reporte NO puede modificarse:
    // no existen setIdEmpleado(), setPromedio() ni setFeedback().
    // =========================================================
    private static void emitirReportesDesempenoFinMes() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        System.out.println("\n=== Emisión de reportes de desempeño de fin de mes (Record) ===");
        for (int i = 0; i < empleados.size(); i++) {
            Empleado emp = empleados.get(i);

            double promedio = 0;
            if (i < MAX_EMPLEADOS) {
                double suma = 0;
                for (int trimestre = 0; trimestre < 3; trimestre++) {
                    suma += calificaciones[i][trimestre];
                }
                promedio = suma / 3;
            }

            String feedback = promedio >= 4.0
                    ? "Desempeño sobresaliente, listo para evaluación de ascenso."
                    : "Desempeño dentro de lo esperado, con oportunidades de mejora.";

            DesempeñoReport reporte = new DesempeñoReport(emp.getIdEmpleado(), promedio, feedback);
            historialReportes.add(reporte);

            System.out.printf(
                    "Reporte inmutable -> idEmpleado=%d | promedio=%.2f | feedback=\"%s\"%n",
                    reporte.idEmpleado(), reporte.promedio(), reporte.feedback()
            );
        }
        System.out.println("Total de reportes emitidos e históricos: " + historialReportes.size());
    }

    // =========================================================
    // TASK 4 (Semana 3): reporte final con var (Java 11+).
    // Frente a la declaración explícita de Java 8 ("double sumaSalarios = 0;",
    // "Empleado empleado : empleados"), var deja que el compilador infiera
    // el tipo a partir del valor asignado, reduciendo ruido visual sin
    // perder seguridad de tipos (se sigue verificando en compilación).
    // =========================================================
    private static void reporteFinal() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados todavía.");
            return;
        }

        var sumaSalarios = 0.0;
        for (var empleado : empleados) {
            sumaSalarios += empleado.getSalarioBase();
        }
        var promedioSalarios = sumaSalarios / empleados.size();

        System.out.println("\n=== Reporte final ===");
        System.out.println("Total de empleados: " + empleados.size());
        System.out.printf("Promedio de salarios: %.2f%n", promedioSalarios);
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

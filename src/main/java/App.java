import modelo.Empleado;
import modelo.EmpresaRecord;

public class App {

    public static void main(String[] args) {

        System.out.println("""
                |=====================================|
                |     CORPORATE TALENT HUB            |
                |   Gestión del talento humano        |
                |=====================================|
                """);

        Empleado empleado = crearEmpleado();
        EmpresaRecord empresa = new EmpresaRecord(
                "CodeUp Solutions",
                "900123456-7",
                2015
        );

        System.out.println(empleado);
        System.out.println("Empresa: " + empresa.nombre());
        System.out.println("Salario final: " + empleado.calcularSalarioFinal());
        System.out.println("¿Tiene bono extra?: " + empleado.tieneBonoExtra());
        System.out.println("¿Es elegible?: " + empleado.validarElegibilidad());

        if (empleado.tieneBonoExtra()) {
            empleado.actualizarBonoMensual(100_000);
            System.out.println("Nuevo bono: " + empleado.getBonoMensual());
        }

        compararObjetos();
        probarNullPointer(empleado);
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
}
package modelo;

public class Empleado {
    private byte nivelAcceso;
    private short anioIngreso;
    private int idEmpleado;
    private long numeroDocumento;
    private float puntajeTest;
    private double salarioBase;
    private char tipoContrato;
    private boolean esActivo;
    // String no es un tipo primitivo; es una clase que representa texto.
    private String nombre;
    private int edad;
    private int idSede;
    private double bonoMensual;

    public Empleado(
            byte nivelAcceso,
            short anioIngreso,
            int idEmpleado,
            long numeroDocumento,
            float puntajeTest,
            double salarioBase,
            char tipoContrato,
            boolean esActivo,
            String nombre,
            int edad,
            int idSede,
            double bonoMensual) {
        this.nivelAcceso = nivelAcceso;
        this.anioIngreso = anioIngreso;
        this.idEmpleado = idEmpleado;
        this.numeroDocumento = numeroDocumento;
        this.puntajeTest = puntajeTest;
        this.salarioBase = salarioBase;
        this.tipoContrato = tipoContrato;
        this.esActivo = esActivo;
        this.nombre = nombre;
        this.edad = edad;
        this.idSede = idSede;
        this.bonoMensual = bonoMensual;
    }

    // Primero se calcula el bono con un 10% adicional (multiplicación),
    // luego se suma al salario base y finalmente se resta el 5% de descuento.
    // Los paréntesis hacen explícito el orden de evaluación
    public double calcularSalarioFinal() {
        return (salarioBase + (bonoMensual * 1.10))
                - (salarioBase * 0.05);
    }

    public boolean tieneBonoExtra() {
        return idEmpleado % 2 == 0;
    }

    // ! tiene mayor precedencia que &&, y && mayor precedencia que ||.
    // Los paréntesis hacen más clara la expresión.
    public boolean validarElegibilidad() {
        return (puntajeTest > 85 && edad < 30)
                || (idSede == 1 && !esActivo);
    }

    public void actualizarBonoMensual(double incremento) {
        bonoMensual += incremento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public double getBonoMensual() {
        return bonoMensual;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "nivelAcceso=" + nivelAcceso +
                ", anioIngreso=" + anioIngreso +
                ", idEmpleado=" + idEmpleado +
                ", numeroDocumento=" + numeroDocumento +
                ", puntajeTest=" + puntajeTest +
                ", salarioBase=" + salarioBase +
                ", tipoContrato=" + tipoContrato +
                ", esActivo=" + esActivo +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", idSede=" + idSede +
                ", bonoMensual=" + bonoMensual +
                '}';
    }
}

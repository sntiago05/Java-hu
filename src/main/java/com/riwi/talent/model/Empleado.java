package com.riwi.talent.model;

/**
 * TASK 1 (HU4): Empleado ahora forma parte de la jerarquía sellada de
 * Persona (permits Empleado, ConsultorExterno) y, a su vez, se declara
 * "sealed" para restringir su propia herencia únicamente a las dos
 * especializaciones del negocio: Desarrollador y Gerente. Esto forma
 * una jerarquía sellada de dos niveles, totalmente protegida:
 *
 *      Persona (sealed)
 *        ├── Empleado (sealed)
 *        │      ├── Desarrollador (final)
 *        │      └── Gerente (final)
 *        └── ConsultorExterno (final)
 */
public sealed class Empleado extends Persona permits Desarrollador, Gerente {

    // TASK 4: encapsulamiento total. Los atributos "propios" de Empleado
    // se marcan como "protected" únicamente porque Desarrollador y
    // Gerente (sus únicas subclases posibles, gracias a "sealed") pueden
    // necesitar leerlos directamente; todo lo demás sigue siendo private
    // y se expone solo a través de getters/setters.
    private byte nivelAcceso;
    private short anioIngreso;
    protected int idEmpleado;
    private long numeroDocumento;
    private float puntajeTest;
    protected double salarioBase;
    private char tipoContrato;
    private boolean esActivo;
    private int idSede;
    protected double bonoMensual;

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
        super(nombre, edad);
        this.nivelAcceso = nivelAcceso;
        this.anioIngreso = anioIngreso;
        this.idEmpleado = idEmpleado;
        this.numeroDocumento = numeroDocumento;
        this.puntajeTest = puntajeTest;
        this.salarioBase = salarioBase;
        this.tipoContrato = tipoContrato;
        this.esActivo = esActivo;
        this.idSede = idSede;
        this.bonoMensual = bonoMensual;
    }

    // Primero se calcula el bono con un 10% adicional (multiplicación),
    // luego se suma al salario base y finalmente se resta el 5% de descuento.
    public double calcularSalarioFinal() {
        return (salarioBase + (bonoMensual * 1.10))
                - (salarioBase * 0.05);
    }

    public boolean tieneBonoExtra() {
        return idEmpleado % 2 == 0;
    }

    public boolean validarElegibilidad() {
        return (puntajeTest > 85 && getEdad() < 30)
                || (idSede == 1 && !esActivo);
    }

    public void actualizarBonoMensual(double incremento) {
        bonoMensual += incremento;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public double getBonoMensual() {
        return bonoMensual;
    }

    public float getPuntajeTest() {
        return puntajeTest;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    // TASK 2 (HU5): getters adicionales requeridos para mapear el
    // objeto completo hacia y desde la base de datos (INSERT/UPDATE con
    // PreparedStatement y lectura de ResultSet).
    public byte getNivelAcceso() {
        return nivelAcceso;
    }

    public short getAnioIngreso() {
        return anioIngreso;
    }

    public long getNumeroDocumento() {
        return numeroDocumento;
    }

    public char getTipoContrato() {
        return tipoContrato;
    }

    public boolean isEsActivo() {
        return esActivo;
    }

    public int getIdSede() {
        return idSede;
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
                ", nombre='" + getNombre() + '\'' +
                ", edad=" + getEdad() +
                ", idSede=" + idSede +
                ", bonoMensual=" + bonoMensual +
                '}';
    }
}

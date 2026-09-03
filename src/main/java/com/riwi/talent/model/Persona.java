package com.riwi.talent.model;

/**
 * =========================================================
 * TASK 1 (HU4): Herencia sellada vs herencia abierta.
 * =========================================================
 *
 * ---------------------------------------------------------
 * ESTILO LEGACY (Java 8/11) — herencia abierta:
 * ---------------------------------------------------------
 * En Java 8/11 la única forma de "proteger" una jerarquía era declarar
 * la clase abstracta y confiar en la disciplina del equipo:
 *
 *     public abstract class Persona {
 *         protected String nombre;
 *         protected int edad;
 *         // ... constructor, getters ...
 *     }
 *
 * Cualquier clase, en cualquier módulo o incluso en otro JAR agregado
 * al classpath, podía escribir "class Cualquiera extends Persona {}"
 * sin que el autor original de Persona pudiera evitarlo ni enterarse.
 * Esto es herencia ABIERTA: el compilador no impone ningún límite
 * sobre quién extiende la clase.
 *
 * ---------------------------------------------------------
 * ESTILO MODERNO (Java 17/21) — sealed class:
 * ---------------------------------------------------------
 * La palabra clave "sealed" + "permits" convierte la lista de
 * subclases autorizadas en parte del contrato del lenguaje, verificado
 * en tiempo de COMPILACIÓN (no es una convención ni un comentario).
 *
 * ¿Por qué esto da más seguridad en el diseño de APIs?
 *  1) Superficie de extensión controlada: solo Empleado y
 *     ConsultorExterno pueden heredar de Persona. Un consumidor externo
 *     de esta librería no puede crear "class Hacker extends Persona {}"
 *     y colarla donde el dominio espera un tipo conocido.
 *  2) Exhaustividad verificable: al combinarse con Pattern Matching
 *     (switch sobre tipos sellados), el compilador puede advertir si
 *     falta cubrir una subclase, evitando bugs por casos no contemplados.
 *  3) Documentación viva: con solo leer "permits Empleado, ConsultorExterno"
 *     cualquier desarrollador conoce TODO el árbol de tipos posible,
 *     sin tener que buscar en todo el proyecto quién extiende la clase.
 *  4) Rompe la ilusión de extensibilidad infinita típica de la POO
 *     clásica: el diseñador de la API decide explícitamente dónde el
 *     dominio de negocio puede crecer y dónde no.
 * =========================================================
 */
public sealed abstract class Persona permits Empleado, ConsultorExterno {

    // TASK 4: encapsulamiento total -> atributos private, expuestos solo
    // mediante getters, y protected únicamente cuando una subclase
    // realmente necesita acceso directo (ver Empleado).
    private String nombre;
    private int edad;

    protected Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}

package main.java.ec.edu.espe.negocio.logica_negocio.validaciones;

public interface ValidacionStrategy {
    void validar(String nombre, String edad) throws Exception;
}
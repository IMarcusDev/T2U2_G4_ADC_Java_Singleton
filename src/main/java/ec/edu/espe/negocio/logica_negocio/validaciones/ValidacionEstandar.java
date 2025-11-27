package main.java.ec.edu.espe.negocio.logica_negocio.validaciones;

import java.util.regex.Pattern;

public class ValidacionEstandar implements ValidacionStrategy {
    private final Pattern validNameRegex = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    @Override
    public void validar(String nombre, String edad) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío");
        }
        if (!validNameRegex.matcher(nombre).matches()) {
            throw new Exception("El nombre solo debe contener letras");
        }

        try {
            int estAge = Integer.parseInt(edad);
            if (estAge < 0 || estAge > 100) {
                throw new Exception("La edad debe estar entre 0 y 100");
            }
        } catch (NumberFormatException e) {
            throw new Exception("La edad debe ser un número válido");
        }
    }
}
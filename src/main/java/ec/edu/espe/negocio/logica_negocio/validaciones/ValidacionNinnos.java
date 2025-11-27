package main.java.ec.edu.espe.negocio.logica_negocio.validaciones;

import java.util.regex.Pattern;

public class ValidacionNinnos implements ValidacionStrategy {
    private final Pattern validNameRegex = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    @Override
    public void validar(String nombre, String edad) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del niño no puede estar vacío.");
        }
        if (!validNameRegex.matcher(nombre).matches()) {
            throw new Exception("El nombre solo debe contener letras.");
        }

        try {
            int edadNino = Integer.parseInt(edad);
            if (edadNino < 5 || edadNino > 12) {
                throw new Exception("Para el curso vacacional, la edad debe estar entre 5 y 12 años.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("La edad debe ser un número válido.");
        }
    }
}
package main.java.ec.edu.espe.negocio.logica_negocio;

import java.util.List;
import java.util.regex.Pattern;
import main.java.ec.edu.espe.negocio.datos.entidades.Estudiante;
import main.java.ec.edu.espe.negocio.datos.repositorios.EstudianteRepository;


public class EstudianteService {
    private final EstudianteRepository repo = EstudianteRepository.getInstance();
    
    private final Pattern validNameRegex = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    public void agregarEstudiante(String name, String age) throws Exception {
        validarNombre(name);
        int validAge = validarEdad(age);
        String id = String.valueOf(System.nanoTime());
        repo.addEstudiante(new Estudiante(id, name, validAge));
    }

    public void modificarEstudiante(String id, String name, String age) throws Exception {
        validarNombre(name);
        int validAge = validarEdad(age);
        repo.updateEstudiante(new Estudiante(id, name, validAge));
    }

    public void eliminarEstudiante(String id) {
        repo.deleteEstudiante(id);
    }

    public List<Estudiante> obtenerLista() {
        return repo.listEstudiantes();
    }

    private void validarNombre(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) throw new Exception("El nombre no puede estar vacío");
        if (!validNameRegex.matcher(name).matches()) throw new Exception("El nombre solo letras");
    }

    private int validarEdad(String age) throws Exception {
        try {
            int estAge = Integer.parseInt(age);
            if (estAge < 0 || estAge > 100) throw new Exception("Edad entre 0 y 100");
            return estAge;
        } catch (NumberFormatException e) {
            throw new Exception("La edad debe ser número");
        }
    }
}
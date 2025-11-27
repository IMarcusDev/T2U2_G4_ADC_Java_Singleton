package main.java.ec.edu.espe.negocio.logica_negocio;

import java.util.List;
import main.java.ec.edu.espe.negocio.datos.entidades.Estudiante;
import main.java.ec.edu.espe.negocio.datos.repositorios.EstudianteRepository;
import main.java.ec.edu.espe.negocio.logica_negocio.validaciones.ValidacionEstandar;
import main.java.ec.edu.espe.negocio.logica_negocio.validaciones.ValidacionStrategy;

public class EstudianteService {
    private final EstudianteRepository repo = EstudianteRepository.getInstance();
    private ValidacionStrategy estrategiaValidacion;

    public EstudianteService() {
        this.estrategiaValidacion = new ValidacionEstandar();
    }

    public void setEstrategiaValidacion(ValidacionStrategy estrategia) {
        this.estrategiaValidacion = estrategia;
    }

    public void agregarEstudiante(String name, String age) throws Exception {
        estrategiaValidacion.validar(name, age);
        
        String id = String.valueOf(System.nanoTime());
        int validAge = Integer.parseInt(age);
        repo.addEstudiante(new Estudiante(id, name, validAge));
    }

    public void modificarEstudiante(String id, String name, String age) throws Exception {
        estrategiaValidacion.validar(name, age);
        
        int validAge = Integer.parseInt(age);
        repo.updateEstudiante(new Estudiante(id, name, validAge));
    }

    public void eliminarEstudiante(String id) {
        repo.deleteEstudiante(id);
    }

    public List<Estudiante> obtenerLista() {
        return repo.listEstudiantes();
    }
}
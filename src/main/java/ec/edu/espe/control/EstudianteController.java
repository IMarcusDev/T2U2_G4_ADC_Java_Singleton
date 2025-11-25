package main.java.ec.edu.espe.control;

import java.util.List;
import main.java.ec.edu.espe.modelo.datos.entidades.Estudiante;
import main.java.ec.edu.espe.modelo.logica_negocio.EstudianteService;



public class EstudianteController {
    private final EstudianteService service;

    public EstudianteController() {
        this.service = new EstudianteService();
    }

    public void agregar(String nombre, String edad) throws Exception {
        service.agregarEstudiante(nombre, edad);
    }

    public void editar(String id, String nombre, String edad) throws Exception {
        service.modificarEstudiante(id, nombre, edad);
    }

    public void eliminar(String id) {
        service.eliminarEstudiante(id);
    }

    public List<Estudiante> listar() {
        return service.obtenerLista();
    }
}
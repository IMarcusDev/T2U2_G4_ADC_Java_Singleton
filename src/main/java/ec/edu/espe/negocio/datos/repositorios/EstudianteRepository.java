package main.java.ec.edu.espe.negocio.datos.repositorios;

import java.util.ArrayList;
import java.util.List;

import main.java.ec.edu.espe.negocio.datos.entidades.Estudiante;





public class EstudianteRepository {
    private static EstudianteRepository instance;
    
    private final List<Estudiante> estudianteList;

    private EstudianteRepository() {
        estudianteList = new ArrayList<>();
        System.out.println("Inicializando Repositorio (Singleton)...");
    }

    public static synchronized EstudianteRepository getInstance() {
        if (instance == null) {
            instance = new EstudianteRepository();
        }
        return instance;
    }

    public void addEstudiante(Estudiante estudiante) {
        estudianteList.add(estudiante);
    }

    public void updateEstudiante(Estudiante updatedEstudiante) {
        for (int i = 0; i < estudianteList.size(); i++) {
            if (estudianteList.get(i).getId().equals(updatedEstudiante.getId())) {
                estudianteList.set(i, updatedEstudiante);
                return;
            }
        }
    }

    public void deleteEstudiante(String deleteId) {
        estudianteList.removeIf(e -> e.getId().equals(deleteId));
    }

    public List<Estudiante> listEstudiantes() {
        return estudianteList;
    }
}
package main.java.ec.edu.espe.negocio.datos.repositorios;

import java.util.ArrayList;
import java.util.List;
import main.java.ec.edu.espe.negocio.datos.entidades.Estudiante;

public class EstudianteRepository {
    private static EstudianteRepository instance;
    private final List<Estudiante> estudianteList;
    private final List<ObserverRepository> observadores;

    private EstudianteRepository() {
        estudianteList = new ArrayList<>();
        observadores = new ArrayList<>();
        System.out.println("Inicializando Repositorio (Singleton)...");
    }

    public static synchronized EstudianteRepository getInstance() {
        if (instance == null) {
            instance = new EstudianteRepository();
        }
        return instance;
    }

    public void agregarObservador(ObserverRepository obs) {
        observadores.add(obs);
    }

    public void eliminarObservador(ObserverRepository obs) {
        observadores.remove(obs);
    }

    private void notificarObservadores() {
        for (ObserverRepository obs : observadores) {
            obs.actualizarDatos();
        }
    }

    public void addEstudiante(Estudiante estudiante) {
        estudianteList.add(estudiante);
        notificarObservadores();
    }

    public void updateEstudiante(Estudiante updatedEstudiante) {
        for (int i = 0; i < estudianteList.size(); i++) {
            if (estudianteList.get(i).getId().equals(updatedEstudiante.getId())) {
                estudianteList.set(i, updatedEstudiante);
                notificarObservadores();
                return;
            }
        }
    }

    public void deleteEstudiante(String deleteId) {
        boolean eliminado = estudianteList.removeIf(e -> e.getId().equals(deleteId));
        if (eliminado) {
            notificarObservadores();
        }
    }

    public List<Estudiante> listEstudiantes() {
        return estudianteList;
    }
}
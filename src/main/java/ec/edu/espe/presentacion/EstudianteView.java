package main.java.ec.edu.espe.presentacion;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.java.ec.edu.espe.control.EstudianteController;
import main.java.ec.edu.espe.negocio.datos.entidades.Estudiante;



public final class EstudianteView extends JFrame {
    private final EstudianteController controller;
    
    private String estudianteId = null;
    private JTextField txtNombre, txtEdad;
    private DefaultTableModel tableModel;
    private JTable table;

    public EstudianteView() {
        this.controller = new EstudianteController();
        
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Patrón Singleton + NVC");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 121, 107));
        JLabel title = new JLabel("Gestión Estudiantes (NVC)");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        
        // Formulario
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Datos"));
        txtNombre = new JTextField();
        txtEdad = new JTextField();
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());

        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Edad:"));   form.add(txtEdad);
        form.add(new JLabel(""));        form.add(btnGuardar);
        body.add(form);

        // Tabla
        String[] cols = {"ID", "Nombre", "Edad"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        body.add(new JScrollPane(table));

        JPanel actions = new JPanel();
        JButton btnEditar = new JButton("Editar");
        JButton btnBorrar = new JButton("Borrar");

        JButton btnNuevaVentana = new JButton("Abrir Nueva Ventana (Test Singleton)");
        btnNuevaVentana.setBackground(Color.CYAN);

        JButton btnRefrescar = new JButton("Recargar lista");
        btnRefrescar.setBackground(Color.GREEN);

        btnEditar.addActionListener(e -> cargarEdicion());
        btnBorrar.addActionListener(e -> borrar());
        btnNuevaVentana.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new EstudianteView().setVisible(true));
        });
        btnRefrescar.addActionListener(e -> refreshTable());

        actions.add(btnEditar);
        actions.add(btnBorrar);
        actions.add(btnNuevaVentana);
        actions.add(btnRefrescar);
        body.add(actions);

        add(body, BorderLayout.CENTER);
    }

    private void guardar() {
        try {
            if (estudianteId == null) {
                controller.agregar(txtNombre.getText(), txtEdad.getText());
            } else {
                controller.editar(estudianteId, txtNombre.getText(), txtEdad.getText());
                estudianteId = null;
            }
            txtNombre.setText(""); txtEdad.setText("");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Guardado con éxito");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void cargarEdicion() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            estudianteId = (String) tableModel.getValueAt(row, 0);
            txtNombre.setText((String) tableModel.getValueAt(row, 1));
            txtEdad.setText(tableModel.getValueAt(row, 2).toString());
        }
    }

    private void borrar() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.eliminar(id);
            refreshTable();
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Estudiante> lista = controller.listar();
        for (Estudiante e : lista) {
            tableModel.addRow(new Object[]{e.getId(), e.getNombres(), e.getEdad()});
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) refreshTable();
    }
}
package main.java.ec.edu.espe.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.java.ec.edu.espe.control.EstudianteController;
import main.java.ec.edu.espe.negocio.datos.entidades.Estudiante;
import main.java.ec.edu.espe.negocio.datos.repositorios.EstudianteRepository;
import main.java.ec.edu.espe.negocio.datos.repositorios.ObserverRepository;
import main.java.ec.edu.espe.negocio.logica_negocio.validaciones.ValidacionEstandar;
import main.java.ec.edu.espe.negocio.logica_negocio.validaciones.ValidacionNinnos;

public final class EstudianteView extends JFrame implements ObserverRepository {
    
    private final EstudianteController controller;
    
    // --- PALETA DE COLORES Y FUENTES ---
    private final Color COLOR_PRIMARY = new Color(0, 150, 136); // Teal Moderno
    private final Color COLOR_SECONDARY = new Color(52, 73, 94); // Azul Oscuro
    private final Color COLOR_BG = new Color(245, 245, 245); // Gris muy claro
    private final Color COLOR_WHITE = Color.WHITE;
    
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    // --- COMPONENTES ---
    private String estudianteId = null;
    private JTextField txtNombre, txtEdad;
    private DefaultTableModel tableModel;
    private JTable table;

    public EstudianteView() {
        this.controller = new EstudianteController();

        initComponents();
        
        // 2. Suscribirse al Observer (Patrón Observer)
        SwingUtilities.invokeLater(() -> {
            EstudianteRepository.getInstance().agregarObservador(this);
            // 3. Cargar datos iniciales
            refreshTable();
        });
    }

    private void initComponents() {
        setTitle("Gestión de Estudiantes (Patrones: Singleton + Observer + Strategy)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Limpieza de memoria al cerrar la ventana (Desuscribir Observer)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                EstudianteRepository.getInstance().eliminarObservador(EstudianteView.this);
            }
        });

        // =================================================================================
        // SECCIÓN 1: HEADER (Barra Superior)
        // =================================================================================
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new GridBagLayout()); 

        JLabel titleLabel = new JLabel("Registro Académico");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        titleLabel.setIcon(UIManager.getIcon("FileView.computerIcon")); 
        titleLabel.setIconTextGap(15);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // =================================================================================
        // SECCIÓN 2: CUERPO PRINCIPAL
        // =================================================================================
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); 

        // ----------------------------------------------------------------------
        // A. PANEL IZQUIERDO (Formulario y Selector de Estrategia)
        // ----------------------------------------------------------------------
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(COLOR_WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setPreferredSize(new Dimension(320, 0));

        // Título del Formulario
        JLabel lblForm = new JLabel("Datos del Estudiante");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblForm.setForeground(COLOR_SECONDARY);
        lblForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblForm);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(crearLabel("Modo de Validación:"));
        String[] opciones = {"Estándar (0-100 años)", "Curso Niños (5-12 años)"};
        JComboBox<String> cmbEstrategia = new JComboBox<>(opciones);
        cmbEstrategia.setFont(FONT_BODY);
        cmbEstrategia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbEstrategia.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbEstrategia.setBackground(COLOR_WHITE);

        cmbEstrategia.addActionListener(e -> {
            int index = cmbEstrategia.getSelectedIndex();
            if (index == 0) {
                controller.cambiarEstrategia(new ValidacionEstandar());
                mostrarNotificacion("Modo cambiado: Validación Estándar activada.");
            } else {
                controller.cambiarEstrategia(new ValidacionNinnos());
                mostrarNotificacion("Modo cambiado: Validación para Niños activada.");
            }
        });
        
        formPanel.add(cmbEstrategia);
        formPanel.add(Box.createVerticalStrut(15));

        // Campos de Texto
        formPanel.add(crearLabel("Nombre Completo:"));
        txtNombre = crearTextField();
        formPanel.add(txtNombre);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(crearLabel("Edad:"));
        txtEdad = crearTextField();
        formPanel.add(txtEdad);
        formPanel.add(Box.createVerticalStrut(25));

        // Botones del Formulario
        JButton btnGuardar = crearBoton("Guardar Estudiante", COLOR_PRIMARY, COLOR_WHITE);
        btnGuardar.addActionListener(e -> guardar());
        formPanel.add(btnGuardar);
        
        formPanel.add(Box.createVerticalStrut(10));
        
        JButton btnLimpiar = crearBoton("Limpiar Formulario", new Color(224, 224, 224), Color.DARK_GRAY);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        formPanel.add(btnLimpiar);

        formPanel.add(Box.createVerticalGlue()); // Empujar todo arriba

        mainPanel.add(formPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(COLOR_BG);

        // Configuración de Tabla
        String[] columnNames = {"ID", "Nombre", "Edad"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        estilizarTabla(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Barra de Herramientas Inferior
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(COLOR_BG);

        JButton btnNuevaVentana = crearBoton("Nueva Ventana", COLOR_SECONDARY, COLOR_WHITE);
        JButton btnEditar = crearBoton("Editar", new Color(255, 152, 0), COLOR_WHITE); // Naranja
        JButton btnBorrar = crearBoton("Eliminar", new Color(244, 67, 54), COLOR_WHITE); // Rojo

        btnNuevaVentana.setPreferredSize(new Dimension(160, 40));
        btnEditar.setPreferredSize(new Dimension(120, 40));
        btnBorrar.setPreferredSize(new Dimension(120, 40));

        btnNuevaVentana.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new EstudianteView().setVisible(true));
        });
        btnEditar.addActionListener(e -> cargarEdicion());
        btnBorrar.addActionListener(e -> borrar());

        actionPanel.add(btnNuevaVentana);
        actionPanel.add(btnEditar);
        actionPanel.add(btnBorrar);

        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    // =================================================================================
    // MÉTODOS DE LÓGICA
    // =================================================================================

    // Patrón Observer: Este método es llamado automáticamente por el Repositorio
    @Override
    public void actualizarDatos() {
        refreshTable();
    }

    private void guardar() {
        try {
            if (estudianteId == null) {
                controller.agregar(txtNombre.getText(), txtEdad.getText());
            } else {
                controller.editar(estudianteId, txtNombre.getText(), txtEdad.getText());
                estudianteId = null;
            }
            limpiarFormulario();
            mostrarNotificacion("Operación realizada con éxito.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEdicion() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            estudianteId = (String) tableModel.getValueAt(row, 0);
            txtNombre.setText((String) tableModel.getValueAt(row, 1));
            txtEdad.setText(tableModel.getValueAt(row, 2).toString());
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla.");
        }
    }

    private void borrar() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int opt = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                String id = (String) tableModel.getValueAt(row, 0);
                controller.eliminar(id);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante para borrar.");
        }
    }
    
    private void limpiarFormulario() {
        estudianteId = null;
        txtNombre.setText("");
        txtEdad.setText("");
    }

    public void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Estudiante> lista = controller.listar();
            for (Estudiante e : lista) {
                tableModel.addRow(new Object[]{e.getId(), e.getNombres(), e.getEdad()});
            }
        });
    }

    // =================================================================================
    // MÉTODOS DE ESTILO (HELPER METHODS)
    // =================================================================================

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_BOLD);
        label.setForeground(Color.GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField crearTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(100, 35));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(250, 40));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Efecto Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { btn.setBackground(bg.darker()); }
            @Override
            public void mouseExited(MouseEvent evt) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void estilizarTabla(JTable table) {
        table.setRowHeight(30);
        table.setFont(FONT_BODY);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 245, 233));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(COLOR_WHITE);
        header.setForeground(COLOR_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARY));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }
    
    private void mostrarNotificacion(String mensaje) {
        // Un Toast o Dialog pequeño y elegante
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
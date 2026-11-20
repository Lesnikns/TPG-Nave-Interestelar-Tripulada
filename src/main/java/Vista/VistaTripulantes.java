package Vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;

public class VistaTripulantes extends JPanel {

    public VistaTripulantes(TripulantesTableModel modelo) {
        setLayout(new BorderLayout());
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}
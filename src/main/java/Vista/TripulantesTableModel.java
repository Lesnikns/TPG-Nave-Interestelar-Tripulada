package Vista;
 
import javax.swing.table.AbstractTableModel;

import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;
import Naves.SesionDeNave;
import Tripulacion.CargoDecorator; 

public class TripulantesTableModel extends AbstractTableModel implements IObservadorModelo<String> {
    private String[] columnas = {"Nombre", "Origen", "Cargo", "Antiguedad (años)", "Sueldo mensual"};
    private SesionDeNave sesion;
 
    public TripulantesTableModel(SesionDeNave sesion) {
        this.sesion = sesion;
        sesion.agregarObservador(this);
    }
 
    @Override
    public int getRowCount() {
        return sesion.getTripulantes().size();
    }
 
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
 
    @Override
    public String getColumnName(int columna) {
        return columnas[columna];
    }
 
    @Override
    public Object getValueAt(int fila, int columna) {
        CargoDecorator t = (CargoDecorator) sesion.getTripulantes().get(fila);
        switch (columna) {
            case 0:
                return t.getNombre();
            case 1:
                return t.getOrigen();
            case 2:
                return t.getCargo();
            case 3:
                return t.getAntiguedad();
            default:
                return String.format("%.2f PG", t.getSueldo());
        }
    }
    @Override
    public void actualizar(ObservadoModelo<String> origen, String evento) {
        fireTableDataChanged();
    }
}
 
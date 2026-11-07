2/10/26
Herramienta usada: Claude

Proposito de la consulta: pedido para la generacion de javadocs, documentando las funciones hechas (motor + estados, bitacora, entrada)

Documentos afectados: Asistente.Bitacora.java, Asistente.Entrada.java, Motor.MotorWarp.java, Motor.EstadoMotor.java, Motor.Disponible.java, Motor.PreparandoSalto.java, Motor.EnWarp.java, Motor.Enfriamiento.java 

Se verifico el resultado a partir de la lectura de estos javadocs para comprobar su correcta documentacion 

7/10/26 Herramienta usada: Gemini

Se utilizó la IA para una vez integrado todo el proyecto, detectar los errores de compatibilidad. A raíz de eso se pidió su opinión para implementar unos pequeños ajustes de diseño y correcciones menores.
También se utilizó para comentar el código de Asistente.AsistenteDeComando.java y Asistente.Mision.java

9/10/26 herramienta usada: Gemini

Se hizo uso de la IA para generar el código del sistema de prueba, donde replicamos los escenarios pedidos por la cátedra en el pdf del TPG.
Se utilizó para detectar errores después de haber agregado paquetes para separarv las clases.

11/10/26 Herramienta usada: Claude

Se hizo uso de la IA para realizar los archivos README.md y Documentacion.md.

5/11/26 Herramienta usada: Claude

Proposito de la consulta: pedido para la generacion de javadocs de la entrega 2 y de los assert de las precondiciones, documentando las funciones hechas (motor + estados con los pedidos saltar y saltarWarp, interfaz del observador y clase observada propias del patron Observer)

Documentos afectados: Motor.MotorWarp.java, Motor.EstadoMotor.java, Motor.Disponible.java, Motor.PreparandoSalto.java, Motor.EnWarp.java, Motor.Enfriamiento.java, Eventos.ObservadoModelo.java, Eventos.IObservadorModelo.java

Se verifico el resultado a partir de la lectura de estos javadocs para comprobar su correcta documentacion, y ejecutando con -ea para comprobar que los assert no se disparan en los casos de uso normales

7/11/26
Herramienta usada: Claude

Proposito de la consulta: pedido para la generacion de javadocs y de los assert de las precondiciones de las clases de la bitacora observable y del informe final de mision

Documentos afectados: Asistente.Bitacora.java, Asistente.Entrada.java, Asistente.Informe.java, Asistente.HistorialDeInformes.java

Se verifico el resultado a partir de la lectura de estos javadocs para comprobar su correcta documentacion, y ejecutando con -ea para comprobar que los assert no se disparan en los casos de uso normales

7/11/26
Herramienta usada: Claude

Proposito de la consulta: pedido para la generacion de los javadocs y assert restantes (clases del UniversoParalelo Recondito: plataforma de carga, generadoras y naves consumidoras) y de las clases de interfaz con Swing: armado de los paneles con JPanel, JLabel, JButton, JTextArea, JScrollPane y JTable (con su modelo de tabla de solo lectura), su disposicion con BorderLayout y FlowLayout, los comandos de accion de los botones (setActionCommand) y su conexion con el controlador (ActionListener), y la actualizacion de los paneles desde los hilos del modelo con SwingUtilities.invokeLater

Documentos afectados: UniversoParalelo.PlataformaDeCarga.java, UniversoParalelo.GeneradoraDeEnergia.java, UniversoParalelo.NaveConsumidora.java, Vista.InterfazVisor.java, Vista.PanelVisor.java, Vista.PanelBitacora.java, Vista.PanelInformes.java, Controlador.ControladorVisor.java

Se verifico el resultado leyendo los javadocs, compilando, ejecutando con -ea para comprobar que los assert no se disparan en los casos de uso normales, y probando los paneles en una ventana (botones y consola del visor, bitacora e historial de informes actualizandose solos)
public interface EstadoMotor {
	void cerrar(MotorWarp motor);
	void preparar(MotorWarp motor);
	void saltar(MotorWarp motor);
	//void enfriar(MotorWarp motor); //metodo enfriar se definira en la entrega 2
}

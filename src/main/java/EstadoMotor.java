public interface EstadoMotor {
	void dejarDisponible(MotorWarp motor);
	void prepararSalto(MotorWarp motor);
	void warpear(MotorWarp motor);
	void enfriar(MotorWarp motor);
}

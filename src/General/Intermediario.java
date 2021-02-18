package General;

public class Intermediario extends Thread
{
	private static BuzonProductores inicio;
	private static BuzonIntermedio medioCampo = new BuzonIntermedio(1);
	private static BuzonConsumidores fin;
	
	private int action;
	private int id;
	
	public Intermediario(int pAction, int pId, BuzonProductores deTodos, BuzonConsumidores pFin) {
		this.inicio = deTodos;
		this.fin = pFin;
		this.action = pAction;
		this.id = pId;
	}
	
	public void run() {
		if (this.action == 1) {
			String retirado = inicio.retirar(this.id);
			medioCampo.almacenar(retirado, this.id);
		}	
		else {
			String transportado = medioCampo.retirar(this.id);
			fin.almacenar(transportado, this.id);
		}
			
			
	}
	
}

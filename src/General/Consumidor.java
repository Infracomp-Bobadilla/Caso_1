package General;

public class Consumidor extends Thread
{
	private static BuzonConsumidores fin;
	
	private int id;
	private String produc;
	private int MAX_PRODUCTOS;
	
	public Consumidor(int pID, String pProduc, int max, BuzonConsumidores deTodos) {
		this.fin = deTodos;
		this.id = pID;
		this.produc = pProduc;
		this.MAX_PRODUCTOS = max;
	}
	
	public void run() {
		for(int i = 0; i < this.MAX_PRODUCTOS; i++) {
			fin.retirar(this.id, this.produc);
		}
	}
}

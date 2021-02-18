package General;

public class Productor extends Thread
{
	private static BuzonProductores inicio;
	
	private int id;
	private String produc;
	private int MAX_PRODUCTOS;
	
	public Productor(int pID, String pProduc, int max, BuzonProductores deTodos) {
		this.inicio = deTodos;
		this.id = pID;
		this.produc = pProduc;
		this.MAX_PRODUCTOS = max;
	}
	
	public void run() {
		for(int i = 0; i < this.MAX_PRODUCTOS; i++) {
			inicio.almacenar(this.produc, this.id);
		}
	}
	
}

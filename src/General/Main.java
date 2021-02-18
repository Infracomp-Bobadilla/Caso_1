package General;

public class Main {

	public static void main(String[] args) {
		
		BuzonProductores inicioTodos = new BuzonProductores(4);
		BuzonConsumidores finTodos = new BuzonConsumidores(8);
		
		for(int i = 0; i < 4; i++)
			if(i%2 == 0)
				new Productor(i+100, "A", 7, inicioTodos).start();
			else
				new Productor(i+100, "B", 7, inicioTodos).start();

		for(int i = 0; i < 56; i++) {
			if(i%2==0)
				new Intermediario(0, i+1, inicioTodos, finTodos).start();
			else
				new Intermediario(1, i+1, inicioTodos, finTodos).start();
		}
		
		for(int i = 0; i < 4; i++)
			if(i%2 == 0)
				new Consumidor(i+350, "A", 7, finTodos).start();
			else
				new Consumidor(i+350, "B", 7, finTodos).start();
	}

}

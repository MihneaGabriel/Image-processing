package PackWork;

import java.util.LinkedList; // Importam conceptul de coada dintr-o bibloteca

public class SharedBuffer {
	private LinkedList<Integer> buffer = new LinkedList<>();
	private int capacity;
	
	public SharedBuffer(int capacity) {
		this.capacity = capacity;
	}
	
	public synchronized void produce(int item) throws InterruptedException {
		while( buffer.size() == capacity ) {
			wait(); // Buffer is full, wait for consumer to consume
		}
		
		buffer.add(item);
		//System.out.println("Produced: " + item);
		notifyAll(); // Notify consumers that there's data available
	}

	public synchronized int consume() throws InterruptedException {
		while( buffer.isEmpty() ){
			wait(); // Buffer is empty, wait for producer to produce
		}
		
		int item = buffer.remove();
		//System.out.println("Consumed: " + item);
		notifyAll(); // Notify producers that there's space available
		return item;
		
	}
}

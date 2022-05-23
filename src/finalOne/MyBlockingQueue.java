package finalOne;

public class MyBlockingQueue<E>{
	private class Node<e>{
		e item;
		Node<e> rightNode;
		
		public Node(e item){
			this.item = item;
			this.rightNode = null;
		}
	}

	private Node<E> head;
	private Node<E> tail;
	private static int defaultCapacity = Integer.MAX_VALUE;
	private int size;
	private int capacity;
	
	public MyBlockingQueue() {
		head = null;
		tail = null;
		size = 0;
		this.capacity = defaultCapacity;
	}
		
	public MyBlockingQueue(int capacity) {
		head = null;
		tail = null;
		size = 0;
		this.capacity = capacity;
	}
	
	public int getSize() {return size;}

	public boolean isEmpty() {return head == null;}
	
	public boolean isFull() {return size == capacity;}
	
	public synchronized void enqueue(E item) {
		Node<E> insertedNode = new Node<>(item);
		if(isEmpty()) {
			head = tail = insertedNode;
		}
		else {
			while(isFull()) {
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
			tail.rightNode = insertedNode;
			tail = insertedNode;
		}
		size++;
		notifyAll();
	}
	
	public synchronized E dequeue() {
		while(isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		Node<E> removedNode = head;
		head = removedNode.rightNode;
		size--;
		notifyAll();
		return removedNode.item;
	}
}

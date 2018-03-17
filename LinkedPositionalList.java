import java.util.Iterator;
import java.util.NoSuchElementException;

// implementation of positional list stored as a doubly linked list
public class LinkedPositionalList<E> implements PositionalList<E>
{
	// nested node class
	private static class Node<E> implements Position<E>
	{
		private E element;
		private Node<E> prev;
		private Node<E> next;
		public Node(E e, Node<E> p, Node<E> n)
		{
			element = e;
			prev = p;
			next = n;
		}
		public E getElement() throws IllegalStateException
		{
			if(next == null)
				throw new IllegalStateException("Position no longer valid");
			return element;
		}
		public Node<E> getPrev()
		{
			return prev;
		}
		
		public Node<E> getNext()
		{
			return next;
		}
		
		public void setElement(E e)
		{
			element = e;
		}
		public void setPrev(Node<E> p)
		{
			prev = p;
		}
		public void setNext(Node<E> n)
		{
			next = n;
		}
	} // end of nested Node class
	

private class PositionIterator implements Iterator<Position<E>>
{
	private Position<E> cursor = first(); // position of the next element to report
	private Position<E> recent = null;
	
	// tests whether the iterator has a next object
	public boolean hasNext()
	{
		return (cursor != null);
	}
	
	// returns the next position in the iterator
	public Position<E> next() throws NoSuchElementException
	{
		if(cursor == null) throw new NoSuchElementException("nothing left");
		recent = cursor;   // element at this position might later be removed
		cursor = after(cursor);
		return recent;
	}
	// removes the element returned by most recent call to next
	public void remove() throws IllegalStateException
	{
		if(recent == null) throw new IllegalStateException("nothing to remove");
		LinkedPositionalList.this.remove(recent); 	// remove from outer list
		recent = null;	// do not allow remove again until next is called
	}
} // end of nested PositionIterator class

// nested PositionIterable class
private class PositionIterable implements Iterable<Position<E>>
{
	public Iterator<Position<E>> iterator()
	{
		return new PositionIterator();
	}
}	// end of nested PositionIterable class

// returns an iterable representation of the list's positions
public Iterable<Position<E>> positions()
{
	return new PositionIterable();	// create a new instance of the inner class
}

// nested ElementIterator class
// this class adapts the iteration produced by positions() to return elements
private class ElementIterator implements Iterator<E>
{
	Iterator<Position<E>> posIterator = new PositionIterator();
	public boolean hasNext(){return posIterator.hasNext();}
	public E next() {return posIterator.next().getElement();} // return element
	public void remove(){posIterator.remove();}
}

// returns an iterator of the elements stored in the list
public Iterator<E> iterator()
{
	return new ElementIterator();
}

	// instance variables of LinkedPositionalList
	private Node<E> header;
	private Node<E> trailer;
	private int size = 0;
	
	// constructs a new empty list
	public LinkedPositionalList()
	{
		header = new Node<>(null, null, null);
		trailer = new Node<>(null, header, null);
		header.setNext(trailer);
	}
	
	// private utilities
	// validates the position and returns it as a node
	private Node<E> validate(Position<E> p) throws IllegalArgumentException
	{
		if(!(p instanceof Node)) throw new IllegalArgumentException("Invalid p");
		Node<E> node = (Node<E>) p; 
		if(node.getNext() == null)
			throw new IllegalArgumentException("p is no longer in the list");
		return node;
	}
	
	// returns the given node as a Position (or null, if it is a sentinel)
	private Position<E> position(Node<E> node)
	{
		if(node == header || node == trailer)
			return null;
		return node;
	}
	
	// return the number of elements in the linked list
	public int size() {return size;}
	
	// test whether linked list is empty
	public boolean isEmpty() {return size == 0;}
	
	// returns first Position in linked list (or null, if empty)
	public Position<E> first() 
	{
		return position(header.getNext());
	}
	
	// returns last Position in linked list (or null, if empty)
	public Position<E> last()
	{
		return position(trailer.getPrev());
	}
	
	// returns Position immediately before Position p (or null, if p is first)
	public Position<E> before(Position<E> p) throws IllegalArgumentException
	{
		Node<E> node = validate(p);
		return position(node.getPrev());
	}
	
	// returns Position immediately after Position p (or null, if p is last)
	public Position<E> after(Position<E> p) throws IllegalArgumentException
	{
		Node<E> node = validate(p);
		return position(node.getNext());
	}
	
	// private utilities
	// adds element e to linked list between the given nodes
	private Position<E> addBetween(E e, Node<E> pred, Node<E> succ)
	{
		Node<E> newest = new Node<>(e, pred, succ);
		pred.setNext(newest);
		succ.setPrev(newest);
		size++;
		return newest;
	}
	
	// public update methods
	// insert element e at the front of linked list and returns its new Position
	public Position<E> addFirst(E e)
	{
		return addBetween(e, header, header.getNext());
	}
	// insert element e at back of linked list and return its new Position
	public Position<E> addLast(E e)
	{
		return addBetween(e, trailer.getPrev(), trailer);
	}
	// insert element e immediately before Position p and returns its new Position
	public Position<E> addBefore(Position<E> p, E e)
								throws IllegalArgumentException
	{
		Node<E> node = validate(p);
		return addBetween(e, node.getPrev(), node);
	}
	
	// inserts element e immediately after Position p, and returns its new Position
	public Position<E> addAfter(Position<E> p, E e)
								throws IllegalArgumentException
	{
		Node<E> node = validate(p);
		return addBetween(e, node, node.getNext());
	}
	
	// replace element stored at Position p and returns the replaced element
	public E set(Position<E> p, E e) throws IllegalArgumentException
	{
		Node<E> node = validate(p);
		E answer = node.getElement();
		node.setElement(e);
		return answer;
	}
	// remove element stored at Position p and return it (invalidating p)
	public E remove(Position<E> p) throws IllegalArgumentException
	{
		Node<E> node = validate(p);
		Node<E> predecessor = node.getPrev();
		Node<E> successor = node.getNext();
		predecessor.setNext(successor);
		successor.setPrev(predecessor);
		size--;
		E answer = node.getElement();
		node.setElement(null);
		node.setNext(null);
		node.setPrev(null);
		return answer;
	}	
	
	// return index of specified position. Cursor is excluded.
	public int indexOf(Position<E> p)
	{
		//retrieve index number
		int index = 0;
		
		Iterable<Position<E>> iterable = positions();
		Iterator<Position<E>> iterator = iterable.iterator();
		//loop through whole thing until reached desired position
		while(iterator.hasNext())
		{
			Position<E> node = iterator.next();
			
			if(node == p)
				return index;
			// increment if element is not the cursor
			if(!(node.getElement().equals('>')))
				index++;
		}
		return index;
	}
	
	// returns position of specified index. Cursor is excluded.
	public Position<E> atIndex(int index)
	{
		Iterable<Position<E>> iterable = positions();
		Iterator<Position<E>> iterator = iterable.iterator();
		
		for(int i = 0; i <= index; i++)
		{
			if(iterator.hasNext())
			{
				Position<E> node = iterator.next();
				
				if(node.getElement().equals('>'))
					i--;
				else if(i == index)
					return node;
			}
		}
		return null;
	}
} 

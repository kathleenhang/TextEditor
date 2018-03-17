
public interface PositionalList<E>
{
	// return the number of elements in the list
	int size();
	
	// tests whether the list is empty
	boolean isEmpty();
	
	// returns the first Position in list (or null if empty)
	Position<E> first();
	
	// returns last Position in list ( or null if empty)
	Position<E> last();
	
	// return Position immediately before Position p (or null, if p is first)
	Position<E> before(Position<E> p) throws IllegalArgumentException;
	
	// return Position immediately after Position p (or null, if p is last)
	Position<E> after(Position<E> p) throws IllegalArgumentException;
	
	// insert element e at front of the list and returns its new Position
	Position<E> addFirst(E e);
	
	// insert element e at back of list and return its new Position
	Position<E> addLast(E e);
	
	// insert element e immediately before Position p and returns its new Position
	Position<E> addBefore(Position<E> p, E e)
		throws IllegalArgumentException;
	
	// insert element e immediately after Position p and returns its new Position
	Position<E> addAfter(Position<E> p, E e)
		throws IllegalArgumentException;
	
	// replaces element stored at Position p and returns the replaced element
	E set(Position<E> p, E e) throws IllegalArgumentException;
	
	// removes element stored at Position p and returns it (invalidating p)
	E remove(Position<E> p) throws IllegalArgumentException;
	
}

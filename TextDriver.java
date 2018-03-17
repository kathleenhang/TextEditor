import java.util.Iterator;
import java.util.Scanner;

public class TextDriver extends LinkedPositionalList
{
	public static void main(String[] args)
	{
		String input;
		LinkedPositionalList<Character> myList = new LinkedPositionalList();
		// create new scanner
		Scanner scan = new Scanner(System.in);

		System.out.println("-----[ TEXT EDITOR ]-----\n"
								+ "<<< AVAILABLE OPERATIONS >>> \n" 
								+ "[ left ]   [ right ]   [ insert _ ]   [ delete ]   \n[ atIndex _ ]   [ indexOf cursor ]\n---------------------------- \n");
		// create new cursor
		myList.addFirst('>');
		
		//choose an available menu operation
		while(scan.hasNext())
		{
			input = scan.nextLine();
			
			if(input.equals("right"))
				moveRight(myList);
			
			else if(input.equals("left"))
				moveLeft(myList);
			
			else if(input.startsWith("insert"))
				insert(myList, input);
			
			else if(input.equals("delete"))
				delete(myList);
			
			else if(input.startsWith("atIndex"))
				atIndex(myList, input);
			
			else if(input.startsWith("indexOf"))
				indexOf(myList, input);
		}
	}
	
	// print list
	public static void output(Iterable<Position<Character>> list)
	{
		for(Position<Character> p : list)
		{
			System.out.print(p.getElement());
		}
		System.out.println();
	}
	
	public static void moveRight(LinkedPositionalList list)
	{
		// create a list that is iterable
		Iterable<Position<Character>> iterable = list.positions();
		// create iterator for the iterable list in order to navigate through that list
		Iterator<Position<Character>> iterator = iterable.iterator();
		
		while(iterator.hasNext())
		{
			// make the current iterator position be named "node"
			Position<Character> node = iterator.next();
			// if current position is the cursor...
			if(node.getElement() == '>')
			{
				// stop the loop if there is no more positions remaining
				if(!(iterator.hasNext()))
					break;
				// make the position after the cursor (current position) be named "nodeTwo"
				Position<Character> nodeTwo = iterator.next();
				// swap the position of the cursor with the element of the position after the cursor
				list.set(node, nodeTwo.getElement());
				list.set(nodeTwo, '>');
			}
		}
		output(list.positions());
	}
	
	public static void moveLeft(LinkedPositionalList list)
	{
		Iterable<Position<Character>> iterable = list.positions();
		Iterator<Position<Character>> iterator = iterable.iterator();
		
		while(iterator.hasNext())
		{
			Position<Character> node = iterator.next();
			
			if(node.getElement() == '>')
			{
				if(list.before(node) == null)
					break;
				
				Position<Character> nodeTwo = list.before(node);
				
				list.set(node, nodeTwo.getElement());
				list.set(nodeTwo, '>');
			}
		}
		output(list.positions());
	}
	
	// insert character to left of cursor
	public static void insert(LinkedPositionalList list, String input)
	{
		Iterable<Position<Character>> iterable = list.positions();
		Iterator<Position<Character>> iterator = iterable.iterator();
		
		while(iterator.hasNext())
		{
			//store the current character
			Position<Character> node = iterator.next();
			// if the character is cursor
			if(node.getElement() == '>')
			{
				// insert this character located at x. example: insert x
				char tempChar = input.charAt(7);
				// insert before cursor
				list.addBefore(node, tempChar);
			}
		}
		output(list.positions());
	}
	
	// delete char to the right of cursor
	public static void delete(LinkedPositionalList list)
	{
		Iterable<Position<Character>> iterable = list.positions();
		Iterator<Position<Character>> iterator = iterable.iterator();
		
		while(iterator.hasNext())
		{
			if(iterator.next().getElement() == '>')
				if(iterator.hasNext())
				{
					iterator.next();
					iterator.remove();
				}
		}
		output(list.positions());
	}
	
	// print element at specified index
	public static void atIndex(LinkedPositionalList list, String input)
	{
		String [] tempString = input.split(" ", 2);
		// user index input converted from string to integer
		int userInput = Integer.parseInt(tempString[1]);
		
		// returns the position node we want
		Position<Character> node = list.atIndex(userInput);
		
		// print that node's element
		System.out.println("Character at index " + userInput + ": " + node.getElement());
	}

	// print index location of cursor
	public static void indexOf(LinkedPositionalList list, String input)
	{
		Iterable<Position<Character>> iterable = list.positions();
		Iterator<Position<Character>> iterator = iterable.iterator();
		
		String [] tempString = input.split(" ", 2);
		if(tempString[1].equals("cursor"))
		{
			while(iterator.hasNext())
			{
				//store the next character
				Position<Character> node = iterator.next();
				// if the character is cursor
				if(node.getElement() == '>')
					System.out.println(list.indexOf(node));
			}
		}
	}
}

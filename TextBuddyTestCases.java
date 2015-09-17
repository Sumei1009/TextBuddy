import static org.junit.Assert.*;

import org.junit.Test;

public class TextBuddyTestCases {

	@Test
	public void clearTest() {
		TextBuddy.fileName = "a.txt";
		assertEquals("all content deleted from a.txt", 
				TextBuddy.executeCommand("clear"));
		assertEquals(0, TextBuddy.getLineCount());
	}

	@Test
	public void addTest() {
		TextBuddy.fileName = "a.txt";
		assertEquals("added to a.txt: \"I love CS!!!\"", TextBuddy.executeCommand("add I love CS!!!"));
	}
	
	@Test
	public void displayTest() {
		TextBuddy.fileName = "a.txt";
		String expected = TextBuddy.convertLineToString().trim();
		assertEquals(expected, TextBuddy.executeCommand("display"));
	}
	
	@Test
	public void deleteTest() {
		TextBuddy.fileName = "a.txt";
		assertEquals("deleted from a.txt: \"I love CS!!!\"", TextBuddy.executeCommand("delete 1"));
	}
}

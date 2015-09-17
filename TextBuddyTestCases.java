import static org.junit.Assert.*;

import org.junit.Test;

public class TextBuddyTestCases {

	@Test
	public void executeCommandTest() {
		TextBuddy.fileName = "a.txt";
		TextBuddy.readFileInLine("a.txt");
		
		assertEquals("all content deleted from a.txt",
				TextBuddy.executeCommand("clear"));
		assertEquals(0, TextBuddy.getLineCount());
		
		assertEquals("added to a.txt: \"I love CS!!!\"",
				TextBuddy.executeCommand("add I love CS!!!"));
		
		String expected = TextBuddy.convertLineToString().trim();
		assertEquals(expected, TextBuddy.executeCommand("display"));
		
		assertEquals("deleted from a.txt: \"I love CS!!!\"",
				TextBuddy.executeCommand("delete 1"));
		
		assertEquals(0, TextBuddy.getLineCount());
	}

}

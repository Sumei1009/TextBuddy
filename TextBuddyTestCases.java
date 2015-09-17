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

		String expected = "1. I love CS!!!";
		assertEquals(expected, TextBuddy.executeCommand("display"));

		assertEquals("deleted from a.txt: \"I love CS!!!\"",
				TextBuddy.executeCommand("delete 1"));

		assertEquals(0, TextBuddy.getLineCount());
	}

	@Test
	public void sortTest() {
		TextBuddy.fileName = "a.txt";
		TextBuddy.readFileInLine("a.txt");

		assertEquals("added to a.txt: \"I love CS!!!\"",
				TextBuddy.executeCommand("add I love CS!!!"));

		assertEquals("added to a.txt: \"You love CS!!!\"",
				TextBuddy.executeCommand("add You love CS!!!"));

		assertEquals("added to a.txt: \"He loves CS!!!\"",
				TextBuddy.executeCommand("add He loves CS!!!"));

		assertEquals("Successully sorted", TextBuddy.executeCommand("sort"));

		String expected = "1. He loves CS!!!" + System.lineSeparator()
				+ "2. I love CS!!!" + System.lineSeparator()
				+ "3. You love CS!!!";
		assertEquals(expected, TextBuddy.executeCommand("display"));

	}

}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextBuddy {

	private static final String MASSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_INVALID = "Invalid instruction";
	private static final String MESSAGE_NONE_ROW = "There is not row %1$s";

	// for reading the command from the users.
	private static Scanner scanner = new Scanner(System.in);
	// To save the text content
	private static ArrayList<String> line = new ArrayList<String>();

	public static String fileName;
	
	enum Command {
		ADD_TEXT, DELETE_TEXT, DISPLAY, CLEAR, INVALID, EXIT
	}

	/*
	 * Assumptions: 1.only txt file will be considered valid. 2. only delete,
	 * add, display, clear and exit commands are considered valid command. 3.
	 * The file can be saved to the disk only after the user exits.
	 */


	public static void main(String args[]) {
		fileName = args[0];
		//fileName = removeFirstWord(scanner.nextLine().trim());
		prepareFileForUse(fileName);
		showToUser(String.format(MASSAGE_WELCOME, fileName));
		while (true) {
			String userCommand = readUserCommand();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
		}
	}

	private static void prepareFileForUse(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		line = readFileInLine(fileName);
	}

	private static void showToUser(String text) {
		System.out.println(text);
	}

	private static String readUserCommand() {
		System.out.print("command: ");
		String userCommand = scanner.nextLine().trim();
		return userCommand;
	}


	public static String executeCommand(String userCommand) {

		if (userCommand.trim().equals("")) {
			return MESSAGE_INVALID;
		}

		String commandTypeString = getFirstWord(userCommand);
		Command commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD_TEXT:
			return addText(userCommand);
		case DELETE_TEXT:
			return deleteText(userCommand);
		case DISPLAY:
			return display(userCommand);
		case CLEAR:
			return clear(userCommand);
		case INVALID:
			return MESSAGE_INVALID;
		case EXIT:
			exit(userCommand);
			System.exit(0);
		default:
			// throw an error if the command is not recognized
			throw new Error(MESSAGE_INVALID);
		}
	}

	/*
	 * Different types of command execution.Lower level
	 */

	private static void exit(String userCommand) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			String finalText = convertLineToString();
			writer.write(finalText);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String addText(String userCommand) {
		line.add(removeFirstWord(userCommand) + "\n");
		return String.format(MESSAGE_ADDED, fileName,
				removeFirstWord(userCommand));
	}

	private static String deleteText(String userCommand) {
		String[] parameters = splitParameters(userCommand);
		if (parameters.length != 2) {
			return MESSAGE_INVALID;
		}
		String deleteRow = getLastWord(userCommand).trim();

		int rowNumber = 0;
		try {
			rowNumber = Integer.parseInt(deleteRow);
		} catch (NumberFormatException e) {
			return "Unrecognized row number.";
		}

		if (rowNumber > line.size()) {
			return String.format(MESSAGE_NONE_ROW, deleteRow);
		}

		String deletedRow = line.get(rowNumber - 1);
		line.remove(rowNumber - 1);
		return String.format(MESSAGE_DELETED, fileName, deletedRow.trim());
	}

	private static String display(String userCommand) {
		String displayContent = "";
		if (line.size() == 0) {
			return String.format(MESSAGE_EMPTY, fileName);
		} else {
			displayContent = convertLineToString();
		}
		return displayContent.trim();
	}

	private static String clear(String userCommand) {
		line.clear();
		return String.format(MESSAGE_CLEAR, fileName,
				removeFirstWord(userCommand.trim()));
	}

	/*
	 * The reader can go to the next level of abstraction by reading the methods
	 * (given below) that is referenced by the method above.
	 */

	private static Command determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return Command.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return Command.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return Command.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return Command.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return Command.EXIT;
		} else {
			return Command.INVALID;
		}
	}

	public static ArrayList<String> readFileInLine(String fileName) {
		ArrayList<String> line = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String newLine = reader.readLine();
			while (newLine != null) {
				line.add(removeFirstWord(newLine).trim() + "\n");
				newLine = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public static String convertLineToString() {
		String textString = "";
		for (int i = 0; i < line.size(); i++) {
			textString += (i + 1) + ". " + line.get(i);
		}
		return textString;
	}

	/*
	 * These level is to deal with the command text.
	 */
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String getLastWord(String setUpInstruction) {
		String[] parameters = splitParameters(setUpInstruction);
		int length = parameters.length;
		String commandTypeString = parameters[length - 1].trim();
		return commandTypeString;
	}

	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}
	
	public static int getLineCount(){
		return line.size();
	}
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class TextBuddy {

	private static final String MASSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MASSAGE_ERROR_FILE = "Can't create such file, please key in file name again";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_INVALID = "Invalid instruction";
	private static final String MESSAGE_NONE_ROW = "There is not row %1$s";
	private static final String MESSAGE_SUCCESSFUL_WRITE_INTO_FILE = "Successfully write into the file";
	private static final String MESSAGE_SUCCESSFUL_SORT = "Successully sorted";
	private static final String MESSAGE_INVALID_ROW_NUMBER = "Unrecognized row number";

	private static final int DELETECOMMANDPARAMETERS = 2;

	private static Scanner scanner = new Scanner(System.in);
	private static ArrayList<String> textBuffer = new ArrayList<String>();
	public static String fileName = null;

	enum Command {
		ADD_TEXT, DELETE_TEXT, DISPLAY, CLEAR, SORT, INVALID, EXIT
	}

	/*
	 * Assumptions: 1. only delete, add, display, clear and exit commands are
	 * considered valid command. 2. The file can be saved to the disk only after
	 * the user exits.
	 */

	public static void main(String args[]) {
		fileName = args[0];
		fileName = removeFirstWord(scanner.nextLine().trim());
		prepareFileForUse(fileName);
		showToUser(String.format(MASSAGE_WELCOME, fileName));
		while (true) {
			String userCommand = readUserCommand();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
		}
	}

	private static void prepareFileForUse(String localFileName) {
		File file = new File(localFileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				showToUser(MASSAGE_ERROR_FILE);
				fileName = scanner.nextLine().trim();
				prepareFileForUse(fileName);
			}
		}
		textBuffer = readFileInLine(fileName);
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
		case SORT:
			return sort(userCommand);
		case EXIT:
			exit(userCommand);
			System.exit(0);
		default:
			throw new Error(MESSAGE_INVALID);
		}
	}

	private static String exit(String userCommand) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			String finalText = convertLineToString();
			writer.write(finalText);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MESSAGE_SUCCESSFUL_WRITE_INTO_FILE;
	}

	private static String addText(String userCommand) {
		textBuffer.add(removeFirstWord(userCommand) + System.lineSeparator());

		return String.format(MESSAGE_ADDED, fileName,
				removeFirstWord(userCommand));
	}

	private static String deleteText(String userCommand) {

		String[] parameters = splitParameters(userCommand);

		if (parameters.length != DELETECOMMANDPARAMETERS) {
			return MESSAGE_INVALID;
		}

		String deleteRow = getLastWord(userCommand).trim();

		int rowNumber = 0;
		try {
			rowNumber = Integer.parseInt(deleteRow);
		} catch (NumberFormatException e) {
			return MESSAGE_INVALID_ROW_NUMBER;
		}

		if (rowNumber > textBuffer.size()) {
			return String.format(MESSAGE_NONE_ROW, deleteRow);
		}

		String deletedRow = textBuffer.get(rowNumber - 1);
		textBuffer.remove(rowNumber - 1);
		return String.format(MESSAGE_DELETED, fileName, deletedRow.trim());
	}

	private static String display(String userCommand) {
		String displayContent = "";
		if (textBuffer.size() == 0) {
			return String.format(MESSAGE_EMPTY, fileName);
		} else {
			displayContent = convertLineToString();
		}
		return displayContent.trim();
	}

	private static String clear(String userCommand) {
		textBuffer.clear();
		return String.format(MESSAGE_CLEAR, fileName,
				removeFirstWord(userCommand.trim()));
	}

	public static String sort(String userCommand) {
		Collections.sort(textBuffer, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		return MESSAGE_SUCCESSFUL_SORT;
	}

	private static Command determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error("command type string cannot be null!");
		}

		if (commandTypeString.equalsIgnoreCase("add")) {
			return Command.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return Command.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return Command.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return Command.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
			return Command.SORT;
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
				line.add(removeFirstWord(newLine).trim()
						+ System.lineSeparator());
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
		for (int i = 0; i < textBuffer.size(); i++) {
			textString += (i + 1) + ". " + textBuffer.get(i);
		}
		return textString;
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String getLastWord(String userCommand) {
		String[] parameters = splitParameters(userCommand);
		int length = parameters.length;
		String commandTypeString = parameters[length - 1].trim();
		return commandTypeString;
	}

	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}

	public static int getLineCount() {
		return textBuffer.size();
	}
}

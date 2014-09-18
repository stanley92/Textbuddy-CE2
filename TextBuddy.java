/**
 * TextBuddy is used to store and retrieve Text that the user wishes to save. A
 * Text is a single line of characters. The user's texts are then stored onto a
 * file on disk. The command format is given by the interaction below:
 *
 * <pre>
 * Welcome to TextBuddy. mytextfile.txt is ready for use
 * command: add little brown fox
 * added to mytextfile.txt: "little brown fox"
 * command: display
 * 1. little brown fox
 * command: add jumped over the moon
 * added to mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * 2. jumped over the moon
 * command: delete 2
 * deleted from mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * command: clear
 * all content deleted from mytextfile.txt
 * command: display
 * mytextfile.txt is empty
 * command: exit
 * </pre>
 *
 *This program has the following assumptions:
 *It is assumed that user wanted to save the data after each time he edits.
 *It is assumed that user would like to edit the created file.
 *It is assumed that user will key in only the possible commands.
 *It is assumed that user will key in inputs in the valid format.
 * Done By: Stanley Ong
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddy {

	private static final String MSG_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private static final String MSG_IO_OPERATION_FAILED = "I/O operation failed for %s. Please try again";
	private static final String MSG_COMMMAND_PROMPT = "command: ";
	private static final String MSG_INVALID_COMMAND = "Invalid command";
	private static final String MSG_INVALID_PARAM = "Invalid parameters for %s command";
	private static final String MSG_ADD = "added to %s: \"%s\"";
	private static final String MSG_DELETE = "deleted from %s: \"%s\"";
	private static final String MSG_CLEAR = "all content deleted from %s";
	private static final String MSG_EMPTY_FILE = "%s is empty";
	
	private static final String DISPLAY_FORMAT = "%d. %s";
	
	static File file_object = null;

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		ArrayList<String> texts = new ArrayList<String>();
		String fileName;

		if (args.length < 1) {
			fileName = sc.next();
			file_object = new File(fileName);
		}

		else {
			fileName = args[0];
			file_object = new File(fileName);
		}

		// Create new text file
		if (!file_object.exists()) {
			file_object.createNewFile();
			// System.out.print(fileName + " has been created \n");
		}

		// To modify an existing text file
		else if (file_object.exists()) {
			System.out.print("Would you like to load and edit file? (Y/N)");
			String choice = sc.nextLine();
			if (choice.equalsIgnoreCase("N")) {
				System.exit(0);
			}

			else if (choice.equalsIgnoreCase("Y")) {
				// To copy existing data to an arraylist
				String[] previousData = oldData(file_object);
				for (int i = 0; i < previousData.length; i++) {
					texts.add(previousData[i]);
				}
			}
		}

		System.out.println("Welcome to TextBuddy. " + fileName
				+ " is ready for use");

		command_type(sc, texts, fileName);

		saveData(texts, file_object);

		sc.close();
	}

	private static void command_type(Scanner sc, ArrayList<String> texts,
			String fileName) {
		String command;
		do {
			System.out.print("command: ");
			command = sc.next();
			if (command.equals("add")) {
				addText(sc, texts, fileName);
			} else if (command.equals("delete")) {
				deleteText(sc, texts, fileName);
			} else if (command.equals("clear")) {
				clearTexts(texts, fileName);
			} else if (command.equals("display")) {
				displayTexts(texts, fileName);
			} else if (command.equals("sort")) {
				sortTexts(texts, fileName);
			} else if (command.equals("search")) {
				searchTexts(sc, texts, fileName);
			} else {
				showUser(MSG_INVALID_COMMAND);
				//System.out.println("Invalid Command!");
			}
		} while (!command.equals("exit"));

	}

	private static void displayTexts(ArrayList<String> texts, String fileName) {
		if (texts.size() == 0) {
			showUser(String.format(MSG_EMPTY_FILE, fileName));
			//System.out.println(fileName + " is empty");
		} else
			for (int i = 0; i < texts.size(); i++) {
				showUser(String.format(DISPLAY_FORMAT, i+1, texts.get(i)));
				//System.out.println(i + 1 + ". " + texts.get(i));
			}
	}

	private static void sortTexts(ArrayList<String> texts, String fileName) {
		Collections.sort(texts);
	}

	private static void searchTexts(Scanner sc, ArrayList<String> texts,
			String fileName) {

		if (texts.size() == 0) {
			showUser(String.format(MSG_EMPTY_FILE, fileName));
			//System.out.println(fileName + " is empty");
		} else {
			String searchItem;
			searchItem = sc.nextLine();
			searchItem = searchItem.trim();

			for (int i = 0; i < texts.size(); i++) {
				if (texts.get(i).toUpperCase()
						.contains(searchItem.toUpperCase())) {
					showUser(String.format(DISPLAY_FORMAT, i+1, texts.get(i)));
					//System.out.println(i + 1 + ". " + texts.get(i));
				}
			}
		}
	}

	private static void clearTexts(ArrayList<String> texts, String fileName) {
		while (!(texts.size() == 0)) {
			texts.remove(texts.size() - 1);
		}
		showUser
		//System.out.println("All content is clear from " + fileName);
	}

	private static void deleteText(Scanner sc, ArrayList<String> texts,
			String fileName) {
		int deleteNo;
		deleteNo = sc.nextInt();

		if (texts.size() == 0) {
			showUser
			//System.out.println(fileName + " is empty. Nothing to be deleted.");
		}

		else if (deleteNo - 1 >= texts.size()) {
			showUser
			//System.out.println("Invalid Command!");
			return;
		}

		else {
			showUser
			//System.out.println("deleted from " + fileName + " \""
				//	+ texts.get(deleteNo - 1) + "\"");
			texts.remove(deleteNo - 1);
		}
	}

	private static void addText(Scanner sc, ArrayList<String> texts,
			String fileName) {
		String display;
		display = sc.nextLine();
		display = display.trim();
		texts.add(display);
		showUser
		//System.out.println("added to " + fileName + " \"" + display + "\"");
	}
	
	private static void showUser(String text){
		System.out .println(text);
		}

	private static void saveData(ArrayList<String> arr, File file_object)
			throws FileNotFoundException {
		PrintWriter write = new PrintWriter(file_object);
		for (int i = 0; i < arr.size(); i++) {
			write.println(arr.get(i));
		}
		write.close();
	}

	private static String[] oldData(File file_object) throws IOException {
		FileReader fr = new FileReader(file_object);
		BufferedReader textReader = new BufferedReader(fr);
		int numberOfLines = readLines(file_object);
		String[] textData = new String[numberOfLines];
		for (int i = 0; i < numberOfLines; i++) {
			textData[i] = textReader.readLine();
		}
		textReader.close();
		return textData;
	}

	private static int readLines(File file_object) throws IOException {
		FileReader fileToRead = new FileReader(file_object);
		BufferedReader bf = new BufferedReader(fileToRead);
		int numberOfLines = 0;
		while (bf.readLine() != null) {
			numberOfLines++;
		}
		bf.close();
		return numberOfLines;
	}
}

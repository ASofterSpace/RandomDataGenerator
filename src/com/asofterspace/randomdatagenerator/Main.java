/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.randomDataGenerator;

import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

	public final static String PROGRAM_TITLE = "RandomDataGenerator";
	public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "16. March 2019";

	private static List<String> encounteredData = null;

	private final static List<String> FIRST_NAMES = Arrays.asList(
		"Aaron", "Abigail", "Alan", "Amber", "Amy", "Andrea", "Ashley", "Benedict", "Bob",
		"Brittany", "Bruce", "Calvin", "Dale", "Denise", "Dennis", "Diana", "Donna", "Dylan",
		"Edgar", "Elinor", "Eric", "Eva", "Finn", "Franciscus", "George", "Georgia", "Gerhardt",
		"Gregory", "Harald", "Harry", "Hertha", "Imre", "Jacques", "Jakob", "Jennifer", "Jerry",
		"Jesse", "Joe", "Josh", "José", "Juan", "Julie", "Jón", "Karen", "Kayla", "Kelly",
		"Kevin", "Kim", "Kimberly", "Liu", "Lorraine", "Manuel", "Marie", "Muhammad", "Patrick",
		"Paul", "Pauline", "Peter", "Phillip", "Rebekka", "Riccardo", "Ronald", "Ruth", "Sandra",
		"Sarah", "Shirin", "Sigurður", "Stephen", "Susan", "Tasuku", "Thabea", "Thomas",
		"Venkatraman", "Walter", "Wolfgang", "Yoichiro", "Yoshinori", "Yves", "Ágúst"
	);

	private final static List<String> LAST_NAMES = Arrays.asList(
		"Alexievich", "Allison", "Arnold", "Ashkin", "Bednorz", "Betzig", "Charpak", "Cho", "Chu",
		"Corey", "de Gennes", "Deaton", "Dubochet", "Edwards", "Feringa", "Haavelmo", "Harrison",
		"Hart", "Henderson", "Higgs", "Hoffmann", "Holmström", "Honjo", "Ishiguro", "Jónsson",
		"Krebs", "Levitt", "Lindahl", "Lovelace", "Mahfouz", "Miller", "Mirrlees", "Modiano",
		"Moser", "Mourou", "Mukengere", "Munroe", "Muran", "Müller", "Nordhaus", "O'Connor",
		"Ohsumi", "Ramakrishnan", "Reines", "Richardson", "Rodbell", "Romer", "Santos", "Satyarthi",
		"Schwartz", "Selten", "Sen", "Sharpe", "Smith", "Strickland", "Szymborska", "Thorpe",
		"Tirole", "Tonegawa", "Tranströmer", "Trimble", "Turing", "Tyler", "Varmus", "Voss",
		"Warhol", "Weiss", "Williams", "Wineland", "Winter", "Xiaobo", "Yan", "Yousafzai",
		"zum Moore"
	);


	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		initUnique();

		System.out.println(Utils.getFullProgramIdentifierWithDate());
		System.out.println("");

		SimpleFile input;

		if (args.length < 1) {
			System.out.println("Please call this with an input file as first argument!");
			System.out.println("(The input file should contain all of the ids for which data should be generated, " +
				"one id on each line. Signs such as \" and ; will be ignored. " +
				"To do so, e.g. call something like  SELECT id FROM users;  and export the result as CSV.)");
			System.out.println("");
			System.out.println("Will for now use input.csv...");
			System.out.println("");

			input = new SimpleFile("input.csv");

		} else {

			input = new SimpleFile(args[0]);
		}

		System.out.println("Reading input from " + input.getFilename() + "...");

		if (!input.exists()) {
			System.out.println("The input file does not exist!");
			System.out.println("Exiting without doing anything...");
			return;
		}

		List<String> unsanitizedIds = input.getContents();
		List<String> ids = new ArrayList<>();

		for (String id : unsanitizedIds) {

			id = id.replace("\"", "");
			id = id.replace("\'", "");
			id = id.replace(";", "");
			id = id.replace(",", "");
			id = id.replace(" ", "");
			id = id.replace("\t", "");

			ids.add(id);
		}

		List<String> result = new ArrayList<String>();

		System.out.println("Generating random data...");

		for (String id : ids) {
			result.add(generateDataForId(id));
		}

		SimpleFile output = new SimpleFile("output.sql");

		System.out.println("Saving output to " + output.getFilename() + "...");

		output.setContents(result);
		output.save();

		System.out.println("Done!");

	}

	private static String generateDataForId(String id) {

		StringBuilder result = new StringBuilder();

		String firstname = getRandomFirstName();
		String lastname = getRandomLastName();

		result.append("UPDATE users SET first_name = \"");
		result.append(firstname);
		result.append("\", last_name = \"");
		result.append(lastname);
		result.append("\", email = \"");
		result.append(makeUnique(forEmail(firstname + lastname)));
		result.append("@example.com\" ");
		result.append("WHERE id = ");
		result.append(id);
		result.append(";\n");

		result.append("UPDATE profiles SET name = \"");
		result.append(firstname);
		result.append(" ");
		result.append(lastname);
		result.append("\" WHERE user_id = ");
		result.append(id);
		result.append(";");

		return result.toString();
	}

	private static String forEmail(String name) {

		name = name.toLowerCase();

		name = name.replace(" ", "");
		name = name.replace("'", "");
		name = name.replace("é", "e");
		name = name.replace("ó", "o");
		name = name.replace("ö", "oe");
		name = name.replace("ð", "d");

		return name;
	}

	private static void initUnique() {

		encounteredData = new ArrayList<String>();
	}

	private static String makeUnique(String str) {

		if (encounteredData == null) {
			initUnique();
		}

		str = str.intern();

		String curstr = str;
		int i = 1;

		while (encounteredData.contains(curstr)) {
			i++;
			curstr = str + i;
			curstr = curstr.intern();
		}

		encounteredData.add(curstr);

		return curstr;
	}

	private static String getRandomFirstName() {

		if (Math.random() < 0.1) {
			return FIRST_NAMES.get((int) (Math.random() * FIRST_NAMES.size())) + " " +
				   FIRST_NAMES.get((int) (Math.random() * FIRST_NAMES.size()));
		}

		return FIRST_NAMES.get((int) (Math.random() * FIRST_NAMES.size()));
	}

	private static String getRandomLastName() {

		return LAST_NAMES.get((int) (Math.random() * LAST_NAMES.size()));
	}

}

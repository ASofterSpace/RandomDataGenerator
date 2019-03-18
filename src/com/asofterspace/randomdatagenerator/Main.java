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
	public final static String VERSION_NUMBER = "0.0.0.2(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "16. March 2019 - 18. March 2019";

	private static List<String> encounteredData = null;

	private final static List<String> FIRST_NAMES = Arrays.asList(
		"Aaron", "Abigail", "Agustín", "Ahmed", "Alan", "Alvaro", "Amber", "Amy", "Anastasiya",
		"Andrea", "André", "Ariel", "Ashley", "Aya", "Benedict", "Bertrand", "Bob", "Bogdan",
		"Brittany", "Bruce", "Calvin", "Dale", "Denise", "Dennis", "Diana", "Donna", "Dylan",
		"Edgar", "Eevi", "Elinor", "Emma", "Enzo", "Eric", "Eva", "Fatima", "Finn", "Franciscus",
		"Gabriel", "George", "Georgia", "Gerhardt", "Grace", "Gregory", "Harald", "Harry", "Hertha",
		"Imre", "Isabella", "Jacques", "Jakob", "Jan", "Jana", "Jennifer", "Jennifer", "Jerry",
		"Jesse", "Joe", "Josh", "José", "Juan", "Julie", "Jón", "Karen", "Kayla", "Kelly", "Kevin",
		"Kim", "Kimberly", "Lara", "Lina", "Liu", "Lorraine", "Louis", "Luna", "Léon", "Mamadou",
		"Manuel", "Marie", "Martha", "Muhammad", "Olga", "Omar", "Patrick", "Paul", "Pauline",
		"Peter", "Phillip", "Rebekka", "Riccardo", "Ronald", "Ruth", "Sandra", "Sarah", "Shirin",
		"Sigurður", "Sophia", "Stephen", "Susan", "Tasuku", "Thabea", "Thomas", "Venkatraman",
		"Walter", "Wolfgang", "Yoichiro", "Yoshinori", "Yousouf", "Yves", "Zoey", "Ágúst"
	);

	private final static List<String> LAST_NAMES = Arrays.asList(
		"Alexievich", "Allen", "Allison", "Arnold", "Ashkin", "Bednorz", "Betzig", "Blanco",
		"Charpak", "Chen", "Cho", "Chu", "Clark", "Cohen", "Cooper", "Corey", "Cox", "D'Angelo",
		"De Smet", "De la Cruz", "Deaton", "Diederich", "Dubochet", "Díaz", "Edwards", "Farkas",
		"Fekete", "Feringa", "Fernandez", "Fischer", "Friedman", "Gagnon", "Garcia", "Gauthier",
		"Gruber", "Haas", "Haavelmo", "Hansen", "Harrison", "Hart", "Helgason", "Henderson",
		"Higgs", "Hoffmann", "Holmström", "Honjo", "Horvat", "Huber", "Ishiguro", "Jacobs",
		"Jensen", "Jónsson", "Jørgensen", "Kapanadze", "Karlsson", "Korhonen", "Krasniqi",
		"Krebs", "Kukk", "Kumar", "Lee", "Lehtonen", "Levitt", "Lindahl", "Lindholm", "Lombardi",
		"Lovelace", "Mahfouz", "Meyers", "Micallef", "Miller", "Mirrlees", "Modiano", "Moser",
		"Mourou", "Mukengere", "Munroe", "Muran", "Murray", "Mäkinen", "Müller", "Nguyen",
		"Nielsen", "Niemi", "Nordhaus", "Novak", "Nyman", "Němec", "O'Connor", "O'Neill",
		"Ohsumi", "Olsen", "Peeters", "Pérez", "Ramakrishnan", "Rathnayake", "Rebane", "Reines",
		"Rey", "Richardson", "Robinson", "Rodbell", "Rodríguez", "Romer", "Rossi", "Sanders",
		"Santos", "Satyarthi", "Satō", "Schwartz", "Scott", "Selten", "Sen", "Seo", "Sharpe",
		"Singh", "Smith", "Strickland", "Szymborska", "Tahirović", "Takahashi", "Tamm", "Taylor",
		"Thorpe", "Tirole", "Tomić", "Tonegawa", "Torres", "Tranströmer", "Tremblay", "Trimble",
		"Tuominen", "Turing", "Tyler", "Valverde", "Varmus", "Virtanen", "Voss", "Warhol",
		"Weber", "Weiss", "Williams", "Wineland", "Winkler", "Winter", "Wolf", "Wouters",
		"Xiaobo", "Yamamoto", "Yan", "Yousafzai", "Zhang", "de Gennes", "zum Moore"
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
		name = name.replace("á", "a");
		name = name.replace("ä", "ae");
		name = name.replace("é", "e");
		name = name.replace("ě", "e");
		name = name.replace("ć", "c");
		name = name.replace("ó", "o");
		name = name.replace("ō", "o");
		name = name.replace("ö", "oe");
		name = name.replace("ø", "oe");
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

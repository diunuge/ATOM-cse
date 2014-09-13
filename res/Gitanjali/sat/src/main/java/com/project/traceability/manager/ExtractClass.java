package com.project.traceability.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

import com.project.traceability.model.RequirementModel;
import com.project.traceability.utils.Constants;

public class ExtractClass {
	static Set<String> nounPhrases = new HashSet<>();
	static Map<String, String> resultList = null;

	public static Map<String, String> extractClass(
			List<RequirementModel> requirementModel) throws InvalidFormatException, IOException {
		
		getClassName("1", "print mini statement");
		/*for (int i = 0; i < requirementModel.size(); i++) {
			String requirementId = (requirementModel.get(i)).getRequirementId();
			//String specificRequirement = (requirementModel.get(i)).getTitle();
			getClassName(requirementId, "Customer details");
		}*/
		return resultList;

	}

	public static void getClassName(String requirementId, String requirement) {
		//String[] requirementPart = requirement.split(" ");
		try {
			parserAction(requirementId, requirement);
			removeDefaultWord(requirementId, nounPhrases);
			// System.out.println("List of Noun Parse : "+nounPhrases);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getNounPhrases(String requirementId, Parse p) {
		System.out.println(p.toString());
		if (p.getType().equals("VB") || p.getType().equals("VBD")
				|| p.getType().equals("VBG") || p.getType().equals("VBN") || p.getType().equals("VBP")
				|| p.getType().equals("VBZ")) {
		/*if (p.getType().equals("NN") || p.getType().equals("NNS")
				|| p.getType().equals("NNP") || p.getType().equals("NNPS") || p.getType().equals("NP")) {*/
			nounPhrases.add(p.getCoveredText());
		}

		for (Parse child : p.getChildren()) {
			getNounPhrases(requirementId, child);
		}

	}

	public static void parserAction(String requirementId, String partrequirement)
			throws Exception {
		InputStream is = new FileInputStream("./src/en-parser-chunking.zip");
		ParserModel model = new ParserModel(is);
		Parser parser = ParserFactory.create(model);
		Parse topParses[] = ParserTool.parseLine(partrequirement, parser, 1);
		for (Parse p : topParses) {
			p.show();
			getNounPhrases(requirementId, p);
		}
	}

	public static void removeDefaultWord(String requirementId, Set<String> parse) {
		// String[] terms = {"system", "details", "company", "information",
		// "requirement", "organization", "database","Print"};
		List<String> tempList = new ArrayList<String>(parse);
		resultList = new HashMap<String, String>();
		for (int i = 0; i < tempList.size(); i++) {
			if (Constants.getRemovableTerms().contains(tempList.get(i))) {
				tempList.remove(i);
				i -= 1; // after removing the element automatically point to the
						// previous index
			}
			resultList.put(requirementId, tempList.get(i));
		}
		System.out.println(resultList);
		// /////////////////////////////
	}

}

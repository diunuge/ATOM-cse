package com.project.traceability.manager;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import com.project.traceability.model.RequirementModel;

public class ExtractClass {
	static Set<String> nounPhrases = new HashSet<>();
	
	public static void extractClass(List<RequirementModel> requirementModel) {
		for(int i = 0; i < requirementModel.size();i++){
			String specificRequirement = (requirementModel.get(i)).getTitle();
			getClassName(specificRequirement);
		}
		
		
	}
	
	public static void getClassName(String requirement){
		String[] requirementPart= requirement.split("such as");
		try {
			parserAction(requirementPart[0]);
			removeDefaultWord(nounPhrases);
			//System.out.println("List of Noun Parse : "+nounPhrases);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getNounPhrases(Parse p) {
        if (p.getType().equals("NN") || p.getType().equals("NNS") ||  p.getType().equals("NNP") || p.getType().equals("NNPS")) {
            nounPhrases.add(p.getCoveredText());
        }

        for (Parse child : p.getChildren()) {
            getNounPhrases(child);
        }
        
    }
	
	public static void parserAction(String partrequirement) throws Exception {
        InputStream is = new FileInputStream("./src/en-parser-chunking.zip");
        ParserModel model = new ParserModel(is);
        Parser parser = ParserFactory.create(model);       
        Parse topParses[] = ParserTool.parseLine(partrequirement, parser, 1);
        for (Parse p : topParses){
            //p.show();
            getNounPhrases(p);
        }
    }
	
	 public static void removeDefaultWord(Set<String> parse) {
	        String[] terms = {"system", "details", "company", "information", "requirement", "organization", "database"};
	        List<String> resultList = new ArrayList(parse);
	        for (int i = 0; i < resultList.size(); i++) {        	
	            for (int j = 0; j < terms.length; j++) { // check default terms
	                if (resultList.get(i).contains(terms[j])) {
	                    resultList.remove(terms[j]);
	                    i-=1; // after removing the element automatically point to the previous index
	                    break;// if it finds then break the loop
	                }
	            }
	        }
	        System.out.println(resultList);	        
	    }

}

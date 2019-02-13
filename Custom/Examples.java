
//package in.ac.iitb.cfilt.jhwnl.examples;

import in.ac.iitb.cfilt.jhwnl.JHWNL;
import in.ac.iitb.cfilt.jhwnl.JHWNLException;
import in.ac.iitb.cfilt.jhwnl.data.IndexWord;
import in.ac.iitb.cfilt.jhwnl.data.IndexWordSet;
import in.ac.iitb.cfilt.jhwnl.data.Pointer;
import in.ac.iitb.cfilt.jhwnl.data.PointerType;
import in.ac.iitb.cfilt.jhwnl.data.Synset;
import in.ac.iitb.cfilt.jhwnl.data.POS;
import in.ac.iitb.cfilt.jhwnl.dictionary.Dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Examples {
	
	public static String demonstration() {
		String out = "";
		BufferedReader inputWordsFile = null;
		try {
			inputWordsFile = new BufferedReader(new InputStreamReader (new FileInputStream ("inputwords.txt"), "UTF8"));
		} catch( FileNotFoundException e){
			System.err.println("Error opening input words file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		JHWNL.initialize();
		
		String inputLine;
		long[] synsetOffsets;
		
		try {
			while((inputLine = inputWordsFile.readLine()) != null){
				out += ("\n" + inputLine);
				//	 Look up the word for all POS tags
				IndexWordSet demoIWSet = Dictionary.getInstance().lookupAllIndexWords(inputLine.trim());				
				//	 Note: Use lookupAllMorphedIndexWords() to look up morphed form of the input word for all POS tags				
				IndexWord[] demoIndexWord = new IndexWord[demoIWSet.size()];
				demoIndexWord  = demoIWSet.getIndexWordArray();
				for ( int i = 0;i < demoIndexWord.length;i++ ) {
					int size = demoIndexWord[i].getSenseCount();
					out += ("Sense Count is " + size);	
					synsetOffsets = demoIndexWord[i].getSynsetOffsets();
					for ( int k = 0 ;k < size; k++ ) {
						System.out.println("Offsets[" + k +"] " + synsetOffsets[k]);	
					}

					Synset[] synsetArray = demoIndexWord[i].getSenses(); 
					for ( int k = 0;k < size;k++ ) {
						out += ("Synset [" + k +"] "+ synsetArray[k]);
						out += ("Synset POS: " + synsetArray[k].getPOS());
						Pointer[] pointers = synsetArray[k].getPointers();
						out += ("Synset Num Pointers:" + pointers.length);
						for (int j = 0; j < pointers.length; j++) {							
							if(pointers[j].getType().equals(PointerType.ONTO_NODES)) {	// For ontology relation
								out += (pointers[j].getType() + " : "  + Dictionary.getInstance().getOntoSynset(pointers[j].getOntoPointer()).getOntoNodes());
							} else {
								out += (pointers[j].getType() + " : "  + pointers[j].getTargetSynset());
							}							
						}
						
					}
				}
				
			}
		} catch (IOException e) {
			System.err.println("Error in input/output.");			
			e.printStackTrace();
		} catch (JHWNLException e) {
			System.err.println("Internal Error raised from API.");
			e.printStackTrace();
		} 
		return out;
	}
	
	//Source: sivareddy.in/downloads
	public static String getRoot(String word) {
		JHWNL.initialize();
		Dictionary dict = Dictionary.getInstance();
		try {
			IndexWord[] I = dict.lookupMorphedIndexWords(POS.NOUN, word).getIndexWordArray();
			if(I.length != 0)
				return I[0].getLemma();
			
			I = dict.lookupMorphedIndexWords(POS.ADJECTIVE, word).getIndexWordArray();
			if(I.length != 0)
				return I[0].getLemma();

			I = dict.lookupMorphedIndexWords(POS.VERB, word).getIndexWordArray();
			if(I.length != 0)
				return I[0].getLemma();

			I = dict.lookupMorphedIndexWords(POS.ADVERB, word).getIndexWordArray();
			if(I.length != 0)
				return I[0].getLemma();
		} catch(JHWNLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String args[]) throws Exception {
		demonstration();
	}
}

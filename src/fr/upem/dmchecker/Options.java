package fr.upem.dmchecker;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

public class Options extends JSAP {



	public	enum	MyColor {

		//NUMBER_OF_COMMAND(),
		OPTION_ONE("optionOne",'1'),
		OPTION_TWO("optionTwo",'2'),
		OPTION_THREE("optionThree",'3'),
		OPTION_FOUR("optionFour",'4'),
		VERBOSE("verbose",'v'),
		DESTINATION("destination",'d'),		
		ONE_TOP("oneTop",'o'),
		FORCE_ONE_TOP("forceOneTop",'O'),
		ENDS_WITH("endsWith",'e'),
		FORCE_ENDS_WITH("forceEndsWith",'E'),
		EXIST("exist",'x'),
		FORCE_EXIST("forceExist",'X'),
		FORBIDS("forbids",'i'),
		FORCE_FORBIDS("forceForbids",'I'),
		BEGIN_WITH("beginWith",'b'),
		FORCE_BEGIN_WITH("forceBeginWith",'B');

		String longFlag; 
		char shortFlag;
		String  value[];
		static final int nbEnum=16;
		static final int nbBoolean=5;

		MyColor(){

		}
		MyColor(String longFlag, char shortFlag){
			this.longFlag=longFlag;
			this.shortFlag=shortFlag;
		}

		public String getName(){
			return longFlag;
		}
		public char getFlag(){
			return shortFlag;
		}
		public String[] getValue(){

			return value;			
		}
		public void setValue(String value[]){
			this.value=value;
		}
		public void setValue(String string) {
			String value[]=new String[1];
			value[0]=string;
			this.value=value;			
		}
	}

	public static EnumSet<MyColor> getOption(String args[]) throws JSAPException{
		JSAP jsap = new JSAP();
		EnumSet<MyColor> usedOption = EnumSet.noneOf(MyColor.class) ;


		// The select options (-1,-2,-3,-4)
		for(MyColor opt : MyColor.values()){
			if(opt.ordinal()<4){
				jsap.registerParameter(new Switch(opt.getName()).setShortFlag(opt.getFlag()));
			}
		}

		//Destination
		FlaggedOption destinationOption = new FlaggedOption(MyColor.DESTINATION.longFlag)
		.setStringParser(JSAP.STRING_PARSER).setDefault(".") 
		.setRequired(false).setShortFlag(MyColor.DESTINATION.shortFlag) 
		.setLongFlag(MyColor.DESTINATION.longFlag);
		jsap.registerParameter(destinationOption);

		//Verbose
		Switch verboseSwitch = new Switch(MyColor.VERBOSE.longFlag)
		.setShortFlag(MyColor.VERBOSE.shortFlag).setLongFlag(MyColor.VERBOSE.longFlag);
		jsap.registerParameter(verboseSwitch);

		UnflaggedOption unflaggedOption = new UnflaggedOption("unflaggedOption")
		.setStringParser(JSAP.STRING_PARSER)
		.setRequired(true)
		.setGreedy(true); // Recover all unflagged option
		jsap.registerParameter(unflaggedOption);


		// For the others options
		for(MyColor opt : MyColor.values()){
			if(opt.ordinal()>=MyColor.nbBoolean+1){	// +1 for destinationOption which is registered after

				//	System.out.println("->"+opt);
				jsap.registerParameter( getFlaggedOption(opt));
			}
		}

		System.out.println("BEFORE PARSE "+Arrays.toString(args));
		// Parse all the arguments
		JSAPResult config = jsap.parse(args);    


		// If parsing is unsuccessful, print the usage
		if (!config.success()) {
			System.err.println();
			System.err.println("Usage: java ");
			System.err.println("                "
					+ jsap.getUsage());
			System.err.println();
			System.exit(1);
		}
		String[] option;

		//penser aux tableaux de string
		for(MyColor opt : MyColor.values()){

			if(opt.ordinal()<MyColor.nbBoolean && config.getBoolean(opt.longFlag)){ 
				usedOption.add(opt);
			}
			else if(opt.ordinal()>MyColor.nbBoolean && (option=config.getStringArray(opt.longFlag)).length>0){ 
				//System.out.println("DEBUG : "+opt.getName()+" "+Arrays.toString(option));
				opt.setValue(option);
				usedOption.add(opt);
			}
		}


		// DESTINATION
		// The priority is for the second argument, and after the option -d
		boolean destinationIsAlreadyKnown=false;
		if((option=config.getStringArray("unflaggedOption")) != null ){
			switch(option.length){
			case 1 : MyColor.OPTION_ONE.setValue(option); break;
			case 2 : MyColor.OPTION_TWO.setValue(option);
					MyColor.DESTINATION.setValue(option[1]); 
					destinationIsAlreadyKnown=true;			break;
			case 3 : MyColor.OPTION_THREE.setValue(option); break;
			case 4 : MyColor.OPTION_FOUR.setValue(option); break;
			default : // #TODO  erreur 
				break;
			}
			//MyColor.DESTINATION.setValue(option[1]);
		}
		if(!destinationIsAlreadyKnown &&(option=config.getStringArray(MyColor.DESTINATION.longFlag))!=null){
			//System.out.println("DESTINATION ---> "+option);
			MyColor.DESTINATION.setValue(option);
		}
		else{
			// Error, pas de destination
			// #TODO  erreur 
			// Apel to DMMESSAGE
			System.exit(0);
		}		
		//	MyColor.DESTINATION.setValue(option[1]);
		usedOption.add(MyColor.DESTINATION);


		// Test if there is only one selected option
		int nbSelectOption = 0;
		for(MyColor opt : MyColor.values()){
			if(opt.ordinal()<4 && usedOption.contains(opt)){
				nbSelectOption++;				
			}
		}
		if(nbSelectOption != 1){
			// #TODO  erreur 
			// Erreur, option invalide
		}


		System.out.println(config.getString("destination"));
		System.out.println("Sont interdit les fichiers finissant par: "+config.getString("exist"));

		/*	for (int i = 0; i < config.getInt("count"); ++i) {
					System.out.println("Hello, World!");
				}*/
		//return MyColor.values();
		return usedOption;
	}

	private static FlaggedOption getFlaggedOption(final MyColor option) {
		FlaggedOption flaggedOption = new FlaggedOption(option.getName())
		.setStringParser(JSAP.STRING_PARSER)
		.setRequired(false).setShortFlag(option.getFlag()) 
		.setLongFlag(option.getName());
		return flaggedOption;
	}



	//@SuppressWarnings("static-access")
	public static void main(String[] args) throws JSAPException {
		Options options = new Options();
		//String string[]={""}; 

		String string[]={"-1","-d","./local","-x",".rar","coucou"};
		options.getOption(string);
		for(MyColor my : MyColor.values()){
			System.out.println(my+" : "+my.getValue());
		}

		EnumSet<MyColor> mc=options.getOption(string);
		for(MyColor my : mc){
			System.out.println("ENUM : "+my+" : "+my.getValue());
		}


		//System.out.println("Destination :"+(mc..getValue());
	}

}

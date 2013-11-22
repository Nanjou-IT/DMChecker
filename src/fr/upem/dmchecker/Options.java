package fr.upem.dmchecker;

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

	static final int nbEnum=12;
	static final int nbBoolean=5;
	
	static class Option{
		String name;
		char flag;

		public Option(String name, char flag) {
			this.name = name;
			this.flag = flag;
		}	
	}

	/*
	 * Mettre la hashmap des options dans le enum
	 * 
	 */
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


		//static final HashMap<String,MyColor> map;

		String longFlag; //longflag
		char shortFlag;
		String  value;

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
		public String getValue(){

			return value;			
		}
		public void setValue(String value){
			this.value=value;
		}
		static
		{
			/*DESTINATION.longFlag="destination";
			DESTINATION.shortFlag='d';
			VERBOSE.longFlag="verbose";
			VERBOSE.shortFlag='v';
			ONE_TOP.longFlag="oneTop";
			ONE_TOP.shortFlag='o';
			FORCE_ONE_TOP.longFlag="forceOneTop";
			FORCE_ONE_TOP.shortFlag='O';
			ENDS_WITH.longFlag="endsWith";
			ENDS_WITH.shortFlag='e';
			FORCE_ENDS_WITH.longFlag="forceEndsWith";
			FORCE_ENDS_WITH.shortFlag='E';
			EXIST.longFlag="exist";
			EXIST.shortFlag='x';
			FORCE_EXIST.longFlag="forceExist";
			FORCE_EXIST.shortFlag='X';
			FORBIDS.longFlag="forbids";
			FORBIDS.shortFlag='i';
			FORCE_FORBIDS.longFlag="forceForbids";
			FORCE_FORBIDS.shortFlag='I';
			BEGIN_WITH.longFlag="beginWith";
			BEGIN_WITH.shortFlag='b';
			FORCE_BEGIN_WITH.longFlag="forceBeginWith";
			FORCE_BEGIN_WITH.shortFlag='B';
			 */



			//DESTINATION = new MyColor();
			/*HashMap<String,Color> map =
					new HashMap<String,Color>();
			for(MyColor m:values()) {
				map.put(m.getName(),m);
			}
			MyColor.map = map;*/
		}
	}

	public EnumSet<MyColor> getOption(String args[]) throws JSAPException{
		JSAP jsap = new JSAP();
		//MyColor options = new MyColor();
		//HashMap<String,LinkedList<String>> options = new HashMap<>();

		//LinkedList<Option> optionsList = new LinkedList<>();

		EnumSet<MyColor> usedOption = EnumSet.noneOf(MyColor.class) ;


		/*final Option DESTINATION = new Option("destination",'d');
		final Option VERBOSE = new Option("verbose",'v');
		final Option ONE_TOP = new Option("oneTop",'o');
		final Option FORCE_ONE_TOP = new Option("forceOneTop",'O');
		final Option ENDS_WITH = new Option("endsWith",'e');
		final Option FORCE_ENDS_WITH = new Option("forceEndsWith",'E');
		final Option EXIST = new Option("exist",'x');
		final Option FORCE_EXIST = new Option("forceExist",'X');
		final Option FORBIDS = new Option("forbids",'i');
		final Option FORCE_FORBIDS = new Option("forceForbids",'I');
		final Option BEGIN_WITH = new Option("beginWith",'b');
		final Option FORCE_BEGIN_WITH = new Option("forceBeginWith",'B');*/

		/*	optionsList.add(DESTINATION);
		optionsList.add(VERBOSE);
		optionsList.add(ONE_TOP);
		optionsList.add(FORCE_ONE_TOP);
		optionsList.add(ENDS_WITH);
		optionsList.add(FORCE_ENDS_WITH);
		optionsList.add(EXIST);
		optionsList.add(FORCE_EXIST);
		optionsList.add(FORBIDS);
		optionsList.add(FORCE_FORBIDS);
		optionsList.add(BEGIN_WITH);
		optionsList.add(FORCE_BEGIN_WITH);
		 */

		/*
		 * Number of the command Option
		 */
		/*
		 * FlaggedOption flaggedOption = new FlaggedOption(option.getName())
		.setStringParser(JSAP.STRING_PARSER)
		.setRequired(false).setShortFlag(option.getFlag()) 
		.setLongFlag(option.getName());
		return flaggedOption;
		 */

		for(MyColor opt : MyColor.values()){
			//opt.
			if(opt.ordinal()<4){
				jsap.registerParameter(new Switch(opt.getName()).setShortFlag(opt.getFlag()));
			}
			/*jsap.registerParameter(new Switch("optionTwo").setShortFlag('2'));
		jsap.registerParameter(new Switch("optionThree").setShortFlag('3'));
		jsap.registerParameter(new Switch("optionFour").setShortFlag('4'));*/
		}

		//Destination
		FlaggedOption destinationOption = new FlaggedOption(MyColor.DESTINATION.longFlag)
		.setStringParser(JSAP.STRING_PARSER).setDefault("./") 
		.setRequired(false).setShortFlag(MyColor.DESTINATION.shortFlag) 
		.setLongFlag(MyColor.DESTINATION.longFlag);

		//Verbose
		Switch verboseSwitch = new Switch(MyColor.VERBOSE.longFlag)
		.setShortFlag(MyColor.VERBOSE.shortFlag).setLongFlag(MyColor.VERBOSE.longFlag);


		UnflaggedOption unflaggedOption = new UnflaggedOption("name")
		.setStringParser(JSAP.STRING_PARSER)
		.setRequired(true)
		.setGreedy(true); // Recover all unflagged option



		// For the others options
		for(MyColor opt : MyColor.values()){
			if(opt.ordinal()>=nbBoolean+1){	// +1 for destinationOption which is registered after

				System.out.println("->"+opt);
				jsap.registerParameter( getFlaggedOption(opt));
			}
		}
		jsap.registerParameter(destinationOption);
		jsap.registerParameter(verboseSwitch);
		jsap.registerParameter(unflaggedOption);
		/*for(Option opt : optionsList){
			FlaggedOption oneTopOption = getFlaggedOption(opt);

		}*/

		//FlaggedOption[] flaggedOptions = new FlaggedOption[optionsList.size()];


		/*
		//Destination
		FlaggedOption destinationOption = new FlaggedOption(DESTINATION.name)
		.setStringParser(JSAP.STRING_PARSER).setDefault("./") 
		.setRequired(false).setShortFlag(DESTINATION.flag) 
		.setLongFlag(DESTINATION.name);

		//Verbose
		Switch verboseSwitch = new Switch(VERBOSE.name)
		.setShortFlag(VERBOSE.flag).setLongFlag(VERBOSE.name);

		FlaggedOption oneTopOption = getFlaggedOption(ONE_TOP);
		FlaggedOption forceOneTopOption = getFlaggedOption(FORCE_ONE_TOP);


		FlaggedOption endsWithOption = getFlaggedOption(ENDS_WITH);
		FlaggedOption forceEndsWithOption = getFlaggedOption(FORCE_ENDS_WITH);

		FlaggedOption existOption = getFlaggedOption(EXIST);
		FlaggedOption forceExistOption = getFlaggedOption(FORCE_EXIST);

		FlaggedOption forbidsOption = getFlaggedOption(FORBIDS);
		//Force-Forbids 
		FlaggedOption forceForbidsOption = getFlaggedOption(FORCE_FORBIDS);

		FlaggedOption beginsWithOption = getFlaggedOption(BEGIN_WITH);
		FlaggedOption forceBeginsWithOption = getFlaggedOption(FORCE_BEGIN_WITH);
		 */
		//System.out.println(opt.length);

		/*jsap.registerParameter(destinationOption);
		jsap.registerParameter(verboseSwitch);
		jsap.registerParameter(oneTopOption);
		jsap.registerParameter(forceOneTopOption);
		jsap.registerParameter(endsWithOption);
		jsap.registerParameter(forceEndsWithOption);
		jsap.registerParameter(existOption);
		jsap.registerParameter(forceExistOption);
		jsap.registerParameter(forbidsOption);
		jsap.registerParameter(forceForbidsOption);
		jsap.registerParameter(beginsWithOption);
		jsap.registerParameter(forceBeginsWithOption);
		 */
		JSAPResult config = jsap.parse(args);    


		// If parsing is unsuccessful
		if (!config.success()) {
			System.err.println();
			System.err.println("Usage: java ");
			System.err.println("                "
					+ jsap.getUsage());
			System.err.println();
			System.exit(1);
		}
		/*
		 * jsap.registerParameter(oneTopOption);
		jsap.registerParameter(forceOneTopOption);
		jsap.registerParameter(endsWithOption);
		jsap.registerParameter(forceEndsWithOption);
		jsap.registerParameter(existOption);
		jsap.registerParameter(forceExistOption);
		jsap.registerParameter(forbidsOption);
		jsap.registerParameter(forceForbidsOption);
		jsap.registerParameter(beginsWithOption);
		jsap.registerParameter(forceBeginsWithOption);

		 */
		String option;
		/*
		 * Gaffe au cas de destination et de verbose
		 */
		//penser aux tableaux de string
		for(MyColor opt : MyColor.values()){
			
			if(opt.ordinal()<nbBoolean && config.getBoolean(opt.longFlag)){ 
				usedOption.add(opt);
			}
			else if(opt.ordinal()>=nbBoolean && (option=config.getString(opt.longFlag))!=null){ 
				opt.setValue(option);
				usedOption.add(opt);
			}
		}
		



		/*	if((option=config.getString("optionOne"))!=null){
			MyColor.NUMBER_OF_COMMAND.setValue("1");
		}
		else if((option=config.getString("optionTwo"))!=null){
			MyColor.NUMBER_OF_COMMAND.setValue("2");
		}
		else if((option=config.getString("optionThree"))!=null){
			MyColor.NUMBER_OF_COMMAND.setValue("3");
		}
		else if((option=config.getString("optionFour"))!=null){
			MyColor.NUMBER_OF_COMMAND.setValue("4");
		}
		else{
			//ERREUR
		}
		usedOption.add(MyColor.NUMBER_OF_COMMAND);*/





		/*String option;

		if((option=config.getString(DESTINATION.name))!=null){ // The default value is ./

		}
		if((option=config.getString(VERBOSE.name))!=null){ 

		}
		if((option=config.getString(ONE_TOP.name))!=null){ 

		}
		if((option=config.getString(FORCE_ONE_TOP.name))!=null){ 

		}
		if((option=config.getString(ENDS_WITH.name))!=null){ 

		}
		if((option=config.getString(FORCE_ENDS_WITH.name))!=null){ 

		}
		if((option=config.getString(EXIST.name))!=null){ 

		}
		if((option=config.getString(FORCE_EXIST.name))!=null){ 

		}
		if((option=config.getString(EXIST.name))!=null){ 

		}
		if((option=config.getString(EXIST.name))!=null){ 

		}
		if((option=config.getString(EXIST.name))!=null){ 

		}
		if((option=config.getString(EXIST.name))!=null){ 

		}
		 */

		// Traité les deux derniers args et le -1 ou -2 ...

		System.out.println(config.getString("destination"));
		System.out.println("Sont interdit les fichiers finissant par: "+config.getString("exist"));

		/*	for (int i = 0; i < config.getInt("count"); ++i) {
					System.out.println("Hello, World!");
				}*/
		//return MyColor.values();
		return usedOption;
	}

	private FlaggedOption getFlaggedOption(final MyColor option) {
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

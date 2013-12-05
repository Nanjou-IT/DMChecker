package fr.upem.dmchecker;

import java.util.EnumSet;

import com.martiansoftware.jsap.JSAPException;

import fr.upem.dmchecker.Options.MyColor;

public class DmChecker {
	public static void main(String[] args) throws JSAPException {
		if (args == null) {
			System.err.println("Bad arguments"); 
			System.exit(1);
		}
		
		EnumSet<MyColor> properties = Options.getOption(args);
		// {"-1","-O","/tmp/notop.zip"}
		
		
		if (properties.contains(MyColor.OPTION_ONE) || properties.contains(MyColor.OPTION_TWO)) {
			Archive zip = new Archive(properties);
			zip.start();
			
			
		} else if (properties.contains(MyColor.OPTION_THREE)) {
			
		} else if (properties.contains(MyColor.OPTION_FOUR)) {
			
		} else {
			
		}
		
		
		
	}
}

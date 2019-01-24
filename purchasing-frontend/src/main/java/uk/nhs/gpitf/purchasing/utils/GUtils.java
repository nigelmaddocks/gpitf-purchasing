package uk.nhs.gpitf.purchasing.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class GUtils {
	public static Object makeObjectForId(Class<?> clazz, long id) throws Exception {
		Constructor<?> oCon = clazz.getConstructor();
		Method setId = clazz.getMethod("setId", long.class);
		Object obj = oCon.newInstance();
		setId.invoke(obj, new Object[] {id});
		return obj;
	}
	
    /****************************************************************************
     * Returns the same string with each word starting with a capital letter.
     * There are special sequences of letters that are kept capitalised. These
     * are: GP, GMC, GDP, OO, OMP, DO, PPA, PCT, PCG, PCO, NHS, MPC, GOC, GOC/OQC,
     * ID, HQ, GOC/GMC, PM, RPSGB.
     * Hyphens are considered as word separators.
     *
     * @param phrase The text to be Capitalized
     * @return the same phrase with capitals applied.
     */
    public static String getCapitalized(String phrase) {
 
        phrase = phrase.toUpperCase();
 
        if (phrase.equals("AT CODE") || phrase.equals("UTLA CODE")) {
            return phrase.replace("CODE", "Code");
        }
 
        String specials  = ":CCG:GMC/GDC/GOC:GP:GMC:GDP:OO:OMP:OPL:DO:PPA:PCT:PCG:PCO:NHS:MPC:GOC:GOC/OQC:ID:HQ:GOC/GMC:PM:RPSGB:KC53:KC63:GDC:VT:WTE:PTS:GSE:MPIG:CHS:ENT:PMS:HHAT:PDP:NMC:PIN:QOF:XML:DOB:FHSA:DHA:EEA:VI:MMR:DTP:SOL:CRB:LSD:PBE:LRMP:FTP:FP69:TM:DBS:CAMHS:CGL:ER:HC:GPSI:GPWSI:DMC:PCT:OOH:UK:HMP:YOI:NE:NW:SE:SW:CSU:WIC:TPP:EMIS:";
        String specials2 = ":GPS:GDPS:OOS:OMPS:OPLS:PCTS:PCGS:PCOS:IDS:";
//    String lowerSpecials = ":DU:DE:LA:VAN:";
        String delimiters = " \t\n\r\f";  // StringTokenizer default delimiters, and capitalsAfter
        String capitalsAfter = "-(./&";
        String allDelimiters = delimiters + capitalsAfter;
        StringTokenizer st = new StringTokenizer(phrase.trim(), allDelimiters, true);
        String retval = "";
        String token = null;
 
        try {
            while (st.hasMoreElements()) {
                token = st.nextToken();
                boolean bIsDelimiter = allDelimiters.contains(token);
                if (bIsDelimiter) {
                    if (capitalsAfter.contains(token)) {
                        if (token.equals("(")) {
                            retval += token;
                        } else {
                            retval = retval.trim() + token;
                        }
                    }
                } else {
                    String tokenColoned = ":" + token + ":";
                    if (specials.indexOf(tokenColoned) > -1) {
                        retval += token;
                    } else if (specials2.indexOf(tokenColoned) > -1) {
                        retval += token.substring(0, token.length()-1) + token.substring(token.length()-1).toLowerCase();
                    } else if (token.length() > 2 && (token.toUpperCase().startsWith("O'") || token.toUpperCase().startsWith("D'"))) {
                        retval += token.substring(0, 3) + token.substring(3).toLowerCase();
                    } else if (token.length() > 1 && token.toUpperCase().startsWith("'")) {
                        retval += token.substring(0, 2) + token.substring(2).toLowerCase();
                    } else if (token.toUpperCase().startsWith("MC") && token.length() > 6) {
                        retval += "Mc" + token.charAt(2) + token.substring(3).toLowerCase();
                    } else if (token.toUpperCase().equals("GPHC")) {
                        retval += "GPhC";
                    } else {
                        retval += token.charAt(0) + token.substring(1).toLowerCase();
                    }
                    if (st.hasMoreElements()) {
                        retval += " ";
                    }
                }
            }
        } catch (NoSuchElementException e) {
            retval = "";
        }
       
        return retval;
    }	
}

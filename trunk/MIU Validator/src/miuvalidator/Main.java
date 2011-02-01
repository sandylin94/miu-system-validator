/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package miuvalidator;
import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eherrerag
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        String line;
        boolean valid = false;
        
        try {
            System.out.println("Introduce una cadena para válidar: ");
            line = br.readLine();

            LinkedList rules = reconstructChain(line.toUpperCase(), 1);
            
            valid = rules.size() > 0 ? true : false;
            System.out.println("La cadena \"" + line + (valid ? "\"" : "\" NO") + " es válida");


        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }


    private static LinkedList reconstructChain(String line, int deep){
        LinkedList rules = new LinkedList();
        LinkedList rulesAux;
        String cad;
        boolean flag = true;

        int rule = 1;

        int i = 0;
        
        if (validateStructure(line)){
            if (line.equals("MI")){
                rules.push(0);
            }else{
                while(rule < 4 && !rules.contains(0)){
                    cad = line;
                    
                    cad = doReverseRule(line, rule);
                    if (!cad.equals(line) && rule != 4){
                        rules.push(rule);
                        rulesAux = reconstructChain(cad, deep +1);

                        if (rulesAux.size() == 0)
                            rules.pop();
                        else
                            System.out.println("Usando: " + rule + " en " + cad + " obtenemos " + line);
                        while (rulesAux.size() > 0)
                            rules.push(rulesAux.pollLast());
                    }
                    rule++;
                }
            }
        }
        return rules;
    }

    private static  boolean validateStructure(String cad){
        String aux = cad;
        int cantI;
        double pow2 = 0;
        
        aux = aux.replace("M", "");
        
        cantI = aux.length();

        if (aux.matches("^I*"))
            pow2 = Math.log(cantI)/Math.log(2);
        
        return cad.matches("^M[IU]*") && (pow2 == Math.round(pow2));

    }

    private static boolean canApplyRule(String cad, int rule){
        boolean res = false;

        switch (rule){
            case 1:
                res = cad.endsWith("I");
                break;
            case 2:
                res = true;
                break;
            case 3:
                res = cad.contains("III");
                break;
            case 4:
                res = cad.contains("UU");
                break;
        }

        return res;
    }

    /* REGLAS
         * I.   Si la cadena términa con I se puede agregar una U al final
         * II.  Si se tiene la cadena Mx se puede producir Mxx
         * III. Si 'III' existe en la cadena se puede remplazar por U
         * IV.  Si 'UU' existe en la cadena su puede eliminar
         */

    private static String doRule(String cad, int rule){
        String res = cad;
        if (canApplyRule(cad, rule))
            switch (rule){
                case 1:
                    res = cad + "U";
                    break;
                case 2:
                    res = cad + cad.substring(1);
                    break;
                case 3:
                    res = cad.replaceFirst("I{3}", "U");
                    break;
                case 4:
                    res = cad.replaceFirst("U{2}", "");
                    break;
            }

        return res;
    }

    private static String doRules(String cad, LinkedList rules){
        String res=cad;
        int rule;

        while (rules.size() > 0){
            rule = (Integer)rules.pop();
            res = doRule(res, rule);
            System.out.println("Using " + rule + ": " + res);
        }
        return res;
    }

    private static String doReverseRule(String cad, int rule){
        String res = cad;
        if (canReverseRule(cad, rule))
            switch (rule){
                case 1:
                    res = cad.substring(0, cad.length()-1);
                    break;
                case 2:
                    int index = (cad.length() + 1)/2 - 1;
                    res = cad.substring(0, index + 1);
                    break;
                case 3:
                    res = cad.replaceFirst("U", "III");
                    break;
                //case 4:
                //    res = cad.replaceFirst("U{2}", "");
                    //cad.
                  //  break;
            }

        return res;
    }

    private static boolean canReverseRule(String cad, int rule){
        boolean res = false;

        switch (rule){
            case 1:
                res = cad.endsWith("IU");
                break;
            case 2:
                int index = (cad.length() - 1)/2;
                res = cad.substring(1, index +1 ).equals(cad.substring(index + 1));
                break;
            case 3:
                res = cad.contains("U");
                break;
            case 4:
                res = true;
                break;
        }

        return res;
    }

}

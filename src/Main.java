import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class Main {

    static int spacingOutput = 4;
    static String HEADER_NAME = "Produto";
    static String HEADER_QTT = "Quantidade";
    static String HEADER_VALUE = "Valor";
    static String MEDIUM_PRICE = "Preco Medio";

    public static void main(String[] args) {

        if(args.length == 1) {

            try {
                FileInputStream fileIS = new FileInputStream(args[0]);
                Parser p = new Parser(fileIS);
                Map<String, Product> prods = p.getProducts();

                printResults(prods);

            } catch (FileNotFoundException e) {
                System.err.println("Wrong File.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Missing file argument");
        }
    }

    /**
     * Prints the header and content
     * @param prods the products
     */
    private static void printResults(Map<String,Product> prods) {
        Iterator<String> it = prods.keySet().iterator();
        int maxNameLength = 0;
        int maxLengthQtt = 0;
        int maxValueLength = 0;
        int maxMediumPriceLength = 0;

        while(it.hasNext()) {
            String next = it.next();
            Product prod = prods.get(next);

            if(next.length() > maxNameLength) {
                maxNameLength = next.length();
            }

            int currQttLength = String.valueOf(prod.getQttSold()).length();

            if(currQttLength > maxLengthQtt) {
                maxLengthQtt = currQttLength;
            }

            int currValueLength = String.valueOf(prod.getSoldValue()).length();

            if(currValueLength > maxValueLength) {
                maxValueLength = currValueLength;
            }

            float mediumPrice =  (float)(prod.getSoldValue()/prod.getQttSold());
            int mediumPriceLength = String.valueOf(mediumPrice).length();

            if(mediumPriceLength > maxMediumPriceLength) {
                maxMediumPriceLength = mediumPriceLength;
            }

        }

        printHeaderElement(HEADER_NAME,maxNameLength+spacingOutput);
        printHeaderElement(HEADER_QTT,maxLengthQtt+spacingOutput);
        printHeaderElement(HEADER_VALUE,maxValueLength+spacingOutput);
        printHeaderElement(MEDIUM_PRICE,maxMediumPriceLength+spacingOutput);

        System.out.println();
        System.out.println(getRepeatedString("-",maxNameLength+maxLengthQtt+maxValueLength+maxMediumPriceLength+spacingOutput*4));

        printContent(prods,maxNameLength,maxLengthQtt,maxValueLength,maxMediumPriceLength);
    }

    /**
     * Prints the main content
     * @param prods the products
     * @param maxNameLength
     * @param maxLengthQtt
     * @param maxValueLength
     * @param maxMediumPriceLength
     */
    private static void printContent(Map<String, Product> prods, int maxNameLength, int maxLengthQtt, int maxValueLength, int maxMediumPriceLength) {
        Iterator<String> it = prods.keySet().iterator();

        it = prods.keySet().iterator();
        int spacing = 0;
        String spacer;
        while (it.hasNext()) {
            String next = it.next();
            Product prod = prods.get(next);
            double qttSold = prod.getQttSold();
            double valueSold = prod.getSoldValue() * qttSold;

            System.out.printf("%s",next);
            addSpacing(next.length(),maxNameLength+spacingOutput);

            String currQtt = String.valueOf(prod.getQttSold());
            System.out.printf("%s",currQtt);
            addSpacing(currQtt.length(),maxLengthQtt+spacingOutput);

            String soldValueString = String.valueOf(prod.getSoldValue());
            System.out.printf("%s", soldValueString);
            addSpacing(soldValueString.length(),maxValueLength+spacingOutput);

            float mediumPrice = (float)(prod.getSoldValue()/prod.getQttSold());
            System.out.printf("%f", mediumPrice);

            System.out.println();
        }
    }

    /**
     * Prints a part of the Header
     * @param str the String to print
     * @param maxLength the maximum length available to the header part space
     */
    private static void printHeaderElement(String str, int maxLength) {
        System.out.printf("%s", str);
        addSpacing(str.length(),maxLength-1);
        System.out.printf("|");
    }

    /**
     * Adds spacing in order to align the output
     * @param lengthLastString length of the last String
     * @param totalLengthToUse total length available to write
     */
    private static void addSpacing(int lengthLastString, int totalLengthToUse) {
        int spacing = totalLengthToUse-lengthLastString;
        String spacer = getRepeatedString(" ", spacing);
        System.out.printf("%s",spacer);
    }

    /**
     * Gets String str repeated n times
     * @param str the String
     * @param n the number of times to repeat
     * @return a string repeated n times
     */
    private static String getRepeatedString(String str, int n) {
        String newString = "".concat(str);
        for(int i = 1;i<n;i++) {
            newString = newString.concat(new String(str));
        }
        return newString;
    }

}

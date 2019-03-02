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
                Map<String, Product> prods = p.getList();

                getResult(prods);


            } catch (FileNotFoundException e) {
                System.err.println("Wrong File.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Missing file argument");
        }
    }

    public static void getResult(Map<String, Product> prods) {
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


        String nextLine = printHeader(HEADER_NAME,maxNameLength+spacingOutput);
        nextLine = nextLine.concat(printHeader(HEADER_QTT,maxLengthQtt+spacingOutput));
        nextLine = nextLine.concat(printHeader(HEADER_VALUE,maxValueLength+spacingOutput));
        nextLine = nextLine.concat(printHeader(MEDIUM_PRICE,maxMediumPriceLength+spacingOutput));
        System.out.println();
        System.out.println(nextLine);

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

    private static String printHeader(String str, int maxLength) {
        System.out.printf("%s", str);
        addSpacing(str.length(),maxLength-1);
        System.out.printf("|");
        return getRepeatedString("-",maxLength);
    }

    private static void addSpacing(int lengthLastString, int totalLengthToUse) {
        int spacing = totalLengthToUse-lengthLastString;
        String spacer = getRepeatedString(" ", spacing);
        System.out.printf("%s",spacer);
    }

    private static String getRepeatedString(String str, int n) {
        String newString = "".concat(str);
        for(int i = 1;i<n;i++) {
            newString = newString.concat(new String(str));
        }
        return newString;
    }

}

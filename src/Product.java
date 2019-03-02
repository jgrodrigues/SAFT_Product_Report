public class Product {

    private String name;
    private double soldValue;
    private double qttSold;

    public Product(String name, double qttSold, double soldValue ) {
        this.name = name;
        this.soldValue = soldValue;
        this.qttSold = qttSold;
    }

    public String getName() {
        return name;
    }

    public double getSoldValue() {
        return soldValue;
    }

    public double getQttSold() {
        return qttSold;
    }

    public void addSoldQtt(double qttToAdd) {
        qttSold += qttToAdd;
    }

    public void addValueSold(double valueToAdd) {
        soldValue += valueToAdd;
    }

}

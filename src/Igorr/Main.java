package Igorr;

public class Main {

    public static void main(String[] args) {

        Credit credit = new Credit(1000000, 120);
        credit.getPay(	20000,0);
        credit.getPay(	20000,50000);
        credit.getPay(20000,0);
        credit.getPay(	20000,0);
        credit.getPay(20000,0);
        System.out.printf(";");
    }
}

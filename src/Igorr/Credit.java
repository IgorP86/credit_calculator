package Igorr;

import java.util.ArrayList;

public class Credit {
    private static final String MONTH = "После внесения платежа: ";
    private static final String MAIN_DEBT_REM_AFTER_GET_PAY = "Остаток осн. долга после совершения текущего платежа: ";
    private static final String COMMON_EVERY_MONTH_PAY = "Общий ежемесячный платеж: ";
    private static final String PAY_DEBT_MAIN = "Платеж в счет погашения основного долга: ";
    private static final String PAY_DEBT_PERCENT = "Проценты по кредиту: ";

    private ArrayList<MonthInfo> tableOfPayments = new ArrayList<>();    //Для сохранения параметров

    private float primaryDept;          //общая сумма долга
    private float currentDebt;          //остаток долга

    public int getPeriodsAll() {
        return periodsAll;
    }

    public int getPassedPeriods() {
        return passedPeriods;
    }

    private int periodsAll;             //весь срок
    private int passedPeriods = 0;      //прошло периодов
    private float percentRate = 0.12f;  //ставка
    private float mainPartOnPay;        //основная часть долга
    private float percentPartOnPay;     //уплаченные проценты
    private float payment;              //ежемесячный платеж
    private Calculations calculations;  //калькулятор для расчетоа

    public Credit(float debt, int periodsAll) {
        calculations = new Calculations();
        this.primaryDept = debt;
        this.currentDebt = debt;
        this.periodsAll = periodsAll;
        this.payment = calculations.getMonthPayment();
        this.mainPartOnPay = calculations.getMainPartOnPayment(currentDebt);
        this.percentPartOnPay = calculations.getPercentsOnPayment(currentDebt);
        getNewTableOfPayments(tableOfPayments);
    }

    private void updateDebt() {
        mainPartOnPay = calculations.getMainPartOnPayment(currentDebt);
        percentPartOnPay = calculations.getPercentsOnPayment(currentDebt);
        currentDebt -= mainPartOnPay;
        passedPeriods++;
    }

    private void printState(float overPay) {
        System.out.printf(MONTH + "%d\n"
                        + MAIN_DEBT_REM_AFTER_GET_PAY + "%.2f\n"
                        + COMMON_EVERY_MONTH_PAY + "%.2f\n"
                        + PAY_DEBT_MAIN + "%.2f\n"
                        + PAY_DEBT_PERCENT + "%.2f\n\n",
                passedPeriods, currentDebt - overPay, payment, mainPartOnPay, percentPartOnPay);
    }

    public boolean getPay(float pay, float overPay) {
        if (pay > payment)
            updateDebt();
        else {
            System.out.println("платеж не прошел");
            return false;
        }
        printState(overPay);

        if (overPay != 0) {
            changeParams(overPay);
            getNewTableOfPayments(tableOfPayments);
        }
        return true;
    }

    private void changeParams(float overPay) {
        currentDebt -= overPay;
        primaryDept -= overPay;
        this.payment = calculations.getMonthPayment();
        this.mainPartOnPay = calculations.getMainPartOnPayment(currentDebt);
    }

    private void getNewTableOfPayments(ArrayList<MonthInfo> infoTemp) {
        float temp = currentDebt;
        infoTemp.clear();
        for (int i = 0; i < periodsAll-passedPeriods; i++) {
            infoTemp.add(new MonthInfo(
                    i + 1,
                    calculations.getMainPartOnPayment(temp),
                    calculations.getPercentsOnPayment(temp),
                    calculations.getMonthPayment(),
                    temp -= calculations.getMainPartOnPayment(temp))
            );
        }
    }

    private class MonthInfo {
        private int indexNumber;
        private float payDebtMain;
        private float payDebtPercent;
        private float payment;
        private float debtRemain;

        public MonthInfo(int indexNumber, float payDebtMain, float payDebtPercent, float payment, float debtRemain) {
            this.indexNumber = indexNumber;
            this.payDebtMain = payDebtMain;
            this.payDebtPercent = payDebtPercent;
            this.payment = payment;
            this.debtRemain = debtRemain;
        }
    }

    private class Calculations {
        private final float P = (1.0f / 12.0f) * percentRate;

        /**
         * @return Ежемесячный платеж
         */
        public float getMonthPayment() {
            return primaryDept * (P + (P / ((float) Math.pow(1 + P, periodsAll) - 1)));
        }

        /**
         * @return процентная составляющая
         */
        public float getPercentsOnPayment(float currentDebt) {
            return currentDebt * P;
        }

        /**
         * @return часть на погашение долга
         */
        public float getMainPartOnPayment(float currentDebt) {
            return getMonthPayment() - getPercentsOnPayment(currentDebt);
        }
    }
}
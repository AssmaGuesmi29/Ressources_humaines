package Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payroll {
    private int id;
    private Employee employee;
    private String month;
    private int year;
    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private LocalDate paymentDate;

    public Payroll(int id, Employee employee, String month, int year, BigDecimal baseSalary, BigDecimal bonus, BigDecimal deductions, LocalDate paymentDate) {
        this.id = id;
        this.employee = employee;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.paymentDate = paymentDate;
        this.netSalary = calculateNetSalary();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
        this.netSalary = calculateNetSalary();
    }

    public BigDecimal getBonus() { return bonus; }
    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
        this.netSalary = calculateNetSalary();
    }

    public BigDecimal getDeductions() { return deductions; }
    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
        this.netSalary = calculateNetSalary();
    }

    public BigDecimal getNetSalary() { return netSalary; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    private BigDecimal calculateNetSalary() {
        if (baseSalary != null && bonus != null && deductions != null) {
            return baseSalary.add(bonus).subtract(deductions);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
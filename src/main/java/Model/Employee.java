package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private String hireDate;
    private String status;
    private String email;
    private String phone;
    private String address;
    private BigDecimal baseSalary;

    public Employee(int id, String firstName, String lastName, String position, String department,
                    String hireDate, String status, String email, String phone, String address,BigDecimal baseSalary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
        this.hireDate = hireDate;
        this.status = status;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.baseSalary = baseSalary;

    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }

    public StringProperty fullNameProperty() {
        return new SimpleStringProperty(firstName + " " + lastName);
    }
}
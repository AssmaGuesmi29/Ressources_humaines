package Model;

import java.time.LocalDate;

public class LeaveRequest {
    private int id;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private String approver;
    private String status;

    // Constructeur
    public LeaveRequest(int id, String employeeName, LocalDate startDate, LocalDate endDate, int duration, String approver, String status) {
        this.id = id;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.approver = approver;
        this.status = status;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getApprover() { return approver; }
    public void setApprover(String approver) { this.approver = approver; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
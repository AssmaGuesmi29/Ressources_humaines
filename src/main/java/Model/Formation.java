package Model;


import java.math.BigDecimal;

public class Formation {
    private int id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String trainer;
    private String duree;
    private BigDecimal cost;

    public Formation(int id, String title, String description, String startDate, String endDate, String trainer,  String duree, BigDecimal cost) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainer = trainer;
        this.duree = duree;
        this.cost = cost;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getTrainer() { return trainer; }
    public void setTrainer(String trainer) { this.trainer = trainer; }

    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
}

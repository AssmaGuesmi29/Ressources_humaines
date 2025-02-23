package service;

import Model.Formation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private final List<Formation> formations = new ArrayList<>();

    public List<Formation> getAllFormations() {
        return formations;
    }

    public void addFormation(Formation formation) {
        formation.setId(formations.size() + 1);
        formations.add(formation);
    }

    public void updateFormation(Formation formation) {
        for (int i = 0; i < formations.size(); i++) {
            if (formations.get(i).getId() == formation.getId()) {
                formations.set(i, formation);
                break;
            }
        }
    }

    public void deleteFormation(int id) {
        formations.removeIf(formation -> formation.getId() == id);
    }
}

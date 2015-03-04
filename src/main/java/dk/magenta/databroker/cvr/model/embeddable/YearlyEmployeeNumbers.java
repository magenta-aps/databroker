package dk.magenta.databroker.cvr.model.embeddable;

import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class YearlyEmployeeNumbers {
    @Column(name = "yearlyEmplyeesYear")
    private int year;
    @Column(name = "yearlyEmplyeesNumber")
    private int employees;
    @Column(name = "yearlyEmplyeesInterval")
    private String employeesInterval;
    @Column(name = "yearlyEmplyeesFullTimeEquivalent")
    private int fullTimeEquivalent;
    @Column(name = "yearlyEmplyeesFullTimeEquivalentInterval")
    private String fullTimeEquivalentInterval;
    @Column(name = "yearlyEmplyeesIncludingOwners")
    private int includingOwners;
    @Column(name = "yearlyEmplyeesIncludingOwnersInterval")
    private String includingOwnersInterval;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public String getEmployeesInterval() {
        return employeesInterval;
    }

    public void setEmployeesInterval(String employeesInterval) {
        this.employeesInterval = employeesInterval;
    }

    public int getFullTimeEquivalent() {
        return fullTimeEquivalent;
    }

    public void setFullTimeEquivalent(int fullTimeEquivalent) {
        this.fullTimeEquivalent = fullTimeEquivalent;
    }

    public String getFullTimeEquivalentInterval() {
        return fullTimeEquivalentInterval;
    }

    public void setFullTimeEquivalentInterval(String fullTimeEquivalentInterval) {
        this.fullTimeEquivalentInterval = fullTimeEquivalentInterval;
    }

    public int getIncludingOwners() {
        return includingOwners;
    }

    public void setIncludingOwners(int includingOwners) {
        this.includingOwners = includingOwners;
    }

    public String getIncludingOwnersInterval() {
        return includingOwnersInterval;
    }

    public void setIncludingOwnersInterval(String includingOwnersInterval) {
        this.includingOwnersInterval = includingOwnersInterval;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("year", this.year);

        if(this.employees != 0)
            obj.put("numberOfEmployees", this.employees);
        if(this.employeesInterval != null)
            obj.put("numberOfEmployeesInterval", this.employeesInterval);
        if(this.fullTimeEquivalent != 0)
            obj.put("fullTimeEquivalent", this.fullTimeEquivalent);
        if(this.fullTimeEquivalentInterval != null)
            obj.put("fullTimeEquivalentInterval", this.fullTimeEquivalentInterval);
        if(this.includingOwners != 0)
            obj.put("includingOwners", this.includingOwners);
        if(this.includingOwnersInterval != null)
            obj.put("includingOwnersInterval", this.includingOwnersInterval);

        return obj;
    }
}

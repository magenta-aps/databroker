package dk.magenta.databroker.cvr.model.embeddable;

import dk.magenta.databroker.util.Util;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class QuarterlyEmployeeNumbers {
    @Column(name = "quarterlyEmplyeesYear")
    private int year;
    @Column(name = "quarterlyEmplyeesQuarter")
    private int quarter;
    @Column(name = "quarterlyEmplyeesNumber")
    private int employees;
    @Column(name = "quarterlyEmplyeesInterval")
    private String employeesInterval;
    @Column(name = "quarterlyEmplyeesFullTimeEquivalent")
    private int fullTimeEquivalent;
    @Column(name = "quarterlyEmplyeesFullTimeEquivalentInterval")
    private String fullTimeEquivalentInterval;
    @Column(name = "quarterlyEmplyeesIncludingOwners")
    private int includingOwners;
    @Column(name = "quarterlyEmplyeesIncludingOwnersInterval")
    private String includingOwnersInterval;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
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

    public QuarterlyEmployeeNumbers() {
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        if (this.year > 0) {
            obj.put("year", this.year);
        }
        if (this.quarter > 0) {
            obj.put("quarter", this.quarter);
        }

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


    public boolean equals(Object otherObject) {
        if (otherObject == null || otherObject.getClass() != QuarterlyEmployeeNumbers.class) {
            return false;
        }
        return this.equals((QuarterlyEmployeeNumbers) otherObject);
    }
    public boolean equals(QuarterlyEmployeeNumbers otherQuarterlyEmployeeNumbers) {
        return
                Util.compare(this.quarter, otherQuarterlyEmployeeNumbers.getQuarter()) &&
                Util.compare(this.year, otherQuarterlyEmployeeNumbers.getYear()) &&
                Util.compare(this.employees, otherQuarterlyEmployeeNumbers.getEmployees()) &&
                Util.compare(this.fullTimeEquivalent, otherQuarterlyEmployeeNumbers.getFullTimeEquivalent()) &&
                Util.compare(this.includingOwners, otherQuarterlyEmployeeNumbers.getIncludingOwners()) &&
                Util.compare(this.employeesInterval, otherQuarterlyEmployeeNumbers.getEmployeesInterval()) &&
                Util.compare(this.fullTimeEquivalentInterval, otherQuarterlyEmployeeNumbers.getFullTimeEquivalentInterval()) &&
                Util.compare(this.includingOwnersInterval, otherQuarterlyEmployeeNumbers.getIncludingOwnersInterval());
    }
}

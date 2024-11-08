package com.durga.employee.service;

import com.durga.employee.dto.TaxResponse;
import com.durga.employee.entity.Employee;
import com.durga.employee.exception.CustomException;
import com.durga.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        // Validate employeeId uniqueness and other rules
        if (employeeRepository.existsById(employee.getEmployeeId())) {
            throw new CustomException("Employee ID already exists");
        }
        employeeRepository.save(employee);
    }

    public TaxResponse calculateTaxDeductions(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException("Employee not found"));

        double yearlySalary = calculateYearlySalary(employee.getSalary(), employee.getDoj());
        double taxAmount = calculateTax(yearlySalary);
        double cessAmount = (yearlySalary > 2500000) ? (yearlySalary - 2500000) * 0.02 : 0;

        return new TaxResponse(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(), yearlySalary, taxAmount, cessAmount);
    }

    private double calculateYearlySalary(double salary, LocalDate doj) {
        // Logic to calculate prorated salary based on DOJ
        int monthsWorked = calculateMonthsWorked(doj);
        return salary * monthsWorked;
    }

    private double calculateTax(double yearlySalary) {
        // Logic for tax calculation based on slabs
        double tax = 0;
        if (yearlySalary > 250000) {
            tax += Math.min(yearlySalary - 250000, 250000) * 0.05;
        }
        if (yearlySalary > 500000) {
            tax += Math.min(yearlySalary - 500000, 500000) * 0.10;
        }
        if (yearlySalary > 1000000) {
            tax += (yearlySalary - 1000000) * 0.20;
        }
        return tax;
    }

    private int calculateMonthsWorked(LocalDate doj) {
        // Logic to calculate the number of months an employee worked
        LocalDate now = LocalDate.now();
        int months = Period.between(doj, now).getMonths();
        return Math.max(1, months);
    }
}
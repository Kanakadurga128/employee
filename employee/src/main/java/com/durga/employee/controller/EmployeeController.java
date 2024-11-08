package com.durga.employee.controller;

import com.durga.employee.dto.TaxResponse;
import com.durga.employee.entity.Employee;
import com.durga.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> createEmployee(@Valid @RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    @GetMapping("/{employeeId}/tax-deductions")
    public ResponseEntity<TaxResponse> getTaxDeductions(@PathVariable String employeeId) {
        TaxResponse taxResponse = employeeService.calculateTaxDeductions(employeeId);
        return ResponseEntity.ok(taxResponse);
    }
}

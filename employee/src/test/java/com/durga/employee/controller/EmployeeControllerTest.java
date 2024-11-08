package com.durga.employee.controller;

import com.durga.employee.dto.TaxResponse;
import com.durga.employee.entity.Employee;
import com.durga.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testCreateEmployee_Success() throws Exception {
        Employee employee = new Employee("E123", "John", "Doe", "john.doe@example.com",
                List.of("1234567890"), LocalDate.of(2023, 5, 16), 50000.0);

        doNothing().when(employeeService).saveEmployee(Mockito.any(Employee.class));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee created successfully"));
    }

    @Test
    public void testGetTaxDeductions_Success() throws Exception {
        TaxResponse taxResponse = new TaxResponse("E123", "John", "Doe", 600000, 37500, 6000);

        Mockito.when(employeeService.calculateTaxDeductions("E123")).thenReturn(taxResponse);

        mockMvc.perform(get("/api/employees/E123/tax-deductions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("E123"))
                .andExpect(jsonPath("$.taxAmount").value(37500))
                .andExpect(jsonPath("$.cessAmount").value(6000));
    }
}

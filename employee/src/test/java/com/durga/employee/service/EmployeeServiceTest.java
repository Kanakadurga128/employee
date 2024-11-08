package com.durga.employee.service;

import com.durga.employee.dto.TaxResponse;
import com.durga.employee.entity.Employee;
import com.durga.employee.exception.CustomException;
import com.durga.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void testSaveEmployee_Success() {
        Employee employee = new Employee("E123", "John", "Doe", "john.doe@example.com",
                List.of("1234567890"), LocalDate.of(2023, 5, 16), 50000.0);

        Mockito.when(employeeRepository.existsById("E123")).thenReturn(false);
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        assertDoesNotThrow(() -> employeeService.saveEmployee(employee));
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void testSaveEmployee_ThrowsCustomException_WhenEmployeeExists() {
        Employee employee = new Employee("E123", "John", "Doe", "john.doe@example.com",
                List.of("1234567890"), LocalDate.of(2023, 5, 16), 50000.0);

        Mockito.when(employeeRepository.existsById("E123")).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> employeeService.saveEmployee(employee));
        assertEquals("Employee ID already exists", exception.getMessage());
    }

    @Test
    public void testCalculateTaxDeductions_Success() {
        Employee employee = new Employee("E123", "John", "Doe", "john.doe@example.com",
                List.of("1234567890"), LocalDate.of(2023, 5, 16), 50000.0);

        Mockito.when(employeeRepository.findById("E123")).thenReturn(Optional.of(employee));

        TaxResponse taxResponse = employeeService.calculateTaxDeductions("E123");
        assertNotNull(taxResponse);
        assertEquals("E123", taxResponse.getEmployeeId());

    }
}

package com.csi.controller;

import com.csi.exception.RecordNotFoundException;
import com.csi.model.Employee;
import com.csi.service.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
public class EmployeeController {

    @Autowired

    private EmployeeServiceImpl employeeServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<Employee> signUp(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeServiceImpl.signUp(employee));
    }

    @GetMapping("/signin/{emailid}/{pwd}")
    public ResponseEntity<Boolean> signIn(@PathVariable String emailid, @PathVariable String pwd) {
        return ResponseEntity.ok(employeeServiceImpl.signIn(emailid, pwd));
    }

    @PostMapping("/saveall")
    public ResponseEntity<List<Employee>> saveAll(@RequestBody List<Employee> employeeList) {

        return ResponseEntity.ok(employeeServiceImpl.saveAll(employeeList));
    }

    @GetMapping("/findbyid/{empId}")
    public ResponseEntity<Optional<Employee>> findById(@PathVariable int empId) {
        return ResponseEntity.ok(employeeServiceImpl.findById(empId));
    }

    @GetMapping("/findbyname/{empName}")
    public ResponseEntity<List<Employee>> findByName(@PathVariable String empName) {
        return ResponseEntity.ok(employeeServiceImpl.findByName(empName));
    }

    @GetMapping("/findbycontactno/{empContactNo}")
    public ResponseEntity<Employee> findByContactNo(@PathVariable long empContactNo) {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().filter(emp -> emp.getEmpContactNo() == empContactNo).toList().get(0));
    }

    @GetMapping("/findbyemailid/{empEmailId}")
    public ResponseEntity<Employee> findByEmailId(@PathVariable String empEmailId) {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().filter(emp -> emp.getEmpEmailId().equals(empEmailId)).toList().get(0));
    }

    @GetMapping("/findbydob/{empDOB}")
    public ResponseEntity<List<Employee>> findByEmpDOB(@PathVariable String empDOB) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().filter(emp -> simpleDateFormat.format(emp.getEmpDOB()).equals(empDOB)).toList());

    }

    @GetMapping("/findbysalary/{empSalary}")
    public ResponseEntity<List<Employee>> findBySalary(@PathVariable double empSalary) {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().filter(emp -> emp.getEmpSalary() == empSalary).toList());
    }

    @GetMapping("/filterbysalary/{salary}")
    public ResponseEntity<List<Employee>> filterBySalary(@PathVariable double salary) {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().filter(emp -> emp.getEmpSalary() >= salary).toList());
    }

    @GetMapping("/findbyanyinput/{input}")
    public ResponseEntity<List<Employee>> findByAnyInput(@PathVariable String input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().filter(emp -> simpleDateFormat.format(emp.getEmpDOB()).equals(input)
                || String.valueOf(emp.getEmpId()) == input
                || String.valueOf(emp.getEmpSalary()) == input
                || emp.getEmpName().equals(input)
                || String.valueOf(emp.getEmpContactNo()).equals(input)).toList());

    }


    @GetMapping("/findall")
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeServiceImpl.findAll());
    }

    @GetMapping("/sortbyid")
    public ResponseEntity<List<Employee>> sortById() {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().sorted(Comparator.comparingInt(Employee::getEmpId)).toList());
    }

    @GetMapping("/sortbyname")
    public ResponseEntity<List<Employee>> sortByName() {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().sorted(Comparator.comparing(Employee::getEmpName)).toList());
    }

    @GetMapping("/sortbysalary")
    public ResponseEntity<List<Employee>> sortBySalary() {
        return ResponseEntity.ok(employeeServiceImpl.findAll().stream().sorted(Comparator.comparingDouble(Employee::getEmpSalary)).toList());
    }

    @GetMapping("/loaneligibility/{empId}")
    public ResponseEntity<String> checkLoanEligibility(@PathVariable int empId) {
        String msg = " ";

        boolean flag = false;

        for (Employee employee : employeeServiceImpl.findAll()) {
            if (employee.getEmpId() == empId && employee.getEmpSalary() >= 50000) {
                msg = "Eligible for loan";
                flag = true;
                break;

            }
        }

        if (!flag) {
            msg = "Not Eligible for loan";
        }

        return ResponseEntity.ok(msg);

    }


    @PutMapping("/update/{empId}")
    public ResponseEntity<Employee> update(@PathVariable int empId, @RequestBody Employee employee) {
        Employee employee1 = employeeServiceImpl.findById(empId).orElseThrow(() -> new RecordNotFoundException("Employee Id does not exist..."));

        employee1.setEmpPassword(employee.getEmpPassword());
        employee1.setEmpDOB(employee.getEmpDOB());
        employee1.setEmpEmailId(employee.getEmpEmailId());
        employee1.setEmpContactNo(employee.getEmpContactNo());
        employee1.setEmpName(employee.getEmpName());
        employee1.setEmpSalary(employee.getEmpSalary());
        employee1.setEmpAddress(employee.getEmpAddress());

        return ResponseEntity.ok(employeeServiceImpl.update(employee1));

    }

    @DeleteMapping("/deletebyid/{empId}")
    public ResponseEntity<String> deleteById(@PathVariable int empId) {
        employeeServiceImpl.deleteById(empId);

        return ResponseEntity.ok("Data Deleted Successfully");
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAll() {
        employeeServiceImpl.deleteAll();

        return ResponseEntity.ok("All data deleted Successfully");
    }


}

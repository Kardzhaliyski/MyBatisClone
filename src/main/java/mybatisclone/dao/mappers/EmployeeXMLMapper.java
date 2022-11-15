package mybatisclone.dao.mappers;

import mybatisclone.models.Employee;

import java.util.List;

public interface EmployeeXMLMapper extends EmployeeMapper{
    Employee getEmployeeById(int id);
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByJob(int jobId);
    int addEmployee(Employee e);
    int deleteEmployee(int id);
    int updateEmployee(Employee e);
    int updateEmployeeSalaries(float percent, float minSalary);
    Long employeeCount();
}

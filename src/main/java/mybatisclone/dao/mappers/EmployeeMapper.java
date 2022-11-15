package mybatisclone.dao.mappers;


import mybatisclone.models.Employee;

import java.util.List;

public interface EmployeeMapper {
    public Employee getEmployeeById(int id);

    public List<Employee> getAllEmployees();

    public List<Employee> getEmployeesByJob(int jobId);

    public int addEmployee(Employee e);

    public int deleteEmployee(int id);

    public int updateEmployee(Employee e);

    public int updateEmployeeSalaries(float percent, float minSalary);

    Long employeeCount();
}

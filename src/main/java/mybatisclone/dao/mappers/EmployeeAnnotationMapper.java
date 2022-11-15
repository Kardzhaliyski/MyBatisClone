package mybatisclone.dao.mappers;


import mybatisclone.annotations.Delete;
import mybatisclone.annotations.Insert;
import mybatisclone.annotations.Select;
import mybatisclone.annotations.Update;
import mybatisclone.models.Department;
import mybatisclone.models.Employee;
import mybatisclone.models.Job;

import java.util.List;

public interface EmployeeAnnotationMapper extends EmployeeMapper {

    @Select("SELECT * FROM employees WHERE employee_id = #{arg0}")
    public Employee getEmployeeById(int id);

    @Select("Select * From employees WHERE job_id = #{job.jobId}")
    public Employee[] getEmployeeByJobId(Employee employee);

//    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "employeeId", before = false, resultType = int.class) //todo
    @Insert("""
            INSERT INTO
              employees(
            	first_name,
            	last_name,
            	email,
            	hire_date,
            	phone_number,
            	salary)
            VALUES
            (#{firstName},#{lastName},#{email},#{hireDate},#{phoneNumber},#{salary})""")
    public int addEmployee(Employee e);

    @Select("SELECT * FROM jobs WHERE job_id = #{id}")
    public Job getJobById(int id);

    @Select("SELECT department_id, department_name AS name FROM departments WHERE department_id = #{id}")
    public Department getDepartment(int id);

    @Select("SELECT Count(*) FROM employees")
    public Long employeeCount();

    @Delete("DELETE FROM employees WHERE employee_id = #{arg0}")
    public int deleteEmployee(int id);

    @Update("""
                UPDATE 
                    employees
                SET
                    first_name = #{firstName},
                    last_name = #{lastName},
                    email = #{email},
                    phone_number = #{phoneNumber},
                    hire_date = #{hireDate},
                    salary = #{salary}
                WHERE
                    employee_id = #{employeeId};
            """)
    public int updateEmployee(Employee employee);

    @Update("UPDATE employees SET salary = salary * #{arg0} WHERE salary <= #{arg1}")
    public int updateEmployeeSalaries(float percent, float minSalary);

    @Select("SELECT * FROM employees")
    public List<Employee> getAllEmployees();

    @Select("SELECT * FROM employees WHERE job_id = #{arg0}")
    public List<Employee> getEmployeesByJob(int jobId);
}

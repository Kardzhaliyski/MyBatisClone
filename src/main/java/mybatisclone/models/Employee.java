package mybatisclone.models;


import java.time.LocalDate;

public class Employee {
    public int employeeId;
    public String firstName;
    public String lastName;
    public String email;
    public LocalDate hireDate;
    public String phoneNumber;
    public Job job;
    public Double salary;
    public Employee manager;
    public Department department;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email,
                    LocalDate hireDate, String phoneNumber, Job job,
                    Double salary, Employee manager, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.job = job;
        this.salary = salary;
        this.manager = manager;
        this.department = department;
    }

    public Employee(Integer employeeId, String name, String phone) {
        this.employeeId = employeeId;
        this.lastName = name;
        this.phoneNumber = phone;
    }

    public Integer getManagerId() {
        return manager == null ? null : manager.employeeId;
    }

    public Integer getDepartmentId() {
        return department == null ? null : department.departmentId;
    }

    public Integer getJobId() {
        return job == null ? null : job.jobId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", hireDate=" + hireDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", job=" + job +
                ", salary=" + salary +
                ", manager=" + manager +
                ", department=" + department +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;

        if (employeeId != employee.employeeId) return false;
        return lastName.equals(employee.lastName);
    }

    @Override
    public int hashCode() {
        int result = employeeId;
        result = 31 * result + lastName.hashCode();
        return result;
    }
}

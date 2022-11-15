package mybatisclone.dao;


import mybatisclone.dao.mappers.EmployeeMapper;
import mybatisclone.models.Employee;
import mybatisclone.session.SqlSession;
import mybatisclone.session.SqlSessionFactory;

import java.util.List;

public class EmployeeDAO {
    Class<? extends EmployeeMapper> mapperClass;
    SqlSessionFactory seasonFactory;

    public EmployeeDAO(Class<? extends EmployeeMapper> mapperClass, SqlSessionFactory seasonFactory) {
        this.mapperClass = mapperClass;
        this.seasonFactory = seasonFactory;
    }

    public Long getEmployeeCount() {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            return mapper.employeeCount();
        }
    }
    public Employee getEmployeeById(int id) {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            return mapper.getEmployeeById(id);
        }
    }

    public List<Employee> getAllEmployees() {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            return mapper.getAllEmployees();
        }
    }

    public List<Employee> getEmployeesByJob(int jobId) {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            return mapper.getEmployeesByJob(jobId);
        }
    }

    public int addEmployee(Employee e) {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            int i = mapper.addEmployee(e);
            sqlSession.commit();
            return i;
        }
    }

    public boolean deleteEmployee(int id) {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            boolean deleted = mapper.deleteEmployee(id) > 0;
            sqlSession.commit();
            return deleted;
        }
    }

    public boolean updateEmployee(Employee e) {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            boolean u = mapper.updateEmployee(e) > 0;
            sqlSession.commit();
            return u;
        }
    }

    public int updateEmployeeSalaries(float percent, float minSalary) {
        try (SqlSession sqlSession = seasonFactory.openSession()) {
            EmployeeMapper mapper = sqlSession.getMapper(mapperClass);
            return mapper.updateEmployeeSalaries(percent, minSalary);
        }
    }


}

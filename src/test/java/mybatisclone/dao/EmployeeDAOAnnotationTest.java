package mybatisclone.dao;

import mybatisclone.dao.mappers.EmployeeAnnotationMapper;
import mybatisclone.models.Employee;
import mybatisclone.session.SqlSessionFactory;
import mybatisclone.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOAnnotationTest {

    EmployeeDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/application.properties"));
        Path path = Path.of("src/main/resources/config/mybatis-config.xml");
        InputStream in = Files.newInputStream(path);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in, properties);
        this.dao = new EmployeeDAO(EmployeeAnnotationMapper.class, sqlSessionFactory);
    }

    @Test
    void getEmployeeByIdReturnNotNullForValidId() {
        assertNotNull(dao.getEmployeeById(200));
    }

    @Test
    void getEmployeeByIdReturnNullForInvalidId() {
        assertNull(dao.getEmployeeById(99999));
    }

    @Test
    void getEmployeeCountShouldReturnCorrectValue() {
        assertEquals(40, dao.getEmployeeCount());
    }

    @Test
    void getAllEmployeesShouldReturnCorrectCountOfEmployees() {
        List<Employee> allEmployees = dao.getAllEmployees();
        long expectedValue = dao.getEmployeeCount();
        long actualValue = allEmployees.size();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getEmployeesByJobReturnCorrectCountOfEmployees() {
        List<Employee> employeesByJob = dao.getEmployeesByJob(6);
        int expectedValue = 5;
        int actualValue = employeesByJob.size();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void addEmployeeIncreaseEmployeeCountByOne() {
        Long oldCount = dao.getEmployeeCount();
        Employee employee = dao.getEmployeeById(200);
        dao.addEmployee(employee);
        Long newCount = dao.getEmployeeCount();
        assertEquals(oldCount + 1, newCount);
    }

    @Test
    void deleteEmployeeShouldReduceTotalEmployeeCountByOne() {
        Long oldCount = dao.getEmployeeCount();
        boolean deleted = dao.deleteEmployee(206);
        assertTrue(deleted);
        Long newCount = dao.getEmployeeCount();
        assertEquals(oldCount - 1, newCount);
    }

    @Test
    void updateEmployeeFirstNameShouldChangeIt() {
        Employee e1 = dao.getEmployeeById(201);
        String oldFirstName = e1.firstName;
        e1.firstName = "FirstName";
        assertTrue(dao.updateEmployee(e1));
        Employee e2 = dao.getEmployeeById(201);
        assertEquals(e2.firstName,e1.firstName);
        assertNotEquals(e2.firstName, oldFirstName);

        e1.firstName = oldFirstName;
        dao.updateEmployee(e1);
    }

}
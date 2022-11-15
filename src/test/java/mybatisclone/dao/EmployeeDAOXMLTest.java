package mybatisclone.dao;

import mybatisclone.dao.mappers.EmployeeAnnotationMapper;
import mybatisclone.models.Employee;
import mybatisclone.session.SqlSession;
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

class EmployeeDAOXMLTest {

    SqlSessionFactory sqlSessionFactory;
    EmployeeDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/application.properties"));
        Path path = Path.of("src/main/resources/config/mybatis-config.xml");
        InputStream in = Files.newInputStream(path);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in, properties);
    }

    @Test
    void getEmployeeByIdReturnNotNullForValidId() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Employee e = session.selectOne("getEmployeeById", 200);
//            Employee e = session.selectOne("getSimpleEmployeeById", 200);
            assertNotNull(e);
        }
    }

    @Test
    void getEmployeeByIdReturnNullForInvalidId() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Employee e = session.selectOne("getSimpleEmployeeById", 9999);
            assertNull(e);
        }
    }

    @Test
    void getEmployeeCountShouldReturnCorrectValue() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Long c = session.selectOne("employeeCount", null);
            assertEquals(42, c);
        }
    }

    @Test
    void getAllEmployeesShouldReturnCorrectCountOfEmployees() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            List<Employee> allEmployees = session.selectList("getAllEmployees", null);
            Long expectedValue = session.selectOne("employeeCount", null);
            long actualValue = allEmployees.size();
            assertEquals(expectedValue, actualValue);
        }
    }

    @Test
    void getEmployeesByJobReturnCorrectCountOfEmployees() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            List<Employee> employeesByJob = session.selectList("getEmployeesByJob", 6);
            int expectedValue = 5;
            int actualValue = employeesByJob.size();
            assertEquals(expectedValue, actualValue);
        }
    }

    @Test
    void addEmployeeIncreaseEmployeeCountByOne() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Long oldCount = session.selectOne("employeeCount", null);
            Employee e = session.selectOne("getSimpleEmployeeById", 200);
            session.insert("addEmployee", e);
            Long newCount = session.selectOne("employeeCount", null);
            assertEquals(oldCount + 1, newCount);
        }
    }

    @Test
    void deleteEmployeeShouldReduceTotalEmployeeCountByOne() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Long oldCount = session.selectOne("employeeCount", null);
            boolean deleted = session.delete("deleteEmployee", 213) == 1;
            assertTrue(deleted);
            Long newCount = session.selectOne("employeeCount", null);
            assertEquals(oldCount - 1, newCount);
        }
    }

    @Test
    void updateEmployeeFirstNameShouldChangeIt() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Employee e1 = session.selectOne("getSimpleEmployeeById", 201);
            String oldFirstName = e1.firstName;
            e1.firstName = "FirstName";
            assertEquals(1, session.update("updateEmployee", e1));
            Employee e2 = session.selectOne("getSimpleEmployeeById", 201);
            assertEquals(e2.firstName, e1.firstName);
            assertNotEquals(e2.firstName, oldFirstName);

            e1.firstName = oldFirstName;
            session.update("updateEmployee", e1);
        }
    }

}
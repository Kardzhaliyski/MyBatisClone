package mybatisclone;

import mybatisclone.dao.EmployeeDAO;
import mybatisclone.dao.mappers.EmployeeAnnotationMapper;
import mybatisclone.models.Employee;
import mybatisclone.session.SqlSession;
import mybatisclone.session.SqlSessionFactory;
import mybatisclone.session.SqlSessionFactoryBuilder;

import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/application.properties"));
        Path path = Path.of("src/main/resources/config/mybatis-config.xml");
        InputStream in = Files.newInputStream(path);

        try (SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in, properties)) {
//            SqlSession sqlSession = sqlSessionFactory.openSession();
//
//        EmployeeAnnotationMapper mapper = sqlSession.getMapper(EmployeeAnnotationMapper.class);
//        Employee[] employeesByJob = mapper.getEmployeesByJob(9);
//        for (Employee employee : employeesByJob) {
//            System.out.println(employee);
//        }

//        Employee employee  = sqlSession.selectOne("getSimpleEmployeeById", 106);
//        System.out.println(employee);
//            List<Employee> employees = sqlSession.selectList("getEmployeesByJob", 9);
//            for (Employee e : employees) {
//                System.out.println(e);
//            }

            EmployeeDAO dao = new EmployeeDAO(EmployeeAnnotationMapper.class, sqlSessionFactory);
            Employee employeeById = dao.getEmployeeById(200);
            System.out.println(employeeById);

        }

    }
}

package mybatisclone.utils;

import mybatisclone.config.Configuration;
import mybatisclone.config.mappers.ParsedMapper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class XMLParser {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
//        Path path = Path.of("src/main/resources/config/mybatis-config.xml");
        Path path = Path.of("src/main/resources/config/EmployeеXMLMapper.xml");
        Document doc = documentBuilder.parse(path.toFile());
//        Configuration configuration = parseElement(Configuration.class, doc.getDocumentElement());
        ParsedMapper mapper = parseElement(ParsedMapper.class, doc.getDocumentElement());
        System.out.println();
    }

    public static <T> T parse(Class<T> clazz, String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        InputStream in = Files.newInputStream(Path.of(path));
        Document doc = documentBuilder.parse(in);
        return parseElement(clazz, doc.getDocumentElement());
    }

    public static Configuration parseConfiguration(String pathString) throws Exception {
        Path path = Path.of(pathString);
        return parseConfiguration(Files.newInputStream(path));
    }
    public static Configuration parseConfiguration(InputStream in) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.parse(in);
        return parseElement(Configuration.class, doc.getDocumentElement());
    }

    private static <T> T[] parseArray(Class<T> clazz, Node element, int[] index) throws Exception {
        NodeList childNodes = element.getChildNodes();
        ArrayList<T> list = new ArrayList<>();

        String firstNodeName = null;
        for (; index[0] < childNodes.getLength(); index[0]++) {
            Node item = childNodes.item(index[0]);
            if (!(item instanceof Element)) {
                continue;
            }

            if (firstNodeName == null) {
                firstNodeName = item.getNodeName();
            }

            if (!item.getNodeName().equals(firstNodeName)) {
                index[0]--;
                break;
            }

            T o = parseElement(clazz, item);
            list.add(o);
        }

        if (list.isEmpty()) {
            return null;
        }

        T[] arr = (T[]) Array.newInstance(clazz, list.size());
        return list.toArray(arr);
    }

    private static <T> T parseElement(Class<T> clazz, Node node) throws Exception {
        T instance = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getFields();
        Field sqlField = getField(fields, "sql");
        if(sqlField != null) {
            sqlField.set(instance, node.getTextContent());
        }

        setAttributes(fields, node, instance);
        setElements(fields, node, instance);
        return instance;
    }

    private static <T> void setElements(Field[] fields, Node node, T instance) throws Exception {

        NodeList childNodes = node.getChildNodes();
        int[] index = new int[1];
        for (; index[0] < childNodes.getLength(); index[0]++) {
            Node fieldData = childNodes.item(index[0]);
            if (!(fieldData instanceof Element)) {
                    continue;
            }

            String fieldName = fieldData.getNodeName();
            Field field = getField(fields, fieldName);
            if (field == null) {
                continue;
            }

            Class<?> fieldType = field.getType();
            Object fieldValue;
            if (fieldType.isArray()) {
                fieldValue = parseArray(fieldType.getComponentType(), node, index);
            } else {
                fieldValue = parseValue(fieldType, fieldData);
            }

            field.set(instance, fieldValue);
        }
    }

    private static Field getField(Field[] fields, String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        return null;
    }

    private static <T> void setAttributes(Field[] fields, Node node, T instance) throws Exception {
        NamedNodeMap attributes = node.getAttributes();
        // getFields
        for (int i = 0; i < attributes.getLength(); i++) {
            Node fieldData = attributes.item(i);
            String fieldName = fieldData.getNodeName();

            if (fieldName.equals("default")) {
                fieldName = "_default";
            } else if (fieldName.equals("class")) {
                fieldName = "_class";
            }

            Field field = getField(fields, fieldName);
            if (field == null) {
                continue;
            }

            Object fieldValue = parseValue(field.getType(), fieldData);
            field.set(instance, fieldValue);
        }
    }

    private static Object parseValue(Class<?> type, Node value) throws Exception {
        if (int.class == type || Integer.class == type) {
            return Integer.parseInt(value.getTextContent());
        }

        if (double.class == type || Double.class.equals(type)) {
            return Double.parseDouble(value.getTextContent());
        }

        if (long.class.equals(type) || Long.class.equals(type)) {
            return Long.parseLong(value.getTextContent());
        }

        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return Boolean.parseBoolean(value.getTextContent());
        }

        if (String.class.equals(type)) {
            return value.getTextContent();
        }

        return parseElement(type, value);
    }
}

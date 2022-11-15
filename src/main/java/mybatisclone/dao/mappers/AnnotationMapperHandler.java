package mybatisclone.dao.mappers;

import mybatisclone.annotations.*;
import mybatisclone.dao.GenericDao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class AnnotationMapperHandler implements InvocationHandler {
    GenericDao dao;

    public AnnotationMapperHandler(GenericDao dao) {
        this.dao = dao;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation[] annotations = method.getAnnotations();
        if (annotations.length == 0) {
            return method.invoke(args);
        }

        Annotation annotation = annotations[0];
        Class<? extends Annotation> annotationType = annotation.annotationType();

        Object params = null;
        if (args != null && args.length > 0) {
            params = args[0];
        }
        if (annotationType.equals(Select.class)) {
            Select ann = (Select) annotation;
            String sql = ann.value();
            Class<?> returnType = method.getReturnType();
            if (returnType.isArray()) {
                return dao.selectList(sql, returnType.getComponentType(), params);
            } else if (List.class.isAssignableFrom(returnType)) {
                ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                Class<?> actualTypeArgument = (Class<?>) type.getActualTypeArguments()[0];
                return dao.selectList(sql, actualTypeArgument, params);
            } else {
                return dao.selectObject(sql, returnType, params);
            }
        } else if (annotationType.equals(Update.class)) {
            Update ann = (Update) annotation;
            String sql = ann.value();
            return dao.update(sql, params);
        } else if (annotationType.equals(Insert.class)) {
            Insert ann = (Insert) annotation;
            String sql = ann.value();
            return dao.insert(sql, params);
        } else if (annotationType.equals(Delete.class)) {
            Delete ann = (Delete) annotation;
            String sql = ann.value();
            return dao.delete(sql, params);
        }

        return method.invoke(args);
    }
}

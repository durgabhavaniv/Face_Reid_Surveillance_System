package com.honeygo.app.hackster.util;

import com.honeygo.app.hackster.model.NodeData;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.neo4j.driver.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Service
public class RecordToPojoMapperUtil<T> {


    public T valueToPojoMapper(Value value, T dataObj)  {
        Field[] fields = dataObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                PropertyUtils.setProperty(dataObj, field.getName(), value.get(field.getName()).asString());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return dataObj;
    }
}

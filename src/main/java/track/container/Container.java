package track.container;

import java.lang.reflect.Field;
import java.util.*;

import track.container.config.Bean;
import track.container.config.Property;
import track.container.config.ValueType;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {
    HashMap<String, Object> idMap;
    List<Bean> beans;

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        this.beans = beans;
        idMap = new HashMap<String, Object>();
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) throws ReflectiveOperationException, NoSuchElementException {
        if (idMap.containsKey(id)) {
            return idMap.get(id);
        } else {

            // Ищем запрашиваемый кдасс среди бинов. Если не находим, выбрасывается NoSuchElementException
            Bean bean;
            Iterator<Bean> iterator = beans.iterator();
            do {
                bean = iterator.next();
            } while (!bean.getId().equals(id));

            // Создаем объект класса и вносим его в обе мапы
            Class clazz = Class.forName(bean.getClassName());
            Object obj = clazz.newInstance();
            idMap.put(bean.getId(), obj);

            // Пробегаемся по всем характеристикам (properties) и устанавливаем нужные значения
            // для соответствующих полей создаваемого класса
            Map<String, Property> properties = bean.getProperties();
            for (Map.Entry<String, Property> entry : properties.entrySet()) {
                Property property = entry.getValue();
                Field field = clazz.getDeclaredField(property.getName());
                field.setAccessible(true);
                if (property.getType() == ValueType.VAL) {
                    try {
                        field.setInt(obj, Integer.parseInt(property.getValue()));
                    } catch (Exception ex) {
                        System.out.println("Невозможно создать объект с таким полем");
                    }
                } else {
                    field.set(obj, getById(property.getValue()));
                }
            }
            return obj;
        }
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) throws ReflectiveOperationException, NoSuchElementException {
        // Ищем запрашиваемый кдасс среди бинов. Если не находим, выбрасывается NoSuchElementException
        Bean bean;
        Iterator<Bean> iterator = beans.iterator();
        do {
            bean = iterator.next();
        } while (!bean.getClassName().equals(className));

        return  getById(bean.getId());
    }
}

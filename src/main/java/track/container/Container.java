package track.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Iterator;
import track.container.config.Bean;
import track.container.config.Property;
import track.container.config.ValueType;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {

    private Map<String, Object> map = new HashMap<>();
    private List<Bean> beans;

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) throws Exception {
        this.beans = beans;
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) throws ReflectiveOperationException, NoSuchElementException {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {

            // Ищем запрашиваемый кдасс среди бинов. Если не находим, выбрасывается NoSuchElementException
            Bean bean;
            Iterator<Bean> iterator = beans.iterator();
            do {
                bean = iterator.next();
            } while (!bean.getId().equals(id));

            // Создаем объект класса и вносим его в мап
            Class clazz = Class.forName(bean.getClassName());
            Object obj = clazz.newInstance();
            map.put(bean.getId(), obj);

            // Пробегаемся по всем характеристикам (properties) и устанавливаем нужные значения
            // для соответствующих полей создаваемого класса
            Map<String, Property> properties = bean.getProperties();
            for (Map.Entry<String, Property> entry : properties.entrySet()) {
                Property property = entry.getValue();
                Field field = clazz.getDeclaredField(property.getName());
                field.setAccessible(true);
                // Обработка примитивных типов
                if (property.getType() == ValueType.VAL) {
                    switch (field.getType().toString()) {
                        case "int" : field.setInt(obj, Integer.parseInt(property.getValue()));
                        break;
                        case "byte" : field.setByte(obj, Byte.parseByte(property.getValue()));
                        break;
                        case "short" : field.setShort(obj, Short.parseShort(property.getValue()));
                        break;
                        case "long" : field.setLong(obj, Long.parseLong(property.getValue()));
                        break;
                        case "float" : field.setFloat(obj, Float.parseFloat(property.getValue()));
                        break;
                        case "double" : field.setDouble(obj, Double.parseDouble(property.getValue()));
                        break;
                        case "char" : field.setChar(obj, property.getValue().charAt(0));
                        break;
                        case "string" : field.set(obj, property.getValue());
                        break;
                        default:
                            System.out.println("Неверное имя примитивного типа");
                    }
                    // Обработка ссылочных типов
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

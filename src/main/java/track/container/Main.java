package track.container;

import com.fasterxml.jackson.databind.ObjectMapper;
import track.container.beans.Car;
import track.container.beans.Engine;
import track.container.beans.Gear;
import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;
import track.container.config.Root;
import track.lections.lection4.Item;
import track.lections.lection4.Person;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> beans;
        try {
            beans = reader.parseBeans(new File("src/main/resources/config.json"));
            Container container = new Container(beans);
            Car car = (Car) container.getByClass("track.container.beans.Car");
            car = (Car) container.getById("carBean");
            System.out.print("gear_count = " + car.getGear().getCount());
            System.out.println(", engine_power = " + car.getEngine().getPower());

        } catch (ReflectiveOperationException ex) {
            System.out.println("Проблемы с рефлекшн");
        } catch (InvalidConfigurationException ex) {
            System.out.println("Ошибка чтения конфигурационного файла");
        } catch (NoSuchElementException ex) {
            System.out.println("Нет такого бина");;
        } catch (ClassCastException ex) {
            System.out.println("Неправильное приведение классов");
        }
        /*

        ПРИМЕР ИСПОЛЬЗОВАНИЯ

         */

//        // При чтении нужно обработать исключение
//        ConfigReader reader = new JsonReader();
//        List<Bean> beans = reader.parseBeans("config.json");
//        Container container = new Container(beans);
//
//        Car car = (Car) container.getByClass("track.container.beans.Car");
//        car = (Car) container.getById("carBean");


    }
}

package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {
    private int[] array;
    static final int DEFAULT_CAPACITY = 100;
    static final int EXPANSION_MULTIPLIER = 2;
    private int size; // Индекс хвоста: указывает на следующую после последней заполненной ячейки
                    // и задает фактический размер массива


    public MyArrayList() {
        array = new int[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyArrayList(int capacity) {
        if (capacity < 1) {
            capacity = 1;
            System.out.println("Размер не может быть меньше 1. Создан массив размером 1.");
        }
        array = new int[capacity];
        size = 0;
    }

    @Override
    void add(int item) {
        if (size == array.length) {
            int[] arrayTemp = new int[array.length * EXPANSION_MULTIPLIER];
            System.arraycopy(array, 0, arrayTemp,0 , array.length);
            array = arrayTemp;
        }
        array[size++] = item;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= size || idx < 0) {
            throw new NoSuchElementException();
        } else {
            int item = array[idx];
            System.arraycopy(array, idx + 1, array, idx , --size - idx);
            return item;
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= size || idx < 0) {
            throw new NoSuchElementException();
        } else {
            return array[idx];
        }
    }

    @Override
    int size() {
        return size;
    }

}

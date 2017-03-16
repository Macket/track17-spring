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
    public static final int startLen = 100;
    private int tail; // Индекс хвоста: указывает на следующую после последней заполненной ячейки
                    // и задает фактическую длину массива
    private int len; // Длина, доступная для зополнения без расширения массива

    public MyArrayList() {
        array = new int[startLen];
        len = startLen;
        tail = 0;
    }

    public MyArrayList(int capacity) {
        if (capacity < 1) {
            capacity = 1;
            System.out.println("Размер не может быть меньше 1. Создан массив размером 1.");
        }
        array = new int[capacity];
        len = capacity;
        tail = 0;
    }

    @Override
    void add(int item) {
        if (tail == len) {
            int[] arrayTemp = new int[len + startLen];
            System.arraycopy(array, 0, arrayTemp,0 , len);
            array = arrayTemp;
            len += startLen;
        }
        array[tail++] = item;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= tail || idx < 0) {
            throw new NoSuchElementException();
        } else {
            int item = array[idx];
            int[] arrayTemp = new int[len];
            System.arraycopy(array, 0, arrayTemp,0 , idx);
            System.arraycopy(array, idx + 1, arrayTemp,idx , --tail - idx);
            array = arrayTemp;
            return array[idx];
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= tail || idx < 0) {
            throw new NoSuchElementException();
        } else {
            return array[idx];
        }
    }

    @Override
    int size() {
        return tail;
    }

}

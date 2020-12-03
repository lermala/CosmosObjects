package sample.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CosmosModel {
    private ArrayList<CosmosObject> cosmosObjectsList = new ArrayList<>();
    private int counter = 1; // добавили счетчик

    // поле фильтр, по умолчанию используем базовый класс
    Class<? extends CosmosObject> cosmosFilter = CosmosObject.class;

    // Создаем наш любимый функциональный интерфейс
    // с помощью него мы организуем общение между моделью и контроллером
    public interface DataChangedListener {
        void dataChanged(ArrayList<CosmosObject> cosmosObjectsList);
    }

    private ArrayList<DataChangedListener> dataChangedListeners = new ArrayList<>();
    // добавляем метод который позволяет привязать слушателя
    public void addDataChangedListener(DataChangedListener listener) {
        // ну просто его в список добавляем
        this.dataChangedListeners.add(listener);
    }

    public void load(){
        cosmosObjectsList.clear();

        // заполняем список данными
        this.add(new Planet(100000123, 181818,
                true,9.11), false);
        this.add(new Comet(12121212, 200,
                "Comet1"), false);
        this.add(new Star(44444444.34, 52,
                Star.Colour.orange, 555), false);

        this.emitDataChanged();
    }

    public void add(CosmosObject cosmosObject, boolean emit){
        cosmosObject.id = counter;// присваиваем id еды, значение счетчика
        counter += 1; // увеличиваем счетчик на единицу

        this.cosmosObjectsList.add(cosmosObject);

        // оповестили всех слушателей что данные изменились
        if (emit)
            this.emitDataChanged();
    }

    public void add(CosmosObject cosmosObject){
        add(cosmosObject, true);
    }

    public void edit(CosmosObject cosmosObject){
        // ищем объект в списке
        for (int i = 0; i< this.cosmosObjectsList.size(); ++i) {
            // чтобы id совпадал с id переданным форме
            if (this.cosmosObjectsList.get(i).id == cosmosObject.id) {
                // если нашли, то подменяем объект
                this.cosmosObjectsList.set(i, cosmosObject);
                break;
            }
        }

        this.emitDataChanged();
    }

    public void delete(int id){
        for (int i = 0; i < this.cosmosObjectsList.size(); ++i){
            if (this.cosmosObjectsList.get(i).id == id){
                // если нашли то удаляем
                this.cosmosObjectsList.remove(i);
                break;
            }
        }

        // оповещаем об изменениях
        this.emitDataChanged();
    }

    public void saveToFile(String path){
        // открываем файл для чтения
        try (Writer writer =  new FileWriter(path)) {
            // создаем сериализатор
            ObjectMapper mapper = new ObjectMapper();
            // записываем данные списка cosmosObjectsList в файл
            mapper.writerFor(new TypeReference<ArrayList<CosmosObject>>() { }) // указали какой тип подсовываем
                    .withDefaultPrettyPrinter() // кстати эта строчка чтобы в файлике все красиво печаталось
                    .writeValue(writer, cosmosObjectsList); // а это непосредственно запись
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String path) {
        try (Reader reader =  new FileReader(path)) {
            // создаем сериализатор
            ObjectMapper mapper = new ObjectMapper();

            // читаем из файла
            cosmosObjectsList = mapper.readerFor(new TypeReference<ArrayList<CosmosObject>>() { })
                    .readValue(reader);

            // рассчитываем счетчик как максимальное значение id плюс 1
            this.counter = cosmosObjectsList.stream()
                    .map(cosmosObject -> cosmosObject.id)
                    .max(Integer::compareTo)
                    .orElse(0) + 1;

        } catch (IOException e) {
            e.printStackTrace();
        }

        // оповещаем что данные загрузились
        this.emitDataChanged();
    }

    public void setCosmosFilter(Class<? extends CosmosObject> cosmosObject) {
        this.cosmosFilter = cosmosObject;

        this.emitDataChanged();
    }

    /**
     * оповестили всех слушателей что данные изменились
     */
    private void emitDataChanged() {
        for (DataChangedListener listener : dataChangedListeners) {
            ArrayList<CosmosObject> filteredList = new ArrayList<>(
                    cosmosObjectsList.stream() // запускаем стрим
                            .filter(object -> cosmosFilter.isInstance(object)) // фильтруем по типу
                            .collect(Collectors.toList()) // превращаем в список
            );
            listener.dataChanged(filteredList); // подсовывам сюда список
        }
    }
}

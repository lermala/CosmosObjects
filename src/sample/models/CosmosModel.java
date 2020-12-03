package sample.models;

import java.util.ArrayList;

public class CosmosModel {
    private ArrayList<CosmosObject> cosmosObjectsList = new ArrayList<>();
    private int counter = 1; // добавили счетчик

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

    // добавили параметр emit в метод,
    // если там true то вызывается оповещение слушателей
    public void add(CosmosObject cosmosObject, boolean emit){
        cosmosObject.id = counter;// присваиваем id еды, значение счетчика
        counter += 1; // увеличиваем счетчик на единицу

        this.cosmosObjectsList.add(cosmosObject);

        // оповестили всех слушателей что данные изменились
        if (emit)
            this.emitDataChanged();
    }

    // а это получается перегруженный метод, но с одним параметром
// который вызывает add с двумя параметрами,
// передавая в качестве второго параметра true
// то есть вызывая add с одним параметром будет происходит оповещение
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

    /**
     * оповестили всех слушателей что данные изменились
     */
    private void emitDataChanged() {
        for (DataChangedListener listener: dataChangedListeners) {
            listener.dataChanged(cosmosObjectsList);
        }
    }
}

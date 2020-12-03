package sample.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.models.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    public TableView mainTable;
    public ComboBox cmbObjectType;

    ObservableList<Class<? extends CosmosObject>> objectTypes = FXCollections.observableArrayList(
            CosmosObject.class,
            Planet.class,
            Comet.class,
            Star.class
    );

    // создали экземпляр класса модели
    CosmosModel cosmosModel = new CosmosModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cosmosModel.addDataChangedListener(objects -> {
            mainTable.setItems(FXCollections.observableArrayList(objects));
        });

        // создаем столбец, указываем что столбец преобразует CosmosObject в String,
        // указываем заголовок колонки как "Название"
        TableColumn<CosmosObject, String> distanceFromTheGroundColumn = new TableColumn<>("Удаленность от земли");
        // указываем какое поле брать у модели CosmosObject
        distanceFromTheGroundColumn.setCellValueFactory(new PropertyValueFactory<>("distanceFromTheGround"));

        // добавляем столбец с описанием
        TableColumn<CosmosObject, String> descriptionColumn = new TableColumn<>("Описание");
        descriptionColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

        // подцепляем столбцы к таблице
        mainTable.getColumns().addAll(distanceFromTheGroundColumn, descriptionColumn);

        // привязали список
        cmbObjectType.setItems(objectTypes);
        // выбрали первый элемент в списке
        cmbObjectType.getSelectionModel().select(0);

        // переопределил метод преобразования имени класса в текст
        cmbObjectType.setConverter(new StringConverter<Class>() {
            @Override
            public String toString(Class object) {
                // просто перебираем тут все возможные варианты
                if (CosmosObject.class.equals(object)) {
                    return "Все";
                } else if (Planet.class.equals(object)) {
                    return "Планета";
                } else if (Star.class.equals(object)) {
                    return "Звезда";
                } else if (Comet.class.equals(object)) {
                    return "Комета";
                }
                return null;
            }

            @Override
            public Class fromString(String string) {
                return null;
            }
        });

        cmbObjectType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.cosmosModel.setCosmosFilter((Class<? extends CosmosObject>) newValue);
        });
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CosmosObjectForm.fxml"));
        Parent root = loader.load();

        // ну а тут создаем новое окно
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        // указываем что оно должно блокировать главное окно
        // ну если точнее, то окно, на котором мы нажали на кнопку
        stage.initOwner(this.mainTable.getScene().getWindow());

        // сначала берем контроллер
        CosmosObjectFormController controller = loader.getController();
        // передаем модель
        controller.cosmosModel = cosmosModel;

        // показываем форму
        stage.showAndWait();

        /*
        // вытаскиваем контроллер который привязан к форме
        CosmosObjectFormController controller = loader.getController();
        // проверяем что нажали кнопку save
        if (controller.getModalResult()) {
            // собираем data с формы
            CosmosObject newObj = controller.getCosmosObject();
            // добавляем в список
            this.cosmosObjectsList.add(newObj);
        }*/
    }

    public void onEditClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CosmosObjectForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.mainTable.getScene().getWindow());

        // передаем выбранный элемент
        CosmosObjectFormController controller = loader.getController();
        controller.setCosmosObject((CosmosObject) this.mainTable.getSelectionModel().getSelectedItem());
        controller.cosmosModel = cosmosModel; // передаем модель в контроллер

        stage.showAndWait();

       /* // если нажали кнопку сохранить
        if (controller.getModalResult()) {
            // узнаем индекс выбранной в таблице строки
            int index = this.mainTable.getSelectionModel().getSelectedIndex();
            // подменяем строку в таблице данными на форме
            this.mainTable.getItems().set(index, controller.getCosmosObject());
        }*/
    }

    public void onDeleteClick(ActionEvent actionEvent) {
        // берем выбранный на форме объект
        CosmosObject object = (CosmosObject) this.mainTable.getSelectionModel().getSelectedItem();

        // выдаем подтверждающее сообщение
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Точно удалить?");

        // если пользователь нажал OK
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            cosmosModel.delete(object.id); // тут вызываем метод модели, и передаем идентификатор
        }
    }

    public void onSaveToFileClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить данные");
        fileChooser.setInitialDirectory(new File("."));


        // тут вызываем диалог для сохранения файла
        File file = fileChooser.showSaveDialog(mainTable.getScene().getWindow());

        if (file != null) {
            cosmosModel.saveToFile(file.getPath());
        }
    }

    public void onLoadToFileClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить данные");
        fileChooser.setInitialDirectory(new File("."));

        // а тут диалог для открытия файла
        File file = fileChooser.showOpenDialog(mainTable.getScene().getWindow());

        if (file != null) {
            cosmosModel.loadFromFile(file.getPath());
        }
    }
}

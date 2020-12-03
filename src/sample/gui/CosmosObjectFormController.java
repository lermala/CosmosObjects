package sample.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.models.Comet;
import sample.models.CosmosObject;
import sample.models.Planet;
import sample.models.Star;

import java.net.URL;
import java.util.ResourceBundle;

public class CosmosObjectFormController implements Initializable {

    public ChoiceBox cmbCosmosObjType;
    public TextField txtDistance;

    public VBox planetPane;
    public TextField txtPlanetRadius;
    public TextField txtPlanetForceOfGravity;
    public CheckBox chkPlanetAtmosphere;

    public VBox cometPane;
    public TextField txtCometPeriod;
    public TextField txtCometName;

    public VBox starPane;
    public TextField txtStarDensity;
    public TextField txtStarTemperature;
    public ChoiceBox cmbStarColour;

    final String COSMOS_PLANET = "Планета";
    final String COSMOS_COMET = "Комета";
    final String COSMOS_STAR = "Звезда";

    // добавляем новое поле
    private Boolean modalResult = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCosmosObjType.setItems(FXCollections.observableArrayList(
                COSMOS_PLANET,
                COSMOS_COMET,
                COSMOS_STAR
        ));

        // отображаем панель в соответствии с выбранным типом
        cmbCosmosObjType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updatePanes((String) newValue);
        });

        // 1) добавляем все типа цвета звезд в комобобокс
        cmbStarColour.getItems().setAll(
                Star.Colour.orange,
                Star.Colour.red,
                Star.Colour.white,
                Star.Colour.yellow
        );

        // 2) и используем метод setConverter,
        // чтобы типы объекты рендерились как строки
        cmbStarColour.setConverter(new StringConverter<Star.Colour>() {
            @Override
            public String toString(Star.Colour object) {
                // просто указываем как рендерить
                switch (object) {
                    case orange:
                        return "Оранжевая";
                    case red:
                        return "Красная";
                    case white:
                        return "Белая";
                    case yellow:
                        return "Жёлтая";
                }
                return null;
            }

            @Override
            public Star.Colour fromString(String string){
                // этот метод не трогаем так как наш комбобкос имеет фиксированный набор элементов
                return null;
            }
        });


        // вызываю новую функцию при инициализации формы
        updatePanes("");
    }

    /**
     * Обновление панели в соответствии с выбранным типом космич объекта
     * также отключаем учет размеров остальных панелек при расчете размеров формы.
     * @param value выбранное значение (тип космического объекта)
     */
    public void updatePanes(String value) {
        this.planetPane.setVisible(value.equals(COSMOS_PLANET));
        this.planetPane.setManaged(value.equals(COSMOS_PLANET));
        this.cometPane.setVisible(value.equals(COSMOS_COMET));
        this.cometPane.setManaged(value.equals(COSMOS_COMET));
        this.starPane.setVisible(value.equals(COSMOS_STAR));
        this.starPane.setManaged(value.equals(COSMOS_STAR));
    }

    // обработчик нажатия на кнопку Сохранить
    public void onSaveClick(ActionEvent actionEvent) {
        this.modalResult = true; // ставим результат модального окна на true
        // закрываем окно к которому привязана кнопка
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    // обработчик нажатия на кнопку Отменить
    public void onCancelClick(ActionEvent actionEvent) {
        this.modalResult = false; // ставим результат модального окна на false
        // закрываем окно к которому привязана кнопка
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    // геттер для результата модального окна
    public Boolean getModalResult() {
        return modalResult;
    }

    public CosmosObject getCosmosObject(){
        CosmosObject result = null;
        double distanceFromTheGround = Double.parseDouble(this.txtDistance.getText());

        switch ((String) this.cmbCosmosObjType.getValue()){
            case COSMOS_PLANET:
                double radius = Double.parseDouble(this.txtPlanetRadius.getText());
                double force = Double.parseDouble(this.txtPlanetForceOfGravity.getText());
                boolean isAtmo = chkPlanetAtmosphere.isSelected();
                result = new Planet(distanceFromTheGround, radius, isAtmo, force);
                break;
            case COSMOS_COMET:
                String name = this.txtCometName.getText();
                int period = Integer.parseInt(this.txtCometPeriod.getText());
                result = new Comet(distanceFromTheGround, period, name);
                break;
            case COSMOS_STAR:
                double density = Double.parseDouble(this.txtStarDensity.getText());
                double temperature = Double.parseDouble(this.txtStarTemperature.getText());
                result = new Star(distanceFromTheGround, density,
                        (Star.Colour)this.cmbStarColour.getValue(), temperature);
                break;
        }
        return result;
    }

    public void setCosmosObject(CosmosObject cosmosObject){
        // делаем так что если объект редактируется, то нельзя переключать тип
        this.cmbCosmosObjType.setDisable(cosmosObject != null);
        if (cosmosObject != null){
            // ну а тут стандартное заполнение полей в соответствии с переданным объектом
            this.txtDistance.setText(String.valueOf(cosmosObject.getDistanceFromTheGround()));

            if (cosmosObject instanceof Planet){ //если планета
                cmbCosmosObjType.setValue(COSMOS_PLANET);
                txtPlanetRadius.setText(String.valueOf(((Planet) cosmosObject).getRadius()));
                txtPlanetForceOfGravity.setText(String.valueOf(((Planet) cosmosObject).getForceOfGravity()));
                chkPlanetAtmosphere.setSelected(((Planet) cosmosObject).isAtmosphere());
            } else if (cosmosObject instanceof Comet) { // если комета
                cmbCosmosObjType.setValue(COSMOS_COMET);
                txtCometName.setText(((Comet) cosmosObject).getName());
                txtCometPeriod.setText(String.valueOf(((Comet) cosmosObject).getPeriodOfPassageThroughTheSolarSystem()));
            } else if (cosmosObject instanceof Star){
                cmbCosmosObjType.setValue(COSMOS_STAR);
                txtStarTemperature.setText(String.valueOf(((Star) cosmosObject).getTemperature()));
                txtStarDensity.setText(String.valueOf(((Star) cosmosObject).getDensity()));
                cmbStarColour.setValue(((Star) cosmosObject).getColour());
            }
        }
    }
}

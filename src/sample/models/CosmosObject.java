package sample.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties({"description", "atmosphere"}) // указали что свойство description нужно игнорировать
// тут написано что создай свойство @class и пропиши в нем имя класса
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class CosmosObject {
    public Integer id = null;// идентификатор, по умолчанию равен null
    private double distanceFromTheGround; // удалённость от земли

    public CosmosObject(double distanceFromTheGround) {
        this.distanceFromTheGround = distanceFromTheGround;
    }

    public CosmosObject() {
    }

    @Override
    public String toString() {
        return "CosmosObject{" +
                "distanceFromTheGround=" + distanceFromTheGround +
                '}';
    }

    public double getDistanceFromTheGround() {
        return distanceFromTheGround;
    }

    public void setDistanceFromTheGround(double distanceFromTheGround) {
        this.distanceFromTheGround = distanceFromTheGround;
    }

    public String getDescription() {
        return "";
    }

}

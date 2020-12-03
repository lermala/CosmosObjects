package sample.models;

public class CosmosObject {

    private double distanceFromTheGround; // удалённость от земли

    public CosmosObject(double distanceFromTheGround) {
        this.distanceFromTheGround = distanceFromTheGround;
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

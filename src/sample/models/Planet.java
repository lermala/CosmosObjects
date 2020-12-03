package sample.models;

public class Planet extends CosmosObject{
    private double radius; // радиус
    private boolean isAtmosphere; // наличие атмосферы
    private double forceOfGravity; // сила притяжения

    public Planet(double distanceFromTheGround, double radius, boolean isAtmosphere, double forceOfGravity) {
        super(distanceFromTheGround);
        this.radius = radius;
        this.isAtmosphere = isAtmosphere;
        this.forceOfGravity = forceOfGravity;
    }

    public Planet() {
    }

    @Override
    public String getDescription() {
        String isAtmoString = this.isAtmosphere ? "есть" : "отсутствует";
        return String.format("Планета, радиус %f, атомосфера %s, сила притяжения %f",
                radius, isAtmoString, forceOfGravity);
    }

    public double getRadius() {
        return radius;
    }

    public boolean isAtmosphere() {
        return isAtmosphere;
    }

    public double getForceOfGravity() {
        return forceOfGravity;
    }
}

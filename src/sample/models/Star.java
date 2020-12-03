package sample.models;

public class Star extends CosmosObject{
    private double density; // плотность

    public enum Colour {white, yellow, orange, red};
    private Colour colour; // цвет

    private double temperature; // температура

    public Star(double distanceFromTheGround, double density, Colour colour, double temperature) {
        super(distanceFromTheGround);
        this.density = density;
        this.colour = colour;
        this.temperature = temperature;
    }

    @Override
    public String getDescription() {
        String typeString = "";
        switch (this.colour)
        {
            case white:
                typeString = "белая";
                break;
            case yellow:
                typeString = "жёлтая";
                break;
            case orange:
                typeString = "оранжевая";
                break;
            case red:
                typeString = "красная";
                break;
        }
        return String.format("Звезда %s, плотность %f, температура %fК",
                typeString, density,temperature);
    }

    public double getDensity() {
        return density;
    }

    public Colour getColour() {
        return colour;
    }

    public double getTemperature() {
        return temperature;
    }
}

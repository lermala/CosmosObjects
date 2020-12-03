package sample.models;

public class Comet extends CosmosObject{
    private int periodOfPassageThroughTheSolarSystem; // период прохождения через солнечную систему (миллионов лет)
    private String name; // название

    public Comet(double distanceFromTheGround, int periodOfPassageThroughTheSolarSystem, String name) {
        super(distanceFromTheGround);
        this.periodOfPassageThroughTheSolarSystem = periodOfPassageThroughTheSolarSystem;
        this.name = name;
    }

    @Override
    public String getDescription() {
        return String.format("Комета %s, период прохождения через солнечную систему %d",
                name, periodOfPassageThroughTheSolarSystem);
    }

    public int getPeriodOfPassageThroughTheSolarSystem() {
        return periodOfPassageThroughTheSolarSystem;
    }

    public String getName() {
        return name;
    }

    public Comet() {
    }
}

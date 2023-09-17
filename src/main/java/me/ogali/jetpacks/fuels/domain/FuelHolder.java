package me.ogali.jetpacks.fuels.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FuelHolder {

    private final Map<String, Double> fuelTypeAmountMap;

    public FuelHolder() {
        this.fuelTypeAmountMap = new HashMap<>();
    }

    public FuelHolder(Map<String, Double> fuelTypeAmountMap) {
        this.fuelTypeAmountMap = fuelTypeAmountMap;
    }

    public FuelHolder(FuelHolder originalFuelHolder) {
        this.fuelTypeAmountMap = new HashMap<>(originalFuelHolder.fuelTypeAmountMap);
    }

    public void incrementFuelAmount(String abstractFuelId, double amount) {
        fuelTypeAmountMap.compute(abstractFuelId, (key, oldValue) -> (oldValue == null) ? amount : oldValue + amount);
    }

    public boolean consumeAndCheckFuelStatus(double amountToRemove) {
        Optional<String> firstFuel = getFirstFuel();

        if (firstFuel.isEmpty()) return false;

        String fuelToConsume = firstFuel.get();
        double currentAmount = fuelTypeAmountMap.get(fuelToConsume);

        if (currentAmount <= amountToRemove) {
            fuelTypeAmountMap.remove(fuelToConsume);

            return getFirstFuel().isPresent();
        }

        double newFuelAmount = currentAmount - amountToRemove;

        if (newFuelAmount <= 0) {
            fuelTypeAmountMap.remove(fuelToConsume);
        } else {
            fuelTypeAmountMap.replace(fuelToConsume, newFuelAmount);
        }
        return true;
    }

    public boolean hasNoFuel() {
        return fuelTypeAmountMap.isEmpty();
    }

    public Map<String, Double> getFuelTypeAmountMap() {
        return fuelTypeAmountMap;
    }

    private Optional<String> getFirstFuel() {
        return fuelTypeAmountMap.keySet()
                .stream()
                .findFirst();
    }

    @Override
    public String toString() {
        return "FuelHolder{" +
                "fuelTypeAmountMap=" + fuelTypeAmountMap +
                '}';
    }

}

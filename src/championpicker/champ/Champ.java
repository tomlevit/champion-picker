package championpicker.champ;

import championpicker.uncertainty.UncertainValue;
import championpicker.uncertainty.UncertainMap;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import championpicker.io.JSONAble;

import org.json.JSONObject;

public class Champ implements JSONAble {

    private String name;
    private int id;
    private double         pickRate;
    private double         banRate;
    private UncertainValue winRate;

    private UncertainMap goodWith;
    private UncertainMap goodAiganst;

    private double compiledStaticSum;
    private Map<Champ, Double> compiledGoodWith;
    private Map<Champ, Double> compiledGoodAiganst;

    public Champ(String name, int id) {
        this.name = name;
        this.id = id;
        pickRate = .5;
        banRate = .5;
        winRate = new UncertainValue(.5, 0);
    }

    public Champ(String name, JSONObject json) {
        this.name = name;
        id = json.getInt("id");
        pickRate = json.getDouble("pickRate");
        banRate = json.getDouble("banRate");
        winRate = new UncertainValue(json.getString("winRate"));
    }

    public JSONObject toJSON() {
        return new JSONObject()
            .put("id", id)
            .put("pickRate", pickRate)
            .put("banRate", banRate)
            .put("winRate", winRate.toString())
            .put("goodWith", goodWith.toJSON())
            .put("goodAiganst", goodAiganst.toJSON());
    }

    public void initRelationals(ChampList champs) {
        goodWith = new UncertainMap(champs);
        goodAiganst = new UncertainMap(champs);
    }

    public void setRelationals(JSONObject json) {
        goodWith = new UncertainMap(json.getJSONObject("goodWith"));
        goodAiganst = new UncertainMap(json.getJSONObject("goodAiganst"));
    }

    public void compile(double pickRateWeight, double banRateWeight, double winRateWeight, double learningWeight) {
        compiledStaticSum = 0;
        compiledStaticSum += pickRateWeight * pickRate;
        compiledStaticSum += banRateWeight * banRate;
        compiledStaticSum += winRateWeight * winRate.getRateBelief(learningWeight);

        compiledGoodWith = new HashMap<Champ, Double>();
        for(Map.Entry<Champ, UncertainValue> entry : goodWith.entrySet())
            compiledGoodWith.put(entry.getKey(), entry.getValue().getRateBelief(learningWeight));
        compiledGoodAiganst = new HashMap<Champ, Double>();
        for(Map.Entry<Champ, UncertainValue> entry : goodAiganst.entrySet())
            compiledGoodAiganst.put(entry.getKey(), entry.getValue().getRateBelief(learningWeight));
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }

    public void setPickRate(double pickRate) {
        this.pickRate = pickRate;
    }

    public void setBanRate(double banRate) {
        this.banRate = banRate;
    }

    public void setWinRate(UncertainValue winRate) {
        this.winRate = winRate;
    }

    public void setGoodWith(UncertainMap goodWith) {
        this.goodWith = goodWith;
    }

    public void setGoodAiganst(UncertainMap goodAiganst) {
        this.goodAiganst = goodAiganst;
    }

    public double getPickRate() {
        return pickRate;
    }

    public double getBanRate() {
        return banRate;
    }

    public UncertainValue getWinRate() {
        return winRate;
    }

    public double getCompiledStaticSum() {
        return compiledStaticSum;
    }

    public Map<Champ, Double> getCompiledGoodWith() {
        return compiledGoodWith;
    }

    public Map<Champ, Double> getCompiledGoodAiganst() {
        return compiledGoodAiganst;
    }
}

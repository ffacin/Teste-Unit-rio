package br.com.fatec.donationhaapi.enums;

public enum Unit {
    UN("UN"), // unidade
    LT("LT"), // litro
    KG("KG"), // kilo grama
    MT("MT"), // metros
    PR("PR"), // par
    FD("FD"), // fardo
    CX("CX"); // caixa

    private String unit;

    Unit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

}

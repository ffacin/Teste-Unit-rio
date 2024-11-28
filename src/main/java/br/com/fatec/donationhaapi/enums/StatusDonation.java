package br.com.fatec.donationhaapi.enums;

public enum StatusDonation {
    COMPLETE(0),
    PENDINGSCHEDULE(1),
    PENDINGDEVIVERY(2),
    CANCELED(3);

    private final int valor;
    StatusDonation(int valor) {
        this.valor = valor;
    }
    public int getValor() {
        return valor;
    }
}

package org.example.bazyapp.models;

import java.util.Objects;

public class Produkt {
    private final int idProduktu;
    private String nazwa;
    private int cena;
    private int ilosc;

    public Produkt(int idProduktu, String nazwa, int cena, int ilosc) {
        this.idProduktu = idProduktu;
        this.setNazwa(nazwa);
        this.setCena(cena);
        this.setIlosc(ilosc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produkt produkt = (Produkt) o;
        return getIdProduktu() == produkt.getIdProduktu() && getCena() == produkt.getCena() && getIlosc() == produkt.getIlosc() && getNazwa().equals(produkt.getNazwa());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdProduktu(), getNazwa(), getCena(), getIlosc());
    }

    public int getIdProduktu() {
        return idProduktu;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }
}

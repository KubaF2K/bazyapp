package org.example.bazyapp.models;

import java.util.Objects;

public class Klient {
    private final int idKlienta;
    private String imie;
    private String nazwisko;
    private int pesel;

    public Klient(int idKlienta, String imie, String nazwisko, int pesel) {
        this.idKlienta = idKlienta;
        this.setImie(imie);
        this.setNazwisko(nazwisko);
        this.setPesel(pesel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klient klient = (Klient) o;
        return getIdKlienta() == klient.getIdKlienta() && getPesel() == klient.getPesel() && getImie().equals(klient.getImie()) && getNazwisko().equals(klient.getNazwisko());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdKlienta(), getImie(), getNazwisko(), getPesel());
    }

    public int getIdKlienta() {
        return idKlienta;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public int getPesel() {
        return pesel;
    }

    public void setPesel(int pesel) {
        this.pesel = pesel;
    }
}

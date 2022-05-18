package org.example.bazyapp.models;

import java.util.Objects;

public class Zamowienie {
    private final int idZamowienia;
    private int idKlienta;
    private int cena;
    private String status;
    private int idPracownika;

    public Zamowienie(int idZamowienia, int idKlienta, int cena, String status, int idPracownika) {
        this.idZamowienia = idZamowienia;
        this.setIdKlienta(idKlienta);
        this.setCena(cena);
        this.setStatus(status);
        this.setIdPracownika(idPracownika);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zamowienie that = (Zamowienie) o;
        return getIdZamowienia() == that.getIdZamowienia() && getIdKlienta() == that.getIdKlienta() && getCena() == that.getCena() && getIdPracownika() == that.getIdPracownika() && getStatus().equals(that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdZamowienia(), getIdKlienta(), getCena(), getStatus(), getIdPracownika());
    }

    public int getIdZamowienia() {
        return idZamowienia;
    }

    public int getIdKlienta() {
        return idKlienta;
    }

    public void setIdKlienta(int idKlienta) {
        this.idKlienta = idKlienta;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdPracownika() {
        return idPracownika;
    }

    public void setIdPracownika(int idPracownika) {
        this.idPracownika = idPracownika;
    }
}

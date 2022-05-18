package org.example.bazyapp.models;

import java.util.Objects;

public class Pracownik {
    private final int idPracownika;
    private int idMagazynu;
    private String imie;
    private String nazwisko;
    private String adresZamieszkania;
    private long pesel;
    private int wyplata;

    public Pracownik(int idPracownika, int idMagazynu, String imie, String nazwisko, String adresZamieszkania,
                     long pesel, int wyplata) {
        this.idPracownika = idPracownika;
        this.setIdMagazynu(idMagazynu);
        this.setImie(imie);
        this.setNazwisko(nazwisko);
        this.setAdresZamieszkania(adresZamieszkania);
        this.setPesel(pesel);
        this.setWyplata(wyplata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pracownik pracownik = (Pracownik) o;
        return getIdPracownika() == pracownik.getIdPracownika() && getIdMagazynu() == pracownik.getIdMagazynu() && getPesel() == pracownik.getPesel() && getWyplata() == pracownik.getWyplata() && getImie().equals(pracownik.getImie()) && getNazwisko().equals(pracownik.getNazwisko()) && getAdresZamieszkania().equals(pracownik.getAdresZamieszkania());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPracownika(), getIdMagazynu(), getImie(), getNazwisko(), getAdresZamieszkania(), getPesel(), getWyplata());
    }

    public int getIdPracownika() {
        return idPracownika;
    }

    public int getIdMagazynu() {
        return idMagazynu;
    }

    public void setIdMagazynu(int idMagazynu) {
        this.idMagazynu = idMagazynu;
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

    public String getAdresZamieszkania() {
        return adresZamieszkania;
    }

    public void setAdresZamieszkania(String adresZamieszkania) {
        this.adresZamieszkania = adresZamieszkania;
    }

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    public int getWyplata() {
        return wyplata;
    }

    public void setWyplata(int wyplata) {
        this.wyplata = wyplata;
    }
}

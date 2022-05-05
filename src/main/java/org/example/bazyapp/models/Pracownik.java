package org.example.bazyapp.models;

import java.util.Objects;

public class Pracownik {
    public final int id_pracownika;
    public int id_magazynu;
    public String imie;
    public String nazwisko;
    public String adres_zamieszkania;
    public int pesel;
    public int wyplata;

    public Pracownik(int id_pracownika, int id_magazynu, String imie, String nazwisko, String adres_zamieszkania,
                     int pesel, int wyplata) {
        this.id_pracownika = id_pracownika;
        this.id_magazynu = id_magazynu;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.adres_zamieszkania = adres_zamieszkania;
        this.pesel = pesel;
        this.wyplata = wyplata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pracownik pracownik = (Pracownik) o;
        return id_pracownika == pracownik.id_pracownika && id_magazynu == pracownik.id_magazynu && pesel == pracownik.pesel && wyplata == pracownik.wyplata && imie.equals(pracownik.imie) && nazwisko.equals(pracownik.nazwisko) && adres_zamieszkania.equals(pracownik.adres_zamieszkania);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_pracownika, id_magazynu, imie, nazwisko, adres_zamieszkania, pesel, wyplata);
    }
}

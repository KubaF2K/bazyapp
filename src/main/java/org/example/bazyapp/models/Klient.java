package org.example.bazyapp.models;

import java.util.Objects;

public class Klient {
    public final int id_klienta;
    public String imie;
    public String nazwisko;
    public int pesel;

    public Klient(int id_klienta, String imie, String nazwisko, int pesel) {
        this.id_klienta = id_klienta;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klient klient = (Klient) o;
        return id_klienta == klient.id_klienta && pesel == klient.pesel && imie.equals(klient.imie) && nazwisko.equals(klient.nazwisko);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_klienta, imie, nazwisko, pesel);
    }
}

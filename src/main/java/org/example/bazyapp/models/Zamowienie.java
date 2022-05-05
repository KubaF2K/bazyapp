package org.example.bazyapp.models;

import java.util.Objects;

public class Zamowienie {
    public final int id_zamowienia;
    public int id_klienta;
    public int cena;
    public String status;
    public int id_pracownika;

    public Zamowienie(int id_zamowienia, int id_klienta, int cena, String status, int id_pracownika) {
        this.id_zamowienia = id_zamowienia;
        this.id_klienta = id_klienta;
        this.cena = cena;
        this.status = status;
        this.id_pracownika = id_pracownika;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zamowienie that = (Zamowienie) o;
        return id_zamowienia == that.id_zamowienia && id_klienta == that.id_klienta && cena == that.cena && id_pracownika == that.id_pracownika && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_zamowienia, id_klienta, cena, status, id_pracownika);
    }
}

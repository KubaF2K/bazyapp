package org.example.bazyapp.models;

import java.util.Objects;

public class Produkt {
    public final int id_produktu;
    public String nazwa;
    public int cena;
    public int ilosc;

    public Produkt(int id_produktu, String nazwa, int cena, int ilosc) {
        this.id_produktu = id_produktu;
        this.nazwa = nazwa;
        this.cena = cena;
        this.ilosc = ilosc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produkt produkt = (Produkt) o;
        return id_produktu == produkt.id_produktu && cena == produkt.cena && ilosc == produkt.ilosc && nazwa.equals(produkt.nazwa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_produktu, nazwa, cena, ilosc);
    }
}

package org.example.bazyapp.models;

import java.util.Objects;

public class Dostawa {
    public final int id_dostawy;
    public int id_produktu;
    public int id_magazynu;
    public int id_pracownika;
    public int koszt;
    public int ilosc;

    public Dostawa(int id_dostawy, int id_produktu, int id_magazynu, int id_pracownika, int koszt, int ilosc) {
        this.id_dostawy = id_dostawy;
        this.id_produktu = id_produktu;
        this.id_magazynu = id_magazynu;
        this.id_pracownika = id_pracownika;
        this.koszt = koszt;
        this.ilosc = ilosc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dostawa dostawa = (Dostawa) o;
        return id_dostawy == dostawa.id_dostawy && id_produktu == dostawa.id_produktu && id_magazynu == dostawa.id_magazynu && id_pracownika == dostawa.id_pracownika && koszt == dostawa.koszt && ilosc == dostawa.ilosc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_dostawy, id_produktu, id_magazynu, id_pracownika, koszt, ilosc);
    }
}

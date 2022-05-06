package org.example.bazyapp.models;

import java.util.Objects;

public class ZamowienieSzcz {
    public final int id_szczegoly;
    public int id_zamowienia;
    public int id_produktu;
    public int ilosc;

    public ZamowienieSzcz(int id_szczegoly, int id_zamowienia, int id_produktu, int ilosc) {
        this.id_szczegoly = id_szczegoly;
        this.id_zamowienia = id_zamowienia;
        this.id_produktu = id_produktu;
        this.ilosc = ilosc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZamowienieSzcz that = (ZamowienieSzcz) o;
        return id_szczegoly == that.id_szczegoly && id_zamowienia == that.id_zamowienia && id_produktu == that.id_produktu && ilosc == that.ilosc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_szczegoly, id_zamowienia, id_produktu, ilosc);
    }
}

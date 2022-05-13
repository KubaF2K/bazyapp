package org.example.bazyapp.models;

import java.util.Objects;

public class ZamowienieSzcz {
    public final int idSzczegoly;
    public int idZamowienia;
    public int idProduktu;
    public int ilosc;

    public ZamowienieSzcz(int idSzczegoly, int idZamowienia, int idProduktu, int ilosc) {
        this.idSzczegoly = idSzczegoly;
        this.idZamowienia = idZamowienia;
        this.idProduktu = idProduktu;
        this.ilosc = ilosc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZamowienieSzcz that = (ZamowienieSzcz) o;
        return idSzczegoly == that.idSzczegoly && idZamowienia == that.idZamowienia && idProduktu == that.idProduktu && ilosc == that.ilosc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSzczegoly, idZamowienia, idProduktu, ilosc);
    }

    public int getIdSzczegoly() {
        return idSzczegoly;
    }

    public int getIdZamowienia() {
        return idZamowienia;
    }

    public void setIdZamowienia(int idZamowienia) {
        this.idZamowienia = idZamowienia;
    }

    public int getIdProduktu() {
        return idProduktu;
    }

    public void setIdProduktu(int idProduktu) {
        this.idProduktu = idProduktu;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }
}

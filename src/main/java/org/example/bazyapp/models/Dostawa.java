package org.example.bazyapp.models;

import java.util.Objects;

public class Dostawa {
    private final int idDostawy;
    private int idProduktu;
    private int idMagazynu;
    private int idPracownika;
    private int koszt;
    private int ilosc;

    public int getIdDostawy() {
        return idDostawy;
    }

    public int getIdProduktu() {
        return idProduktu;
    }

    public void setIdProduktu(int id_produktu) {
        this.idProduktu = id_produktu;
    }

    public int getIdMagazynu() {
        return idMagazynu;
    }

    public void setIdMagazynu(int id_magazynu) {
        this.idMagazynu = id_magazynu;
    }

    public int getIdPracownika() {
        return idPracownika;
    }

    public void setIdPracownika(int id_pracownika) {
        this.idPracownika = id_pracownika;
    }

    public int getKoszt() {
        return koszt;
    }

    public void setKoszt(int koszt) {
        this.koszt = koszt;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    public Dostawa(int idDostawy, int idProduktu, int idMagazynu, int idPracownika, int koszt, int ilosc) {
        this.idDostawy = idDostawy;
        this.idProduktu = idProduktu;
        this.idMagazynu = idMagazynu;
        this.idPracownika = idPracownika;
        this.koszt = koszt;
        this.ilosc = ilosc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dostawa dostawa = (Dostawa) o;
        return idDostawy == dostawa.idDostawy && idProduktu == dostawa.idProduktu && idMagazynu == dostawa.idMagazynu && idPracownika == dostawa.idPracownika && koszt == dostawa.koszt && ilosc == dostawa.ilosc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDostawy, idProduktu, idMagazynu, idPracownika, koszt, ilosc);
    }
}

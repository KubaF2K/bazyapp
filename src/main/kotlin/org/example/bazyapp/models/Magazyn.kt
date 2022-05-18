package org.example.bazyapp.models;

import java.util.Objects;

public class Magazyn {
    private final int idMagazynu;
    private String miejsce;
    private String nazwa;

    public int getIdMagazynu() {
        return idMagazynu;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Magazyn(int idMagazynu, String miejsce, String nazwa) {
        this.idMagazynu = idMagazynu;
        this.miejsce = miejsce;
        this.nazwa = nazwa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magazyn magazyn = (Magazyn) o;
        return idMagazynu == magazyn.idMagazynu && miejsce.equals(magazyn.miejsce) && nazwa.equals(magazyn.nazwa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMagazynu, miejsce, nazwa);
    }
}

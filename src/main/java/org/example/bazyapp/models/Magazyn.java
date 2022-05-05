package org.example.bazyapp.models;

import java.util.Objects;

public class Magazyn {
    public final int id_magazynu;
    public String miejsce;
    public String nazwa;

    public Magazyn(int id_magazynu, String miejsce, String nazwa) {
        this.id_magazynu = id_magazynu;
        this.miejsce = miejsce;
        this.nazwa = nazwa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magazyn magazyn = (Magazyn) o;
        return id_magazynu == magazyn.id_magazynu && miejsce.equals(magazyn.miejsce) && nazwa.equals(magazyn.nazwa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_magazynu, miejsce, nazwa);
    }
}

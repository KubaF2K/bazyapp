package org.example.bazyapp.models

data class Pracownik(
    var idPracownika: Int, var idMagazynu: Int, var imie: String, var nazwisko: String, var adresZamieszkania: String,
    var pesel: Long, var wyplata: Int
)
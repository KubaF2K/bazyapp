package org.example.bazyapp.models

data class Zamowienie(
    var idZamowienia: Int, var idKlienta: Int, var cena: Int, var status: String, var idPracownika: Int
)
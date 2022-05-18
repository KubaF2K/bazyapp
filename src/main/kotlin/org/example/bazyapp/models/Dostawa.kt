package org.example.bazyapp.models

data class Dostawa(
    var idDostawy: Int, var idProduktu: Int, var idMagazynu: Int, var idPracownika: Int, var koszt: Int,
    var ilosc: Int
)
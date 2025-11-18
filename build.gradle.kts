/*
 * Aquest és el fitxer build de l'arrel del projecte.
 * Defineix el plugin 'base' i els repositoris
 * per a tots els submòduls.
 */

plugins {
    // El plugin 'base' és per al projecte arrel.
    base
}

allprojects {
    // Assegurem que tots els projectes (inclòs l'arrel)
    // tinguin accés a mavenCentral.
    repositories {
        mavenCentral()
    }
}
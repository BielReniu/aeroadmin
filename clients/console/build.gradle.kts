plugins {
    // AQUEST ÉS EL PLUGIN CORRECTE PER A UNA APLICACIÓ
    id("buildlogic.java-application-conventions")
}

dependencies {
    implementation(project(":model"))
    implementation(project(":utilities"))
    implementation("com.github.freva:ascii-table:1.8.0")

    // AQUESTA ÉS LA DEPENDÈNCIA CORRECTA DE RAWHTTP AMB LA VERSIÓ
    implementation("com.athaydes.rawhttp:rawhttp-core:2.6.0")

    // AFEGEIX GSON PER GESTIONAR JSON
    implementation("com.google.code.gson:gson:2.10.1")
}

application {
    // DEFINEIX LA CLASSE PRINCIPAL PER PODER EXECUTAR EL CLIENT
    mainClass.set("cat.uvic.teknos.dam.aeroadmin.client.ConsoleApp")
}
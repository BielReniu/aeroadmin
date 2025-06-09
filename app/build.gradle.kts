plugins {
    // Plugin per poder córrer l'aplicació amb `gradle :app:run`
    application
}

dependencies {
    implementation(project(":jdbc"))
    implementation(project(":model"))
    implementation(project(":repositories"))
    implementation(project(":jpa"))

    // Llibreria per connectar a MySQL
    implementation("com.mysql:mysql-connector-j:9.3.0")
    // Llibreria per taules ASCII a la consola
    implementation("com.github.freva:ascii-table:1.8.0")
}

application {
    // Fully-qualified name de la teva classe Main
    mainClass.set("cat.uvic.teknos.dam.aeroadmin.console.App")
}

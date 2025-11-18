plugins {
    id("buildlogic.java-library-conventions")
}

// NO HI HA D'HAVER CAP BLOC repositories {} AQUÍ

dependencies {
    implementation(project(":jdbc"))
    implementation(project(":model"))
    implementation(project(":repositories"))
    implementation(project(":jpa"))
    implementation(project(":utilities"))

    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("com.athaydes.rawhttp:rawhttp-core:2.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("com.google.code.gson:gson:2.10.1")
}
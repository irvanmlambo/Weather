plugins {
    id("java-platform")
    id("maven-publish")
}

val appcompat = "1.3.0"
val constraintLayout = "2.0.4"
val core = "1.6.0"
val playServices = "18.0.0"
val lifecycle = "2.3.1"
val lifecycleExtension = "2.2.0"
val material = "1.4.0"
var retrofit = "2.9.0"


dependencies {
    constraints {
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.PLAY_SERVICES_LOCATION}:$playServices")
        api("${Libs.LIFECYCLE_EXTENSION}:$lifecycleExtension")
        api("${Libs.LIFECYCLE_RUNTIME_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.RETROFIT_CONVERTER_GSON}:$retrofit")
    }
}

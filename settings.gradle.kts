pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ChallengeTicketmaster"
include(":app")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:database")
include(":core:network")
include(":core:designsystem")
include(":core:models:domain")
include(":core:models:entity")
include(":core:models:network")
include(":core:models:ui")
include(":core:ui")
include(":core:testing")
include(":feature:events")
include(":feature:favorites")
include(":feature:detailevent")
include(":feature:searchevent")
include(":feature:lastvisited")
include(":feature:onboarding")
include(":feature:countries")

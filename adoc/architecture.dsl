workspace "Clean Architecture" "Auto-extracted from bytecode — do not edit manually" {

    model {

        cleanApp = softwareSystem "Clean Application" {

            app = container "clean-application" "Spring Boot app" "Java 17" {

                domain = component "domain" "victor.training.clean.domain"
                application = component "application" "victor.training.clean.application"
                infra = component "infra" "victor.training.clean.infra"
                vsa = component "vsa" "victor.training.clean.vsa"

                domain -> application ""
                application -> domain ""
                application -> infra ""
                infra -> domain ""
                vsa -> domain ""

            }
        }
    }

    views {

        component app "packages" "Top-level package dependencies" {
            include *
            autoLayout lr
        }

        styles {
            element "Component" { background #85bbf0 color #ffffff }
        }

    }

}

rootProject.name = "boot-react"

include("backend", "frontend")

findProject(":backend")?.name = "backend"
findProject(":frontend")?.name = "frontend"



class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(view: "/index")
        "500"(view: "/error")
        "405"(view: "/error")
        "404"(view:'/404')
        "/login/$action?"(controller: "login")
        "/logout/$action?"(controller: "logout")
    }
}

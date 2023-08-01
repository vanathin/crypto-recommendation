package com.xm.crypto.recommendation.info.documentation;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Crypto Info API - OpenAPI 3.0 documentation",
                description = """
                        API for managing crypto information.
                        """,
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8080")
)
public class OpenApiConfiguration {
}

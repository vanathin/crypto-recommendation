package com.xm.crypto.recommendation.common.exception.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Crypto Recommendations API",
                description = """
                        API for crypto recommendations.
                        """,
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8080")
)
public class OpenApiConfiguration {
}

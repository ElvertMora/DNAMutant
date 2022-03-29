package co.com.emorae.api;

import co.com.emorae.model.sequence.Sequence;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/mutant", method = RequestMethod.POST,
                    beanClass = Handler.class, beanMethod = "listenPOSTMutant",
                    operation = @Operation(operationId = "listenPOSTMutant", responses = {
                            @ApiResponse(responseCode = "200", description = "is Mutant"),
                            @ApiResponse(responseCode = "403", description = "is Human"),
                            @ApiResponse(responseCode = "400", description = "invalid DNA sequence")},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Sequence.class))))
            ),
            @RouterOperation(path = "/stats", method = RequestMethod.GET,
                    beanClass = Handler.class, beanMethod = "listenGETStats",
                    operation = @Operation(operationId = "listenGETStats", responses = {
                            @ApiResponse(responseCode = "200", description = "stats of application", content = @Content(schema = @Schema(implementation = HashMap.class)))
                    })
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/stats"), handler::listenGETStats)
                .andRoute(POST("/mutant"), handler::listenPOSTMutant);

    }

}

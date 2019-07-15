package endpoint

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
class EndpointController {

    @GetMapping("/endpoint")
    fun hello(@RequestParam(value = "name") name: String, @RequestParam(value = "email") email: String,@RequestParam(value = "type") eventType: String,@RequestParam(value = "date") date: String,@RequestParam(value = "dur") duration: Int): Endpoint {
        val dateUnformattedText = LocalDate.parse("$date")
        val dateFormattedText: String = dateUnformattedText.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return Endpoint(
            "$name", "$email","$eventType", "$dateFormattedText", duration
        )
    }

}
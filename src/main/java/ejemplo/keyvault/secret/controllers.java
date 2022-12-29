package ejemplo.keyvault.secret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controllers {

	@Value("${POSTGRESQL-DATABASE-NAME}")
    private String connectionString;
    

	@GetMapping("get")
    public String get() {
        return connectionString;
    }

    public void run(String... varl) throws Exception {
        System.out.println(String.format("\nConnection String stored in Azure Key Vault:\n%s\n",connectionString));
    }

    
}

package springproject.imprimaqui.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springproject.imprimaqui.domain.product.PaperSize;
import springproject.imprimaqui.domain.product.PrintType;
import springproject.imprimaqui.domain.product.Product;
import springproject.imprimaqui.domain.product.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class SeederConfig {

    @Bean
    CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                return;
            }

            productRepository.saveAll(List.of(
                    Product.builder()
                            .name("Impressão Colorida A4")
                            .description("Ideal para apresentacões, trabalhos e materiais com alto impacto visual.")
                            .printType(PrintType.COLORIDO)
                            .paperSize(PaperSize.A4)
                            .unitPrice(BigDecimal.valueOf(0.99))
                            .active(true)
                            .build(),
                    Product.builder()
                            .name("Impressão Colorida A3")
                            .description("Formato maior para comunicação visual, banners internos e material promocional.")
                            .printType(PrintType.COLORIDO)
                            .paperSize(PaperSize.A3)
                            .unitPrice(BigDecimal.valueOf(1.89))
                            .active(true)
                            .build(),
                    Product.builder()
                            .name("Impressão Preto e Branco A4")
                            .description("Opção econômica para apostilas, relatórios e documentos do dia a dia.")
                            .printType(PrintType.PRETO_E_BRANCO)
                            .paperSize(PaperSize.A4)
                            .unitPrice(BigDecimal.valueOf(0.35))
                            .active(true)
                            .build(),
                    Product.builder()
                            .name("Impressão Preto e Branco A3")
                            .description("Perfeito para projetos ampliados com baixo custo por pagina.")
                            .printType(PrintType.PRETO_E_BRANCO)
                            .paperSize(PaperSize.A3)
                            .unitPrice(BigDecimal.valueOf(0.79))
                            .active(true)
                            .build()
            ));
        };
    }
}

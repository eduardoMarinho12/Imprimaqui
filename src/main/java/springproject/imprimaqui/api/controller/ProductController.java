package springproject.imprimaqui.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springproject.imprimaqui.dto.product.ProductRequestDTO;
import springproject.imprimaqui.dto.product.ProductResponseDTO;
import springproject.imprimaqui.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDTO> listarProdutos() {
        return productService.listarProdutosAtivos();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO buscarProduto(@PathVariable Long id) {
        return productService.buscarProduto(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductResponseDTO criarProduto(@RequestBody @Valid ProductRequestDTO dto) {
        return productService.criarProduto(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO atualizarProduto(@PathVariable Long id,
                                               @RequestBody @Valid ProductRequestDTO dto) {
        return productService.atualizarProduto(id, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id) {
        productService.deletarProduto(id);
    }
}

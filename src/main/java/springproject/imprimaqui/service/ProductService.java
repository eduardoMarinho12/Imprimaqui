package springproject.imprimaqui.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.domain.product.Product;
import springproject.imprimaqui.domain.product.ProductRepository;
import springproject.imprimaqui.dto.product.ProductRequestDTO;
import springproject.imprimaqui.dto.product.ProductResponseDTO;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> listarProdutos() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDTO::from)
                .toList();
    }

    public List<ProductResponseDTO> listarProdutosAtivos() {
        return productRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(ProductResponseDTO::from)
                .toList();
    }

    public Product buscarEntidade(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    public ProductResponseDTO buscarProduto(Long id) {
        return ProductResponseDTO.from(buscarEntidade(id));
    }

    public ProductResponseDTO criarProduto(ProductRequestDTO dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .printType(dto.getPrintType())
                .paperSize(dto.getPaperSize())
                .unitPrice(dto.getUnitPrice())
                .active(dto.isActive())
                .build();

        return ProductResponseDTO.from(productRepository.save(product));
    }

    public ProductResponseDTO atualizarProduto(Long id, ProductRequestDTO dto) {
        Product product = buscarEntidade(id);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrintType(dto.getPrintType());
        product.setPaperSize(dto.getPaperSize());
        product.setUnitPrice(dto.getUnitPrice());
        product.setActive(dto.isActive());
        return ProductResponseDTO.from(productRepository.save(product));
    }

    public void deletarProduto(Long id) {
        productRepository.delete(buscarEntidade(id));
    }
}

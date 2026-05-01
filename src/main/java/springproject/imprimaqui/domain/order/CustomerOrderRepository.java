package springproject.imprimaqui.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findAllByOrderByCreatedAtDesc();

    List<CustomerOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
}

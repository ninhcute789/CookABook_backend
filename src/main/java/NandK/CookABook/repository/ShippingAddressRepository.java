package NandK.CookABook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.ShippingAddress;
import NandK.CookABook.entity.User;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    public List<ShippingAddress> findByUserId(Long userId);

    public List<ShippingAddress> findByUser(User user);

    public Integer countByUser(User user);
}

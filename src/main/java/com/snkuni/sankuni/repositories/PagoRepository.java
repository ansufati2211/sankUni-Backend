package com.snkuni.sankuni.repositories;
import com.snkuni.sankuni.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {}
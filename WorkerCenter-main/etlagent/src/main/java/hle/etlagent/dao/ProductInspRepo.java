package hle.etlagent.dao;

import hle.etlagent.dto.ProductInspReq;
import hle.etlagent.model.ProductInspect;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductInspRepo {

    List<ProductInspect> findAll();
    List<ProductInspect> findBetween(LocalDateTime from, LocalDateTime to);
    void createNewInsp(ProductInspReq inspReq);
}

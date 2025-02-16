package hle.etlagent.controller;

import hle.etlagent.dao.ProductInspRepo;
import hle.etlagent.dto.ProductInspReq;
import hle.etlagent.model.ProductInspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-insp")
public class ProductInspController {

    @Autowired
    ProductInspRepo productInspRepo;

    @PostMapping
    public List<ProductInspect> createInspRecord(@RequestBody ProductInspReq req) {
        productInspRepo.createNewInsp(req);
        return productInspRepo.findAll();
    }
}

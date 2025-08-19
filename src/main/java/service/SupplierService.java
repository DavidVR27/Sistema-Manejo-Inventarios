package service;

import dto.Response;
import dto.SupplierDTO;

public interface SupplierService {
    Response addSuppier(SupplierDTO supplierDTO);
    Response updateSuppier(Long id, SupplierDTO supplierDTO);
    Response getAllSuppliers();
    Response getSupplierById(Long id);
    Response deleteSupplier(Long id);
}

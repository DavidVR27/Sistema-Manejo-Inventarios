package service.impl;

import dto.ProductDTO;
import dto.Response;
import entity.Category;
import entity.Product;
import exceptions.NotFoundException;
import repository.CategoryRepository;
import repository.ProductRepository;
import service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";

   // Carpeta para el Frontend
    private static final String IMAGE_DIRECTOR_FRONTEND = "Users/davo2/OneDrive/Documentos/Code/InventarioFrontend/inventario-frontend/public/products";


    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category Not Found"));

        // mapear producto dto a producto entity
        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if (imageFile != null){
            String imagePath = saveImageToFrontendPublicFolder(imageFile);
            productToSave.setImageUrl(imagePath);
        }

        // guardado del producto en la base de datos
        productRepository.save(productToSave);
        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {

        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        // verificar si la imagen esta asociada con la solicitud de actualizacion
        if (imageFile != null && !imageFile.isEmpty()){
            String imagePath = saveImageToFrontendPublicFolder(imageFile);
            existingProduct.setImageUrl(imagePath);
        }

        // verificacion de la categoria
        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0){

            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()-> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);
        }

        // verificar y actualizar campos

        if (productDTO.getName() !=null && !productDTO.getName().isBlank()){
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() !=null && !productDTO.getSku().isBlank()){
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() !=null && !productDTO.getDescription().isBlank()){
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() !=null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >=0){
            existingProduct.setPrice(productDTO.getPrice());
        }

        if (productDTO.getStockQuantity() !=null && productDTO.getStockQuantity() >=0){
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }

        //Update the product
        productRepository.save(existingProduct);
        return Response.builder()
                .status(200)
                .message("Product successfully Updated")
                .build();

    }

    @Override
    public Response getAllProducts() {

        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDTO> productDTOS = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOS)
                .build();
    }

    @Override
    public Response getProductById(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));


        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();
    }

    @Override
    public Response deleteProduct(long id) {
        productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product successfully deleted")
                .build();
    }

    private String saveImageToFrontendPublicFolder(MultipartFile imageFile){
        // validar la imagen
        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }
        // crear la ruta o directorio si no existe
        File directory = new File(IMAGE_DIRECTOR_FRONTEND);

        if (!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }
        // generar un nombre unico para el archivo
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        // obtener la ruta absoluta
        String imagePath = IMAGE_DIRECTOR_FRONTEND + uniqueFileName;

        try {
            File desctinationFile = new File(imagePath);
            imageFile.transferTo(desctinationFile);

        }catch (Exception e){
            throw new IllegalArgumentException("Error occurend while saving image" + e.getMessage());
        }

        return "products/"+uniqueFileName;
    }

    private String saveImage(MultipartFile imageFile){

        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }

        File directory = new File(IMAGE_DIRECTORY);

        if (!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File desctinationFile = new File(imagePath);
            imageFile.transferTo(desctinationFile);

        }catch (Exception e){
            throw new IllegalArgumentException("Error occurred while saving image" + e.getMessage());
        }

        return imagePath;
    }











}
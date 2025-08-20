package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.constant.RoundingConstants;
import com.staj.gib.shopapi.dto.mapper.ProductMapper;
import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.UpdateProductRequest;
import com.staj.gib.shopapi.dto.response.CategoryResponse;
import com.staj.gib.shopapi.dto.response.CategoryTaxResponse;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.dto.response.TaxDetailDto;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.ProductImage;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryService categoryService;
    private final TaxService taxService;
    private final ProductMapper productMapper;
    private final Cloudinary cloudinary;


    public ProductResponse getProduct(UUID productid) {
        Product product = repository.findById(productid).orElseThrow(()
                -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, productid));

        return mapper.toResponse(product, calculateAfterTaxPrice(product));
    }

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required");
        }

        for (MultipartFile file : images) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("One of the uploaded files is empty");
            }

            if (file.getSize() > MAX_SIZE) {
                throw new IllegalArgumentException("File " + file.getOriginalFilename() + " exceeds maximum size of 5MB");
            }

            if (!ALLOWED_TYPES.contains(file.getContentType())) {
                throw new IllegalArgumentException("File " + file.getOriginalFilename() + " has unsupported type " + file.getContentType());
            }
        }
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, List<MultipartFile> images) {
        Product product = mapper.toEntity(request);
        product.setImages(new ArrayList<>());

        validateImages(images);

        // upload images and get urls

        // Upload the image
        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", true,
                "overwrite", false
        );

        for(MultipartFile imageFile : images){
            try {
                Map result = cloudinary.uploader().upload(
                        imageFile.getBytes(), params1);

                String secureUrl = (String) result.get("secure_url"); // CDN URL
                String publicId  = (String) result.get("public_id");  // store for deletions/updates

                ProductImage img = new ProductImage();
                img.setProduct(product);
                img.setImageUrl(secureUrl);
                img.setPublicId(publicId);
                product.getImages().add(img);
            }
            catch (IOException e){
                System.err.println(e.toString());
                throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        return mapper.toResponse(this.repository.save(product), calculateAfterTaxPrice(product));
    }

    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, List<MultipartFile> newImages, List<UUID> removeImageIds) {
        Product existingProduct = repository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, request.getId()));

        // update only non-null fields
        mapper.updateEntity(existingProduct, request);
        existingProduct.getImages().forEach(img -> img.setProduct(existingProduct));

        return mapper.toResponse(repository.save(existingProduct), calculateAfterTaxPrice(existingProduct));
    }

    @Transactional
    public void deleteProductById(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, id);
        }
        repository.deleteById(id);
    }

    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(p -> mapper.toResponse(p, calculateAfterTaxPrice(p)))
                .toList();
    }

    @Transactional
    public void decrementStock(UUID productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        Product product = repository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }

        product.setStock(product.getStock() - quantity);
        repository.save(product); // version prevents lost update
    }

    public BigDecimal calculateAfterTaxPrice(UUID productId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, productId));

        return calculateAfterTaxPrice(product);
    }

    private BigDecimal calculateAfterTaxPrice(Product product) {
        BigDecimal productPrice = product.getPrice().setScale(RoundingConstants.SCALE, RoundingConstants.ROUNDING);
        BigDecimal totalItemPrice = productPrice;

        CategoryResponse category = categoryService.getCategory(product.getCategoryId());
        List<CategoryTaxResponse> categoryTaxResponse = category.getTaxes();
        List<TaxDetailDto> taxDetails = taxService.calculateTaxBreakdown(productPrice, categoryTaxResponse);

        for (TaxDetailDto taxDetail : taxDetails) {
            totalItemPrice = totalItemPrice.add(
                    taxDetail.getAmount()
            );
        }
        return totalItemPrice.setScale(RoundingConstants.SCALE, RoundingConstants.ROUNDING);
    }

    public List<ProductResponse> getProductsByKeyword(String keyword) {
        return this.repository.search(keyword)
                .stream()
                .map(product -> mapper.toResponse(product, calculateAfterTaxPrice(product)))
                .toList();
    }
}

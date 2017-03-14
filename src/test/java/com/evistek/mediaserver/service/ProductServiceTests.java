package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Category;
import com.evistek.mediaserver.entity.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql",
        "classpath:test-admins-authorities-data.sql",
        "classpath:test-categories-data.sql",
        "classpath:test-products-data.sql"})
public class ProductServiceTests {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Before
    public void setup() {

    }

    @Test
    public void findProduct() {
        int result = this.productService.findProductNumber();
        assertThat(result).isEqualTo(7);

        Product product = this.productService.findProductById(1);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("98寸裸眼3D电视_01");
        assertThat(product.getImgUrl()).isEqualTo("http://121.199.6.174:8080/splashPage/ads_98inch_01.jpg");
        assertThat(product.getWebsiteUrl()).isEqualTo("http://www.evistek.com/e5788e1f-5b15-32d3-3116-f7cc4fb69e15/be4a6041-96fd-1780-ac09-ae158b6c67fe.shtml");
        assertThat(product.getIntroduction()).isNull();
        assertThat(product.getCategoryId()).isEqualTo(8);
        assertThat(product.getCategoryName()).isEqualTo("裸眼3D电视");
        assertThat(product.getOwnerId()).isEqualTo(2);
        assertThat(product.isAudit()).isTrue();

        List<Product> productList = this.productService.findProducts();
        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(7);

        List<Product> productList1 = this.productService.findProducts(1, 5);
        assertThat(productList1).isNotNull();
        assertThat(productList1.size()).isEqualTo(5);

        List<Product> productList2 = this.productService.findProducts(2, 5);
        assertThat(productList2).isNotNull();
        assertThat(productList2.size()).isEqualTo(2);

        List<Product> productList3 = this.productService.findProductsByCategoryId(8);
        assertThat(productList3).isNotNull();
        assertThat(productList3.size()).isEqualTo(5);

        List<Product> productList4 = this.productService.findProductsByCategoryId(8, 2, 5);
        assertThat(productList4).isNotNull();
        assertThat(productList4.size()).isEqualTo(0);

        List<Product> productList5 = this.productService.findProductsByCategoryName("裸眼3D手机");
        assertThat(productList5).isNotNull();
        assertThat(productList5.size()).isEqualTo(1);

        List<Product> productList6 = this.productService.findProductsByCategoryName("裸眼3D手机", 1, 10);
        assertThat(productList6).isNotNull();
        assertThat(productList6.size()).isEqualTo(1);

        List<Product> productList7 = this.productService.findProductsByAudit(true);
        assertThat(productList7).isNotNull();
        assertThat(productList7.size()).isEqualTo(5);
        List<Product> productList8 = this.productService.findProductsByAudit(false);
        assertThat(productList8).isNotNull();
        assertThat(productList8.size()).isEqualTo(2);

        List<Product> productList9 = this.productService.findProductsByAudit(true, 2, 5);
        assertThat(productList9).isNotNull();
        assertThat(productList9.size()).isEqualTo(0);

        Product product1 = this.productService.findProductByName("裸眼3D手机_01");
        assertThat(product1).isNotNull();
        assertThat(product1.getId()).isEqualTo(5);
    }

    @Test
    public void addProduct() {
        int result = this.productService.findProductNumber();
        assertThat(result).isEqualTo(7);

        Product product = new Product();
        product.setName("testProduct");
        product.setImgUrl("http://121.199.6.174:8080/splashPage/testProduct.jpg");
        product.setWebsiteUrl("http://www.evistek.com/product/testProduct.html");
        product.setIntroduction("This is a product information for test.");
        product.setOwnerId(2);

        List<Category> categoryList = this.categoryService.findCategoriesByType("product");
        for(Category c: categoryList) {
            if (c.getName().equals("裸眼3D手机")) {
                product.setCategoryId(c.getId());
                product.setCategoryName(c.getName());
            }
        }
        product.setAudit(false);

        Product product1 = this.productService.addProduct(product);
        assertThat(product1).isNotNull();
        assertThat(product1.getName()).isEqualTo("testProduct");
        assertThat(product1.getId()).isEqualTo(8);

        result = this.productService.findProductNumber();
        assertThat(result).isEqualTo(8);
    }

    @Test
    public void deleteProduct() {
        int result = this.productService.findProductNumber();
        assertThat(result).isEqualTo(7);

        Product product = this.productService.findProductById(1);
        assertThat(product).isNotNull();
        result = this.productService.deleteProduct(product);
        assertThat(result).isEqualTo(1);

        result = this.productService.findProductNumber();
        assertThat(result).isEqualTo(6);
        Product product1 = this.productService.findProductById(1);
        assertThat(product1).isNull();

        Product product2 = this.productService.findProductById(2);
        assertThat(product2).isNotNull();
        result = this.productService.deleteProductById(2);
        assertThat(result).isEqualTo(1);

        result = this.productService.findProductNumber();
        assertThat(result).isEqualTo(5);
        Product product3 = this.productService.findProductById(2);
        assertThat(product3).isNull();

        List<Product> productList = this.productService.findProductsByCategoryName("裸眼3D电视");
        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(3);
        result = this.productService.deleteProducts(productList);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void updateProduct() {
        List<Product> productList = this.productService.findProductsByAudit(false);
        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(2);

        for(Product p: productList) {
            p.setAudit(true);
            int result = this.productService.updateProduct(p);
            assertThat(result).isEqualTo(1);
        }

        List<Product> productList2 = this.productService.findProductsByAudit(false);
        assertThat(productList2).isNotNull();
        assertThat(productList2.size()).isEqualTo(0);
    }
}

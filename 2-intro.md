# Giới thiệu Spring Data JPA

## I. Tổng Quan

*   **Spring Data JPA là gì?**
    *   Mục tiêu: Đơn giản hóa việc truy cập dữ liệu JPA.
    *   Vai trò: Abstraction layer trên JPA Provider (Hibernate, EclipseLink...).
    *   Ưu điểm: Giảm code boilerplate, tăng tốc độ phát triển.
*   **Tại sao nên dùng Spring Data JPA?**
    *   Giảm thiểu đáng kể lượng code so với JPA thuần.
    *   Code dễ đọc, dễ bảo trì.
    *   Tích hợp tốt với Spring (DI, AOP, Transaction Management).
*   **Sự khác biệt giữa JPA và Spring Data JPA:**
    *   JPA: Đặc tả (Specification). Định nghĩa các interface, annotations.
    *   Spring Data JPA: Implementation (Triển khai) của JPA, cung cấp Repository abstraction.
*   **Các thành phần chính:**
    *   `JpaRepository`: Interface cơ bản cung cấp các phương thức CRUD.
    *   `@Entity`, `@Id`, `@GeneratedValue`, `@Column`: Annotations cho mapping object-relational.
    *   `EntityManager`: Interface JPA để tương tác với persistence context (quản lý các entity).
    *   `Transaction Management`: Spring Data JPA quản lý transactions tự động (mặc định).

## II. Các Bước Cơ Bản Để Sử Dụng

1.  **Thêm Dependencies (Maven/Gradle):**

    *   **Maven:**
        ```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        ```
    *   **Gradle:**
        ```gradle
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        ```

2.  **Cấu Hình Datasource:**

    *   Sử dụng `application.properties` hoặc `application.yml`.
    *   Cấu hình:
        *   `spring.datasource.url` (JDBC URL)
        *   `spring.datasource.username`
        *   `spring.datasource.password`
        *   `spring.datasource.driver-class-name`
        *   `spring.jpa.hibernate.ddl-auto` (Tùy chọn: create, update, validate, none)
    *   Ví dụ:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
        spring.datasource.username=root
        spring.datasource.password=password
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.jpa.hibernate.ddl-auto=update
        ```

3.  **Tạo Entity (POJO with JPA Annotations):**

    *   Ví dụ:
        ```java
        @Entity
        @Table(name = "products") // Optional: Đặt tên bảng khác với tên class
        public class Product {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @Column(name = "name", nullable = false) // Optional: Customize cột
            private String name;

            private double price;  // Nếu không có @Column, tên cột sẽ là 'price'

            // Getters and Setters (rất quan trọng!)
        }
        ```

4.  **Tạo Repository Interface (extends JpaRepository):**

    *   Ví dụ:
        ```java
        import org.springframework.data.jpa.repository.JpaRepository;

        public interface ProductRepository extends JpaRepository<Product, Long> {
            // Không cần code ở đây cho CRUD cơ bản!
        }
        ```

5.  **Sử Dụng Repository (Inject vào Service/Controller):**

    *   Ví dụ:
        ```java
        @Service
        public class ProductService {

            @Autowired
            private ProductRepository productRepository;

            public List<Product> getAllProducts() {
                return productRepository.findAll();
            }

            public Product getProductById(Long id) {
                return productRepository.findById(id).orElse(null);
            }

            public Product saveProduct(Product product) {
                return productRepository.save(product);
            }

            public void deleteProduct(Long id) {
                productRepository.deleteById(id);
            }
        }
        ```

## III. Các Tính Năng Nâng Cao

*   **Custom Query Methods (Query Derivation):**
    *   Spring Data JPA tự động tạo truy vấn dựa trên tên method.
    *   Ví dụ:
        *   `findByProductName(String name)`:  `SELECT p FROM Product p WHERE p.name = ?1`
        *   `findByPriceGreaterThan(double price)`: `SELECT p FROM Product p WHERE p.price > ?1`
        *   `findByProductNameContaining(String keyword)`: `SELECT p FROM Product p WHERE p.name LIKE %?1%`
*   **@Query Annotation (JPQL/Native SQL):**
    *   Cho phép viết truy vấn trực tiếp (khi Query Derivation không đủ).
    *   Ví dụ JPQL:
        ```java
        @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
        List<Product> searchProducts(@Param("keyword") String keyword);
        ```
    *   Ví dụ Native SQL:
        ```java
        @Query(value = "SELECT * FROM products WHERE name LIKE %:keyword%", nativeQuery = true)
        List<Product> searchProductsNative(@Param("keyword") String keyword);
        ```
*   **Pagination và Sorting:**
    *   Sử dụng `Pageable` và `Sort` để phân trang và sắp xếp.
    *   Ví dụ:
        ```java
        Page<Product> products = productRepository.findAll(PageRequest.of(0, 10, Sort.by("price").descending()));
        ```
*   **Auditing (Theo Dõi Tạo/Sửa Entity):**
    *   Tự động ghi lại thông tin về người tạo, người sửa, thời gian tạo, thời gian sửa.
    *   Sử dụng annotations: `@CreatedBy`, `@LastModifiedBy`, `@CreatedDate`, `@LastModifiedDate`.
    *   Cần cấu hình Auditing trong Spring context.
*   **Projections (Chọn Lọc Fields Cần Thiết):**
    *   Truy vấn chỉ một số fields của entity thay vì toàn bộ.
    *   Giảm tải, tăng hiệu suất.
    *   Interface-based projections, Class-based DTO projections.
*   **Specifications (Dynamic Queries):**
    *   Xây dựng truy vấn động dựa trên các điều kiện khác nhau (hữu ích cho search form).
    *   Sử dụng `JpaSpecificationExecutor` và `Specification`.
*   **Querydsl (Type-Safe Queries):**
    *   Thư viện hỗ trợ viết truy vấn type-safe (code-completion, compile-time checking).
    *   Tích hợp tốt với Spring Data JPA.

## IV. Transaction Management

*   **@Transactional Annotation:**
    *   Kiểm soát transaction một cách rõ ràng hơn (thay vì dựa vào default behavior).
    *   `propagation`, `isolation`, `timeout`, `readOnly`.
    *   Ví dụ:
        ```java
        @Transactional(propagation = Propagation.REQUIRED)
        public void saveProduct(Product product) {
            productRepository.save(product);
        }
        ```

## V. Best Practices

*   **Sử Dụng DTOs (Data Transfer Objects):**
    *   Truyền dữ liệu giữa các layers.
    *   Decoupling, không expose entity trực tiếp ra bên ngoài.
*   **Tránh SELECT N+1 Problem:**
    *   Load related entities một cách hiệu quả.
    *   Sử dụng `JOIN FETCH` hoặc `EntityGraph`.
*   **Tối Ưu Truy Vấn:**
    *   Projections, Specifications, Querydsl.
    *   Caching (second-level cache của Hibernate).
*   **Unit Tests:**
    *   Đảm bảo code hoạt động đúng.
    *   `@DataJpaTest`.

## VI. Các Vấn Đề Thường Gặp

*   **LazyInitializationException:** Truy cập lazy-loaded association bên ngoài transaction.
    *   Giải pháp: `JOIN FETCH`, `EntityGraph`, hoặc load dữ liệu trong transaction.
*   **StaleStateException:** Concurrent modification.
    *   Giải pháp: Optimistic locking (versioning) với `@Version`.
*   **Exceptions liên quan đến Database:** Kiểm tra logs, JDBC driver version.

## VII. Kết Luận

*   Spring Data JPA là một công cụ mạnh mẽ để đơn giản hóa việc truy cập dữ liệu.
*   Nắm vững các khái niệm cơ bản và nâng cao để sử dụng hiệu quả.
*   Chú trọng đến performance và best practices.

## **I. Cơ bản (Essential):**

*   **1. JPA (Java Persistence API) Fundamentals:**
    *   **Entities:** Định nghĩa và ánh xạ các đối tượng Java thành các bảng trong cơ sở dữ liệu.  (Annotations: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@Transient`)
    *   **Persistence Context:** Hiểu về `EntityManager`, `EntityManagerFactory` và lifecycle của entity trong persistence context.
    *   **JPQL (Java Persistence Query Language):**  Viết các truy vấn sử dụng JPQL để thao tác với dữ liệu.
    *   **Transactions:** Quản lý giao dịch và đảm bảo tính nhất quán của dữ liệu.

*   **2. Spring Data JPA Repositories:**
    *   **Repository Interfaces:**  Sử dụng các interface như `JpaRepository`, `CrudRepository`, `PagingAndSortingRepository`.
    *   **SimpleJpaRepository Implementation:** Hiểu cơ bản về implement mặc định.
    *   **Query Methods:**  Định nghĩa các query methods bằng cách sử dụng naming conventions (ví dụ: `findByLastName`, `findByEmailAndActive`).
    *   **@Query Annotation:** Sử dụng `@Query` để viết các truy vấn JPQL tùy chỉnh trực tiếp trong repository interface.
    *   **@Param Annotation:** Sử dụng `@Param` để truyền tham số vào các truy vấn JPQL.

*   **3. Data Source Configuration:**
    *   **Spring Data JPA Configuration:** Cấu hình Spring Data JPA với database connection information. (DataSource, EntityManagerFactory)
    *   **Database Drivers:** Chọn và cấu hình driver phù hợp với database.
    *   **Connection Pooling:** Sử dụng connection pooling để tối ưu hiệu suất. (e.g., HikariCP)
    *   **Spring Boot Auto-Configuration:** Hiểu cách Spring Boot tự động cấu hình Spring Data JPA.

*   **4. Basic CRUD Operations:**
    *   **Create (Save):** Lưu các entity mới vào cơ sở dữ liệu.
    *   **Read (Find):** Truy xuất entity từ cơ sở dữ liệu bằng ID hoặc các thuộc tính khác.
    *   **Update (Save):** Cập nhật các entity đã tồn tại trong cơ sở dữ liệu.
    *   **Delete:** Xóa các entity khỏi cơ sở dữ liệu.

*   **5. Testing:**
    *   **Unit Testing Repositories:** Viết các unit test để kiểm tra chức năng của repository.
    *   **Integration Testing:** Viết các integration test để kiểm tra sự tương tác giữa Spring Data JPA và cơ sở dữ liệu.
    *   **Using an Embedded Database (e.g., H2, HSQLDB):**  Sử dụng embedded database cho testing.

## **II. Nâng cao (Advanced):**

*   **1. Advanced Querying:**
    *   **Specification API:** Xây dựng các truy vấn phức tạp và động bằng cách sử dụng Specification API.
    *   **Querydsl Integration:** Sử dụng Querydsl để tạo các truy vấn kiểu type-safe.
    *   **Native Queries:** Thực thi các truy vấn SQL trực tiếp.

*   **2. Relationships and Associations:**
    *   **One-to-One, One-to-Many, Many-to-One, Many-to-Many Relationships:**  Ánh xạ các mối quan hệ giữa các entity.
    *   **Lazy Loading vs. Eager Loading:**  Hiểu và kiểm soát cách các related entities được tải. (Annotations: `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `FetchType.LAZY`, `FetchType.EAGER`)
    *   **Cascade Types:**  Cấu hình cascade operations (e.g., `CascadeType.ALL`, `CascadeType.PERSIST`, `CascadeType.REMOVE`).
    *   **Join Tables:** Quản lý các bảng join cho các mối quan hệ many-to-many.

*   **3. Auditing:**
    *   **@CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy Annotations:**  Tự động theo dõi thông tin tạo và cập nhật entity.
    *   **AuditorAware Interface:**  Cung cấp thông tin người dùng hiện tại cho auditing.

*   **4. Transactions and Concurrency:**
    *   **Transaction Propagation:** Hiểu các mức transaction propagation (e.g., `REQUIRED`, `REQUIRES_NEW`, `NESTED`).
    *   **Optimistic Locking vs. Pessimistic Locking:**  Xử lý concurrency issues. (Annotations: `@Version`, `@Lock`)
    *   **@Transactional Annotation:** Sử dụng `@Transactional` để quản lý giao dịch.

*   **5. Projections:**
    *   **Interface-based Projections:** Định nghĩa interfaces để chỉ lấy các thuộc tính cần thiết từ entity.
    *   **Class-based Projections (DTOs):** Sử dụng Data Transfer Objects (DTOs) để trả về dữ liệu.
    *   **Dynamic Projections:**  Chọn các thuộc tính cần lấy dựa trên tham số truyền vào.

*   **6. Custom Repositories and Fragments:**
    *   **Creating Custom Repository Base Classes:** Tạo repository base classes với các phương thức chung.
    *   **Adding Custom Functionality to Repositories:**  Thêm các phương thức tùy chỉnh vào repository interface.
    *   **Repository Fragments:**  Sử dụng fragments để chia sẻ logic giữa các repository.

*   **7. Performance Tuning:**
    *   **Understanding Query Execution Plans:**  Phân tích query execution plans để tìm điểm nghẽn.
    *   **Using Indexes:** Tạo indexes trên các cột thường được sử dụng trong truy vấn.
    *   **Batch Processing:** Sử dụng batch processing để tăng tốc độ lưu và cập nhật dữ liệu.
    *   **Caching:** Sử dụng caching để giảm số lượng truy vấn database. (e.g., Second-Level Cache - Hibernate)

*   **8. Spring Data REST:**
    *   **Exposing Repositories as REST Endpoints:**  Tự động tạo REST API từ Spring Data JPA repositories.
    *   **Customizing REST API:**  Tùy chỉnh REST API endpoints.
    *   **HATEOAS (Hypermedia as the Engine of Application State):** Sử dụng HATEOAS để làm cho REST API dễ khám phá hơn.

## **III. Chuyên sâu (Expert):**

*   **1. Custom Data Access Strategies:**
    *   **Implementing Custom Repository Factories:**  Tạo các repository factories tùy chỉnh để kiểm soát cách các repository được tạo.
    *   **Integrating with Other Data Access Technologies:**  Kết hợp Spring Data JPA với các công nghệ truy cập dữ liệu khác (e.g., JDBC, MyBatis).

*   **2. Advanced Mapping Techniques:**
    *   **Using Custom Field Types:**  Định nghĩa các field types tùy chỉnh để ánh xạ các kiểu dữ liệu phức tạp.
    *   **Working with Composite Keys:**  Ánh xạ các bảng có composite keys.
    *   **Handling Inheritance Strategies:**  Sử dụng các inheritance strategies (e.g., `SINGLE_TABLE`, `JOINED`, `TABLE_PER_CLASS`).

*   **3. Spring Data Commons:**
    *   **Understanding the Underlying Abstractions:**  Nắm vững các abstraction cơ bản của Spring Data Commons (e.g., `Repositories`, `QueryLookupStrategy`, `EntityInformation`).

*   **4. Reactive Programming with Spring Data JPA:**
    *   **Using Reactive Repositories:**  Sử dụng reactive repositories để xây dựng các ứng dụng non-blocking và responsive.
    *   **Working with Project Reactor:**  Hiểu và sử dụng Project Reactor để thao tác với reactive data streams.


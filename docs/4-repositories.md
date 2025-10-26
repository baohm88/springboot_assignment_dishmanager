# Spring Data JPA Repositories: Chi Tiết

Spring Data JPA Repositories là một tính năng cốt lõi của Spring Data JPA giúp đơn giản hóa việc truy cập dữ liệu và giảm thiểu code boilerplate. Chúng cung cấp một lớp trừu tượng trên JPA `EntityManager`, cho phép bạn tập trung vào logic nghiệp vụ thay vì viết code truy vấn dữ liệu phức tạp.

## 2. Spring Data JPA Repositories

### *   Repository Interfaces: Khám Phá Các Lựa Chọn

*   **Repository Abstraction:** Spring Data JPA repositories cung cấp một abstraction layer cho phép bạn truy cập dữ liệu mà không cần quan tâm đến chi tiết implementation của JPA provider.
*   **Base Interfaces:** Spring Data JPA cung cấp một số interface repository cơ bản:

    *   **`Repository`**:  Base interface cho tất cả Spring Data repositories. Đánh dấu một interface như một repository component.  Không cung cấp bất kỳ phương thức nào.
    *   **`CrudRepository<T, ID>`**:  Cung cấp các phương thức CRUD cơ bản (Create, Read, Update, Delete):
        *   `save(T entity)`: Lưu hoặc cập nhật entity.
        *   `findById(ID id)`: Tìm entity theo ID.
        *   `existsById(ID id)`: Kiểm tra entity có tồn tại theo ID không.
        *   `findAll()`: Lấy tất cả entities.
        *   `delete(T entity)`: Xóa entity.
        *   `deleteById(ID id)`: Xóa entity theo ID.
        *   `count()`: Đếm số lượng entities.
    *   **`PagingAndSortingRepository<T, ID>`**: Kế thừa `CrudRepository` và cung cấp các phương thức để phân trang và sắp xếp kết quả:
        *   `findAll(Pageable pageable)`: Lấy một trang dữ liệu.
        *   `findAll(Sort sort)`: Lấy tất cả entities và sắp xếp.
    *   **`JpaRepository<T, ID>`**:  Kế thừa `PagingAndSortingRepository` và cung cấp các phương thức JPA-specific:
        *   `flush()`: Đồng bộ hóa Persistence Context với cơ sở dữ liệu.
        *   `saveAndFlush(T entity)`: Lưu entity và flush Persistence Context.
        *   `deleteInBatch(Iterable<T> entities)`: Xóa một loạt entities trong một batch.
        *   `deleteAllInBatch()`: Xóa tất cả entities trong một batch.
        *   `getOne(ID id)`: Lấy entity theo ID (lazy-loaded, không khuyến khích sử dụng).
    *   **Custom Repository Interfaces**: Bạn có thể tạo các interface repository tùy chỉnh bằng cách mở rộng các interface trên hoặc tạo các interface hoàn toàn mới.

    **Ví dụ:**

    ```java
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
        // Các phương thức truy vấn tùy chỉnh sẽ được định nghĩa ở đây
    }
    ```

### *   SimpleJpaRepository Implementation: Bên Dưới Lớp Trừu Tượng

*   **Default Implementation:** `SimpleJpaRepository` là implementation mặc định của các Spring Data JPA repository interfaces.
*   **JpaEntityInformation:** `SimpleJpaRepository` sử dụng `JpaEntityInformation` để lấy thông tin về entity, chẳng hạn như tên bảng, ID và các thuộc tính khác.
*   **EntityManager Usage:** Nó sử dụng `EntityManager` để thực hiện các thao tác CRUD.
*   **Customization:** Mặc dù thường không cần thiết, bạn có thể tùy chỉnh `SimpleJpaRepository` bằng cách tạo một subclass và override các phương thức cần thiết.

### *   Query Methods: Sức Mạnh Của Naming Conventions

*   **Deriving Queries from Method Names:**  Spring Data JPA cho phép bạn định nghĩa các truy vấn bằng cách sử dụng naming conventions cho tên phương thức trong repository interface.
*   **Prefixes:**
    *   `findBy...`: Tìm một hoặc nhiều entities dựa trên các thuộc tính được chỉ định.
    *   `existsBy...`: Kiểm tra xem một entity có tồn tại hay không dựa trên các thuộc tính được chỉ định.
    *   `countBy...`: Đếm số lượng entities phù hợp với các thuộc tính được chỉ định.
    *   `deleteBy...`, `removeBy...`: Xóa các entities phù hợp với các thuộc tính được chỉ định.
    *   `findTop3By...`, `findFirst10By...`: Lấy một số lượng entities giới hạn.
*   **Operators:**
    *   `Equals`, `Is`:  So sánh bằng.
    *   `NotEquals`, `IsNot`: So sánh không bằng.
    *   `GreaterThan`, `LessThan`, `GreaterThanEqual`, `LessThanEqual`:  So sánh lớn hơn, nhỏ hơn.
    *   `Between`: Nằm giữa hai giá trị.
    *   `Like`, `StartingWith`, `EndingWith`, `Containing`: Tìm kiếm chuỗi.
    *   `In`, `NotIn`:  Giá trị nằm trong/không nằm trong một danh sách.
    *   `True`, `False`: Kiểm tra giá trị boolean.
    *   `IsNull`, `IsNotNull`: Kiểm tra giá trị null.
    *   `OrderBy`: Sắp xếp kết quả.
*   **Chaining Properties:**  Bạn có thể kết hợp nhiều thuộc tính bằng cách sử dụng `And` và `Or`.
*   **Ví dụ:**

    ```java
    public interface UserRepository extends JpaRepository<User, Long> {
        // Tìm user theo lastName
        List<User> findByLastName(String lastName);

        // Tìm user theo email và active
        User findByEmailAndActive(String email, boolean active);

        // Tìm users có tuổi lớn hơn age và sắp xếp theo firstName
        List<User> findByAgeGreaterThanOrderByFirstName(int age);

        // Kiểm tra xem user có tồn tại với email cụ thể hay không
        boolean existsByEmail(String email);
    }
    ```

### *   `@Query` Annotation: Viết Truy Vấn JPQL Tùy Chỉnh

*   **JPQL Power:** Khi các query methods derived từ naming conventions không đủ mạnh, bạn có thể sử dụng `@Query` annotation để viết các truy vấn JPQL (Java Persistence Query Language) tùy chỉnh trực tiếp trong repository interface.
*   **JPQL vs SQL:**  JPQL thao tác trên entities và thuộc tính của chúng, không phải trên các bảng và cột trong cơ sở dữ liệu.
*   **`nativeQuery = true`:** Nếu bạn muốn viết truy vấn SQL native, hãy đặt `nativeQuery = true`.
*   **Ví dụ:**

    ```java
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    public interface UserRepository extends JpaRepository<User, Long> {
        @Query("SELECT u FROM User u WHERE u.email = :email")
        User findUserByEmail(@Param("email") String email);

        @Query(value = "SELECT * FROM users WHERE first_name LIKE %:keyword%", nativeQuery = true)
        List<User> searchUsersByFirstName(@Param("keyword") String keyword);
    }
    ```

### *   `@Param` Annotation: Truyền Tham Số Vào Truy Vấn

*   **Parameter Binding:** `@Param` annotation được sử dụng để gán tên cho các tham số trong truy vấn JPQL, cho phép bạn truyền giá trị vào truy vấn.
*   **Named Parameters:**  Việc sử dụng `@Param` giúp truy vấn dễ đọc và bảo trì hơn so với việc sử dụng positional parameters.
*   **Ví dụ:** (Xem ví dụ ở trên phần `@Query` Annotation)

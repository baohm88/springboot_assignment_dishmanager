# Sinh Dữ Liệu Mẫu (Data Seeding) trong Spring Boot

Bài viết này hướng dẫn cách tạo script để sinh dữ liệu mẫu vào cơ sở dữ liệu khi ứng dụng Spring Boot khởi động. Điều này hữu ích cho việc testing, demo, hoặc khởi tạo môi trường phát triển.

## Các Cách Tiếp Cận

Có nhiều cách để thực hiện data seeding trong Spring Boot, bao gồm:

1.  **Sử dụng `CommandLineRunner` hoặc `ApplicationRunner`:** Đây là cách tiếp cận đơn giản, cho phép bạn thực thi code khi ứng dụng khởi động.
2.  **Sử dụng `@PostConstruct`:** Annotation này cho phép bạn thực thi code sau khi bean đã được khởi tạo hoàn toàn.
3.  **Sử dụng `data.sql`:** Spring Boot tự động thực thi file `data.sql` khi khởi động (nếu `spring.jpa.hibernate.ddl-auto` được cấu hình phù hợp).
4.  **Sử dụng Liquibase hoặc Flyway:** Đây là các công cụ migration cơ sở dữ liệu mạnh mẽ, cho phép bạn quản lý schema và dữ liệu một cách có cấu trúc.

Bài viết này tập trung vào cách tiếp cận sử dụng `CommandLineRunner`, vì nó đơn giản và dễ hiểu.

## Ví Dụ Chi Tiết: Sử Dụng `CommandLineRunner`

Giả sử chúng ta có một `User` entity và một `UserRepository`:

```java
// User.java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    // Getters and Setters (bắt buộc)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

// UserRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

Đây là code để tạo data seeding script sử dụng `CommandLineRunner`:

```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    @Transactional
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            // Kiểm tra xem database đã có dữ liệu hay chưa
            if (userRepository.count() == 0) {
                // Tạo dữ liệu mẫu
                User user1 = new User();
                user1.setFirstName("John");
                user1.setLastName("Doe");
                user1.setEmail("john.doe@example.com");

                User user2 = new User();
                user2.setFirstName("Jane");
                user2.setLastName("Smith");
                user2.setEmail("jane.smith@example.com");

                User user3 = new User();
                user3.setFirstName("David");
                user3.setLastName("Lee");
                user3.setEmail("david.lee@example.com");

                // Lưu dữ liệu vào database
                userRepository.saveAll(List.of(user1, user2, user3));
            }
        };
    }
}
```

**Giải thích:**

*   **`@Configuration`**: Đánh dấu class này là một class cấu hình Spring.
*   **`@Bean`**: Đánh dấu phương thức `loadData` là một bean.  Spring sẽ tự động gọi phương thức này khi ứng dụng khởi động.
*   **`CommandLineRunner`**: Interface này cho phép bạn thực thi code khi ứng dụng khởi động.
*   **`@Transactional`**: Đảm bảo rằng tất cả các thao tác lưu dữ liệu được thực hiện trong một transaction duy nhất. Điều này giúp đảm bảo tính nhất quán của dữ liệu.
*   **`userRepository.count() == 0`**: Kiểm tra xem database đã có dữ liệu hay chưa. Điều này giúp tránh việc tạo dữ liệu trùng lặp mỗi khi ứng dụng khởi động.
*   **`userRepository.saveAll(List.of(user1, user2, user3))`**: Lưu danh sách các user vào database sử dụng phương thức `saveAll`.

## Cấu Hình

Để data seeding hoạt động, bạn cần cấu hình datasource và JPA trong `application.properties` (hoặc `application.yml`):

```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop # create, update, validate, none
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

**Giải thích:**

*   **`spring.datasource.url`**: Cấu hình URL cho database H2 in-memory.
*   **`spring.jpa.hibernate.ddl-auto`**:
    *   `create-drop`: Tạo schema khi khởi động và xóa schema khi ứng dụng tắt.  Sử dụng cho testing và phát triển.
    *   `update`: Cập nhật schema hiện có. Sử dụng cho môi trường phát triển.
    *   `validate`: Kiểm tra schema hiện có có phù hợp với entity mappings hay không.
    *   `none`: Không làm gì cả. Schema phải được tạo thủ công.
*   **`spring.jpa.properties.hibernate.dialect`**: Chỉ định dialect cho Hibernate (JPA provider).

## Các Lưu Ý Quan Trọng

*   **Môi Trường Production:**  Không nên sử dụng data seeding trong môi trường production. Thay vào đó, sử dụng các công cụ migration cơ sở dữ liệu như Liquibase hoặc Flyway.
*   **Dependency:** Đảm bảo đã thêm đầy đủ các dependencies cần thiết (H2, Spring Data JPA) vào project.
*   **`ddl-auto`:**  Cẩn thận khi sử dụng `spring.jpa.hibernate.ddl-auto`.  Trong môi trường production, bạn nên sử dụng `validate` hoặc `none` để tránh việc Hibernate tự động sửa đổi schema.
*   **Data Dependant:** Nếu bạn cần data seeding phụ thuộc vào data đã có, hãy đảm bảo thứ tự thực thi các seeders. Có thể dùng `@Order` annotation để sắp xếp thứ tự `CommandLineRunner`.
*   **Logging:** Thêm logging vào script để theo dõi quá trình seeding.

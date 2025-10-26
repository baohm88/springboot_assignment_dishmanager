# Hướng dẫn: Unit Testing Spring Data JPA Repositories

Unit testing repositories là một phần quan trọng của việc phát triển ứng dụng Spring Data JPA. Việc này giúp đảm bảo rằng các repository của bạn hoạt động chính xác và các truy vấn được thực hiện như mong đợi. Bài viết này sẽ hướng dẫn bạn cách viết các unit test cho Spring Data JPA repositories.

## Tại sao cần Unit Test Repositories?

*   **Đảm bảo tính chính xác:** Unit test giúp xác minh rằng các phương thức trong repository (ví dụ: các query methods, các truy vấn JPQL) trả về kết quả đúng như mong đợi.
*   **Phát hiện lỗi sớm:** Việc viết unit test giúp phát hiện các lỗi trong code repository sớm trong quá trình phát triển, giảm thiểu rủi ro phát sinh lỗi trong môi trường production.
*   **Refactoring an toàn:** Unit test cung cấp một lưới an toàn khi bạn cần refactor code repository, giúp đảm bảo rằng các thay đổi không ảnh hưởng đến chức năng hiện có.
*   **Tài liệu sống:** Unit test đóng vai trò như một tài liệu sống mô tả cách repository hoạt động và các trường hợp sử dụng của nó.

## Các bước để Unit Testing Spring Data JPA Repositories

1.  **Thêm Dependencies cần thiết:**

    *   `spring-boot-starter-test`: Cung cấp các công cụ cần thiết cho testing trong Spring Boot.
    *   `h2`:  Một database in-memory thường được sử dụng cho unit testing.
        ```xml
        <!-- Maven -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Gradle -->
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testImplementation 'com.h2database:h2'
        ```

2.  **Sử dụng `@DataJpaTest` Annotation:**

    *   `@DataJpaTest` là một annotation của Spring Boot giúp cấu hình một môi trường testing cho các Spring Data JPA repositories.
    *   Nó cấu hình một in-memory database (ví dụ: H2), cấu hình JPA, và tạo ra các bean cần thiết cho testing.
    *   Các thay đổi dữ liệu trong test được rollback sau mỗi test method.
    *   **Chú ý:** `@DataJpaTest` theo mặc định sẽ không scan các `@Component` , `@Service` hay `@Controller`. Do đó, bạn chỉ nên sử dụng annotation này để test các repositories.

    ```java
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;

    @DataJpaTest
    public class UserRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        @Test
        public void testFindByLastName() {
            // Test code sẽ được viết ở đây
        }
    }
    ```

3.  **Tiêm Dependency Repository:**

    *   Sử dụng `@Autowired` để tiêm (inject) repository vào test class.

4.  **Chuẩn Bị Dữ Liệu Test:**

    *   Trước khi thực hiện test, bạn cần chuẩn bị dữ liệu test bằng cách lưu các entity vào cơ sở dữ liệu in-memory.

5.  **Viết Test Cases:**

    *   Sử dụng các assertion methods (ví dụ: `assertEquals`, `assertNotNull`, `assertTrue`) từ thư viện JUnit hoặc AssertJ để kiểm tra kết quả trả về từ repository methods.
    *   Tập trung vào việc kiểm tra các trường hợp sử dụng khác nhau của repository methods.

6.  **Sử Dụng `TestEntityManager` (Tùy Chọn):**

    *   `TestEntityManager` là một tiện ích của Spring Boot giúp thực hiện các thao tác JPA trong môi trường testing.
    *   Nó cung cấp các phương thức tương tự như `EntityManager` (ví dụ: `persist`, `find`, `remove`), nhưng được thiết kế đặc biệt cho testing.
    *   `TestEntityManager` giúp kiểm soát transaction tốt hơn và có thể hữu ích trong các test phức tạp.

## Ví dụ Chi Tiết

Giả sử chúng ta có một `User` entity và một `UserRepository` như sau:

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

    // Getters and Setters
}

// UserRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByLastName(String lastName);
}
```

Đây là một ví dụ về unit test cho `UserRepository`:

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByLastName() {
        // 1. Chuẩn bị dữ liệu test
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);
        entityManager.flush();  // Lưu dữ liệu vào database

        // 2. Thực hiện test
        List<User> foundUsers = userRepository.findByLastName("Doe");

        // 3. Kiểm tra kết quả
        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindByEmail() {
        // 1. Chuẩn bị dữ liệu test
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        entityManager.persist(user);
        entityManager.flush();

        // 2. Thực hiện test
        User foundUser = userRepository.findByEmail("jane.smith@example.com");

        // 3. Kiểm tra kết quả
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getLastName()).isEqualTo("Smith");
    }
}
```

**Giải thích:**

*   **`@DataJpaTest`**: Cấu hình môi trường test cho JPA.
*   **`TestEntityManager`**: Được sử dụng để lưu dữ liệu test vào database.
*   **`userRepository`**: Repository được tiêm vào để thực hiện test.
*   **`testFindByLastName()`**: Test case kiểm tra phương thức `findByLastName()`.
    *   Tạo một `User` entity và lưu nó vào database bằng `entityManager.persist()`.
    *   Gọi phương thức `userRepository.findByLastName()` với giá trị "Doe".
    *   Sử dụng `assertThat` từ AssertJ để kiểm tra kết quả trả về.

## Best Practices

*   **Viết test trước (Test-Driven Development - TDD):**  Viết test cases trước khi viết code repository.
*   **Giữ cho test đơn giản và tập trung:**  Mỗi test case nên chỉ kiểm tra một chức năng cụ thể.
*   **Sử dụng dữ liệu test thực tế:**  Dữ liệu test nên phản ánh dữ liệu thực tế trong ứng dụng của bạn.
*   **Kiểm tra các trường hợp biên:**  Kiểm tra các trường hợp biên (ví dụ: tìm kiếm với giá trị null, tìm kiếm với giá trị không tồn tại) để đảm bảo repository hoạt động đúng trong mọi tình huống.
*   **Sử dụng các assertion libraries mạnh mẽ:**  Sử dụng các assertion libraries như AssertJ hoặc Hamcrest để viết các assertion dễ đọc và bảo trì.
*   **Chạy test thường xuyên:**  Chạy unit test thường xuyên (ví dụ: sau mỗi lần thay đổi code) để phát hiện lỗi sớm.
*   **Sử dụng profile `test` trong `application.properties` (hoặc `application.yml`)**: Cấu hình database H2 và các thuộc tính khác dành riêng cho môi trường test trong profile `test`.

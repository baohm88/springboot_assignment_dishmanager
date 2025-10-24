# JPA (Java Persistence API) Fundamentals: Tổng Quan Chi Tiết

Java Persistence API (JPA) là một đặc tả (specification) trong Java EE (Enterprise Edition) cho việc quản lý dữ liệu bền vững (persistent data) trong các ứng dụng Java. JPA định nghĩa một chuẩn để ánh xạ (mapping) các đối tượng Java (entities) thành các bảng trong cơ sở dữ liệu quan hệ, giúp các nhà phát triển dễ dàng tương tác với cơ sở dữ liệu mà không cần phải viết các câu lệnh SQL phức tạp.

## 1. Entities: Định nghĩa và Ánh Xạ

*   **Entities là gì?**  Entities là các đối tượng Java đại diện cho dữ liệu trong cơ sở dữ liệu. Mỗi entity tương ứng với một bảng trong cơ sở dữ liệu.

*   **Annotations JPA:** Các annotations được sử dụng để ánh xạ các thuộc tính của entity với các cột trong bảng.

    *   **`@Entity`**: Đánh dấu một class là một JPA entity. Class này sẽ được quản lý bởi JPA provider.
        ```java
        @Entity
        public class User {
            // ...
        }
        ```

    *   **`@Table(name = "users")`**:  Chỉ định tên bảng trong cơ sở dữ liệu mà entity này tương ứng. Nếu không có annotation này, tên bảng sẽ mặc định là tên của class entity.
        ```java
        @Entity
        @Table(name = "users")
        public class User {
            // ...
        }
        ```

    *   **`@Id`**:  Đánh dấu một thuộc tính là primary key của entity.
        ```java
        @Entity
        public class User {
            @Id
            private Long id;
            // ...
        }
        ```

    *   **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**:  Chỉ định cách primary key được tạo ra.  Các strategy phổ biến:
        *   `GenerationType.IDENTITY`: Tự động tăng (auto-increment) bởi cơ sở dữ liệu.
        *   `GenerationType.SEQUENCE`: Sử dụng một sequence để tạo giá trị.
        *   `GenerationType.TABLE`:  Sử dụng một bảng để lưu trữ giá trị.
        *   `GenerationType.AUTO`:  JPA Provider tự động chọn strategy phù hợp.
        ```java
        @Entity
        public class User {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            // ...
        }
        ```

    *   **`@Column(name = "first_name", nullable = false, length = 50)`**:  Ánh xạ một thuộc tính với một cột trong bảng. Các thuộc tính tùy chọn:
        *   `name`: Tên cột.
        *   `nullable`: Xác định cột có thể null hay không (mặc định là `true`).
        *   `length`: Độ dài tối đa của cột (cho kiểu String).
        *   `unique`: Xác định cột là unique hay không (mặc định là `false`).
        ```java
        @Entity
        public class User {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @Column(name = "first_name", nullable = false, length = 50)
            private String firstName;
            // ...
        }
        ```

    *   **`@Transient`**:  Đánh dấu một thuộc tính không được ánh xạ vào cơ sở dữ liệu. Thuộc tính này chỉ tồn tại trong Java object.
        ```java
        @Entity
        public class User {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @Transient
            private String fullName; // Không được lưu vào DB

            public String getFullName() {
                return firstName + " " + lastName;
            }
            // ...
        }
        ```

## 2. Persistence Context: Quản Lý Entities

*   **Persistence Context là gì?**  Persistence Context là một môi trường quản lý các entity instances.  Nó theo dõi các thay đổi được thực hiện trên các entity và đồng bộ hóa những thay đổi này với cơ sở dữ liệu.

*   **`EntityManagerFactory`**:  Là một factory class tạo ra các `EntityManager` instances.  Thường chỉ có một `EntityManagerFactory` cho mỗi ứng dụng.

*   **`EntityManager`**:  Interface chính để tương tác với Persistence Context. Nó cung cấp các phương thức để:
    *   `persist(entity)`: Lưu một entity mới vào cơ sở dữ liệu.
    *   `find(EntityClass, primaryKey)`: Tìm một entity dựa trên primary key.
    *   `merge(entity)`: Cập nhật một entity đã tồn tại trong cơ sở dữ liệu.
    *   `remove(entity)`: Xóa một entity khỏi cơ sở dữ liệu.
    *   `createQuery(jpqlQuery)`: Tạo một JPQL query.

*   **Lifecycle của Entity:**

    *   **New/Transient:** Một entity vừa được tạo và chưa được quản lý bởi Persistence Context.
    *   **Managed/Persistent:** Một entity đang được quản lý bởi Persistence Context. Bất kỳ thay đổi nào được thực hiện trên entity này sẽ được tự động đồng bộ hóa với cơ sở dữ liệu khi transaction commit.
    *   **Detached:**  Một entity đã từng được quản lý bởi Persistence Context nhưng không còn nữa. Ví dụ: Persistence Context đã bị đóng hoặc entity đã bị evicted.
    *   **Removed:**  Một entity đã được đánh dấu để xóa khỏi cơ sở dữ liệu. Entity sẽ bị xóa khi transaction commit.

## 3. JPQL (Java Persistence Query Language): Truy Vấn Dữ Liệu

*   **JPQL là gì?** JPQL là một ngôn ngữ truy vấn hướng đối tượng (object-oriented query language) được sử dụng để truy vấn dữ liệu từ cơ sở dữ liệu quan hệ thông qua các entity objects.  JPQL tương tự như SQL nhưng nó thao tác với các entity và thuộc tính của chúng thay vì các bảng và cột.

*   **Cú pháp JPQL:**

    *   **`SELECT`**:  Chọn các entity hoặc thuộc tính để trả về.
    *   **`FROM`**: Chỉ định entity class để truy vấn.
    *   **`WHERE`**: Lọc kết quả dựa trên các điều kiện.
    *   **`ORDER BY`**: Sắp xếp kết quả.
    *   **`GROUP BY`**:  Nhóm kết quả.
    *   **`JOIN`**:  Kết hợp các entity.

*   **Ví dụ:**

    ```java
    EntityManager em = emf.createEntityManager();
    try {
        // Truy vấn tất cả users
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();

        // Truy vấn users có firstName là 'John'
        List<User> johns = em.createQuery("SELECT u FROM User u WHERE u.firstName = :firstName", User.class)
            .setParameter("firstName", "John")
            .getResultList();

        // Truy vấn số lượng users
        Long count = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
    } finally {
        em.close();
    }
    ```

## 4. Transactions: Đảm Bảo Tính Nhất Quán

*   **Transaction là gì?** Transaction là một đơn vị công việc logic bao gồm một hoặc nhiều thao tác trên cơ sở dữ liệu. Transaction đảm bảo rằng tất cả các thao tác trong transaction được thực hiện thành công hoặc không thao tác nào được thực hiện (Atomicity). Nó cũng đảm bảo rằng dữ liệu luôn ở trạng thái nhất quán (Consistency), các thay đổi được cô lập (Isolation) và dữ liệu được lưu giữ bền vững (Durability).

*   **Quản lý Transaction:**

    *   **`em.getTransaction().begin()`**: Bắt đầu một transaction.
    *   **`em.persist(entity)`**, **`em.merge(entity)`**, **`em.remove(entity)`**: Thực hiện các thao tác trên cơ sở dữ liệu.
    *   **`em.getTransaction().commit()`**:  Commit transaction.  Tất cả các thay đổi được thực hiện trong transaction sẽ được lưu vào cơ sở dữ liệu.
    *   **`em.getTransaction().rollback()`**:  Rollback transaction.  Tất cả các thay đổi được thực hiện trong transaction sẽ bị hủy bỏ.

*   **Ví dụ:**

    ```java
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();

    try {
        transaction.begin();

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        em.persist(user);

        transaction.commit();
    } catch (Exception e) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
        e.printStackTrace();
    } finally {
        em.close();
    }
    ```

*   **Declarative Transaction Management:**  Spring Framework cung cấp một cách dễ dàng hơn để quản lý transactions bằng cách sử dụng annotations.  Sử dụng `@Transactional` annotation để chỉ định rằng một phương thức hoặc class cần được thực thi trong một transaction.

```java
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createUser(User user) {
        em.persist(user);
    }
}
```
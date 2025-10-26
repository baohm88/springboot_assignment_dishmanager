Xây dựng một mô hình dữ liệu cho một blog :

*   **User:** Người dùng.
*   **UserProfile:** Thông tin chi tiết của người dùng.
*   **Post:** Bài viết.
*   **Tag:** Thẻ (nhãn) cho bài viết.

Các mối quan hệ sẽ được thể hiện:
1.  **One-to-One:** `User` ↔ `UserProfile` (Một User chỉ có một UserProfile và ngược lại).
2.  **One-to-Many:** `User` → `Post` (Một User có thể viết nhiều Post).
3.  **Many-to-One:** `Post` → `User` (Nhiều Post thuộc về một User). Đây là mặt còn lại của quan hệ One-to-Many.
4.  **Many-to-Many:** `Post` ↔ `Tag` (Một Post có thể có nhiều Tag, và một Tag có thể được gán cho nhiều Post).

---

### 1. Cấu trúc dự án và Dependencies (`pom.xml`)

Bạn cần `spring-boot-starter-data-jpa` và một driver database. Chúng ta sẽ dùng H2 (in-memory database) để dễ dàng chạy thử mà không cần cài đặt gì.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version> <!-- Hoặc phiên bản của bạn -->
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>jpa-relationships</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>jpa-relationships-demo</name>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <!-- Core dependency cho JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- H2 In-memory Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok để giảm code boilerplate -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

### 2. Cấu hình (`application.properties`)

Cấu hình để kết nối H2 và bật H2 console để bạn có thể xem dữ liệu trong trình duyệt.

`src/main/resources/application.properties`
```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console
spring.h2.console.enabled=true

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# create-drop: tự động tạo bảng khi khởi động và xóa khi tắt
# update: sẽ cập nhật schema nếu có thay đổi
spring.jpa.hibernate.ddl-auto=create-drop
# Hiển thị câu lệnh SQL được tạo ra trong console, rất hữu ích để học và debug
spring.jpa.show-sql=true
```

---

### 3. Khai báo các Entity

#### a. Quan hệ One-to-One: `User` và `UserProfile`

Trong quan hệ này, một bên sẽ "sở hữu" mối quan hệ (chứa khóa ngoại). Chúng ta sẽ để bảng `users` chứa khóa ngoại đến `user_profiles`.

`src/main/java/com/example/jparelationships/entity/User.java`
```java
package com.example.jparelationships.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // --- One-to-One ---
    // User là bên sở hữu quan hệ
    @OneToOne(cascade = CascadeType.ALL) // cascade: Khi save User, Profile cũng sẽ được save.
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id") // Tạo cột user_profile_id trong bảng users
    private UserProfile userProfile;

    // --- One-to-Many ---
    // mappedBy: chỉ ra rằng quan hệ này được quản lý bởi thuộc tính "author" trong class Post.
    // User không có cột foreign key đến Post.
    @OneToMany(
        mappedBy = "author",
        cascade = CascadeType.ALL, // Thao tác trên User sẽ ảnh hưởng đến các Post của nó
        orphanRemoval = true // Nếu một Post bị xóa khỏi list này, nó sẽ bị xóa khỏi DB
    )
    private List<Post> posts = new ArrayList<>();

    // Helper method để giữ 2 chiều của quan hệ được đồng bộ
    public void addPost(Post post) {
        posts.add(post);
        post.setAuthor(this);
    }
}
```

`src/main/java/com/example/jparelationships/entity/UserProfile.java`
```java
package com.example.jparelationships.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String phoneNumber;

    // --- One-to-One (Inverse side) ---
    // mappedBy: chỉ ra rằng quan hệ này được sở hữu bởi thuộc tính "userProfile" trong class User.
    // Không tạo thêm cột foreign key ở bảng này.
    @OneToOne(mappedBy = "userProfile")
    private User user;
}
```

#### b. Quan hệ One-to-Many / Many-to-One: `User` và `Post`

Chúng ta đã khai báo phần `One-to-Many` trong `User`. Giờ là phần `Many-to-One` trong `Post`.

`src/main/java/com/example/jparelationships/entity/Post.java`
```java
package com.example.jparelationships.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // --- Many-to-One ---
    // Post là bên sở hữu quan hệ (chứa khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: chỉ load User khi thực sự cần đến
    @JoinColumn(name = "author_id") // Tạo cột author_id trong bảng posts
    private User author;

    // --- Many-to-Many ---
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "post_tags", // Tên bảng trung gian
        joinColumns = @JoinColumn(name = "post_id"), // Khóa ngoại đến bảng posts
        inverseJoinColumns = @JoinColumn(name = "tag_id") // Khóa ngoại đến bảng tags
    )
    private Set<Tag> tags = new HashSet<>();

    // Helper method để đồng bộ
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPosts().add(this);
    }
}
```

#### c. Quan hệ Many-to-Many: `Post` và `Tag`

Chúng ta đã khai báo phía `Post`. Giờ là phía `Tag`.

`src/main/java/com/example/jparelationships/entity/Tag.java`
```java
package com.example.jparelationships.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // --- Many-to-Many (Inverse side) ---
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();
}
```

---

### 4. Khai báo các Repository

Tạo các interface để Spring Data JPA tự động cung cấp các phương thức CRUD.

`src/main/java/com/example/jparelationships/repository/UserRepository.java`
```java
package com.example.jparelationships.repository;

import com.example.jparelationships.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
```

`src/main/java/com/example/jparelationships/repository/PostRepository.java`
```java
package com.example.jparelationships.repository;

import com.example.jparelationships.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
```

`src/main/java/com/example/jparelationships/repository/TagRepository.java`
```java
package com.example.jparelationships.repository;

import com.example.jparelationships.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}
```

---

### 5. Cách lưu dữ liệu (Ví dụ)

Để chạy thử, chúng ta sẽ tạo một `CommandLineRunner`. Bean này sẽ tự động chạy khi ứng dụng khởi động xong, rất tiện để chèn dữ liệu mẫu.

`src/main/java/com/example/jparelationships/DataInitializer.java`
```java
package com.example.jparelationships;

import com.example.jparelationships.entity.Post;
import com.example.jparelationships.entity.Tag;
import com.example.jparelationships.entity.User;
import com.example.jparelationships.entity.UserProfile;
import com.example.jparelationships.repository.PostRepository;
import com.example.jparelationships.repository.TagRepository;
import com.example.jparelationships.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public DataInitializer(UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional // Đảm bảo tất cả các thao tác đều nằm trong một transaction
    public void run(String... args) throws Exception {
        System.out.println("--- Starting Data Initialization ---");

        // === 1. Lưu quan hệ One-to-One ===
        System.out.println("Saving One-to-One relationship...");
        User user1 = new User();
        user1.setUsername("john.doe");

        UserProfile profile1 = new UserProfile();
        profile1.setAddress("123 Main St");
        profile1.setPhoneNumber("555-1234");
        
        // Liên kết 2 chiều
        user1.setUserProfile(profile1);
        profile1.setUser(user1);
        
        // Chỉ cần save user, userProfile sẽ được save theo nhờ CascadeType.ALL
        userRepository.save(user1);
        System.out.println("User and UserProfile saved.");


        // === 2. Lưu quan hệ One-to-Many ===
        System.out.println("\nSaving One-to-Many relationship...");
        User user2 = new User();
        user2.setUsername("jane.smith");

        Post post1 = new Post();
        post1.setTitle("First Post by Jane");
        post1.setContent("This is the content of the first post.");

        Post post2 = new Post();
        post2.setTitle("Second Post by Jane");
        post2.setContent("Content of the second post.");

        // Sử dụng helper method để liên kết
        user2.addPost(post1);
        user2.addPost(post2);

        // Lưu user2, các post cũng sẽ được lưu theo
        userRepository.save(user2);
        System.out.println("User and their Posts saved.");


        // === 3. Lưu quan hệ Many-to-Many ===
        System.out.println("\nSaving Many-to-Many relationship...");

        // Tạo và lưu các Tag trước
        Tag tagJava = new Tag();
        tagJava.setName("Java");
        Tag tagSpring = new Tag();
        tagSpring.setName("Spring Boot");
        tagRepository.save(tagJava);
        tagRepository.save(tagSpring);
        
        // Lấy một Post đã có (ví dụ post1 của jane.smith)
        Post existingPost = postRepository.findById(post1.getId()).orElseThrow();

        // Sử dụng helper method để thêm tags vào post
        existingPost.addTag(tagJava);
        existingPost.addTag(tagSpring);
        
        // Lưu lại post để cập nhật quan hệ trong bảng trung gian
        postRepository.save(existingPost);
        System.out.println("Post and Tags relationship saved.");

        System.out.println("\n--- Data Initialization Finished ---");
    }
}
```

---

### 6. Chạy và kiểm tra

1.  Chạy ứng dụng Spring Boot của bạn.
2.  Mở trình duyệt và truy cập H2 Console: `http://localhost:8080/h2-console`
3.  Nhập thông tin kết nối như trong file `application.properties`:
    *   **JDBC URL:** `jdbc:h2:mem:testdb`
    *   **User Name:** `sa`
    *   **Password:** `password`
4.  Nhấn **Connect**.
5.  Bây giờ bạn có thể thấy các bảng đã được tạo: `USERS`, `USER_PROFILES`, `POSTS`, `TAGS`, và bảng trung gian `POST_TAGS`. Bạn có thể chạy các câu lệnh SQL để xem dữ liệu đã được chèn vào đúng như mong đợi.

Ví dụ: `SELECT * FROM USERS;`, `SELECT * FROM POSTS;`, `SELECT * FROM POST_TAGS;`
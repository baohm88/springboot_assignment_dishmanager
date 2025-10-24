-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 24, 2025 at 08:44 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hello_t2404e_springboot`
--

-- --------------------------------------------------------

--
-- Table structure for table `dishes`
--

CREATE TABLE `dishes` (
  `id` varchar(16) NOT NULL,
  `description` text NOT NULL,
  `image_url` text NOT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `name` varchar(250) NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `status` enum('DELETED','ON_SALE','STOPPED') NOT NULL,
  `category_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dishes`
--

INSERT INTO `dishes` (`id`, `description`, `image_url`, `last_modified_date`, `name`, `price`, `start_date`, `status`, `category_id`) VALUES
('MN001', 'Sườn heo tẩm ướp đậm vị', 'https://cdn.tgdd.vn/2021/03/CookProduct/CookyVN-CachlamSUONNUONGNGUVIsieungonchobuacomnhadamda-CookyTV1-14screenshot(1)-1200x676.jpg', '2025-10-25 01:08:39.000000', 'Sườn nướng BBQ', 150000, '2025-10-25 01:08:39.000000', 'ON_SALE', 1),
('MN0011', 'Thịt heo nướng than hoa ăn kèm bún, rau sống và nước mắm chua ngọt.', 'https://picsum.photos/200?buncha', '2025-10-25 01:41:13.000000', 'Bún chả Hà Nội', 6, '2025-10-25 01:41:13.000000', 'ON_SALE', 1),
('MN0012', 'Thịt heo ướp sả ớt, nướng vàng thơm trên than hồng.', 'https://picsum.photos/200?thitxien', '2025-10-25 01:41:13.000000', 'Thịt xiên nướng', 4, '2025-10-25 01:41:13.000000', 'ON_SALE', 1),
('MN0013', 'Gà được ướp mật ong, nướng chín vàng, thơm phức.', 'https://picsum.photos/200?ganuong', '2025-10-25 01:41:13.000000', 'Gà nướng mật ong', 6, '2025-10-25 01:41:13.000000', 'ON_SALE', 1),
('MN0014', 'Thịt ba chỉ luộc chín, ăn cùng mắm tôm hoặc nước mắm gừng.', 'https://picsum.photos/200?thitluoc', '2025-10-25 01:41:13.000000', 'Thịt ba chỉ luộc', 4, '2025-10-25 01:41:13.000000', 'ON_SALE', 2),
('MN0015', 'Gà ta luộc chín tới, chấm muối tiêu chanh và lá chanh thái chỉ.', 'https://picsum.photos/200?galuoc', '2025-10-25 01:41:13.000000', 'Gà luộc lá chanh', 7, '2025-10-25 01:41:13.000000', 'ON_SALE', 2),
('MN002', 'Bò Mỹ nhập khẩu, sốt tiêu xanh', 'https://martfood.vn/wp-content/uploads/2021/06/cach-lam-bo-sot-tieu-xanh.jpg', '2025-10-25 01:08:39.000000', 'Bò bít tết sốt tiêu xanh', 250000, '2025-10-25 01:08:39.000000', 'ON_SALE', 1),
('MN003', 'Món chay thanh đạm', 'https://thuanchay.vn/wp-content/uploads/2024/09/mon-chay-thanh-dam.webp', '2025-10-25 01:08:39.000000', 'Đậu hũ chay sốt nấm', 80000, '2025-10-25 01:08:39.000000', 'ON_SALE', 3),
('MN004', 'Thịt heo nướng than hoa ăn kèm bún, rau sống và nước mắm chua ngọt.', 'https://picsum.photos/200?buncha', '2025-10-25 01:29:38.000000', 'Bún chả Hà Nội', 40000, '2025-10-25 01:29:38.000000', 'ON_SALE', 1),
('MN005', 'Thịt heo nướng than hoa ăn kèm bún, rau sống và nước mắm chua ngọt.', 'https://picsum.photos/200?buncha', '2025-10-25 01:30:50.000000', 'Bún chả Hà Nội 1', 40000, '2025-10-25 01:30:50.000000', 'ON_SALE', 1),
('MN006', 'Đậu hũ chiên giòn, sốt cà chua chua ngọt thanh mát.', 'https://picsum.photos/200?dauhu', '2025-10-25 01:41:13.000000', 'Đậu hũ sốt cà chua', 3, '2025-10-25 01:41:13.000000', 'ON_SALE', 3),
('MN007', 'Rau củ tươi xào với nấm và đậu hũ, ít dầu, giữ nguyên vị ngọt tự nhiên.', 'https://picsum.photos/200?xaochay', '2025-10-25 01:41:13.000000', 'Rau củ xào chay', 4, '2025-10-25 01:41:13.000000', 'ON_SALE', 3),
('MN008', 'Thức uống dân dã của người Việt, giải khát nhanh, thanh mát.', 'https://picsum.photos/200?trada', '2025-10-25 01:41:13.000000', 'Trà đá', 1, '2025-10-25 01:41:13.000000', 'ON_SALE', 4),
('MN009', 'Cà phê đậm đà pha cùng sữa đặc, thêm đá mát lạnh.', 'https://picsum.photos/200?caphe', '2025-10-25 01:41:13.000000', 'Cà phê sữa đá', 2, '2025-10-25 01:41:13.000000', 'ON_SALE', 4),
('MN010', 'Sinh tố bơ sánh mịn, béo ngậy, tốt cho sức khỏe.', 'https://picsum.photos/200?sinhb', '2025-10-25 01:41:13.000000', 'Sinh tố bơ', 3, '2025-10-25 01:41:13.000000', 'ON_SALE', 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dishes`
--
ALTER TABLE `dishes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgbu6erefir17660qutbbjnma7` (`category_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `dishes`
--
ALTER TABLE `dishes`
  ADD CONSTRAINT `FKgbu6erefir17660qutbbjnma7` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

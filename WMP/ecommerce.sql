-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 16 Des 2024 pada 16.02
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecommerce`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `product_name` varchar(50) DEFAULT NULL,
  `product_brand` varchar(50) DEFAULT NULL,
  `product_type` varchar(50) DEFAULT NULL,
  `product_price` double DEFAULT NULL,
  `product_image` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `products`
--

INSERT INTO `products` (`id`, `product_name`, `product_brand`, `product_type`, `product_price`, `product_image`) VALUES
(1, 'Nike ACG Mountain Fly', 'Nike', 'Shoes', 220.16, 'uploads/6755baeb8da67.jpg'),
(2, 'Nike Dunk Low Knicks Blue Orange', 'Nike', 'Shoes', 225.15, 'uploads/6755bb271eedb.jpg'),
(3, 'Nike Air Max TN Plus', 'Nike', 'Shoes', 250.25, 'uploads/6755bb4bcccab.jpg'),
(4, 'Nike Sportswear Club', 'Nike', 'Jacket', 155.45, 'uploads/6755bb71ed30b.jpg'),
(5, 'Nike ACG \"Chena Vortex\"', 'Nike', 'Pants', 100.65, 'uploads/6755bb9f8938f.jpg'),
(6, 'Nike Nocta', 'Nike', 'Jacket', 225, 'uploads/6755bbd762449.jpg'),
(7, 'NIKE TECH MEN WOVEN ', 'Nike', 'Pants', 145, 'uploads/6755bbfa2ea04.jpg'),
(8, 'Nike Tech Men Woven Jacket', 'Nike', 'Jacket', 225, 'uploads/6755bc23e3b56.jpg'),
(9, 'Nike Culture of Football Mens', 'NIke', 'Jacket', 267.78, 'uploads/6755bc44dc3d1.jpg'),
(10, 'LeBron XXII \"Tunnel Vision\"', 'Nike', 'Shoes', 335.54, 'uploads/6755bc6a9ad05.jpg'),
(12, 'Samba OG Shoes', 'Adidas', 'Shoes', 250, 'uploads/67603cb639313.jpg'),
(13, 'Adidas Gazelle Indoor White', 'Adidas', 'Shoes', 215.45, 'uploads/67603df04911f.jpg'),
(14, 'Adidas Adizero Prime X', 'Adidas', 'Shoes', 275.45, 'uploads/67603f151add3.jpg'),
(15, 'EQT Windbreaker', 'Adidas', 'Jacket', 310, 'uploads/676040838c73f.jpg'),
(16, 'Adidas PARKA SPLZ F.C', 'Adidas', 'Jacket', 350.65, 'uploads/676040cf112f3.jpg');

-- --------------------------------------------------------

--
-- Struktur dari tabel `recommend`
--

CREATE TABLE `recommend` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `point` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `recommend`
--

INSERT INTO `recommend` (`id`, `user_id`, `product_id`, `point`) VALUES
(1, 1, 4, 1),
(2, 6, 4, 1),
(3, 6, 3, 1),
(4, 6, 5, 1),
(5, 6, 6, 1),
(6, 6, 5, 1),
(7, 6, 5, 1),
(8, 6, 6, 1),
(9, 6, 7, 1),
(10, 6, 8, 1),
(11, 6, 8, 1),
(12, 6, 8, 1),
(13, 6, 8, 1),
(14, 6, 8, 1),
(15, 6, 10, 1),
(16, 6, 10, 1),
(17, 1, 10, 1),
(18, 14, 10, 1),
(19, 6, 1, 1),
(20, 1, 1, 1),
(21, 1, 9, 1),
(22, 1, 9, 1),
(23, 1, 9, 1),
(24, 1, 9, 1),
(25, 1, 8, 1),
(26, 1, 8, 1),
(27, 1, 8, 1),
(28, 1, 8, 1),
(29, 1, 7, 1),
(30, 1, 7, 1),
(31, 1, 7, 1),
(32, 1, 7, 1),
(33, 1, 7, 1),
(34, 1, 7, 1),
(35, 1, 7, 1),
(36, 1, 7, 1),
(37, 1, 7, 1),
(38, 6, 6, 1),
(39, 6, 6, 1),
(40, 6, 6, 1),
(41, 6, 6, 1),
(42, 6, 6, 1),
(43, 6, 6, 1),
(44, 6, 6, 1),
(45, 6, 6, 1),
(46, 6, 6, 1),
(47, 6, 6, 1),
(48, 6, 8, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `regist`
--

CREATE TABLE `regist` (
  `id` int(11) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `regist`
--

INSERT INTO `regist` (`id`, `username`, `email`, `password`) VALUES
(1, 'Viko', 'vikoadrian@gmail.com', '$2y$10$DfIN14tJiMpj/UsPRZ5vS.6kfOU7nhwdwCQOc9xwispdIDVWQXwr6'),
(3, 'adrian', 'adrian@gmail.com', '$2y$10$VNNtVBc5e8kzeHahdDRA3ecqeXL7omPLASccT9P/tmpcTgr9pZHrm'),
(5, 'vksjs', 'silaban@gmail.com', '$2y$10$QpMz9j2VKTDcPEcVwRbZhe.Ta1A6vd/KOg.oIzSe2App294/wj5sm'),
(6, 'satria', 'satria@gmail.com', '$2y$10$5tkpTiRJcZPXJ3wZD7tejutgwPaAM75lpXVC/Zhtqo7eMewFfFof6'),
(7, 'vikoajalah', 'vikoajalah@gmail.com', '$2y$10$7RAq2Pya3GUnyCa/JflgS.uSXYizPOP7NjNd87KhvZGmz5c9aTeR6'),
(8, 'test', 'test@example.com', '$2y$10$k/VkFgGZSJET6Cb.XnevxONUSWq.H1t5clPWFa0cuIkHQKZiCQZu.'),
(9, NULL, 'satria@gmail.com', '$2y$10$XwlVQnbFs8o/ODFRz2EUyukAjbw0KZA1yJqaOFcNRQqiVbUZ4OIG6'),
(10, 'vikolagi', 'viko@gmail.com', '$2y$10$2P23c7g9ozIGndDQIiQkQuiiJ8RLI3wpgtRjgUA9AT.YIlz7h70ra'),
(11, 'vikolagi', 'viko@gmail.com', '$2y$10$VP/vs5oYvCRf3fgiYaAZluVWeHc/QeFZZ5kHla5Zw57BFzLVm4Sa6'),
(12, 'vikolagi', 'viko@gmail.com', '$2y$10$Lj/GG7lCHqi1Xm47X9ROC.NDbrBsCgQnOGr2IPenbx.NdsTqI0cLK'),
(13, NULL, NULL, '$2y$10$MDlF4SMZuZ6SOKyzdVtfHu11dhCbgLBD6Fj2RJtxtezv9IvoCrpRy'),
(14, 'moshe', 'moshe@gmail.com', '$2y$10$aNnK.jf8jSrYHMK46Yu/IOk78hZUhFx6JJJPe2uaVCBCrr1NIMkZy'),
(15, '', '', '$2y$10$ZmgibBZw/vFqvq6OkSMlg.W464U/tYAzOfaiCfJdk/LueLSo66.Oq'),
(16, 'Viko', 'vikoadrian25@gmail.com', '$2y$10$AqozcA8SXHLgSd1cfJ.tcOygEzHV7vpAbYTtX4tS.4WZYor0xRM82'),
(17, 'derryl', 'derryl@gmail.com', '$2y$10$JWXOfvCQX4pGB/Ww5DnUrOSZ36v6PTXWTopxrlTu1n/OHBCE0280W'),
(18, 'test', 'test@gmail.com', '$2y$10$31OeYOPeX83SdNyGDZKRmOLSlR8c6HiZR0uQNn7AURgYanPpBqn/W'),
(19, 'satria', 'satria123@gmail.com', '$2y$10$Ul8HaVpO12JDZ5M0RTS74usNsCrCgAHI2HXnuPNyfgomvqWUwWapu');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `recommend`
--
ALTER TABLE `recommend`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indeks untuk tabel `regist`
--
ALTER TABLE `regist`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT untuk tabel `recommend`
--
ALTER TABLE `recommend`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT untuk tabel `regist`
--
ALTER TABLE `regist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `regist` (`id`);

--
-- Ketidakleluasaan untuk tabel `recommend`
--
ALTER TABLE `recommend`
  ADD CONSTRAINT `recommend_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

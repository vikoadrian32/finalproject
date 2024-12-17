<?php
header('Content-Type: application/json');
include("dbconnect.php");

$query = $_GET['query']; 

$sql = " SELECT * FROM products WHERE product_name LIKE '%$query%' "; 
$result = $conn->query($sql);

$products = array();
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        // Menambahkan setiap produk ke dalam array
        $products[] = $row;
    }
}

// Mengembalikan data dalam format JSON
echo json_encode($products);

// Menutup koneksi
$conn->close();
?>

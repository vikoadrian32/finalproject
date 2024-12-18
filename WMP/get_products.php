<?php
header('Content-Type: application/json');
include("dbconnect.php");

$query = "SELECT * FROM products ORDER BY product_name asc";
$result = mysqli_query($conn, $query);

if($result){
    while ($row = mysqli_fetch_assoc($result)) {
        $products[] = $row;
    }
    echo json_encode([
        "success" => true,
        "message" => "Data produk berhasil diambil",
        "data" => $products
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal mengambil data produk"
    ]);
}

mysqli_close($conn);


?>
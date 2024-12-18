<?php
header('Content-Type: application/json');
include("dbconnect.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $product_name = $_POST["product_name"];
    $product_brand = $_POST["product_brand"];
    $product_type = $_POST["product_type"];
    $product_price = $_POST["product_price"];
    $product_image = $_POST["product_image"]; // Gambar dalam base64

    $image_path = "uploads/" . uniqid() . ".jpg";
    if (file_put_contents($image_path, base64_decode($product_image))) {
        $query = "INSERT INTO products (product_name, product_brand, product_price, product_type, product_image) VALUES (?, ?, ?, ?, ?)";
        $stmt = mysqli_prepare($conn, $query);

        if ($stmt) {
            mysqli_stmt_bind_param($stmt, "ssdss", $product_name, $product_brand, $product_price, $product_type, $image_path);
            if (mysqli_stmt_execute($stmt)) {
                echo json_encode([
                    "success" => true,
                    "message" => "Produk berhasil ditambahkan"
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "Gagal menyimpan produk"
                ]);
            }

            mysqli_stmt_close($stmt);
        } else {
            echo json_encode([
                "success" => false,
                "message" => "Gagal menyiapkan query"
            ]);
        }
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Gagal menyimpan gambar"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Metode HTTP tidak valid"
    ]);
}


mysqli_close($conn);
?>

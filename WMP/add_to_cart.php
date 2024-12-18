<?php
header('Content-Type: application/json');
include("dbconnect.php"); 
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $user_id = $_POST["user_id"];
    $product_id = $_POST['product_id'];
    $product_price = $_POST['product_price'];
    $product_price = floatval(str_replace(',', '.', $product_price));
    $stmt = $conn->prepare("INSERT INTO cart (user_id,product_id, product_price) VALUES (?,?, ?)");
    if ($stmt) {
        $stmt->bind_param("iid", $user_id,$product_id, $product_price);
        if ($stmt->execute()) {
            echo json_encode(["message" => "Produk berhasil ditambahkan ke cart"]);
        } else {
            echo json_encode(["error" => "Gagal menambahkan produk: " . $stmt->error]);
        }
        $stmt->close();
    } else {
        echo json_encode(["error" => "Gagal menyiapkan statement: " . $conn->error]);
    }
} else {
    echo json_encode(["error" => "Hanya menerima request POST"]);
}

mysqli_close($conn);
?>
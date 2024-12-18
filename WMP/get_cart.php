<?php
header('Content-Type: application/json');
include("dbconnect.php");

// Ambil user_id dari parameter GET atau POST
$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if ($user_id > 0) {
    $query = "
    SELECT 
        c.user_id,
        c.product_id,
        p.product_name,
        p.product_brand,
        c.product_price,
        p.product_image,
        COUNT(c.product_id) AS quantity
    FROM cart AS c
    JOIN products AS p ON c.product_id = p.id
    WHERE c.user_id = ?
    GROUP BY c.user_id, c.product_id, p.product_name, p.product_brand, c.product_price, p.product_image;
    ";

    $stmt = $conn->prepare($query);

    if ($stmt) {
        $stmt->bind_param("i", $user_id);
        
        if ($stmt->execute()) {
            $result = $stmt->get_result();
            $products = [];

            while ($row = $result->fetch_assoc()) {
                $products[] = $row;
            }

            echo json_encode([
                "success" => true,
                "message" => "Data cart berhasil diambil",
                "data" => $products
            ]);
        } else {
            echo json_encode([
                "success" => false,
                "message" => "Gagal mengeksekusi query: " . $stmt->error
            ]);
        }

        $stmt->close();
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Gagal menyiapkan statement: " . $conn->error
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Parameter user_id tidak valid"
    ]);
}

mysqli_close($conn);
?>

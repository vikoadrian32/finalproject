<?php
header('Content-Type: application/json');
include("dbconnect.php");
if (isset($_POST['product_ids']) && isset($_POST['total_price'])) {
    $product_ids = $_POST['product_ids']; 
    $total_price = $_POST['total_price'];  

    $stmt = $conn->prepare("INSERT INTO orders (product_ids, total_price) VALUES (?, ?)");
    $stmt->bind_param("sd", $product_ids, $total_price);

    if ($stmt->execute()) {
      
        echo json_encode(["success" => true, "message" => "Order successfully placed"]);
    } else {
       
        echo json_encode(["success" => false, "message" => "Failed to place order"]);
    }

    $stmt->close();
} else {
    
    echo json_encode(["success" => false, "message" => "Missing data"]);
}
mysqli_close($conn);

?>
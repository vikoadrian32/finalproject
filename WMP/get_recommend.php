<?php
header('Content-Type: application/json');
include("dbconnect.php");

if($_SERVER["REQUEST_METHOD"] == "POST"){
    // $user_id = $_POST["user_id"];
    $product_id = $_POST["product_id"];
    $point = $_POST['point'];
    $query = "INSERT INTO recommend (product_id, point) VALUES (?, ?)";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, "ss",  $product_id, $point);
    if (mysqli_stmt_execute($stmt)) {
        echo json_encode(["status" => "success", "message" => "Recommended"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Failed to Recommend Product"]);
    }
    mysqli_stmt_close($stmt);
}else{
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}

mysqli_close($conn);


?>
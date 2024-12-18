<?php
header('Content-Type: application/json');
include("dbconnect.php");
$query = "SELECT product_name FROM products";
$result = mysqli_query($conn, $query);

$productNames = array();
while ($row = mysqli_fetch_assoc($result)) {
    $productNames[] = $row;
}

echo json_encode($productNames);

// mysqli_close($conn);

?>